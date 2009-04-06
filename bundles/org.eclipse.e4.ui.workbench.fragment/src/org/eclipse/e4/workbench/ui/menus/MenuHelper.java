package org.eclipse.e4.workbench.ui.menus;

import java.lang.reflect.Field;
import java.net.URL;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.extensions.ExtensionUtils;
import org.eclipse.e4.ui.model.application.ApplicationFactory;
import org.eclipse.e4.ui.model.application.MCommand;
import org.eclipse.e4.ui.model.application.MMenu;
import org.eclipse.e4.ui.model.application.MMenuItem;
import org.eclipse.e4.ui.model.application.MToolBar;
import org.eclipse.e4.ui.model.application.MToolBarItem;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.LegacyWBImpl;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.commands.ICommandImageService;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.internal.ide.WorkbenchActionBuilder;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.menus.CommandContributionItem;

public class MenuHelper {

	public static final String MAIN_MENU_ID = "org.eclipse.ui.main.menu"; //$NON-NLS-1$
	private static Field urlField;

	public static void loadMenu(IEclipseContext context, MMenu menuModel) {
		String id = menuModel.getId();
		if (id == null || id.length() == 0)
			return;

		if (id.indexOf(MAIN_MENU_ID) >= 0) {
			IActionBarConfigurer actionBarConfigurer = populateActionBarAdvisor(context);
			if (menuModel.getItems().size() == 0) {
				MenuManager barManager = (MenuManager) actionBarConfigurer
						.getMenuManager();
				processMenuManager(context, menuModel, barManager);
			}
		} else {
			populateMenu(menuModel);
		}
	}

