/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.e4.ui.examples.legacy.workbench;

import java.io.InputStream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.handlers.HandlerUtil;

public class GeneratePsfHandler extends AbstractHandler {

	/**
	 * 
	 */
	private static final String README_FILE = "README.html"; //$NON-NLS-1$
	private static final String EXAMPLE_FILE = "e4-examples.psf"; //$NON-NLS-1$
	private static final String PROJ_NAME = "org.eclipse.e4.examples.psf"; //$NON-NLS-1$

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkspace workspace = (IWorkspace) HandlerUtil.getVariable(event,
				IWorkspace.class.getName());
		if (workspace == null) {
			workspace = ResourcesPlugin.getWorkspace();
		}
		try {
			IProject project = workspace.getRoot().getProject(PROJ_NAME);
			if (!project.exists()) {
				project.create(new NullProgressMonitor());
			}
			if (!project.isOpen()) {
				project.open(new NullProgressMonitor());
			}
			IFile examples = project.getFile(EXAMPLE_FILE);
			if (!examples.exists()) {
				InputStream examplesInput = getClass().getResourceAsStream(
						EXAMPLE_FILE);
				examples.create(examplesInput, true, new NullProgressMonitor());
			}
			IFile readme = project.getFile(README_FILE);
			if (!readme.exists()) {
				InputStream readmeInput = getClass().getResourceAsStream(
						README_FILE);
				readme.create(readmeInput, true, new NullProgressMonitor());
			}
		} catch (CoreException e) {
			throw new ExecutionException(
					"Failed to create project " + PROJ_NAME, //$NON-NLS-1$
					e);
		}
		return null;
	}

}
