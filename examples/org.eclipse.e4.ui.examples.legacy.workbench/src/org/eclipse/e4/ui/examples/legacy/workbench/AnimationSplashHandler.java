/*******************************************************************************
 * Copyright (c) 2009 Benjamin Cabe and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Benjamin Cabe - initial API and implementation
 *     IBM Corporation - ongoing development
 *******************************************************************************/
package org.eclipse.e4.ui.examples.legacy.workbench;

import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.splash.AbstractSplashHandler;

/**
 *
 */
public class AnimationSplashHandler extends AbstractSplashHandler {
	
	private final class BundleProgressMonitor extends NullProgressMonitor {
		private static final int NB_TICKS = 42;
		private int worked = 0 ;
		private int total = 0 ;
		
		public void worked(int work) {
			worked++ ;	
			updateProgressBar();
		}

		public void done() {
			worked = total ;
			updateProgressBar();
		}

		public void beginTask(String name, int totalWork) {
			// there are exactly 19 bundles being loaded in the legacy workbench as of 20090722
			// thus, we prefer to use an hardcoded value for the "length" of the monitor, instead of relying 
			// on the arbitrary rule assuming there are 1/10 of the installed bundles which are actually loaded
			total = 19 ; // total = totalWork ;	
			updateProgressBar();
		}

		private void updateProgressBar() {
			int currentImgIdx = (int) (NB_TICKS / (float)total  * worked) ;
			for (int i = 0 ; i < NB_TICKS ; i++) {
				Image imgToDraw = (i <= currentImgIdx) ? imgProgressDark : imgProgressLight ;
				synchronized (shellGC) {
					shellGC.drawImage(imgToDraw, 11 + (11 * i), 292);
				}
			}
		}
	}

	private Image imgProgressDark = null;
	private Image imgProgressLight = null;

	private GC shellGC;

	/**
	 * 
	 */
	public AnimationSplashHandler() {
		super();
	}

	private void configureUISplash() {
		getSplash().setSize(480, 320);
		getSplash().setVisible(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.splash.AbstractSplashHandler#init(org.eclipse.swt.widgets.Shell,
	 *      org.eclipse.ui.IWorkbench)
	 */
	public void init(final Shell splash) {
		// Store the shell
		super.init(splash);
		// Load image sequence
		loadImages();
		// Configure the shell layout
		configureUISplash();
		// Force the UI to layout
		splash.layout(true);
		// create the GC we will draw on
		shellGC = new GC(getSplash());
	}

	private void loadImages() {
		ImageLoader loader = new ImageLoader();
		
		try {
			ImageData imageDataProgressDark = loader.load(Activator.getContext().getBundle().getEntry("splash/progress-dark.png").openStream())[0];//$NON-NLS-1$
			ImageData imageDataProgressLight = loader.load(Activator.getContext().getBundle().getEntry("splash/progress-light.png").openStream())[0]; //$NON-NLS-1$
			
			imgProgressDark = new Image(getSplash().getDisplay(), imageDataProgressDark) ;
			imgProgressLight = new Image(getSplash().getDisplay(), imageDataProgressLight) ;
		} catch (IOException e1) {
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.splash.AbstractSplashHandler#getBundleProgressMonitor()
	 */
	@Override
	public IProgressMonitor getBundleProgressMonitor() {
		return new BundleProgressMonitor();
	}	

	/* (non-Javadoc)
	 * @see org.eclipse.ui.splash.AbstractSplashHandler#dispose()
	 */
	public void dispose() {
		if (shellGC != null) {
			synchronized (shellGC) {
				shellGC.dispose();
			}
		}
		if (imgProgressDark != null)
			imgProgressDark.dispose() ;
		if (imgProgressLight != null)
			imgProgressLight.dispose() ;
		
		super.dispose();
	}
}