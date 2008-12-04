package org.eclipse.e4.compatibility;

import org.eclipse.e4.ui.model.application.Menu;
import org.eclipse.e4.ui.model.workbench.Perspective;
import org.eclipse.e4.workbench.ui.ILegacyHook;
import org.eclipse.e4.workbench.ui.internal.Workbench;
import org.eclipse.e4.workbench.ui.menus.PerspectiveHelper;

public class LegacyHook implements ILegacyHook {
	
	public static Workbench e4Workbench;
	
	public void init(Workbench e4Workbench) {
		LegacyHook.e4Workbench = e4Workbench;
	}
	
	public void loadMenu(Menu menuModel) {
		System.out.println("Should load the default menu here"); //$NON-NLS-1$
		//MenuHelper.loadMenu(menuModel);
	}

	public void loadPerspective(Perspective perspModel) {
		PerspectiveHelper.loadPerspective(perspModel);
	}

}
