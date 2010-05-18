/*******************************************************************************
 * Copyright (c) 2010 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.demo.simpleide.internal;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.statusreporter.StatusReporter;
import org.eclipse.e4.demo.simpleide.services.IProjectService;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.FrameworkUtil;

public class DefaultProjectService implements IProjectService {

	public void createProject(Shell shell, IWorkspace workspace, StatusReporter statusReporter, Logger looger,
			IProgressMonitor monitor, String projectName) {
		final IProject project = workspace.getRoot().getProject(projectName);
		final IProjectDescription pd = workspace
				.newProjectDescription(projectName);
		try {
			workspace.run(new IWorkspaceRunnable() { 

				public void run(IProgressMonitor monitor) throws CoreException {
					if (!project.exists()) {
						project.create(pd, monitor);
					}
					if (!project.isOpen()) {
						project.open(monitor);
					}
				}
				
			},monitor);
		} catch (CoreException e) {
			// TODO: handle exception
		}
	}

	public String getIconURI() {
		return "platform:/plugin/" + FrameworkUtil.getBundle(getClass()).getSymbolicName() + "/icons/newprj_wiz.gif";
	}

	public String getLabel() {
		return "Project";
	}

}
