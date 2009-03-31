package org.eclipse.e4.workbench.ui.menus;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.e4.extensions.ExtensionUtils;
import org.eclipse.e4.ui.model.application.ApplicationFactory;
import org.eclipse.e4.ui.model.application.MMenu;
import org.eclipse.e4.ui.model.application.MMenuItem;
import org.eclipse.e4.ui.model.application.MToolBar;
import org.eclipse.e4.ui.model.application.MToolBarItem;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;

public class MenuHelper {

	public static final String MAIN_MENU_ID = "org.eclipse.ui.main.menu";  //$NON-NLS-1$
	
	public static void loadMenu(MMenu menuModel) {
		String id = menuModel.getId();
		if (id == null || id.length() == 0)
			return;
		
		if (id.indexOf(MAIN_MENU_ID) >= 0)
			populateMainMenu(menuModel);
		else
			populateMenu(menuModel);
	}
	
	private static void printCEAtts(IConfigurationElement ce, String prefix) {
		String[] attNames = ce.getAttributeNames();
		for (int j = 0; j < attNames.length; j++) {
			System.out.println(prefix + "Att: " + attNames[j] + " = " + ce.getAttribute(attNames[j])); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	private static void printCEKids(IConfigurationElement ce, String prefix) {
		IConfigurationElement[] kids = ce.getChildren();
		for (int i = 0; i < kids.length; i++) {
			System.out.println(prefix + "Child: " + kids[i].getName()); //$NON-NLS-1$
			printCEAtts(kids[i], prefix + "  "); //$NON-NLS-1$
			printCEKids(kids[i], prefix + "  "); //$NON-NLS-1$
		}
	}
	
	private static void populateMenu(MMenu menuModel) {
		String menuId = menuModel.getId();
		System.out.println("populateMenu: " + menuId); //$NON-NLS-1$
		IConfigurationElement[] actionSets = ExtensionUtils.getExtensions(IWorkbenchRegistryConstants.PL_ACTION_SETS);
		for (int i = 0; i < actionSets.length; i++) {
			System.out.println("Action Set:" + actionSets[i].getName()); //$NON-NLS-1$
			printCEAtts(actionSets[i], "  "); //$NON-NLS-1$
			printCEKids(actionSets[i], "  "); //$NON-NLS-1$
		}
	}
	
	private static void populateMainMenu(MMenu menu0) {

		MMenuItem menu1 = addMenu(menu0, "&File", null, null, "file", "file"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addSeparator(menu1, "fileStart"); //$NON-NLS-1$
			MMenuItem menu2 = addMenu(menu1, "&New	Alt+Shift+N", null, null, "new", "new"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				addSeparator(menu2, "new"); //$NON-NLS-1$
				//didn't find: org.eclipse.ui.actions.NewWizardMenu
				addSeparator(menu2, "additions"); //$NON-NLS-1$

			addSeparator(menu1, "new.ext"); //$NON-NLS-1$
			addSeparator(menu1, null);
			addMenuItem(menu1, "&Close", null, null, "close", "org.eclipse.ui.file.close"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addMenuItem(menu1, "C&lose All", null, null, "closeAll", "org.eclipse.ui.file.closeAll"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addSeparator(menu1, "close.ext"); //$NON-NLS-1$
			addSeparator(menu1, null);
			
			addMenuItem(menu1, "&Save", null, //$NON-NLS-1$
					"platform:/plugin/org.eclipse.ui/icons/full/etool16/save_edit.gif",  //$NON-NLS-1$
					"save", "org.eclipse.ui.file.save"); //$NON-NLS-1$ //$NON-NLS-2$

			addMenuItem(menu1, "Save &As...", null, "platform:/plugin/org.eclipse.ui/icons/full/etool16/saveas_edit.gif", "saveAs", "org.eclipse.ui.file.saveAs"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
			addMenuItem(menu1, "Sav&e All", null, "platform:/plugin/org.eclipse.ui/icons/full/etool16/saveall_edit.gif", "saveAll", "org.eclipse.ui.file.saveAll"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
			addMenuItem(menu1, "Rever&t", null, null, "revert", "org.eclipse.ui.file.revert"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addSeparator(menu1, null);
			addMenuItem(menu1, "Mo&ve...", null, null, "move", "org.eclipse.ui.edit.move"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addMenuItem(menu1, "Rena&me...", null, null, "rename", "org.eclipse.ui.edit.rename"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addMenuItem(menu1, "Re&fresh", null, "platform:/plugin/org.eclipse.ui/icons/full/elcl16/refresh_nav.gif", "refresh", "org.eclipse.ui.file.refresh"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
			addSeparator(menu1, "save.ext"); //$NON-NLS-1$
			addSeparator(menu1, null);
			addMenuItem(menu1, "&Print...", null, "platform:/plugin/org.eclipse.ui/icons/full/etool16/print_edit.gif", "print", "org.eclipse.ui.file.print"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
			addSeparator(menu1, "print.ext"); //$NON-NLS-1$
			addSeparator(menu1, null);
			addMenuItem(menu1, "Switch &Workspace", null, null, "openWorkspace", "org.eclipse.ui.file.openWorkspace"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addSeparator(menu1, "open.ext"); //$NON-NLS-1$
			addSeparator(menu1, null);
			addMenuItem(menu1, "&Import...", null, "platform:/plugin/org.eclipse.ui/icons/full/etool16/import_wiz.gif", "import", "org.eclipse.ui.file.import"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
			addMenuItem(menu1, "E&xport...", null, "platform:/plugin/org.eclipse.ui/icons/full/etool16/export_wiz.gif", "export", "org.eclipse.ui.file.export"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
			addSeparator(menu1, "import.ext"); //$NON-NLS-1$
			addSeparator(menu1, "additions"); //$NON-NLS-1$
			addSeparator(menu1, null);
			addMenuItem(menu1, "P&roperties", null, null, "properties", "org.eclipse.ui.file.properties"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			//didn't find: org.eclipse.ui.internal.ReopenEditorMenu
			addSeparator(menu1, "mru"); //$NON-NLS-1$
			addSeparator(menu1, null);
			addMenuItem(menu1, "E&xit", null, null, "quit", "org.eclipse.ui.file.exit"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addSeparator(menu1, "fileEnd"); //$NON-NLS-1$


		MMenuItem menu3 = addMenu(menu0, "&Edit", null, null, "edit", "edit"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addSeparator(menu3, "editStart"); //$NON-NLS-1$
			addMenuItem(menu3, "&Undo", null, "platform:/plugin/org.eclipse.ui/icons/full/etool16/undo_edit.gif", "undo", "org.eclipse.ui.edit.undo"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
			addMenuItem(menu3, "&Redo", null, "platform:/plugin/org.eclipse.ui/icons/full/etool16/redo_edit.gif", "redo", "org.eclipse.ui.edit.redo"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
			addSeparator(menu3, "undo.ext"); //$NON-NLS-1$
			addSeparator(menu3, null);
			addMenuItem(menu3, "Cu&t", null, "platform:/plugin/org.eclipse.ui/icons/full/etool16/cut_edit.gif", "cut", "org.eclipse.ui.edit.cut"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
			addMenuItem(menu3, "&Copy", null, "platform:/plugin/org.eclipse.ui/icons/full/etool16/copy_edit.gif", "copy", "org.eclipse.ui.edit.copy"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
			addMenuItem(menu3, "&Paste", null, "platform:/plugin/org.eclipse.ui/icons/full/etool16/paste_edit.gif", "paste", "org.eclipse.ui.edit.paste"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
			addSeparator(menu3, "cut.ext"); //$NON-NLS-1$
			addSeparator(menu3, null);
			addMenuItem(menu3, "&Delete", null, "platform:/plugin/org.eclipse.ui/icons/full/etool16/delete_edit.gif", "delete", "org.eclipse.ui.edit.delete"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
			addMenuItem(menu3, "Select &All", null, null, "selectAll", "org.eclipse.ui.edit.selectAll");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			addSeparator(menu3, null);
			addMenuItem(menu3, "&Find/Replace...", null, null, "find", "org.eclipse.ui.edit.findReplace"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addSeparator(menu3, "find.ext"); //$NON-NLS-1$
			addSeparator(menu3, null);
			addMenuItem(menu3, "Add Bookmar&k...", null, null, "bookmark", "org.eclipse.ui.edit.addBookmark"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addMenuItem(menu3, "Add Ta&sk...", null, null, "addTask", "org.eclipse.ui.edit.addTask"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addSeparator(menu3, "add.ext"); //$NON-NLS-1$
			addSeparator(menu3, "editEnd"); //$NON-NLS-1$
			addSeparator(menu3, "additions"); //$NON-NLS-1$


			MMenuItem menu4 = addMenu(menu0, "&Navigate", null, null, "navigate", "navigate"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addSeparator(menu4, "navStart"); //$NON-NLS-1$
			addMenuItem(menu4, "Go &Into", null, null, "goInto", "org.eclipse.ui.navigate.goInto"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			MMenuItem menu5 = addMenu(menu4, "&Go To", null, null, "goTo", "goTo"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				addMenuItem(menu5, "&Back", null, "platform:/plugin/org.eclipse.ui/icons/full/elcl16/backward_nav.gif", "back", "org.eclipse.ui.navigate.back"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
				addMenuItem(menu5, "&Forward", null, "platform:/plugin/org.eclipse.ui/icons/full/elcl16/forward_nav.gif", "forward", "org.eclipse.ui.navigate.forward"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
				addMenuItem(menu5, "&Up One Level", null, "platform:/plugin/org.eclipse.ui/icons/full/elcl16/up_nav.gif", "up", "org.eclipse.ui.navigate.up"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
				addSeparator(menu5, "additions"); //$NON-NLS-1$

			addSeparator(menu4, "open.ext"); //$NON-NLS-1$
			addSeparator(menu4, "open.ext2"); //$NON-NLS-1$
			addSeparator(menu4, "open.ext3"); //$NON-NLS-1$
			addSeparator(menu4, "open.ext4"); //$NON-NLS-1$
			addSeparator(menu4, "show.ext"); //$NON-NLS-1$

			/*MMenuItem menu6 = */addMenu(menu4, "Sho&w In	Alt+Shift+W", null, null, "showIn", "showIn"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				//didn't find: org.eclipse.ui.internal.ShowInMenu

			addSeparator(menu4, "show.ext2"); //$NON-NLS-1$
			addSeparator(menu4, "show.ext3"); //$NON-NLS-1$
			addSeparator(menu4, "show.ext4"); //$NON-NLS-1$
			addSeparator(menu4, null);
			addMenuItem(menu4, "Ne&xt", "org.eclipse.ui.ide", "$nl$/icons/full/etool16/next_nav.gif", "next", "org.eclipse.ui.navigate.next"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			addMenuItem(menu4, "Pre&vious", "org.eclipse.ui.ide", "$nl$/icons/full/etool16/prev_nav.gif", "previous", "org.eclipse.ui.navigate.previous"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			addSeparator(menu4, "additions"); //$NON-NLS-1$
			addSeparator(menu4, "navEnd"); //$NON-NLS-1$
			addSeparator(menu4, null);
			addMenuItem(menu4, "&Back", null, "platform:/plugin/org.eclipse.ui/icons/full/elcl16/backward_nav.gif", "backardHistory", "org.eclipse.ui.navigate.backwardHistory"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
			addMenuItem(menu4, "&Forward", null, "platform:/plugin/org.eclipse.ui/icons/full/elcl16/forward_nav.gif", "forwardHistory", "org.eclipse.ui.navigate.forwardHistory"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 


			MMenuItem menu7 = addMenu(menu0, "&Project", null, null, "project", "project"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addSeparator(menu7, "projStart"); //$NON-NLS-1$
			addMenuItem(menu7, "Op&en Project", null, null, "openProject", "org.eclipse.ui.project.openProject"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addMenuItem(menu7, "Clo&se Project", null, null, "closeProject", "org.eclipse.ui.project.closeProject"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addSeparator(menu7, "open.ext"); //$NON-NLS-1$
			addSeparator(menu7, null);
			addMenuItem(menu7, "Build &All", "org.eclipse.ui.ide", "$nl$/icons/full/etool16/build_exec.gif", "build", "org.eclipse.ui.project.buildAll"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			addMenuItem(menu7, "&Build Project", null, null, "buildProject", "org.eclipse.ui.project.buildProject"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			/*MMenuItem menu8 = */addMenu(menu7, "Build &Working Set", null, null, null, null); //$NON-NLS-1$
				//didn't find: org.eclipse.ui.internal.ide.actions.BuildSetMenu

			addMenuItem(menu7, "Clea&n...", null, null, "buildClean", "org.eclipse.ui.project.cleanAction"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addMenuItem(menu7, "Build Auto&matically", null, null, "buildAutomatically", "org.eclipse.ui.project.buildAutomatically"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addSeparator(menu7, "build.ext"); //$NON-NLS-1$
			addSeparator(menu7, null);
			addSeparator(menu7, "additions"); //$NON-NLS-1$
			addSeparator(menu7, "projEnd"); //$NON-NLS-1$
			addSeparator(menu7, null);
			addMenuItem(menu7, "&Properties", null, null, "projectProperties", "org.eclipse.ui.project.properties"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

//		addSeparator(menu0, "additions"); //$NON-NLS-1$

		MMenuItem menu9 = addMenu(menu0, "&Window", null, null, "window", "window"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addMenuItem(menu9, "&New MWindow", null, null, "openNewWindow", "org.eclipse.ui.window.newWindow"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addMenuItem(menu9, "New &Editor", null, null, "newEditor", "org.eclipse.ui.window.newEditor"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addSeparator(menu9, null);

			/*MMenuItem menu10 = */addMenu(menu9, "&Open MPerspective", null, null, "openPerspective", "openPerspective"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				//didn't find: org.eclipse.ui.internal.ChangeToPerspectivMenu


			/*MMenuItem menu11 = */addMenu(menu9, "Show &View", null, null, "showView", "showView"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				//didn't find: org.eclipse.ui.internal.ShowViewMenu

			addSeparator(menu9, null);
			addMenuItem(menu9, "Customi&ze MPerspective...", null, null, "editActionSets", "org.eclipse.ui.window.customizePerspective"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addMenuItem(menu9, "Save MPerspective &As...", null, null, "savePerspective", "org.eclipse.ui.window.savePerspective"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addMenuItem(menu9, "&Reset MPerspective...", null, null, "resetPerspective", "org.eclipse.ui.window.resetPerspective"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addMenuItem(menu9, "&Close MPerspective", null, null, "closePerspective", "org.eclipse.ui.window.closePerspective"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addMenuItem(menu9, "Close A&ll Perspectives", null, null, "closeAllPerspectives", "org.eclipse.ui.window.closeAllPerspectives"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addSeparator(menu9, null);

			MMenuItem menu12 = addMenu(menu9, "Navi&gation", null, null, "shortcuts", "shortcuts"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				addMenuItem(menu12, "Show &System MMenu", null, null, "showPartPanMenu", "org.eclipse.ui.window.showSystemMenu"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				addMenuItem(menu12, "Show View &MMenu", null, null, "showViewMenu", "org.eclipse.ui.window.showViewMenu"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				addMenuItem(menu12, "&Quick Access", null, null, "showQuickAccess", "org.eclipse.ui.window.quickAccess"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				addSeparator(menu12, null);
				addMenuItem(menu12, "Maximize Active View or Editor", null, null, "maximize", "org.eclipse.ui.window.maximizePart"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				addMenuItem(menu12, "Minimize Active View or Editor", null, null, "minimize", "org.eclipse.ui.window.minimizePart"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				addSeparator(menu12, null);
				addMenuItem(menu12, "&Activate Editor", null, null, "activateEditor", "org.eclipse.ui.window.activateEditor"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				addMenuItem(menu12, "Next &Editor", null, null, "nextEditor", "org.eclipse.ui.window.nextEditor"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				addMenuItem(menu12, "P&revious Editor", null, null, "previousEditor", "org.eclipse.ui.window.previousEditor"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				addMenuItem(menu12, "S&witch to Editor...", null, null, "showOpenEditors", "org.eclipse.ui.window.switchToEditor"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				addSeparator(menu12, null);
				addMenuItem(menu12, "Next &View", null, null, "nextPart", "org.eclipse.ui.window.nextView"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				addMenuItem(menu12, "Previ&ous View", null, null, "previousPart", "org.eclipse.ui.window.previousView"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				addSeparator(menu12, null);
				addMenuItem(menu12, "Next &MPerspective", null, null, "nextPerspective", "org.eclipse.ui.window.nextPerspective"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				addMenuItem(menu12, "Previo&us MPerspective", null, null, "previousPerspective", "org.eclipse.ui.window.previousPerspective"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			addSeparator(menu9, "additions"); //$NON-NLS-1$
			addMenuItem(menu9, "&Preferences", null, null, "preferences", null); //$NON-NLS-1$ //$NON-NLS-2$
			//didn't find: org.eclipse.ui.internal.SwitchToWindowMenu


			MMenuItem menu13 = addMenu(menu0, "&Help", null, null, "help", "help"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addSeparator(menu13, "group.intro"); //$NON-NLS-1$
			addMenuItem(menu13, "&Welcome", "org.eclipse.ui.intro", "$nl$/icons/welcome16.gif", "intro", "org.eclipse.ui.help.quickStartAction"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			addSeparator(menu13, "group.intro.ext"); //$NON-NLS-1$
			addSeparator(menu13, "group.main"); //$NON-NLS-1$
			addMenuItem(menu13, "&Help Contents", null, "platform:/plugin/org.eclipse.ui/icons/full/etool16/help_contents.gif", "helpContents", "org.eclipse.ui.help.helpContents"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
			addMenuItem(menu13, "S&earch", null, "platform:/plugin/org.eclipse.ui/icons/full/etool16/help_search.gif", "helpSearch", "org.eclipse.ui.help.helpSearch");  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$ 
			addMenuItem(menu13, "&Dynamic Help", null, null, "dynamicHelp", "org.eclipse.ui.help.dynamicHelp"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addSeparator(menu13, "group.assist"); //$NON-NLS-1$
			addMenuItem(menu13, "&Tips and Tricks...", null, null, "tipsAndTricks", "org.eclipse.ui.help.tipsAndTricksAction"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addSeparator(menu13, "helpStart"); //$NON-NLS-1$
			addSeparator(menu13, "group.main.ext"); //$NON-NLS-1$
			addSeparator(menu13, "group.tutorials"); //$NON-NLS-1$
			addSeparator(menu13, "group.tools"); //$NON-NLS-1$
			addSeparator(menu13, "group.updates"); //$NON-NLS-1$
			addSeparator(menu13, "helpEnd"); //$NON-NLS-1$
			addSeparator(menu13, "additions"); //$NON-NLS-1$
			addSeparator(menu13, "group.about"); //$NON-NLS-1$
			addMenuItem(menu13, "&About Eclipse SDK", null, null, "about", "org.eclipse.ui.help.aboutAction"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			addSeparator(menu13, "group.about.ext"); //$NON-NLS-1$
	}

	private static MMenuItem createMenuItem(String label, String imgPath, String id, String cmdId) {
		MMenuItem newItem = ApplicationFactory.eINSTANCE.createMMenuItem();
		newItem.setId(id);
		newItem.setName(label);
		newItem.setIconURI(imgPath);
		
		return newItem;
	}

	private static MToolBarItem createTBItem(String ttip, String imgPath, String id, String cmdId) {
		MToolBarItem newItem = ApplicationFactory.eINSTANCE.createMToolBarItem();
		newItem.setId(id);
		newItem.setTooltip(ttip);
		newItem.setIconURI(imgPath);
		
		return newItem;
	}
	
	public static MMenuItem addMenu(MMenu parentMenu,
			String label, String plugin, String imgPath, String id, String cmdId) {
		// Sub-menus are implemented as an item with a menu... ??
		MMenuItem newItem = createMenuItem(label, imgPath, id, cmdId);
		
		// Create the sub menu
		MMenu newMenu = ApplicationFactory.eINSTANCE.createMMenu();
		newMenu.setId(id);
		newItem.setMenu(newMenu);
		
		// Add the new item to the parent's menu
		parentMenu.getItems().add(newItem);

		return newItem;
	}
	
	public static MMenuItem addMenu(MMenuItem parentMenuItem,
			String label, String plugin, String imgPath, String id, String cmdId) {
		MMenu parentMenu = parentMenuItem.getMenu();
		return addMenu(parentMenu, label, plugin, imgPath, id, cmdId);
	}

	public static void addMenuItem(MMenuItem parentMenuItem,
			String label, String plugin, String imgPath, String id, String cmdId) {
		MMenuItem newItem = createMenuItem(label, imgPath, id, cmdId);
		MMenu parentMenu = parentMenuItem.getMenu();
		parentMenu.getItems().add(newItem);
	}

	public static void addSeparator(MMenuItem parentMenuItem, String id) {
		if (id != null)
			return;
		MMenuItem newItem = ApplicationFactory.eINSTANCE.createMMenuItem();
		newItem.setId(id);
		newItem.setSeparator(true);
		//newItem.setVisible(id == null);
		parentMenuItem.getMenu().getItems().add(newItem);
	}

	public static void loadToolbar(MToolBar tbModel) {
		MToolBarItem tbItem = createTBItem("&New	Alt+Shift+N", null, //$NON-NLS-1$
				"cmdId.New", "cmdId.New");   //$NON-NLS-1$//$NON-NLS-2$
		tbModel.getItems().add(tbItem);

		tbItem = createTBItem("&Save", //$NON-NLS-1$
				"platform:/plugin/org.eclipse.ui/icons/full/etool16/save_edit.gif", //$NON-NLS-1$
				"cmdId.Save", "cmdId.Save");   //$NON-NLS-1$//$NON-NLS-2$
		tbModel.getItems().add(tbItem);

		tbItem = createTBItem("&Print", //$NON-NLS-1$
				"platform:/plugin/org.eclipse.ui/icons/full/etool16/print_edit.gif", //$NON-NLS-1$
				"cmdId.Print", "cmdId.Print");   //$NON-NLS-1$//$NON-NLS-2$
		tbModel.getItems().add(tbItem);
	}
}