	private static void printCEAtts(IConfigurationElement ce, String prefix) {
		String[] attNames = ce.getAttributeNames();
		for (int j = 0; j < attNames.length; j++) {
			System.out
					.println(prefix
							+ "Att: " + attNames[j] + " = " + ce.getAttribute(attNames[j])); //$NON-NLS-1$ //$NON-NLS-2$
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
		IConfigurationElement[] actionSets = ExtensionUtils
				.getExtensions(IWorkbenchRegistryConstants.PL_ACTION_SETS);
		for (int i = 0; i < actionSets.length; i++) {
			System.out.println("Action Set:" + actionSets[i].getName()); //$NON-NLS-1$
			printCEAtts(actionSets[i], "  "); //$NON-NLS-1$
			printCEKids(actionSets[i], "  "); //$NON-NLS-1$
		}
	}

	private static IActionBarConfigurer populateActionBarAdvisor(
			IEclipseContext context) {
		IWorkbench wb = (IWorkbench) context.get(IWorkbench.class.getName());
		LegacyActionBarConfigurer abc = new LegacyActionBarConfigurer(context,
				wb.getActiveWorkbenchWindow());
		WorkbenchActionBuilder builder = new WorkbenchActionBuilder(abc);
		// technically, this should be disposed, so per MWindow
		builder.fillActionBars(ActionBarAdvisor.FILL_MENU_BAR);
		return abc;
	}

	/**
	 * @param menu
	 * @param manager
	 */
	private static void processMenuManager(IEclipseContext context, MMenu menu,
			MenuManager manager) {
		IContributionItem[] items = manager.getItems();
		for (int i = 0; i < items.length; i++) {
			IContributionItem item = items[i];
			if (item instanceof MenuManager) {
				MenuManager m = (MenuManager) item;
				MMenuItem menu1 = addMenu(context, menu, m.getMenuText(), null,
						null, m.getId(), null);
				processMenuManager(context, menu1.getMenu(), m);
			} else if (item instanceof ActionContributionItem) {
				ActionContributionItem aci = (ActionContributionItem) item;
				IAction action = aci.getAction();
				String imageURL = getImageUrl(action.getImageDescriptor());
				addMenuItem(context, menu, action.getText(), null, imageURL,
						aci.getId(), action.getActionDefinitionId());
			} else if (item instanceof CommandContributionItem) {
				CommandContributionItem cci = (CommandContributionItem) item;
				String id = cci.getCommand().getId();
				ICommandService cs = (ICommandService) context
						.get(ICommandService.class.getName());
				Command cmd = cs.getCommand(id);
				if (cmd.isDefined()) {
					ICommandImageService cis = (ICommandImageService) context
							.get(ICommandImageService.class.getName());
					String imageURL = getImageUrl(cis.getImageDescriptor(cmd
							.getId(), ICommandImageService.TYPE_DEFAULT));
					try {
						addMenuItem(context, menu, cmd.getName(), null,
								imageURL, cci.getId(), id);
					} catch (NotDefinedException e) {
						// This should not happen
						e.printStackTrace();
					}
				} else {
					addMenuItem(context, menu,
							"unloaded:" + id, null, null, cci.getId(), id); //$NON-NLS-1$
				}
			} else if (item instanceof Separator) {
				addSeparator(menu, item.getId());
			}
		}
	}

	/**
	 * @param imageDescriptor
	 * @return
	 */
	private static String getImageUrl(ImageDescriptor imageDescriptor) {
		if (imageDescriptor == null)
			return null;
		Class idc = imageDescriptor.getClass();
		if (idc.getName().endsWith("URLImageDescriptor")) { //$NON-NLS-1$
			URL url = getUrl(idc, imageDescriptor);
			return url.toExternalForm();
		}
		return null;
	}

	private static URL getUrl(Class idc, ImageDescriptor imageDescriptor) {
		try {
			if (urlField == null) {
				urlField = idc.getDeclaredField("url"); //$NON-NLS-1$
				urlField.setAccessible(true);
			}
			return (URL) urlField.get(imageDescriptor);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static MMenuItem createMenuItem(IEclipseContext context,
			String label, String imgPath, String id, String cmdId) {
		MMenuItem newItem = ApplicationFactory.eINSTANCE.createMMenuItem();
		newItem.setId(id);
		newItem.setName(label);
		newItem.setIconURI(imgPath);
		if (cmdId != null) {
			LegacyWBImpl legacyWB = (LegacyWBImpl) context
					.get(LegacyWBImpl.class.getName());
			MCommand mcmd = legacyWB.commandsById.get(cmdId);
			if (mcmd != null) {
				newItem.setCommand(mcmd);
			} else {
				//				System.err.println("No MCommand defined for " + cmdId); //$NON-NLS-1$
			}
		} else {
			//			System.err.println("No command id for " + id); //$NON-NLS-1$
		}
		return newItem;
	}

	private static MToolBarItem createTBItem(String ttip, String imgPath,
			String id, String cmdId) {
		MToolBarItem newItem = ApplicationFactory.eINSTANCE
				.createMToolBarItem();
		newItem.setId(id);
		newItem.setTooltip(ttip);
		newItem.setIconURI(imgPath);

		return newItem;
	}

	public static MMenuItem addMenu(IEclipseContext context, MMenu parentMenu,
			String label, String plugin, String imgPath, String id, String cmdId) {
		// Sub-menus are implemented as an item with a menu... ??
		MMenuItem newItem = createMenuItem(context, label, imgPath, id, cmdId);

		// Create the sub menu
		MMenu newMenu = ApplicationFactory.eINSTANCE.createMMenu();
		newMenu.setId(id);
		newItem.setMenu(newMenu);

		// Add the new item to the parent's menu
		parentMenu.getItems().add(newItem);

		return newItem;
	}

	public static MMenuItem addMenu(MMenuItem parentMenuItem, String label,
			String plugin, String imgPath, String id, String cmdId) {
		MMenu parentMenu = parentMenuItem.getMenu();
		return addMenu(null, parentMenu, label, plugin, imgPath, id, cmdId);
	}

	public static void addMenuItem(MMenuItem parentMenuItem, String label,
			String plugin, String imgPath, String id, String cmdId) {
		MMenuItem newItem = createMenuItem(null, label, imgPath, id, cmdId);
		MMenu parentMenu = parentMenuItem.getMenu();
		parentMenu.getItems().add(newItem);
	}

	public static void addMenuItem(IEclipseContext context, MMenu parentMenu,
			String label, String plugin, String imgPath, String id, String cmdId) {
		MMenuItem newItem = createMenuItem(context, label, imgPath, id, cmdId);
		parentMenu.getItems().add(newItem);
	}

	public static void addSeparator(MMenuItem parentMenuItem, String id) {
		if (id != null)
			return;
		MMenuItem newItem = ApplicationFactory.eINSTANCE.createMMenuItem();
		newItem.setId(id);
		newItem.setSeparator(true);
		// newItem.setVisible(id == null);
		parentMenuItem.getMenu().getItems().add(newItem);
	}

	public static void addSeparator(MMenu parentMenu, String id) {
		if (id != null)
			return;
		MMenuItem newItem = ApplicationFactory.eINSTANCE.createMMenuItem();
		newItem.setId(id);
		newItem.setSeparator(true);

		parentMenu.getItems().add(newItem);
	}

	public static void loadToolbar(MToolBar tbModel) {
		MToolBarItem tbItem = createTBItem("&New	Alt+Shift+N", null, //$NON-NLS-1$
				"cmdId.New", "cmdId.New"); //$NON-NLS-1$//$NON-NLS-2$
		tbModel.getItems().add(tbItem);

		tbItem = createTBItem(
				"&Save", //$NON-NLS-1$
				"platform:/plugin/org.eclipse.ui/icons/full/etool16/save_edit.gif", //$NON-NLS-1$
				"cmdId.Save", "cmdId.Save"); //$NON-NLS-1$//$NON-NLS-2$
		tbModel.getItems().add(tbItem);

		tbItem = createTBItem(
				"&Print", //$NON-NLS-1$
				"platform:/plugin/org.eclipse.ui/icons/full/etool16/print_edit.gif", //$NON-NLS-1$
				"cmdId.Print", "cmdId.Print"); //$NON-NLS-1$//$NON-NLS-2$
		tbModel.getItems().add(tbItem);
	}
}
