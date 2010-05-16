package org.eclipse.e4.demo.simpleide.navigator;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public interface IProjectService {
	public Image createIcon(Display display);
	public String getLabel();
	public void createProject(Shell shell, IWorkspace workspace, IProgressMonitor monitor, String projectName);
}
