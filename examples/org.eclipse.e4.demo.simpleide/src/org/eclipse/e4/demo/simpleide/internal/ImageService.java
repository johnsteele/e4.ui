/*******************************************************************************
 * Copyright (c) 2010 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.demo.simpleide.internal;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.demo.simpleide.services.IImageProviderService;
import org.eclipse.e4.demo.simpleide.services.IImageService;
import org.eclipse.e4.demo.simpleide.services.OSGiUtil;
import org.eclipse.osgi.framework.log.FrameworkLog;
import org.eclipse.osgi.framework.log.FrameworkLogEntry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class ImageService implements IImageService {
	private Map<String, IImageProviderService> key2providers = new HashMap<String, IImageProviderService>();
	private FrameworkLog log;
	
	public static class PooledImage implements IPooledImage {
		private int count;
		private Image image;
		private String imageKey;
		private ImageService imageService;
		
		PooledImage(ImageService imageService, String imageKey, Image image) {
			this.count = 1;
			this.image = image;
			this.imageKey = imageKey;
		}

		public Image getImage() {
			return image;
		}

		public void dipose() {
			this.count--;
			if( this.count == 0 ) {
				image.dispose();
				imageService.removePooledImage(this);
				image = null;
				imageService = null;
				imageKey = null;
			}
		}
	}
	
	private static class ImagePool implements IDiposeableImagePool {
		private ImageService imageService;
		private Display display;
		private List<IPooledImage> pooledImages = new ArrayList<IPooledImage>();
		
		public ImagePool(Display display, ImageService imageService) {
			this.imageService = imageService;
			this.display = display;
		}
		
		public Image getImage(String imageKey) throws CoreException {
			if( imageService == null ) {
				throw new CoreException(new Status(IStatus.ERROR, OSGiUtil.getBundleName(getClass()), "The pool is disposed"));
			}
			IPooledImage image = imageService.getPooledImage(display, imageKey);
			pooledImages.add(image);
			return image.getImage();
		}
		
		public Image getImageUnchecked(String imageKey) {
			try {
				return getImage(imageKey);
			} catch (CoreException e) {
				throw new RuntimeException(e);
			}
		}

		public void dispose() {
			for( IPooledImage img : pooledImages ) {
				img.dipose();
			}
			imageService = null;
			display = null;
			pooledImages = null;
		}
	}
	
	public ImageService() {
		Bundle b = FrameworkUtil.getBundle(getClass());
		BundleContext context = b.getBundleContext();
		ServiceReference reference = context.getServiceReference(FrameworkLog.class.getName());
		log = (FrameworkLog) context.getService(reference);
	}
	
	public void addImageProvider(IImageProviderService imageProvider) {
		synchronized (key2providers) {
			for( String s : imageProvider.getImageKeys() ) {
				if( ! key2providers.containsKey(s) ) {
					key2providers.put(s, imageProvider);
				} else {
					IImageProviderService tmp = key2providers.get(s);
					String message = "Can not register imagekey '"+s+"' for "+imageProvider+" ("+OSGiUtil.getBundleName(imageProvider.getClass())+") because is already connected to " + tmp + " ("+OSGiUtil.getBundleName(tmp.getClass())+")";
					log.log(new FrameworkLogEntry(OSGiUtil.getBundleName(getClass()), message, 0, null, null));
				}
			}
		}
	}
	
	public void removeImageProvider(IImageProviderService imageProvider) {
		synchronized (key2providers) {
			Iterator<Entry<String, IImageProviderService>> it = key2providers.entrySet().iterator();
			while( it.hasNext() ) {
				if( it.next().getValue() == imageProvider ) {
					it.remove();
				}
			}
		}
	}
	
	public Image getImage(Display display, String imageKey) throws CoreException {
		IImageProviderService provider = key2providers.get(imageKey);
		if( provider != null ) {
			InputStream stream = provider.getImageData(imageKey);
			return new Image(display, stream);
		} else {
			throw new IllegalArgumentException("There's no image provider known for '"+imageKey+"'.");
		}
	} 

	public IPooledImage getPooledImage(Display display, String imageKey) throws CoreException {
		Map<String, PooledImage> imagePool = getImageMap(display);
		PooledImage image = imagePool.get(imageKey);
		
		if( image != null ) {
			image.count++;
			
		} else {
			image = new PooledImage(this,imageKey,getImage(display, imageKey));
		}
		
		return image;
	}

	void removePooledImage(PooledImage image) {
		Map<String, PooledImage> imagePool = getImageMap((Display) image.image.getDevice());
		imagePool.remove(image.imageKey);
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, PooledImage> getImageMap(Display display) {
		Map<String, PooledImage> imagePool = (Map<String, PooledImage>) display.getData(ImageService.class.getName()+".pooledImages");
		if( imagePool == null ) {
			imagePool = new HashMap<String, ImageService.PooledImage>();
			display.addListener(SWT.Dispose, new Listener() {
				
				public void handleEvent(Event event) {
					Map<String, PooledImage> imagePool = (Map<String, PooledImage>) event.display.getData(ImageService.class.getName()+".pooledImages");
					Iterator<Entry<String, PooledImage>> it = imagePool.entrySet().iterator();
					while( it.hasNext() ) {
						it.next().getValue().image.dispose();
						it.remove();
					}
				}
			});
		}
		
		return imagePool;
	}

	public IDiposeableImagePool getPool(Display display) {
		return new ImagePool(display,this);
	}

	public IImagePool getControlPool(Control control) {
		final IDiposeableImagePool pool = getPool(control.getDisplay());
		control.addDisposeListener(new DisposeListener() {
			
			public void widgetDisposed(DisposeEvent e) {
				pool.dispose();
			}
		});
		return pool;
	}
}