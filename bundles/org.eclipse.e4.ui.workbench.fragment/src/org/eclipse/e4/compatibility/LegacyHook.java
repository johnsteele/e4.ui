package org.eclipse.e4.compatibility;

import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.ui.model.application.MMenu;
import org.eclipse.e4.ui.model.application.MToolBar;
import org.eclipse.e4.ui.model.workbench.MPerspective;
import org.eclipse.e4.workbench.ui.ILegacyHook;
import org.eclipse.e4.workbench.ui.menus.MenuHelper;
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
	static {
		PlatformUI.isWorkbenchRunning();
	}
	
//	public static Workbench e4Workbench;
//	public static MApplication<MWorkbenchWindow> workbench;
	public static IEclipseContext context;
	
	public LegacyHook(IEclipseContext context) {
		init(context);
	}
	
	private void init(IEclipseContext context) {
		// set up PlatformUI
		LegacyHook.context = context;
		PlatformUI.isWorkbenchRunning();

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
	
	public void loadToolbar(MToolBar tbModel) {
		System.out.println("Should load the default menu here"); //$NON-NLS-1$
		MenuHelper.loadToolbar(tbModel);
	}


	/* (non-Javadoc)
	 * @see org.eclipse.e4.workbench.ui.ILegacyHook#loadPerspective(org.eclipse.e4.ui.model.workbench.MPerspective)
	 */
	public void loadPerspective(MPerspective<?> perspModel) {
		//PerspectiveHelper.loadPerspective(perspModel);
	}

}
