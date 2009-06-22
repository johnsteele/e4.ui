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

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.ui.model.application.ApplicationFactory;
import org.eclipse.e4.ui.model.application.MMenu;
import org.eclipse.e4.ui.model.application.MMenuItem;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;

/**
 * 
 */
public class ActionSet {
	private IEclipseContext context;
	private IConfigurationElement config;

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
		String path = element
				.getAttribute(IWorkbenchRegistryConstants.ATT_MENUBAR_PATH);
		if (path == null) {
			return;
		}
		Path menuPath = new Path(path);
		MMenu subMenu = findMenuFromPath(menu, menuPath, 0);
		if (subMenu == null) {
			System.err.println("Failed to find menu for " + path); //$NON-NLS-1$
			return;
		}
		int idx = MenuHelper.indexForId(subMenu, menuPath.lastSegment());
		if (idx == -1) {
			idx = MenuHelper.indexForId(subMenu,
					IWorkbenchActionConstants.MB_ADDITIONS);
		}
		if (idx == -1) {
			System.err.println("Failed to find group for " + path); //$NON-NLS-1$
			return;
		}
		MMenuItem item = createActionElement(element);
		subMenu.getItems().add(idx, item);
	}

	/**
	 * @param menu
	 * @param element
	 */
	private void addMenu(MMenu menu, IConfigurationElement element) {
		String path = element
				.getAttribute(IWorkbenchRegistryConstants.ATT_PATH);
		if (path == null || path.length() == 0) {
			path = IWorkbenchActionConstants.MB_ADDITIONS;
		}
		Path menuPath = new Path(path);
		MMenu subMenu = findMenuFromPath(menu, menuPath, 0);
		if (subMenu == null) {
			System.err.println("Failed to find menu for " + path); //$NON-NLS-1$
			return;
		}
		int idx = MenuHelper.indexForId(subMenu, menuPath.lastSegment());
		if (idx == -1) {
			idx = MenuHelper.indexForId(subMenu,
					IWorkbenchActionConstants.MB_ADDITIONS);
		}
		if (idx == -1) {
			System.err.println("Failed to find group for " + path); //$NON-NLS-1$
			return;
		}
		MMenuItem item = createMenuElement(element);
		if (MenuHelper.indexForId(subMenu, item.getId()) == -1) {
			subMenu.getItems().add(idx, item);
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
			return null;
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
		String cmdId = MenuHelper.getDefinitionId(element);
		String id = MenuHelper.getId(element);
		String label = MenuHelper.getLabel(element);
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
		return item;
	}

}
