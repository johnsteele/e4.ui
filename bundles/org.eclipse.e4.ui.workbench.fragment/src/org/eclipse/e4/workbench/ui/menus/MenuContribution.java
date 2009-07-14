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

import java.util.Map;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionConverter;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.ui.model.application.ApplicationFactory;
import org.eclipse.e4.ui.model.application.MMenu;
import org.eclipse.e4.ui.model.application.MMenuItem;
import org.eclipse.e4.ui.model.application.MParameter;
import org.eclipse.e4.ui.model.workbench.MMenuItemRenderer;
import org.eclipse.e4.workbench.ui.internal.Activator;
import org.eclipse.e4.workbench.ui.internal.Policy;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.LegacyEvalContext;
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
		return mergeModel(idx, menu, model);
	}

	private boolean mergeModel(int idx, MMenu menu, MMenu toInsert) {
		EList<MMenuItem> items = menu.getItems();
		MMenuItem[] modelItems = toInsert.getItems().toArray(
				new MMenuItem[toInsert.getItems().size()]);
		for (int i = 0; i < modelItems.length; i++) {
			MMenuItem modelItem = modelItems[i];
			if (modelItem.getMenu() != null) {
				int tmpIdx = MenuHelper.indexForId(menu, modelItem.getId());
				if (tmpIdx == -1) {
					items.add(idx++, modelItem);
				} else {
					mergeModel(0, items.get(tmpIdx).getMenu(), modelItem
							.getMenu());
				}
			} else {
				items.add(idx++, modelItem);
			}
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
				MenuHelper.addSeparator(menu, MenuHelper.getId(element), true);
			} else if (IWorkbenchRegistryConstants.TAG_DYNAMIC
					.equals(elementType)) {
				addRenderer(menu, element);
			}
		}
	}

	/**
	 * @param menu
	 * @param element
	 */
	private void addRenderer(MMenu menu, IConfigurationElement element) {
		ContributionItem i = (ContributionItem) Util
				.safeLoadExecutableExtension(element,
						IWorkbenchRegistryConstants.ATT_CLASS,
						ContributionItem.class);
		if (i instanceof IWorkbenchContribution) {
			((IWorkbenchContribution) i).initialize(window);
		}
		MMenuItemRenderer renderer = MenuHelper.addMenuRenderer(context, menu,
				i);
		associateVisibleWhen(element, renderer);
	}

	private IWorkbenchWindow window;

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
		ICommandService cs = (ICommandService) context
				.get(ICommandService.class.getName());
		Command cmd = cs.getCommand(cmdId);
		if (label == null) {
			if (cmd.isDefined()) {
				try {
					label = cmd.getName();
				} catch (NotDefinedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		final MMenuItem item = MenuHelper.createMenuItem(context, label,
				imagePath, id, cmdId);
		final Map<String, String> parms = MenuHelper.getParameters(element);
		if (!parms.isEmpty()) {
			final EList<MParameter> modelParms = item.getParameters();
			for (Map.Entry<String, String> entry : parms.entrySet()) {
				MParameter p = ApplicationFactory.eINSTANCE.createMParameter();
				p.setName(entry.getKey());
				p.setValue(entry.getValue());
				modelParms.add(p);
			}
		}

		associateVisibleWhen(element, item);
		return item;
	}

	/**
	 * @param element
	 * @param item
	 */
	private void associateVisibleWhen(final IConfigurationElement element,
			final MMenuItem item) {
		IConfigurationElement[] visibleWhen = element
				.getChildren(IWorkbenchRegistryConstants.TAG_VISIBLE_WHEN);
		if (visibleWhen.length > 0) {
			IConfigurationElement[] visibleChild = visibleWhen[0].getChildren();
			if (visibleChild.length > 0) {
				try {
					final Expression visWhen = ExpressionConverter.getDefault()
							.perform(visibleChild[0]);
					final LegacyEvalContext legacyEvalContext = new LegacyEvalContext(
							context);
					context.runAndTrack(new Runnable() {
						public void run() {
							boolean visible = true;
							try {
								visible = visWhen.evaluate(legacyEvalContext) != EvaluationResult.FALSE;
							} catch (CoreException e) {
								Activator.trace(Policy.DEBUG_MENUS,
										"Failed to evaluate visibleWhen", e); //$NON-NLS-1$
							}
							Activator.trace(Policy.DEBUG_MENUS,
									"visibleWhen set to " + visible, null); //$NON-NLS-1$

							item.setVisible(visible);
						}
					});
				} catch (CoreException e) {
					Activator.trace(Policy.DEBUG_MENUS,
							"Failed to parse visibleWhen", e); //$NON-NLS-1$
				}
			}
		}
	}
}
