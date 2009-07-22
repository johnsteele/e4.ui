/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.e4.workbench.ui.menus;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.ui.model.application.ApplicationFactory;
import org.eclipse.e4.ui.model.application.MHandledItem;
import org.eclipse.e4.ui.model.application.MMenu;
import org.eclipse.e4.ui.model.application.MMenuItem;
import org.eclipse.e4.ui.model.workbench.MMenuItemRenderer;
import org.eclipse.e4.ui.model.workbench.WorkbenchFactory;
import org.eclipse.e4.workbench.ui.internal.Activator;
import org.eclipse.e4.workbench.ui.internal.Policy;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.internal.ActionDescriptor;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.menus.CommandContributionItem;

/**
 * 
 */
public class ActionSet {
	private IEclipseContext context;
	private IConfigurationElement config;
	private boolean visible = true;

	private List<MHandledItem> items = new ArrayList<MHandledItem>();

	public ActionSet(IEclipseContext context, IConfigurationElement element) {
		this.context = context;
		config = element;
	}

	public boolean merge(MMenu menu) {
		IConfigurationElement[] menus = config
				.getChildren(IWorkbenchRegistryConstants.TAG_MENU);
		for (IConfigurationElement element : menus) {
			addMenu(menu, element);
		}
		IConfigurationElement[] actions = config
				.getChildren(IWorkbenchRegistryConstants.TAG_ACTION);
		for (IConfigurationElement element : actions) {
			addAction(menu, element);
		}
		return true;
	}

	/**
	 * @param menu
	 * @param element
	 */
	private void addAction(MMenu menu, IConfigurationElement element) {
		final String elementId = MenuHelper.getId(element);
		String path = element
				.getAttribute(IWorkbenchRegistryConstants.ATT_MENUBAR_PATH);
		if (path == null) {
			return;
		}
		Path menuPath = new Path(path);
		MMenu parentMenu = findMenuFromPath(menu, menuPath, 0);
		if (parentMenu == null) {
			Activator
					.trace(
							Policy.DEBUG_MENUS,
							"Failed to find menu for action " + elementId + ':' + path, null); //$NON-NLS-1$
			return;
		}
		int idx = MenuHelper.indexForId(parentMenu, menuPath.lastSegment());
		if (idx == -1) {
			idx = MenuHelper.indexForId(parentMenu,
					IWorkbenchActionConstants.MB_ADDITIONS);
		}
		if (idx == -1) {
			Activator
					.trace(
							Policy.DEBUG_MENUS,
							"Failed to find group for action " + elementId + ':' + path, null); //$NON-NLS-1$
			return;
		}
		MMenuItem item = createActionElement(element);
		parentMenu.getItems().add(idx, item);
	}

	/**
	 * @param menu
	 * @param element
	 */
	private void addMenu(MMenu menu, IConfigurationElement element) {
		final String elementId = MenuHelper.getId(element);
		String path = element
				.getAttribute(IWorkbenchRegistryConstants.ATT_PATH);
		if (path == null || path.length() == 0) {
			path = IWorkbenchActionConstants.MB_ADDITIONS;
		}
		Path menuPath = new Path(path);
		MMenu parentMenu = menu;
		if (menuPath.segmentCount() > 1) {
			parentMenu = findMenuFromPath(menu, menuPath, 0);
		}
		if (parentMenu == menu) {
			Activator
					.trace(
							Policy.DEBUG_MENUS,
							"Using parent menu for menu " + elementId + ':' + path, null); //$NON-NLS-1$
		}
		String id = MenuHelper.getId(element);
		MMenuItem item = null;
		final int itemIdx = MenuHelper.indexForId(parentMenu, id);
		if (itemIdx == -1) {
			int idx = MenuHelper.indexForId(parentMenu, menuPath.lastSegment());
			if (idx == -1) {
				idx = MenuHelper.indexForId(parentMenu,
						IWorkbenchActionConstants.MB_ADDITIONS);
			}
			if (idx == -1) {
				Activator
						.trace(
								Policy.DEBUG_MENUS,
								"Failed to find group for menu " + elementId + ':' + path, null); //$NON-NLS-1$
				return;
			}
			item = createMenuElement(element);
			parentMenu.getItems().add(idx + 1, item);
		} else {
			item = parentMenu.getItems().get(itemIdx);
		}
		processGroups(item.getMenu(), element);
	}

