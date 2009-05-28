/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.internal.provisional.action.ToolBarManager2;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.SubActionBars;

/**
 * A view container manages the services for a view.
 */
public class ViewSite extends PartSite implements IViewSite {

	/**
	 * @param ref
	 * @param part
	 * @param page
	 */
	public ViewSite(IViewReference ref, IWorkbenchPart part, IWorkbenchPage page) {
		super(ref, part, page);
		SubActionBars bars = new SubActionBars(((WorkbenchPage) page)
				.getActionBars(), serviceLocator) {
			private IToolBarManager tbMgr;
			private MenuManager menuMgr;

			@Override
			public IToolBarManager getToolBarManager() {
				if (tbMgr == null) {
					tbMgr = new ToolBarManager2();
				}
				return tbMgr;
			}

			@Override
			public IMenuManager getMenuManager() {
				if (menuMgr == null) {
					menuMgr = new MenuManager();
				}
				return menuMgr;
			}
		};
		setActionBars(bars);
	}

	/**
	 * Returns the secondary id or <code>null</code>.
	 */
	public String getSecondaryId() {
		return ((IViewReference) getPartReference()).getSecondaryId();
	}

	/**
	 * Returns the view.
	 */
	public IViewPart getViewPart() {
		return (IViewPart) getPart();
	}
}
