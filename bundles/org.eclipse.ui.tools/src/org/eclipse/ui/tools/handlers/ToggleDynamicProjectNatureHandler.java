/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.tools.handlers;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;
import org.eclipse.ui.tools.builder.DynamicNature;

public class ToggleDynamicProjectNatureHandler extends AbstractHandler
		implements IElementUpdater {

	private static final String TOGGLE_ID = "toggleDynamicProjectNature.add"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.commands.IElementUpdater#updateElement(org.eclipse.ui.menus.UIElement,
	 *      java.util.Map)
	 */
	public void updateElement(UIElement element, Map parameters) {
		Boolean enableObj = (Boolean) parameters.get(TOGGLE_ID);
		boolean enable;
		if (enableObj == null) {
			IProject p = getProjectFromSelection((IWorkbenchWindow) parameters
					.get("org.eclipse.ui.IWorkbenchWindow")); //$NON-NLS-1$
			if (p == null)
				return;
			try {
				enable = !p.hasNature(DynamicNature.NATURE_ID);
			} catch (CoreException e) {
				return;
			}
		} else
			enable = enableObj.booleanValue();

		element
				.setText(enable ? "Add dynamic nature" : "Remove dynamic nature"); //$NON-NLS-1$ //$NON-NLS-2$

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);
		ICommandService commandService = (ICommandService) window
				.getService(ICommandService.class);

		IProject project = getProjectFromSelection(window);
		if (project != null) {
			toggleNature(project);
			commandService
					.refreshElements(event.getCommand().getId(), null);

		}

		return null;
	}

	private IProject getProjectFromSelection(IWorkbenchWindow window) {
		ISelection selection = window.getSelectionService().getSelection();
		if (selection instanceof IStructuredSelection) {
			Object o = ((IStructuredSelection) selection).getFirstElement();
			if (o instanceof IAdaptable) {
				IProject project = (IProject) ((IAdaptable) o)
						.getAdapter(IProject.class);
				return project;
			}
		}
		return null;
	}

	private void toggleNature(IProject project) {
		try {
			IProjectDescription description = project.getDescription();
			String[] natures = description.getNatureIds();

			for (int i = 0; i < natures.length; ++i) {
				if (DynamicNature.NATURE_ID.equals(natures[i])) {
					// Remove the nature
					String[] newNatures = new String[natures.length - 1];
					System.arraycopy(natures, 0, newNatures, 0, i);
					System.arraycopy(natures, i + 1, newNatures, i,
							natures.length - i - 1);
					description.setNatureIds(newNatures);
					project.setDescription(description, null);
					return;
				}
			}

			// Add the nature
			String[] newNatures = new String[natures.length + 1];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[natures.length] = DynamicNature.NATURE_ID;
			description.setNatureIds(newNatures);
			project.setDescription(description, null);
		} catch (CoreException e) {
		}
	}

}
