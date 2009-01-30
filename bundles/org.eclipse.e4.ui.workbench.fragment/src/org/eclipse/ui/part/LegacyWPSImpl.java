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

import java.util.Collection;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.e4.ui.model.application.ContributedPart;
import org.eclipse.e4.ui.model.application.Part;
import org.eclipse.e4.workbench.ui.internal.Workbench;
import org.eclipse.jface.action.ContributionManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.internal.provisional.action.ToolBarManager2;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.ISourceProvider;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.menus.AbstractContributionFactory;
import org.eclipse.ui.menus.IMenuService;
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
	private ISelectionProvider selProvider;

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
		if (selProvider == null) {
			selProvider = new ISelectionProvider() {
				public void addSelectionChangedListener(
						ISelectionChangedListener listener) {
				}

				public ISelection getSelection() {
					return null;
				}

				public void removeSelectionChangedListener(
						ISelectionChangedListener listener) {
				}

				public void setSelection(ISelection selection) {
				}				
			};
		}
		
		return selProvider;
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
		selProvider = provider;
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
		if (api == IMenuService.class) {
			return new IMenuService() {

				public void addContributionFactory(
						AbstractContributionFactory factory) {
					// TODO Auto-generated method stub
					
				}

				public IEvaluationContext getCurrentState() {
					// TODO Auto-generated method stub
					return null;
				}

				public void populateContributionManager(
						ContributionManager mgr, String location) {
					// TODO Auto-generated method stub
					
				}

				public void releaseContributions(ContributionManager mgr) {
					// TODO Auto-generated method stub
					
				}

				public void removeContributionFactory(
						AbstractContributionFactory factory) {
					// TODO Auto-generated method stub
					
				}

				public void addSourceProvider(ISourceProvider provider) {
					// TODO Auto-generated method stub
					
				}

				public void removeSourceProvider(ISourceProvider provider) {
					// TODO Auto-generated method stub
					
				}

				public void dispose() {
					// TODO Auto-generated method stub
					
				}				
			};
		}
		if (api == IHandlerService.class) {
			return new IHandlerService() {
				public IHandlerActivation activateHandler(
						IHandlerActivation activation) {
					return null;
				}
				public IHandlerActivation activateHandler(String commandId,
						IHandler handler) {
					return null;
				}
				public IHandlerActivation activateHandler(String commandId,
						IHandler handler, Expression expression) {
					return null;
				}
				public IHandlerActivation activateHandler(String commandId,
						IHandler handler, Expression expression, boolean global) {
					return null;
				}
				public IHandlerActivation activateHandler(String commandId,
						IHandler handler, Expression expression,
						int sourcePriorities) {
					return null;
				}
				public IEvaluationContext createContextSnapshot(
						boolean includeSelection) {
					return null;
				}
				public ExecutionEvent createExecutionEvent(Command command,
						Event event) {
					return null;
				}
				public ExecutionEvent createExecutionEvent(
						ParameterizedCommand command, Event event) {
					return null;
				}
				public void deactivateHandler(IHandlerActivation activation) {
				}
				public void deactivateHandlers(Collection activations) {
				}
				public Object executeCommand(String commandId, Event event)
						throws ExecutionException, NotDefinedException,
						NotEnabledException, NotHandledException {
					return null;
				}
				public Object executeCommand(ParameterizedCommand command,
						Event event) throws ExecutionException,
						NotDefinedException, NotEnabledException,
						NotHandledException {
					return null;
				}
				public Object executeCommandInContext(
						ParameterizedCommand command, Event event,
						IEvaluationContext context) throws ExecutionException,
						NotDefinedException, NotEnabledException,
						NotHandledException {
					return null;
				}
				public IEvaluationContext getCurrentState() {
					return null;
				}
				public void readRegistry() {
				}
				public void setHelpContextId(IHandler handler,
						String helpContextId) {
				}
				public void addSourceProvider(ISourceProvider provider) {
				}
				public void removeSourceProvider(ISourceProvider provider) {
				}
				public void dispose() {
				}
			};
		}
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
			private IStatusLineManager slMgr;
			private IMenuManager menuMgr;
			private IToolBarManager tbMgr;
			private IAction globalHandler;
			
			public void clearGlobalActionHandlers() {
				globalHandler = null;
			}

			public IAction getGlobalActionHandler(String actionId) {
				return globalHandler;
			}

			public IMenuManager getMenuManager() {
				if (menuMgr == null) {
					menuMgr = new MenuManager();
				}
				
				return menuMgr;
			}

			public IServiceLocator getServiceLocator() {
				return null;
			}

			public IStatusLineManager getStatusLineManager() {
				if (slMgr == null) {
					slMgr = new StatusLineManager();
				}
				
				return slMgr;
			}

			public IToolBarManager getToolBarManager() {
				if (tbMgr == null) {
					tbMgr = new ToolBarManager2();
				}
				
				return tbMgr;
			}

			public void setGlobalActionHandler(String actionId, IAction handler) {
				globalHandler = handler;
			}

			public void updateActionBars() {
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
