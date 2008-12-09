/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.part;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * @since 3.3
 *
 */
public class LegacyWPSImpl implements IWorkbenchPartSite, IViewSite, IEditorSite {

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPartSite#getId()
	 */
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPartSite#getKeyBindingService()
	 */
	public IKeyBindingService getKeyBindingService() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPartSite#getPart()
	 */
	public IWorkbenchPart getPart() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPartSite#getPluginId()
	 */
	public String getPluginId() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPartSite#getRegisteredName()
	 */
	public String getRegisteredName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPartSite#registerContextMenu(java.lang.String, org.eclipse.jface.action.MenuManager, org.eclipse.jface.viewers.ISelectionProvider)
	 */
	public void registerContextMenu(String menuId, MenuManager menuManager,
			ISelectionProvider selectionProvider) {
		System.out.println("registerContextMenu: " + menuManager.toString()); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPartSite#registerContextMenu(org.eclipse.jface.action.MenuManager, org.eclipse.jface.viewers.ISelectionProvider)
	 */
	public void registerContextMenu(MenuManager menuManager,
			ISelectionProvider selectionProvider) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchSite#getPage()
	 */
	public IWorkbenchPage getPage() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchSite#getSelectionProvider()
	 */
	public ISelectionProvider getSelectionProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchSite#getShell()
	 */
	public Shell getShell() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchSite#getWorkbenchWindow()
	 */
	public IWorkbenchWindow getWorkbenchWindow() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchSite#setSelectionProvider(org.eclipse.jface.viewers.ISelectionProvider)
	 */
	public void setSelectionProvider(ISelectionProvider provider) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.services.IServiceLocator#getService(java.lang.Class)
	 */
	public Object getService(Class api) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.services.IServiceLocator#hasService(java.lang.Class)
	 */
	public boolean hasService(Class api) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IViewSite#getActionBars()
	 */
	public IActionBars getActionBars() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IViewSite#getSecondaryId()
	 */
	public String getSecondaryId() {
		return ""; //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorSite#getActionBarContributor()
	 */
	public IEditorActionBarContributor getActionBarContributor() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorSite#registerContextMenu(org.eclipse.jface.action.MenuManager, org.eclipse.jface.viewers.ISelectionProvider, boolean)
	 */
	public void registerContextMenu(MenuManager menuManager,
			ISelectionProvider selectionProvider, boolean includeEditorInput) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorSite#registerContextMenu(java.lang.String, org.eclipse.jface.action.MenuManager, org.eclipse.jface.viewers.ISelectionProvider, boolean)
	 */
	public void registerContextMenu(String menuId, MenuManager menuManager,
			ISelectionProvider selectionProvider, boolean includeEditorInput) {
		// TODO Auto-generated method stub
		
	}

}
