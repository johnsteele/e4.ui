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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.ui.model.application.ContributedPart;
import org.eclipse.e4.ui.model.application.Part;
import org.eclipse.e4.workbench.ui.internal.Workbench;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManagerOverrides;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.graphics.Image;
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
import org.eclipse.ui.services.IServiceLocator;

/**
 * @since 3.3
 *
 */
public class LegacyWPSImpl implements IWorkbenchPartSite, IViewSite, IEditorSite {

	private ContributedPart<Part<?>> part;
	private Workbench e4Workbench;
	private WorkbenchPart implementation;
	
	private IKeyBindingService kbService;
	private IActionBars actionBars;

	/**
	 * @param e4Workbench
	 * @param part
	 * @param impl 
	 */
	public LegacyWPSImpl(Workbench e4Workbench, ContributedPart<Part<?>> part, WorkbenchPart impl) {
		this.e4Workbench = e4Workbench;
		
		// HACK! need to reference e4Workbench
		if (this.e4Workbench != null) {
			this.part = part;
			this.implementation = impl;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPartSite#getId()
	 */
	public String getId() {
		return part.getId();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPartSite#getKeyBindingService()
	 */
	public IKeyBindingService getKeyBindingService() {
//		if (kbService == null)
//			kbService = new KeyBindingService(this);
		if (kbService == null)
			kbService = new IKeyBindingService() {

				public String[] getScopes() {
					// TODO Auto-generated method stub
					return null;
				}

				public void registerAction(IAction action) {
					// TODO Auto-generated method stub
					
				}

				public void setScopes(String[] scopes) {
					// TODO Auto-generated method stub
					
				}

				public void unregisterAction(IAction action) {
					// TODO Auto-generated method stub
					
				}
			
		};
		return kbService;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPartSite#getPart()
	 */
	public IWorkbenchPart getPart() {
		return implementation;
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
		return e4Workbench.getShell();
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
		if (actionBars == null) {
		actionBars = new IActionBars() {
			private IStatusLineManager slManager;

			public void clearGlobalActionHandlers() {
				// TODO Auto-generated method stub
				
			}

			public IAction getGlobalActionHandler(String actionId) {
				// TODO Auto-generated method stub
				return null;
			}

			public IMenuManager getMenuManager() {
				// TODO Auto-generated method stub
				return null;
			}

			public IServiceLocator getServiceLocator() {
				// TODO Auto-generated method stub
				return null;
			}

			public IStatusLineManager getStatusLineManager() {
				if (slManager == null) {
					slManager = new IStatusLineManager() {

						public IProgressMonitor getProgressMonitor() {
							return new NullProgressMonitor();
						}

						public boolean isCancelEnabled() {
							// TODO Auto-generated method stub
							return false;
						}

						public void setCancelEnabled(boolean enabled) {
							// TODO Auto-generated method stub
							
						}

						public void setErrorMessage(String message) {
							// TODO Auto-generated method stub
							
						}

						public void setErrorMessage(Image image, String message) {
							// TODO Auto-generated method stub
							
						}

						public void setMessage(String message) {
							// TODO Auto-generated method stub
							
						}

						public void setMessage(Image image, String message) {
							// TODO Auto-generated method stub
							
						}

						public void add(IAction action) {
							// TODO Auto-generated method stub
							
						}

						public void add(IContributionItem item) {
							// TODO Auto-generated method stub
							
						}

						public void appendToGroup(String groupName,
								IAction action) {
							// TODO Auto-generated method stub
							
						}

						public void appendToGroup(String groupName,
								IContributionItem item) {
							// TODO Auto-generated method stub
							
						}

						public IContributionItem find(String id) {
							// TODO Auto-generated method stub
							return null;
						}

						public IContributionItem[] getItems() {
							// TODO Auto-generated method stub
							return null;
						}

						public IContributionManagerOverrides getOverrides() {
							// TODO Auto-generated method stub
							return null;
						}

						public void insertAfter(String id, IAction action) {
							// TODO Auto-generated method stub
							
						}

						public void insertAfter(String id,
								IContributionItem item) {
							// TODO Auto-generated method stub
							
						}

						public void insertBefore(String id, IAction action) {
							// TODO Auto-generated method stub
							
						}

						public void insertBefore(String id,
								IContributionItem item) {
							// TODO Auto-generated method stub
							
						}

						public boolean isDirty() {
							// TODO Auto-generated method stub
							return false;
						}

						public boolean isEmpty() {
							// TODO Auto-generated method stub
							return false;
						}

						public void markDirty() {
							// TODO Auto-generated method stub
							
						}

						public void prependToGroup(String groupName,
								IAction action) {
							// TODO Auto-generated method stub
							
						}

						public void prependToGroup(String groupName,
								IContributionItem item) {
							// TODO Auto-generated method stub
							
						}

						public IContributionItem remove(String id) {
							// TODO Auto-generated method stub
							return null;
						}

						public IContributionItem remove(IContributionItem item) {
							// TODO Auto-generated method stub
							return null;
						}

						public void removeAll() {
							// TODO Auto-generated method stub
							
						}

						public void update(boolean force) {
							// TODO Auto-generated method stub
							
						}
						
					};
				}
				
				return slManager;
			}

			public IToolBarManager getToolBarManager() {
				// TODO Auto-generated method stub
				return null;
			}

			public void setGlobalActionHandler(String actionId, IAction handler) {
				// TODO Auto-generated method stub
				
			}

			public void updateActionBars() {
				// TODO Auto-generated method stub
				
			}
			
		};
		}

		return actionBars;
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
