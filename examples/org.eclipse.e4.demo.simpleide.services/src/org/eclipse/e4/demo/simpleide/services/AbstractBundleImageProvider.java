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
package org.eclipse.e4.demo.simpleide.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;

public abstract class AbstractBundleImageProvider implements IImageProviderService {
	
	public final String[] getImageKeys() {
		return getImageMap().values().toArray(new String[0]);
	}

	public final InputStream getImageData(String imageKey) throws CoreException {
		Map<String, String> map = getImageMap();
		
		if( map.containsKey(imageKey) ) {
			return getStream(imageKey, map.get(imageKey));
		}
		
		throw new CoreException(new Status(IStatus.ERROR, OSGiUtil.getBundleName(getClass()), "The image key '"+imageKey+"' is unknown."));
	}

	private InputStream getStream(String imageKey, String path) throws CoreException {
		URL url = FileLocator.find(getBundle(imageKey), new Path(path), null);
		if( url != null ) {
			try {
				return url.openStream();
			} catch (IOException e) {
				throw new CoreException(new Status(IStatus.ERROR, OSGiUtil.getBundleName(getClass()), "Unable to open stream to URL '"+url+"'"));
			}
		}
		throw new CoreException(new Status(IStatus.ERROR, OSGiUtil.getBundleName(getClass()), "The path '"+path+"' is unknown."));
	}
	
	
	
	protected abstract Map<String, String> getImageMap();
	protected abstract Bundle getBundle(String imageKey);
}
