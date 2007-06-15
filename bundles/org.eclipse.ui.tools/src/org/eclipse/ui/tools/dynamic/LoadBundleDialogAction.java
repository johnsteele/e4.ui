/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.tools.dynamic;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.BundleException;

/**
 * @since 3.1
 */
public class LoadBundleDialogAction extends Action {

	private Shell parent;
	/**
	 * @param parent
	 * 
	 */
	public LoadBundleDialogAction(Shell parent) {
		super();
		this.parent = parent;
		setText("&Load...");
	}
	
	
	public void run() {
		DirectoryDialog dd = new DirectoryDialog(parent);
		dd.setMessage("Select bundle location.");
		String location = dd.open();
		if (location == null)
			return;
		
		File file = new File(location);
		try {
			URL url = file.toURL();
			BundleHistory.getInstance().addBundleReference(url);
		}
		catch (MalformedURLException e) {
			MessageDialog.openError(parent, "Error creating bundle path", file.toString() + " is not a valid path.");
		} catch (BundleException e) {
			MessageDialog.openError(parent, "Error loading bundle", e.getMessage());
		}
		
	}
}
