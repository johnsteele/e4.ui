package org.eclipse.e4.demo.simpleide.services;

import org.eclipse.swt.widgets.Shell;

public interface IExportResourceService {
	public String getCategoryName();
	
	public String getIconURI();
	
	public String getLabel();
	
	public void importResource(Shell shell);
}
