package org.eclipse.e4.demo.simpleide.internal;

import org.eclipse.e4.demo.simpleide.services.IImportResourceService;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.FrameworkUtil;

public class EclipseProjectImportService implements IImportResourceService {

	public String getIconURI() {
		return "platform:/plugin/" + FrameworkUtil.getBundle(getClass()).getSymbolicName() + "/icons/newprj_wiz.gif";
	}

	public String getLabel() {
		return "Existing Projects Into Workspace";
	}

	public void importResource(Shell shell) {
		// TODO Auto-generated method stub
		
	}

	public String getCategoryName() {
		// TODO Auto-generated method stub
		return null;
	}

}
