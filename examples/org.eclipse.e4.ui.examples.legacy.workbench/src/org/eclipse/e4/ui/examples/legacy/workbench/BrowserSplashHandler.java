/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.ui.examples.legacy.workbench;

import java.io.IOException;
import java.net.URL;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.splash.AbstractSplashHandler;

/**
 * @since 3.3
 * 
 */
public class BrowserSplashHandler extends AbstractSplashHandler {
	private Browser browser;

	/**
	 * 
	 */
	public BrowserSplashHandler() {
		super();
	}

	/**
	 * 
	 */
	private void configureUISplash() {
		getSplash().setSize(500, 330);
		getSplash().setLayout(new FillLayout());
	}

	/**
	 * 
	 */
	private void createUI() {
		// Create the web browser
		createUIBrowser();
	}

	/**
	 * 
	 */
	private void createUIBrowser() {
		browser = new Browser(getSplash(), SWT.NONE);
		URL url = Activator.getDefault().getBundle().getEntry("splash/splash.html"); //$NON-NLS-1$
		try {
			browser.setUrl(FileLocator.toFileURL(url).toExternalForm());
		} catch (IOException e) {
			//no splash if this happens
		}
		browser.setVisible(false);
	}

	/**
	 * 
	 */
	private void createUIListeners() {
		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
				// NO-OP
			}

			public void completed(ProgressEvent event) {
				// Only show the UI when the URL is fully loaded into the 
				// browser
				browser.setVisible(true);
			}
		});
	}

	/**
	 * 
	 */
	private void doEventLoop() {
		Shell splash = getSplash();
		while (!splash.isDisposed()) {
			if (splash.getDisplay().readAndDispatch() == false) {
				splash.getDisplay().sleep();
			}
		}
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
		// Configure the shell layout
		configureUISplash();
		// Create UI
		createUI();
		// Create UI listeners
		createUIListeners();
		// Force the UI to layout
		splash.layout(true);
		// Keep the splash screen visible and prevent the RCP application from 
		// loading until the close button is clicked.
		doEventLoop();
	}

}
