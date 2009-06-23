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
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.splash.AbstractSplashHandler;

/**
 *
 */
public class AnimationSplashHandler extends AbstractSplashHandler {
	private class ImageLoadingThread extends Thread {

		private int start;
		private int end;

		/**
		 * @param start
		 * @param end
		 */
		public ImageLoadingThread(int start, int end) {
			this.start = start;
			this.end = end;
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			for (int i = start; i < end; i++) {
				ImageLoader loader = new ImageLoader();
				try {
					imageSequence[i] = loader.load(Activator.getContext().getBundle().getEntry("splash/sequence/" + new DecimalFormat("00").format(i + 1) + ".jpg").openStream())[0];//$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				} catch (IOException e) {
				}
			}
		}
	}

	private static final int SEQ_LENGTH = 75;
	private final int LOOP_POINT = 40;
	private ImageData[] imageSequence = new ImageData[SEQ_LENGTH];
	private GC shellGC;
	private Image currentImage = null;
	private int currentImageIdx = 0;
	private Timer animationTimer;
	private boolean firstPass = true;
	private boolean backward = false;
	private Thread secondHalfLoadingThread;

	/**
	 * 
	 */
	public AnimationSplashHandler() {
		super();
	}

	private void configureUISplash() {
		getSplash().setSize(455, 295);
		getSplash().setLayout(new FillLayout());
		getSplash().setVisible(true);
	}

	private void animate() {
		final Display display = getSplash().getDisplay();
		shellGC = new GC(getSplash());

		TimerTask animationTask = new TimerTask() {
			public void run() {
				switch (currentImageIdx) {
					case LOOP_POINT :
						backward = false;
						break;
					case SEQ_LENGTH - 1 :
						backward = true;
						firstPass = false;
						break;
				}

				if (currentImage != null)
					currentImage.dispose();
				currentImage = new Image(display, imageSequence[currentImageIdx]);
				if (secondHalfLoadingThread.isAlive()) {
					try {
						secondHalfLoadingThread.join();
					} catch (InterruptedException e) {
					}
				}
				synchronized (shellGC) {
					if (!shellGC.isDisposed())
						shellGC.drawImage(currentImage, 0, 0);
				}

				if (!backward || firstPass)
					currentImageIdx++;
				else
					currentImageIdx--;
			}
		};
		animationTimer = new Timer();
		//33 ms/frame == 30 frames/sec
		animationTimer.schedule(animationTask, 0, 33);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.splash.AbstractSplashHandler#init(org.eclipse.swt.widgets.Shell,
	 *      org.eclipse.ui.IWorkbench)
	 */
	public void init(final Shell splash) {
		// Load image sequence
		loadImages();
		// Store the shell
		super.init(splash);
		// Configure the shell layout
		configureUISplash();
		// Force the UI to layout
		splash.layout(true);
		//register monitor to run event loop during startup
		//createStartupMonitor();
		animate();
	}

	private void loadImages() {
		Thread firstHalfLoadingThread = new ImageLoadingThread(0, LOOP_POINT);
		firstHalfLoadingThread.setPriority(Thread.MAX_PRIORITY);
		secondHalfLoadingThread = new ImageLoadingThread(LOOP_POINT, SEQ_LENGTH);
		firstHalfLoadingThread.run();
		secondHalfLoadingThread.start();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.splash.AbstractSplashHandler#dispose()
	 */
	public void dispose() {
		if (animationTimer != null)
			animationTimer.cancel();
		if (currentImage != null)
			currentImage.dispose();
		if (shellGC != null) {
			synchronized (shellGC) {
				shellGC.dispose();
			}
		}
		super.dispose();
	}
}