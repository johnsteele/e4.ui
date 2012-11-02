/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
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
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;
import org.eclipse.ui.tools.Messages;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

public class LoadUnloadHandler extends AbstractHandler implements
		IElementUpdater {

	public static final String LOCATION = "bundleLocation"; //$NON-NLS-1$

	@SuppressWarnings("unchecked")
	public void updateElement(UIElement element, Map parameters) {
		String location = (String) parameters.get(LOCATION);
		if (location == null)
			return;

		element.setChecked(DynamicTools.getBundle(location) != null);
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);

		String location = event.getParameter(LOCATION);
		try {
			if (location == null) {
				DirectoryDialog dd = new DirectoryDialog(window.getShell());
				dd.setMessage(Messages.loadlUnload_selection_location);
				String desiredLocation = dd.open();
				if (desiredLocation == null)
					return null;

				File file = new File(desiredLocation);
				try {
					URL url = file.toURL();
					Bundle bundle = DynamicTools.installBundle("reference:" //$NON-NLS-1$
							+ url.toExternalForm());
					BundleHistory.getInstance().addBundleReference(
							bundle.getSymbolicName(),
							"reference:" + url.toExternalForm()); //$NON-NLS-1$
					BundleHistory.getInstance().save();
				} catch (MalformedURLException e) {
					throw new ExecutionException(file.toString()
							+ " is not a valid path.", e); //$NON-NLS-1$
				} catch (BundleException e) {
					throw new ExecutionException(e.getMessage(), e);
				}

			} else {
				Bundle bundle = DynamicTools.getBundle(location);

				if (bundle == null) {
					load(location);
				} else {
					unload(bundle);
				}
			}
		} finally {
			ICommandService cs = (ICommandService) window
					.getService(ICommandService.class);
			cs.refreshElements(event.getCommand().getId(), null);
		}
		return null;
	}

	private void unload(Bundle bundle) throws ExecutionException {
		try {
			DynamicTools.uninstallBundle(bundle);
		} catch (BundleException e) {
			throw new ExecutionException("Failed to unload", e); //$NON-NLS-1$
		}
	}

	private void load(String location) throws ExecutionException {
		try {
			DynamicTools.installBundle(location);
		} catch (IllegalStateException e) {
			throw new ExecutionException("Failed to load", e); //$NON-NLS-1$
		} catch (BundleException e) {
			throw new ExecutionException("Failed to load", e); //$NON-NLS-1$
		}
	}
}