	/**
	 * @param menu
	 * @param element
	 */
	private void processGroups(MMenu menu, IConfigurationElement element) {
		IConfigurationElement[] children = element.getChildren();
		for (IConfigurationElement child : children) {
			String name = child
					.getAttribute(IWorkbenchRegistryConstants.ATT_NAME);
			if (MenuHelper.indexForId(menu, name) == -1) {
				MenuHelper.addSeparator(menu, name, true);
			}
		}
	}

	/**
	 * @param menu
	 * @param path
	 * @return
	 */
	private MMenu findMenuFromPath(MMenu menu, Path menuPath, int segment) {
		int idx = MenuHelper.indexForId(menu, menuPath.segment(segment));
		if (idx == -1) {
			if (segment + 1 < menuPath.segmentCount()
					|| !menuPath.hasTrailingSeparator()) {
				return null;
			}
			return menu;
		}
		MMenuItem item = menu.getItems().get(idx);
		if (item.getMenu() == null) {
			if (segment + 1 == menuPath.segmentCount()) {
				return menu;
			} else {
				return null;
			}
		}
		return findMenuFromPath(item.getMenu(), menuPath, segment + 1);
	}

	private MMenuItem createMenuElement(IConfigurationElement element) {
		String imagePath = MenuHelper.getImageUrl(MenuHelper
				.getIconDescriptor(element));
		String id = MenuHelper.getId(element);
		String label = MenuHelper.getLabel(element);
		MMenuItem item = MenuHelper.createMenuItem(context, label, imagePath,
				id, null);
		MMenu m = ApplicationFactory.eINSTANCE.createMMenu();
		m.setId(id);
		item.setMenu(m);
		return item;
	}

	private MMenuItem createActionElement(IConfigurationElement element) {
		String imagePath = MenuHelper.getImageUrl(MenuHelper
				.getIconDescriptor(element));
		String cmdId = MenuHelper.getActionSetCommandId(element);
		String id = MenuHelper.getId(element);
		String label = MenuHelper.getLabel(element);
		int style = MenuHelper.getStyle(element);
		if (style == CommandContributionItem.STYLE_PULLDOWN) {
			IWorkbenchWindow window = (IWorkbenchWindow) context
					.get(IWorkbenchWindow.class.getName());
			// we need to treat a pulldown action as a renderer, since the
			// action
			// itself contains the rendering code
			ActionDescriptor desc = new ActionDescriptor(element,
					ActionDescriptor.T_WORKBENCH_PULLDOWN, window);
			final ActionContributionItem item = new ActionContributionItem(desc
					.getAction());
			MMenuItemRenderer r = WorkbenchFactory.eINSTANCE
					.createMMenuItemRenderer();
			r.setId(item.getId() == null ? "item:" + id : item.getId()); //$NON-NLS-1$
			r.setRenderer(item);
			return r;
		}
		if (label == null) {
			if (cmdId == null) {
				label = "none:" + id; //$NON-NLS-1$
			} else {
				ICommandService cs = (ICommandService) context
						.get(ICommandService.class.getName());
				Command cmd = cs.getCommand(cmdId);
				if (cmd.isDefined()) {
					try {
						label = cmd.getName();
					} catch (NotDefinedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		MMenuItem item = MenuHelper.createMenuItem(context, label, imagePath,
				id, cmdId);
		items.add(item);
		return item;
	}

	public void setVisible(boolean v) {
		if (v == visible) {
			return;
		}
		visible = v;
		for (MHandledItem item : items) {
			item.setVisible(visible);
		}
	}

	public String getId() {
		return MenuHelper.getId(config);
	}
}
