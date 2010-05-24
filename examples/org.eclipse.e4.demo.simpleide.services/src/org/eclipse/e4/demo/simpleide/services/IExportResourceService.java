package org.eclipse.e4.demo.simpleide.services;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.widgets.Shell;

public interface IExportResourceService {
	public String getCategoryName();
	
	public String getIconURI();
	
	public String getLabel();
	
	public void exportResource(Shell shell, IEclipseContext context);
}
