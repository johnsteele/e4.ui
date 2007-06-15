/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.tools.dynamic;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate;

/**
 * @since 3.1
 */
public class LoadBundleMenu implements IWorkbenchWindowPulldownDelegate {

	private Shell parent;
	private MenuManager menuManager;
	private LoadBundleDialogAction loadAction;
	
	/**
	 * 
	 */
	public LoadBundleMenu() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	public void dispose() {
		if (menuManager != null) {
			menuManager.dispose();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {
		parent = window.getShell();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		getLoadBundleDialogAction().run();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowPulldownDelegate#getMenu(org.eclipse.swt.widgets.Control)
	 */
	public Menu getMenu(Control parent) {
		createMenuManager();
		return menuManager.createContextMenu(parent);
	}

	/**
	 * 
	 */
	private void createMenuManager() {
		if (menuManager == null) {
			menuManager = new MenuManager();
			menuManager.addMenuListener(new IMenuListener() {
				
			/* (non-Javadoc)
			 * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
			 */
			public void menuAboutToShow(IMenuManager manager) {
				manager.removeAll();
				if (BundleHistory.getInstance().createMenu(menuManager))
					menuManager.add(new Separator());
				menuManager.add(getLoadBundleDialogAction());
			}});
		}
	}
	//c:\eclipse\workspace\org.eclipse.ui.tests\data\org.eclipse.newPerspective1

	/**
	 * @return
	 */
	private LoadBundleDialogAction getLoadBundleDialogAction() {
		if (loadAction == null) {
			loadAction = new LoadBundleDialogAction(parent);
		}
		return loadAction;
	}
}
