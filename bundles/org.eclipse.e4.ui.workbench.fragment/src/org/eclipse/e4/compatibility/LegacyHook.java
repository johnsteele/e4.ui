package org.eclipse.e4.compatibility;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MMenu;
import org.eclipse.e4.ui.model.workbench.MPerspective;
import org.eclipse.e4.ui.model.workbench.MWorkbenchWindow;
import org.eclipse.e4.workbench.ui.ILegacyHook;
import org.eclipse.e4.workbench.ui.internal.Workbench;
import org.eclipse.e4.workbench.ui.menus.MenuHelper;
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
	public static MApplication<MWorkbenchWindow> workbench;
	
	/**
	 * 
	 */
	public LegacyHook() {
	}
	
	public void init(Workbench e4Workbench,
			MApplication<MWorkbenchWindow> workbench) {
		LegacyHook.e4Workbench = e4Workbench;
		LegacyHook.workbench = workbench;
		
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
	
	public void loadMenu(MMenu menuModel) {
		System.out.println("Should load the default menu here"); //$NON-NLS-1$
		MenuHelper.loadMenu(menuModel);
	}


	/* (non-Javadoc)
	 * @see org.eclipse.e4.workbench.ui.ILegacyHook#loadPerspective(org.eclipse.e4.ui.model.workbench.MPerspective)
	 */
	public void loadPerspective(MPerspective<?> perspModel) {
		PerspectiveHelper.loadPerspective(perspModel);
	}

}
