package org.eclipse.e4.demo.simpleide.navigator.internal;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.demo.simpleide.navigator.IProjectService;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class DefaultProjectService implements IProjectService {

	public void createProject(Shell shell, IWorkspace workspace, IProgressMonitor monitor, String projectName) {
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

	public Image createIcon(Display display) {
		return new Image(display, getClass().getClassLoader().getResourceAsStream("/icons/newjprj_wiz.gif"));
	}

	public String getLabel() {
		return "Project";
	}

}
