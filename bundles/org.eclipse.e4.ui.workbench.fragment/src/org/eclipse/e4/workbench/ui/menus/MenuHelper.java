package org.eclipse.e4.workbench.ui.menus;

import org.eclipse.e4.ui.model.application.Menu;

public class MenuHelper {

	public static void loadMenu(Menu menuModel) {
//		String id = menuModel.getId();
//		if (id == null || id.length() == 0)
//			return;
//		
//		if (id.indexOf("org.eclipse.ui.main.menu") >= 0)
//			populateMainMenu(menuModel);
//		else
//			populateMenu(menuModel);
	}
	
//	private static void printCEAtts(IConfigurationElement ce, String prefix) {
//		String[] attNames = ce.getAttributeNames();
//		for (int j = 0; j < attNames.length; j++) {
//			System.out.println(prefix + "Att: " + attNames[j] + " = " + ce.getAttribute(attNames[j]));
//		}
//	}
//	
//	private static void printCEKids(IConfigurationElement ce, String prefix) {
//		IConfigurationElement[] kids = ce.getChildren();
//		for (int i = 0; i < kids.length; i++) {
//			System.out.println(prefix + "Child: " + kids[i].getName());
//			printCEAtts(kids[i], prefix + "  ");
//			printCEKids(kids[i], prefix + "  ");
//		}
//	}
//	
//	private static void populateMenu(Menu menuModel) {
//		String menuId = menuModel.getId();
//		IConfigurationElement[] actionSets = ExtensionUtils.getExtensions(IWorkbenchRegistryConstants.PL_ACTION_SETS);
//		for (int i = 0; i < actionSets.length; i++) {
//			System.out.println("Action Set:" + actionSets[i].getName());
//			printCEAtts(actionSets[i], "  ");
//			printCEKids(actionSets[i], "  ");
//		}
//	}
//	
//	private static void populateMainMenu(Menu menu0) {
//
//		Menu menu1 = addMenu(menu0, "&File", null, null, "file", "file");
//			addSeparator(menu1, "fileStart");
//			Menu menu2 = addMenu(menu1, "&New	Alt+Shift+N", null, null, "new", "new");
//				addSeparator(menu2, "new");
//				//didn't find: org.eclipse.ui.actions.NewWizardMenu
//				addSeparator(menu2, "additions");
//
//			addSeparator(menu1, "new.ext");
//			addSeparator(menu1, null);
//			addMenuItem(menu1, "&Close", null, null, "close", "org.eclipse.ui.file.close");
//			addMenuItem(menu1, "C&lose All", null, null, "closeAll", "org.eclipse.ui.file.closeAll");
//			addSeparator(menu1, "close.ext");
//			addSeparator(menu1, null);
//			addMenuItem(menu1, "&Save", "org.eclipse.ui", "$nl$/icons/full/etool16/save_edit.gif", "save", "org.eclipse.ui.file.save");
//			addMenuItem(menu1, "Save &As...", "org.eclipse.ui", "$nl$/icons/full/etool16/saveas_edit.gif", "saveAs", "org.eclipse.ui.file.saveAs");
//			addMenuItem(menu1, "Sav&e All", "org.eclipse.ui", "$nl$/icons/full/etool16/saveall_edit.gif", "saveAll", "org.eclipse.ui.file.saveAll");
//			addMenuItem(menu1, "Rever&t", null, null, "revert", "org.eclipse.ui.file.revert");
//			addSeparator(menu1, null);
//			addMenuItem(menu1, "Mo&ve...", null, null, "move", "org.eclipse.ui.edit.move");
//			addMenuItem(menu1, "Rena&me...", null, null, "rename", "org.eclipse.ui.edit.rename");
//			addMenuItem(menu1, "Re&fresh", "org.eclipse.ui", "$nl$/icons/full/elcl16/refresh_nav.gif", "refresh", "org.eclipse.ui.file.refresh");
//			addSeparator(menu1, "save.ext");
//			addSeparator(menu1, null);
//			addMenuItem(menu1, "&Print...", "org.eclipse.ui", "$nl$/icons/full/etool16/print_edit.gif", "print", "org.eclipse.ui.file.print");
//			addSeparator(menu1, "print.ext");
//			addSeparator(menu1, null);
//			addMenuItem(menu1, "Switch &Workspace", null, null, "openWorkspace", "org.eclipse.ui.file.openWorkspace");
//			addSeparator(menu1, "open.ext");
//			addSeparator(menu1, null);
//			addMenuItem(menu1, "&Import...", "org.eclipse.ui", "$nl$/icons/full/etool16/import_wiz.gif", "import", "org.eclipse.ui.file.import");
//			addMenuItem(menu1, "E&xport...", "org.eclipse.ui", "$nl$/icons/full/etool16/export_wiz.gif", "export", "org.eclipse.ui.file.export");
//			addSeparator(menu1, "import.ext");
//			addSeparator(menu1, "additions");
//			addSeparator(menu1, null);
//			addMenuItem(menu1, "P&roperties", null, null, "properties", "org.eclipse.ui.file.properties");
//			//didn't find: org.eclipse.ui.internal.ReopenEditorMenu
//			addSeparator(menu1, "mru");
//			addSeparator(menu1, null);
//			addMenuItem(menu1, "E&xit", null, null, "quit", "org.eclipse.ui.file.exit");
//			addSeparator(menu1, "fileEnd");
//
//
//		Menu menu3 = addMenu(menu0, "&Edit", null, null, "edit", "edit");
//			addSeparator(menu3, "editStart");
//			addMenuItem(menu3, "&Undo", "org.eclipse.ui", "$nl$/icons/full/etool16/undo_edit.gif", "undo", "org.eclipse.ui.edit.undo");
//			addMenuItem(menu3, "&Redo", "org.eclipse.ui", "$nl$/icons/full/etool16/redo_edit.gif", "redo", "org.eclipse.ui.edit.redo");
//			addSeparator(menu3, "undo.ext");
//			addSeparator(menu3, null);
//			addMenuItem(menu3, "Cu&t", "org.eclipse.ui", "$nl$/icons/full/etool16/cut_edit.gif", "cut", "org.eclipse.ui.edit.cut");
//			addMenuItem(menu3, "&Copy", "org.eclipse.ui", "$nl$/icons/full/etool16/copy_edit.gif", "copy", "org.eclipse.ui.edit.copy");
//			addMenuItem(menu3, "&Paste", "org.eclipse.ui", "$nl$/icons/full/etool16/paste_edit.gif", "paste", "org.eclipse.ui.edit.paste");
//			addSeparator(menu3, "cut.ext");
//			addSeparator(menu3, null);
//			addMenuItem(menu3, "&Delete", "org.eclipse.ui", "$nl$/icons/full/etool16/delete_edit.gif", "delete", "org.eclipse.ui.edit.delete");
//			addMenuItem(menu3, "Select &All", null, null, "selectAll", "org.eclipse.ui.edit.selectAll");
//			addSeparator(menu3, null);
//			addMenuItem(menu3, "&Find/Replace...", null, null, "find", "org.eclipse.ui.edit.findReplace");
//			addSeparator(menu3, "find.ext");
//			addSeparator(menu3, null);
//			addMenuItem(menu3, "Add Bookmar&k...", null, null, "bookmark", "org.eclipse.ui.edit.addBookmark");
//			addMenuItem(menu3, "Add Ta&sk...", null, null, "addTask", "org.eclipse.ui.edit.addTask");
//			addSeparator(menu3, "add.ext");
//			addSeparator(menu3, "editEnd");
//			addSeparator(menu3, "additions");
//
//
//			Menu menu4 = addMenu(menu0, "&Navigate", null, null, "navigate", "navigate");
//			addSeparator(menu4, "navStart");
//			addMenuItem(menu4, "Go &Into", null, null, "goInto", "org.eclipse.ui.navigate.goInto");
//
//			Menu menu5 = addMenu(menu4, "&Go To", null, null, "goTo", "goTo");
//				addMenuItem(menu5, "&Back", "org.eclipse.ui", "$nl$/icons/full/elcl16/backward_nav.gif", "back", "org.eclipse.ui.navigate.back");
//				addMenuItem(menu5, "&Forward", "org.eclipse.ui", "$nl$/icons/full/elcl16/forward_nav.gif", "forward", "org.eclipse.ui.navigate.forward");
//				addMenuItem(menu5, "&Up One Level", "org.eclipse.ui", "$nl$/icons/full/elcl16/up_nav.gif", "up", "org.eclipse.ui.navigate.up");
//				addSeparator(menu5, "additions");
//
//			addSeparator(menu4, "open.ext");
//			addSeparator(menu4, "open.ext2");
//			addSeparator(menu4, "open.ext3");
//			addSeparator(menu4, "open.ext4");
//			addSeparator(menu4, "show.ext");
//
//			Menu menu6 = addMenu(menu4, "Sho&w In	Alt+Shift+W", null, null, "showIn", "showIn");
//				//didn't find: org.eclipse.ui.internal.ShowInMenu
//
//			addSeparator(menu4, "show.ext2");
//			addSeparator(menu4, "show.ext3");
//			addSeparator(menu4, "show.ext4");
//			addSeparator(menu4, null);
//			addMenuItem(menu4, "Ne&xt", "org.eclipse.ui.ide", "$nl$/icons/full/etool16/next_nav.gif", "next", "org.eclipse.ui.navigate.next");
//			addMenuItem(menu4, "Pre&vious", "org.eclipse.ui.ide", "$nl$/icons/full/etool16/prev_nav.gif", "previous", "org.eclipse.ui.navigate.previous");
//			addSeparator(menu4, "additions");
//			addSeparator(menu4, "navEnd");
//			addSeparator(menu4, null);
//			addMenuItem(menu4, "&Back", "org.eclipse.ui", "$nl$/icons/full/elcl16/backward_nav.gif", "backardHistory", "org.eclipse.ui.navigate.backwardHistory");
//			addMenuItem(menu4, "&Forward", "org.eclipse.ui", "$nl$/icons/full/elcl16/forward_nav.gif", "forwardHistory", "org.eclipse.ui.navigate.forwardHistory");
//
//
//			Menu menu7 = addMenu(menu0, "&Project", null, null, "project", "project");
//			addSeparator(menu7, "projStart");
//			addMenuItem(menu7, "Op&en Project", null, null, "openProject", "org.eclipse.ui.project.openProject");
//			addMenuItem(menu7, "Clo&se Project", null, null, "closeProject", "org.eclipse.ui.project.closeProject");
//			addSeparator(menu7, "open.ext");
//			addSeparator(menu7, null);
//			addMenuItem(menu7, "Build &All", "org.eclipse.ui.ide", "$nl$/icons/full/etool16/build_exec.gif", "build", "org.eclipse.ui.project.buildAll");
//			addMenuItem(menu7, "&Build Project", null, null, "buildProject", "org.eclipse.ui.project.buildProject");
//
//			Menu menu8 = addMenu(menu7, "Build &Working Set", null, null, null, null);
//				//didn't find: org.eclipse.ui.internal.ide.actions.BuildSetMenu
//
//			addMenuItem(menu7, "Clea&n...", null, null, "buildClean", "org.eclipse.ui.project.cleanAction");
//			addMenuItem(menu7, "Build Auto&matically", null, null, "buildAutomatically", "org.eclipse.ui.project.buildAutomatically");
//			addSeparator(menu7, "build.ext");
//			addSeparator(menu7, null);
//			addSeparator(menu7, "additions");
//			addSeparator(menu7, "projEnd");
//			addSeparator(menu7, null);
//			addMenuItem(menu7, "&Properties", null, null, "projectProperties", "org.eclipse.ui.project.properties");
//
//		addSeparator(menu0, "additions");
//
//		Menu menu9 = addMenu(menu0, "&Window", null, null, "window", "window");
//			addMenuItem(menu9, "&New Window", null, null, "openNewWindow", "org.eclipse.ui.window.newWindow");
//			addMenuItem(menu9, "New &Editor", null, null, "newEditor", "org.eclipse.ui.window.newEditor");
//			addSeparator(menu9, null);
//
//			Menu menu10 = addMenu(menu9, "&Open Perspective", null, null, "openPerspective", "openPerspective");
//				//didn't find: org.eclipse.ui.internal.ChangeToPerspectivMenu
//
//
//			Menu menu11 = addMenu(menu9, "Show &View", null, null, "showView", "showView");
//				//didn't find: org.eclipse.ui.internal.ShowViewMenu
//
//			addSeparator(menu9, null);
//			addMenuItem(menu9, "Customi&ze Perspective...", null, null, "editActionSets", "org.eclipse.ui.window.customizePerspective");
//			addMenuItem(menu9, "Save Perspective &As...", null, null, "savePerspective", "org.eclipse.ui.window.savePerspective");
//			addMenuItem(menu9, "&Reset Perspective...", null, null, "resetPerspective", "org.eclipse.ui.window.resetPerspective");
//			addMenuItem(menu9, "&Close Perspective", null, null, "closePerspective", "org.eclipse.ui.window.closePerspective");
//			addMenuItem(menu9, "Close A&ll Perspectives", null, null, "closeAllPerspectives", "org.eclipse.ui.window.closeAllPerspectives");
//			addSeparator(menu9, null);
//
//			Menu menu12 = addMenu(menu9, "Navi&gation", null, null, "shortcuts", "shortcuts");
//				addMenuItem(menu12, "Show &System Menu", null, null, "showPartPanMenu", "org.eclipse.ui.window.showSystemMenu");
//				addMenuItem(menu12, "Show View &Menu", null, null, "showViewMenu", "org.eclipse.ui.window.showViewMenu");
//				addMenuItem(menu12, "&Quick Access", null, null, "showQuickAccess", "org.eclipse.ui.window.quickAccess");
//				addSeparator(menu12, null);
//				addMenuItem(menu12, "Maximize Active View or Editor", null, null, "maximize", "org.eclipse.ui.window.maximizePart");
//				addMenuItem(menu12, "Minimize Active View or Editor", null, null, "minimize", "org.eclipse.ui.window.minimizePart");
//				addSeparator(menu12, null);
//				addMenuItem(menu12, "&Activate Editor", null, null, "activateEditor", "org.eclipse.ui.window.activateEditor");
//				addMenuItem(menu12, "Next &Editor", null, null, "nextEditor", "org.eclipse.ui.window.nextEditor");
//				addMenuItem(menu12, "P&revious Editor", null, null, "previousEditor", "org.eclipse.ui.window.previousEditor");
//				addMenuItem(menu12, "S&witch to Editor...", null, null, "showOpenEditors", "org.eclipse.ui.window.switchToEditor");
//				addSeparator(menu12, null);
//				addMenuItem(menu12, "Next &View", null, null, "nextPart", "org.eclipse.ui.window.nextView");
//				addMenuItem(menu12, "Previ&ous View", null, null, "previousPart", "org.eclipse.ui.window.previousView");
//				addSeparator(menu12, null);
//				addMenuItem(menu12, "Next &Perspective", null, null, "nextPerspective", "org.eclipse.ui.window.nextPerspective");
//				addMenuItem(menu12, "Previo&us Perspective", null, null, "previousPerspective", "org.eclipse.ui.window.previousPerspective");
//
//			addSeparator(menu9, "additions");
//			addMenuItem(menu9, "&Preferences", null, null, "preferences", null);
//			//didn't find: org.eclipse.ui.internal.SwitchToWindowMenu
//
//
//			Menu menu13 = addMenu(menu0, "&Help", null, null, "help", "help");
//			addSeparator(menu13, "group.intro");
//			addMenuItem(menu13, "&Welcome", "org.eclipse.ui.intro", "$nl$/icons/welcome16.gif", "intro", "org.eclipse.ui.help.quickStartAction");
//			addSeparator(menu13, "group.intro.ext");
//			addSeparator(menu13, "group.main");
//			addMenuItem(menu13, "&Help Contents", "org.eclipse.ui", "$nl$/icons/full/etool16/help_contents.gif", "helpContents", "org.eclipse.ui.help.helpContents");
//			addMenuItem(menu13, "S&earch", "org.eclipse.ui", "$nl$/icons/full/etool16/help_search.gif", "helpSearch", "org.eclipse.ui.help.helpSearch");
//			addMenuItem(menu13, "&Dynamic Help", null, null, "dynamicHelp", "org.eclipse.ui.help.dynamicHelp");
//			addSeparator(menu13, "group.assist");
//			addMenuItem(menu13, "&Tips and Tricks...", null, null, "tipsAndTricks", "org.eclipse.ui.help.tipsAndTricksAction");
//			addSeparator(menu13, "helpStart");
//			addSeparator(menu13, "group.main.ext");
//			addSeparator(menu13, "group.tutorials");
//			addSeparator(menu13, "group.tools");
//			addSeparator(menu13, "group.updates");
//			addSeparator(menu13, "helpEnd");
//			addSeparator(menu13, "additions");
//			addSeparator(menu13, "group.about");
//			addMenuItem(menu13, "&About Eclipse SDK", null, null, "about", "org.eclipse.ui.help.aboutAction");
//			addSeparator(menu13, "group.about.ext");
//	}
//
//	private static Menu addMenu(Menu parentMenu,
//			String label, String plugin, String imgPath, String id, String cmdId) {
//		Menu newMenu = ApplicationFactory.eINSTANCE.createMenu(); // WorkbenchFactory.eINSTANCE.createMenu();
//		//newMenu.setPlugin(plugin);
//		newMenu.setId(id);
//		newMenu.setName(label);
//		newMenu.setIconPath(imgPath);
//		parentMenu.getItems().add(newMenu);
//		
//		return newMenu;
//	}
//
//	private static void addMenuItem(Menu parentMenu,
//			String label, String plugin, String imgPath, String id, String cmdId) {
//		MenuItem newMenuItem = WorkbenchFactory.eINSTANCE.createItemModel();
//		//newMenuItem.setPlugin(plugin);
//		newMenuItem.setId(id);
//		newMenuItem.setName(label);
//		newMenuItem.setIconURI(imgPath);
//		parentMenu.getItems().add(newMenuItem);
//	}
//
//	public static void addSeparator(Menu parentMenu, String id) {
//		ItemModel newMenuItem = WorkbenchFactory.eINSTANCE.createItemModel();
//		newMenuItem.setId(id);
//		
//		if (id == null)
//			newMenuItem.setLabel("[Separator]");
//		else
//			newMenuItem.setLabel("[Group] " + id);
//			
//		newMenuItem.setSeparator(true);
//		newMenuItem.setVisible(id == null);
//		parentMenu.getChildren().add(newMenuItem);
//	}
}
