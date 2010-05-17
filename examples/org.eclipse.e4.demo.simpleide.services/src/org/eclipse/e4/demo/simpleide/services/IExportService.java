package org.eclipse.e4.demo.simpleide.services;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public interface IExportService {
	public String getCategoryName();
	
	public Image getIcon(Display display);
	
	public String getLabel();
	
	public void importResource(Shell shell);
}
