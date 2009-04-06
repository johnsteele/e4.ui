package org.eclipse.e4.workbench.ui.menus;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
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
import org.eclipse.ui.plugin.AbstractUIPlugin;

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
				processMenuManager(context, menuModel, barManager.getItems());
				MenuContribution[] contributions = loadMenuContributions(context);
				processMenuContributions(context, menuModel, contributions);
			}
		} else {
			populateMenu(menuModel);
		}
	}

	/**
	 * @param context
	 * @param menuModel
	 */
	private static void processMenuContributions(IEclipseContext context,
			MMenu menuBar, MenuContribution[] contributions) {
		HashSet<MenuContribution> processedContributions = new HashSet<MenuContribution>();
		int size = -1;
		while (size != processedContributions.size()) {
			size = processedContributions.size();
			for (MenuContribution contribution : contributions) {
				if (!processedContributions.contains(contribution)) {
					if (contribution.merge(menuBar)) {
						processedContributions.add(contribution);
					}
				}
			}
		}
		// now, what about sub menus
		EList<MMenuItem> items = menuBar.getItems();
		for (MMenuItem menuItem : items) {
			if (menuItem.getMenu() != null) {
				processMenuContributions(context, menuItem.getMenu(),
						contributions);
			}
		}
	}

	/**
	 * @param context
	 */
	private static MenuContribution[] loadMenuContributions(
			IEclipseContext context) {
		ArrayList<MenuContribution> contributions = new ArrayList<MenuContribution>();
		IConfigurationElement[] elements = ExtensionUtils
				.getExtensions(IWorkbenchRegistryConstants.PL_MENUS);
		for (IConfigurationElement element : elements) {
			if (element.getName().equals(
					IWorkbenchRegistryConstants.PL_MENU_CONTRIBUTION)) {
				contributions.add(new MenuContribution(context, element));
			}
		}
		return contributions
				.toArray(new MenuContribution[contributions.size()]);
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
	public static void processMenuManager(IEclipseContext context, MMenu menu,
			IContributionItem[] items) {

		for (int i = 0; i < items.length; i++) {
			IContributionItem item = items[i];
			if (item instanceof MenuManager) {
				MenuManager m = (MenuManager) item;
				MMenuItem menu1 = addMenu(context, menu, m.getMenuText(), null,
						null, m.getId(), null);
				processMenuManager(context, menu1.getMenu(), m.getItems());
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
			} else if (item instanceof GroupMarker) {
				addSeparator(menu, item.getId());
			}
		}
	}

	/**
	 * @param imageDescriptor
	 * @return
	 */
	public static String getImageUrl(ImageDescriptor imageDescriptor) {
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

	public static MMenuItem createMenuItem(IEclipseContext context,
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

	static boolean getVisibleEnabled(IConfigurationElement element) {
		IConfigurationElement[] children = element
				.getChildren(IWorkbenchRegistryConstants.TAG_VISIBLE_WHEN);
		String checkEnabled = null;

		if (children.length > 0) {
			checkEnabled = children[0]
					.getAttribute(IWorkbenchRegistryConstants.ATT_CHECK_ENABLED);
		}

		return checkEnabled != null && checkEnabled.equalsIgnoreCase("true"); //$NON-NLS-1$
	}

	/*
	 * Support Utilities
	 */
	public static String getId(IConfigurationElement element) {
		String id = element.getAttribute(IWorkbenchRegistryConstants.ATT_ID);

		// For sub-menu management -all- items must be id'd so enforce this
		// here (we could optimize by checking the 'name' of the config
		// element == "menu"
		if (id == null || id.length() == 0) {
			id = getCommandId(element);
		}
		if (id == null || id.length() == 0) {
			id = element.toString();
		}

		return id;
	}

	public static String getName(IConfigurationElement element) {
		return element.getAttribute(IWorkbenchRegistryConstants.ATT_NAME);
	}

	public static int getMode(IConfigurationElement element) {
		if ("FORCE_TEXT".equals(element.getAttribute(IWorkbenchRegistryConstants.ATT_MODE))) { //$NON-NLS-1$
			return CommandContributionItem.MODE_FORCE_TEXT;
		}
		return 0;
	}

	public static String getLabel(IConfigurationElement element) {
		return element.getAttribute(IWorkbenchRegistryConstants.ATT_LABEL);
	}

	public static String getMnemonic(IConfigurationElement element) {
		return element.getAttribute(IWorkbenchRegistryConstants.ATT_MNEMONIC);
	}

	public static String getTooltip(IConfigurationElement element) {
		return element.getAttribute(IWorkbenchRegistryConstants.ATT_TOOLTIP);
	}

	public static String getIconPath(IConfigurationElement element) {
		return element.getAttribute(IWorkbenchRegistryConstants.ATT_ICON);
	}

	public static String getDisabledIconPath(IConfigurationElement element) {
		return element
				.getAttribute(IWorkbenchRegistryConstants.ATT_DISABLEDICON);
	}

	public static String getHoverIconPath(IConfigurationElement element) {
		return element.getAttribute(IWorkbenchRegistryConstants.ATT_HOVERICON);
	}

	public static ImageDescriptor getIconDescriptor(
			IConfigurationElement element) {
		String extendingPluginId = element.getDeclaringExtension()
				.getContributor().getName();

		String iconPath = getIconPath(element);
		if (iconPath != null) {
			return AbstractUIPlugin.imageDescriptorFromPlugin(
					extendingPluginId, iconPath);
		}
		return null;
	}

	public static ImageDescriptor getDisabledIconDescriptor(
			IConfigurationElement element) {
		String extendingPluginId = element.getDeclaringExtension()
				.getContributor().getName();

		String iconPath = getDisabledIconPath(element);
		if (iconPath != null) {
			return AbstractUIPlugin.imageDescriptorFromPlugin(
					extendingPluginId, iconPath);
		}
		return null;
	}

	public static ImageDescriptor getHoverIconDescriptor(
			IConfigurationElement element) {
		String extendingPluginId = element.getDeclaringExtension()
				.getContributor().getName();

		String iconPath = getHoverIconPath(element);
		if (iconPath != null) {
			return AbstractUIPlugin.imageDescriptorFromPlugin(
					extendingPluginId, iconPath);
		}
		return null;
	}

	public static String getHelpContextId(IConfigurationElement element) {
		return element
				.getAttribute(IWorkbenchRegistryConstants.ATT_HELP_CONTEXT_ID);
	}

	public static boolean isSeparatorVisible(IConfigurationElement element) {
		String val = element
				.getAttribute(IWorkbenchRegistryConstants.ATT_VISIBLE);
		return Boolean.valueOf(val).booleanValue();
	}

	public static String getClassSpec(IConfigurationElement element) {
		return element.getAttribute(IWorkbenchRegistryConstants.ATT_CLASS);
	}

	public static String getCommandId(IConfigurationElement element) {
		return element.getAttribute(IWorkbenchRegistryConstants.ATT_COMMAND_ID);
	}

	public static int getStyle(IConfigurationElement element) {
		String style = element
				.getAttribute(IWorkbenchRegistryConstants.ATT_STYLE);
		if (style == null || style.length() == 0) {
			return CommandContributionItem.STYLE_PUSH;
		}
		if (IWorkbenchRegistryConstants.STYLE_TOGGLE.equals(style)) {
			return CommandContributionItem.STYLE_CHECK;
		}
		if (IWorkbenchRegistryConstants.STYLE_RADIO.equals(style)) {
			return CommandContributionItem.STYLE_RADIO;
		}
		if (IWorkbenchRegistryConstants.STYLE_PULLDOWN.equals(style)) {
			return CommandContributionItem.STYLE_PULLDOWN;
		}
		return CommandContributionItem.STYLE_PUSH;
	}

	/**
	 * @param element
	 * @return A map of parameters names to parameter values. All Strings. The
	 *         map may be empty.
	 */
	public static Map getParameters(IConfigurationElement element) {
		HashMap map = new HashMap();
		IConfigurationElement[] parameters = element
				.getChildren(IWorkbenchRegistryConstants.TAG_PARAMETER);
		for (int i = 0; i < parameters.length; i++) {
			String name = parameters[i]
					.getAttribute(IWorkbenchRegistryConstants.ATT_NAME);
			String value = parameters[i]
					.getAttribute(IWorkbenchRegistryConstants.ATT_VALUE);
			if (name != null && value != null) {
				map.put(name, value);
			}
		}
		return map;
	}
}
