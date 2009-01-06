package org.eclipse.e4.compatibility;

import org.eclipse.e4.ui.model.application.Menu;
import org.eclipse.e4.ui.model.workbench.Perspective;
import org.eclipse.e4.workbench.ui.ILegacyHook;
import org.eclipse.e4.workbench.ui.internal.Workbench;
import org.eclipse.e4.workbench.ui.menus.PerspectiveHelper;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchColors;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.themes.ColorDefinition;
import org.eclipse.ui.internal.themes.FontDefinition;
import org.eclipse.ui.internal.themes.ThemeElementHelper;
import org.eclipse.ui.internal.util.PrefUtil;
import org.eclipse.ui.themes.IThemeManager;

public class LegacyHook implements ILegacyHook {
	
	public static Workbench e4Workbench;
	
	public void init(Workbench e4Workbench) {
		LegacyHook.e4Workbench = e4Workbench;
		
		// Images
		WorkbenchImages.getDescriptors();

		initializeFonts();
		initializeApplicationColors();
		initializeColors();
	}

	private void initializeFonts() {
		FontDefinition[] fontDefinitions = WorkbenchPlugin.getDefault()
				.getThemeRegistry().getFonts();

		ThemeElementHelper.populateRegistry(PlatformUI.getWorkbench().getThemeManager()
				.getCurrentTheme(), fontDefinitions, PrefUtil
				.getInternalPreferenceStore());
	}

	private void initializeApplicationColors() {
		ColorDefinition[] colorDefinitions = WorkbenchPlugin
				.getDefault().getThemeRegistry().getColors();
		ThemeElementHelper.populateRegistry(PlatformUI.getWorkbench().getThemeManager().getTheme(
				IThemeManager.DEFAULT_THEME), colorDefinitions,
				PrefUtil.getInternalPreferenceStore());
	}

	/*
	 * Initialize the workbench colors.
	 * 
	 * @since 3.0
	 */
	private void initializeColors() {
		WorkbenchColors.startup();
	}
	
	public void loadMenu(Menu menuModel) {
		System.out.println("Should load the default menu here"); //$NON-NLS-1$
		//MenuHelper.loadMenu(menuModel);
	}

	public void loadPerspective(Perspective perspModel) {
		PerspectiveHelper.loadPerspective(perspModel);
	}

}
