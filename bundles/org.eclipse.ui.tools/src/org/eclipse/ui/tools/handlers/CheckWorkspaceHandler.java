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
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.core.runtime.spi.RegistryContributor;
import org.eclipse.core.runtime.spi.RegistryStrategy;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class CheckWorkspaceHandler extends AbstractHandler {

	private static final String ATTR_ELEMENT_ID = "id"; //$NON-NLS-1$

	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection currentSelection = HandlerUtil.getCurrentSelectionChecked(event);
		
		String projectName = null;
		Object master = new Object();
		Object user = new Object();
		RegistryStrategy strategy = RegistryFactory.createOSGiStrategy(null,
				null, master);
		IExtensionRegistry registry = RegistryFactory.createRegistry(strategy,
				master, user);
		if (currentSelection instanceof IStructuredSelection) {
			Object o = ((IStructuredSelection) currentSelection)
					.getFirstElement();
			if (o instanceof IFile) {
				projectName = ((IFile) o).getProject().getName();
				try {
					process(registry, master, (IFile) o);
				} catch (IllegalArgumentException e) {
					throw new ExecutionException(e.getMessage(), e);
				} catch (CoreException e) {
					throw new ExecutionException(e.getMessage(), e);
				}
			}
		}
		IExtension[] e = registry.getExtensions(projectName);
		for (int i = 0; i < e.length; i++) {
			System.err.println("e: " + e[i].getUniqueIdentifier()); //$NON-NLS-1$
			IConfigurationElement as = e[i].getConfigurationElements()[0];
			IConfigurationElement[] children = as.getChildren();
			for (int j = 0; j < children.length; j++) {
				System.err.println("\t" + children[j].getName() + " " //$NON-NLS-1$ //$NON-NLS-2$
						+ children[j].getAttribute(ATTR_ELEMENT_ID));
			}
		}
		return null;
	}

	private void process(final IExtensionRegistry registry,
			final Object master, final IFile file)
			throws IllegalArgumentException, CoreException {
		String id = file.getProject().getName();
		IContributor contributor = new RegistryContributor(id, id, id, id);
		registry.addContribution(file.getContents(), contributor, false,
				contributor.getName(), null, master);
	}
}
