/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.ui.tools.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.pde.core.plugin.IExtensions;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;

public class CheckExtensionsHandler extends AbstractHandler {

	@SuppressWarnings("unused")
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IPluginModelBase[] models = PluginRegistry.getWorkspaceModels();
		for (int i = 0; i < models.length; i++) {
			IPluginModelBase baseModel = models[i];
			IExtensions baseExtensions = baseModel.getExtensions();
			IPluginExtension[] extensions = baseExtensions.getExtensions();
			for (int j = 0; j < extensions.length; j++) {
				IPluginExtension extension = extensions[j];
				System.err.println(extension.getPoint());
				System.err.println(extension.getChildCount());
				System.err.println(extension.getPluginModel().isLoaded());
			}
		}
		return null;
	}

}
