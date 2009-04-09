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

import java.util.ArrayList;

import org.eclipse.e4.core.services.context.IContextFunction;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.ui.model.application.MContributedPart;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.workbench.ui.api.LegacyMenuService;
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
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.PartService;
import org.eclipse.ui.internal.PartSite;
import org.eclipse.ui.internal.misc.UIListenerLogging;
import org.eclipse.ui.internal.services.IServiceLocatorCreator;
import org.eclipse.ui.internal.services.ServiceLocatorCreator;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.services.IServiceLocator;

/**
 * @since 3.3
 * 
 */
public class LegacyWPSImpl implements IWorkbenchPartSite, IViewSite,
		IEditorSite {
	private MContributedPart<MPart<?>> part;
	private WorkbenchPart implementation;
	private IEclipseContext context;
	private ArrayList menuExtenders;

	/**
	 * @param e4Workbench
	 * @param part
	 * @param impl
	 */
	public LegacyWPSImpl(MContributedPart<MPart<?>> part, WorkbenchPart impl) {
		this.part = part;
		this.implementation = impl;
		context = part.getContext();

		// Register any site-specific services with the context
		registerServices();
	}

	/**
	 * Add any necessary services to the context
	 */
	private void registerServices() {
		context.set(IKeyBindingService.class.getName(), new IContextFunction() {
			public Object compute(IEclipseContext context, Object[] arguments) {
				return new IKeyBindingService() {
					public String[] getScopes() {
						return null;
					}

					public void registerAction(IAction action) {
					}

					public void setScopes(String[] scopes) {
					}

					public void unregisterAction(IAction action) {
					}
				};
			}
		});
		context.set(IActionBars.class.getName(), new IContextFunction() {
			public Object compute(IEclipseContext context, Object[] arguments) {
				IActionBars actionBars = new IActionBars() {
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

					public void setGlobalActionHandler(String actionId,
							IAction handler) {
						globalHandler = handler;
					}

					public void updateActionBars() {
					}
				};

				return actionBars;
			}
		});
		context.set(ISelectionProvider.class.getName(), new IContextFunction() {
			public Object compute(IEclipseContext context, Object[] arguments) {
				ISelectionProvider selProvider = new ISelectionProvider() {
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
				return selProvider;
			}
		});
		context.set(IServiceLocatorCreator.class.getName(),
				new IContextFunction() {
					public Object compute(IEclipseContext context,
							Object[] arguments) {
						return new ServiceLocatorCreator();
					}
				});
		context.set(IPartService.class.getName(), new IContextFunction() {
			public Object compute(IEclipseContext context, Object[] arguments) {
				return new PartService(
						UIListenerLogging.PAGE_PARTLISTENER_EVENTS,
						UIListenerLogging.PAGE_PARTLISTENER2_EVENTS);
			}
		});
		context.set(IMenuService.class.getName(), new IContextFunction() {
			public Object compute(IEclipseContext context, Object[] arguments) {
				return new LegacyMenuService(context);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPartSite#getId()
	 */
	public String getId() {
		return part.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPartSite#getKeyBindingService()
	 */
	public IKeyBindingService getKeyBindingService() {
		return (IKeyBindingService) context.get(IKeyBindingService.class
				.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPartSite#getPart()
	 */
	public IWorkbenchPart getPart() {
		return implementation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPartSite#getPluginId()
	 */
	public String getPluginId() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPartSite#getRegisteredName()
	 */
	public String getRegisteredName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPartSite#registerContextMenu(java.lang.String,
	 * org.eclipse.jface.action.MenuManager,
	 * org.eclipse.jface.viewers.ISelectionProvider)
	 */
	public void registerContextMenu(String menuId, MenuManager menuManager,
			ISelectionProvider selectionProvider) {
		registerContextMenu(menuId, menuManager, selectionProvider, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPartSite#registerContextMenu(org.eclipse.jface
	 * .action.MenuManager, org.eclipse.jface.viewers.ISelectionProvider)
	 */
	public void registerContextMenu(MenuManager menuManager,
			ISelectionProvider selectionProvider) {
		registerContextMenu(getId(), menuManager, selectionProvider, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchSite#getPage()
	 */
	public IWorkbenchPage getPage() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchSite#getSelectionProvider()
	 */
	public ISelectionProvider getSelectionProvider() {
		return (ISelectionProvider) context.get(ISelectionProvider.class
				.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchSite#getShell()
	 */
	public Shell getShell() {
		return (Shell) part.getContext().get(Shell.class.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchSite#getWorkbenchWindow()
	 */
	public IWorkbenchWindow getWorkbenchWindow() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchSite#setSelectionProvider(org.eclipse.jface.
	 * viewers.ISelectionProvider)
	 */
	public void setSelectionProvider(ISelectionProvider provider) {
		context.set(ISelectionProvider.class.getName(), provider);
		if (provider != null) {
			final IEclipseContext outputContext = (IEclipseContext) context
					.get(IServiceConstants.OUTPUTS);
			provider
					.addSelectionChangedListener(new ISelectionChangedListener() {
						public void selectionChanged(SelectionChangedEvent event) {
							outputContext.set(IServiceConstants.SELECTION,
									event.getSelection());
						}
					});
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		return context.get(adapter.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.services.IServiceLocator#getService(java.lang.Class)
	 */
	public Object getService(Class api) {
		return context.get(api.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.services.IServiceLocator#hasService(java.lang.Class)
	 */
	public boolean hasService(Class api) {
		return context.containsKey(api.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IViewSite#getActionBars()
	 */
	public IActionBars getActionBars() {
		return (IActionBars) context.get(IActionBars.class.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IViewSite#getSecondaryId()
	 */
	public String getSecondaryId() {
		return ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorSite#getActionBarContributor()
	 */
	public IEditorActionBarContributor getActionBarContributor() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IEditorSite#registerContextMenu(org.eclipse.jface.action
	 * .MenuManager, org.eclipse.jface.viewers.ISelectionProvider, boolean)
	 */
	public void registerContextMenu(MenuManager menuManager,
			ISelectionProvider selectionProvider, boolean includeEditorInput) {
		registerContextMenu(getId(), menuManager, selectionProvider,
				includeEditorInput);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorSite#registerContextMenu(java.lang.String,
	 * org.eclipse.jface.action.MenuManager,
	 * org.eclipse.jface.viewers.ISelectionProvider, boolean)
	 */
	public void registerContextMenu(String menuId, MenuManager menuManager,
			ISelectionProvider selectionProvider, boolean includeEditorInput) {
		if (menuExtenders == null) {
			menuExtenders = new ArrayList(1);
		}
		PartSite.registerContextMenu(menuId, menuManager, selectionProvider,
				includeEditorInput, getPart(), menuExtenders);
	}

}
