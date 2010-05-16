package org.eclipse.e4.demo.simpleide.navigator.internal;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.e4.demo.simpleide.navigator.IProjectCreator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class DefaultProjectCreator implements IProjectCreator {

	public void createProject(Shell shell, IWorkspace workspace, String projectName) {
		// TODO Auto-generated method stub
		
	}

	public Image createIcon(Display display) {
		return new Image(display, getClass().getClassLoader().getResourceAsStream("/icons/newjprj_wiz.gif"));
	}

}
