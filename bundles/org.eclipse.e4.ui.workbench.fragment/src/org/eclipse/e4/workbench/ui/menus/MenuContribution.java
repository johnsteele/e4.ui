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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.ui.model.application.ApplicationFactory;
import org.eclipse.e4.ui.model.application.MMenu;
import org.eclipse.e4.ui.model.application.MMenuItem;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.internal.menus.MenuLocationURI;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.internal.util.Util;
import org.eclipse.ui.menus.IWorkbenchContribution;

/**
 * @since 3.3
 * 
 */
public class MenuContribution {
	private IConfigurationElement config;
	private IEclipseContext context;
	private MenuLocationURI uri;
	private MMenu model;

	public MenuContribution(IEclipseContext context,
			IConfigurationElement element) {
		this.context = context;
		window = (IWorkbenchWindow) context.get(IWorkbenchWindow.class
				.getName());
		config = element;
		uri = new MenuLocationURI(config
				.getAttribute(IWorkbenchRegistryConstants.TAG_LOCATION_URI));
	}

	public MenuLocationURI getURI() {
		return uri;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return MenuContribution.class.getName() + ": " //$NON-NLS-1$
				+ getURI();
	}

	public boolean merge(MMenu menu) {
		String locationID = getURI().getPath();
		if (locationID.equals(menu.getId())) {
			loadModel();
			return mergeModel(menu);
		}
		return false;
	}

	private boolean mergeModel(MMenu menu) {
		int idx = getInsertionIndex(menu);
		if (idx == -1) {
			return false;
		}
		EList<MMenuItem> items = menu.getItems();
		MMenuItem[] modelItems = model.getItems().toArray(
				new MMenuItem[model.getItems().size()]);
		for (int i = 0; i < modelItems.length; i++) {
			MMenuItem modelItem = modelItems[i];
			items.add(idx++, modelItem);
		}
		return true;
	}

	private int getInsertionIndex(MMenu menu) {
		int additionsIndex = -1;
		String query = getURI().getQuery();
		if (query.length() == 0 || query.equals("after=additions")) { //$NON-NLS-1$
			additionsIndex = MenuHelper.indexForId(menu, "additions"); //$NON-NLS-1$
			if (additionsIndex == -1) {
				additionsIndex = menu.getItems().size();
			} else {
				additionsIndex++;
			}
		} else {
			String[] queryParts = Util.split(query, '=');
			if (queryParts.length > 1 && queryParts[1].length() > 0) {
				additionsIndex = MenuHelper.indexForId(menu, queryParts[1]);
				if (additionsIndex != -1 && queryParts[0].equals("after")) //$NON-NLS-1$
					additionsIndex++;
			}
		}
		return additionsIndex;
	}

	private void loadModel() {
		model = ApplicationFactory.eINSTANCE.createMMenu();
		loadModel(config, model);
	}

	private void loadModel(IConfigurationElement menuElement, MMenu menu) {
		IConfigurationElement[] children = menuElement.getChildren();
		for (IConfigurationElement element : children) {
			String elementType = element.getName();
			if (IWorkbenchRegistryConstants.TAG_COMMAND.equals(elementType)) {
				MMenuItem item = createCommandElement(element);
				menu.getItems().add(item);
			} else if (IWorkbenchRegistryConstants.TAG_MENU.equals(elementType)) {
				MMenuItem item = createMenuElement(element);
				menu.getItems().add(item);
			} else if (IWorkbenchRegistryConstants.TAG_SEPARATOR
					.equals(elementType)) {
				MenuHelper.addSeparator(menu, MenuHelper.getId(element));
			} else if (IWorkbenchRegistryConstants.TAG_DYNAMIC
					.equals(elementType)) {
				ContributionItem i = (ContributionItem) Util
						.safeLoadExecutableExtension(element,
								IWorkbenchRegistryConstants.ATT_CLASS,
								ContributionItem.class);
				if (i instanceof IWorkbenchContribution) {
					((IWorkbenchContribution) i).initialize(window);
				}
				if (i instanceof CompoundContributionItem) {
					add(menu, (CompoundContributionItem) i);
				}
			}
		}
	}

	/**
	 * @param menu
	 * @param i
	 */
	private void add(MMenu menu, CompoundContributionItem i) {
		IContributionItem[] items = getItems(i);
		MenuHelper.processMenuManager(context, menu, items);
	}

	private static Method itemsToFill = null;
	private IWorkbenchWindow window;

	/**
	 * @param i
	 * @return
	 */
	private IContributionItem[] getItems(CompoundContributionItem i) {
		try {
			if (itemsToFill == null) {
				itemsToFill = CompoundContributionItem.class.getDeclaredMethod(
						"getContributionItemsToFill", null); //$NON-NLS-1$
				itemsToFill.setAccessible(true);
			}
			return (IContributionItem[]) itemsToFill.invoke(i, null);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param element
	 * @return
	 */
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
		loadModel(element, m);
		return item;
	}

	private MMenuItem createCommandElement(IConfigurationElement element) {
		String imagePath = MenuHelper.getImageUrl(MenuHelper
				.getIconDescriptor(element));
		String cmdId = MenuHelper.getCommandId(element);
		String id = MenuHelper.getId(element);
		String label = MenuHelper.getLabel(element);
		if (label == null) {
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
		MMenuItem item = MenuHelper.createMenuItem(context, label, imagePath,
				id, cmdId);
		return item;
	}
}
