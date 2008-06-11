/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.tools;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin implements IStartup {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.ui.tools"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private BundleContext context;

	private Listener filter;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		this.context = context;

		filter = new Listener() {

			final int[] correctChars = { SWT.ARROW_UP, SWT.ARROW_UP,
					SWT.ARROW_DOWN, SWT.ARROW_DOWN, SWT.ARROW_LEFT,
					SWT.ARROW_RIGHT, SWT.ARROW_LEFT, SWT.ARROW_RIGHT,
					(int) 'b', (int) 'a' };
			int currentCorrectCount = 0;

			public void handleEvent(Event event) {
				if (correctChars[currentCorrectCount] == event.keyCode) {
					currentCorrectCount++;
					if (currentCorrectCount == correctChars.length) {
						currentCorrectCount = 0;
						doCrazyStuff(event.display);
					}
					return;
				}
				currentCorrectCount = 0;
			}

			private void doCrazyStuff(final Display display) {
				display.asyncExec(new Runnable() {

					public void run() {
						final Image image = imageDescriptorFromPlugin(
								"org.eclipse.ui.tools", "icons/o_rly.png").createImage(); //$NON-NLS-1$ //$NON-NLS-2$
						Shell parentShell = display.getActiveShell();
						final Shell shell = new Shell(parentShell, SWT.NO_TRIM);
						final Region region = new Region();
						final ImageData imageData = image.getImageData();
						if (imageData.alphaData != null) {
							Rectangle pixel = new Rectangle(0, 0, 1, 1);
							for (int y = 0; y < imageData.height; y++) {
								for (int x = 0; x < imageData.width; x++) {
									if (imageData.getAlpha(x, y) == 255) {
										pixel.x = imageData.x + x;
										pixel.y = imageData.y + y;
										region.add(pixel);
									}
								}
							}
						} else {
							ImageData mask = imageData.getTransparencyMask();
							Rectangle pixel = new Rectangle(0, 0, 1, 1);
							for (int y = 0; y < mask.height; y++) {
								for (int x = 0; x < mask.width; x++) {
									if (mask.getPixel(x, y) != 0) {
										pixel.x = imageData.x + x;
										pixel.y = imageData.y + y;
										region.add(pixel);
									}
								}
							}
						}
						shell.setRegion(region);

						Listener l = new Listener() {

							public void handleEvent(Event e) {
								if (e.type == SWT.KeyDown) {
									shell.dispose();
								}
								if (e.type == SWT.Paint) {
									e.gc.drawImage(image, imageData.x,
											imageData.y);
								}
								if (e.type == SWT.Dispose) {
									region.dispose();
									image.dispose();
								}
							}
						};
						shell.addListener(SWT.KeyDown, l);
						shell.addListener(SWT.Paint, l);
						shell.addListener(SWT.Dispose, l);

						shell.setSize(imageData.x + imageData.width,
								imageData.y + imageData.height);
						if (parentShell != null) {

							shell.setLocation(parentShell.getLocation().x,
									parentShell.getBounds().height
											- shell.getSize().y
											+ parentShell.getBounds().y);
						}
						shell.open();

					}
				});
			}
		};
		PlatformUI.getWorkbench().getDisplay().addFilter(SWT.KeyUp, filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		this.context = null;
		super.stop(context);
		PlatformUI.getWorkbench().getDisplay().removeFilter(SWT.KeyUp, filter);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 */
	public void earlyStartup() {
	}

}
