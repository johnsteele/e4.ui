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

package org.eclipse.ui;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.eclipse.e4.core.services.context.IContextFunction;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.extensions.ExtensionUtils;
import org.eclipse.e4.ui.model.application.ApplicationFactory;
import org.eclipse.e4.ui.model.application.MContributedPart;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.model.workbench.MPerspective;
import org.eclipse.e4.ui.model.workbench.MWorkbenchWindow;
import org.eclipse.e4.workbench.ui.api.LegacySelectionService;
import org.eclipse.e4.workbench.ui.api.ModeledPageLayout;
import org.eclipse.e4.workbench.ui.internal.Workbench;
import org.eclipse.e4.workbench.ui.menus.MenuHelper;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.internal.NavigationHistory;
import org.eclipse.ui.internal.WWinPartService;
import org.eclipse.ui.internal.handlers.ActionCommandMappingService;
import org.eclipse.ui.internal.handlers.ActionDelegateHandlerProxy;
import org.eclipse.ui.internal.handlers.IActionCommandMappingService;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.internal.services.EvaluationService;
import org.eclipse.ui.internal.services.IWorkbenchLocationService;
import org.eclipse.ui.model.ContributionComparator;
import org.eclipse.ui.model.IContributionService;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.services.IEvaluationService;

/**
 * @since 3.3
 * 
 */
public class LegacyWBWImpl implements IWorkbenchWindow, IWorkbenchPage {
	private IEclipseContext context;
	private Workbench e4Workbench;
	private MWorkbenchWindow workbenchWindow;
	private LegacyWBImpl legacyWbImpl;

	// private ISelectionService selService;
	public static IEditorInput hackInput;

	/**
	 * @param e4Workbench
	 * @param legacyWbImpl
	 * @param workbenchWindow
	 */
	public LegacyWBWImpl(MWorkbenchWindow workbenchWindow) {
		this.workbenchWindow = workbenchWindow;
		context = workbenchWindow.getContext();
		e4Workbench = (Workbench) context
				.get(org.eclipse.e4.workbench.ui.IWorkbench.class.getName());
		legacyWbImpl = (LegacyWBImpl) context.get(LegacyWBImpl.class.getName());
		context.set(ISources.ACTIVE_WORKBENCH_WINDOW_NAME, this);
		context.set(IWorkbenchWindow.class.getName(), this);

		// Register any window-specific services to the context
		registerServices();
	}

	/**
	 * Register any window-specific services to the context
	 */
	private void registerServices() {
		context.set(IPartService.class.getName(), new IContextFunction() {
			public Object compute(IEclipseContext context, Object[] arguments) {
				return new WWinPartService(LegacyWBWImpl.this);
			}
		});
		context.set(INavigationHistory.class.getName(), new IContextFunction() {
			public Object compute(IEclipseContext context, Object[] arguments) {
				return new NavigationHistory(LegacyWBWImpl.this);
			}
		});
		context.set(IEvaluationService.class.getName(), new IContextFunction() {
			public Object compute(IEclipseContext context, Object[] arguments) {
				return new EvaluationService();
			}
		});
		context.set(IContributionService.class.getName(),
				new IContextFunction() {
					public Object compute(IEclipseContext context,
							Object[] arguments) {
						// TBD the IContributionService is contributed by the
						// WorkbenchAdvisor.
						// The code below corresponds to the case where
						// WorkbenchAdvisor#getComparatorFor()
						// method has not been overridden
						return new IContributionService() {
							public ContributionComparator getComparatorFor(
									String contributionType) {
								return new ContributionComparator();
							}
						};
					}
				});

		context.set(ISelectionService.class.getName(),
				new LegacySelectionService(context));
		context.set(IWorkbenchLocationService.class.getName(),
				new IContextFunction() {

					public Object compute(IEclipseContext context,
							Object[] arguments) {
						return new IWorkbenchLocationService() {

							public IWorkbenchWindow getWorkbenchWindow() {
								return LegacyWBWImpl.this;
							}

							public IWorkbench getWorkbench() {
								return LegacyWBWImpl.this.getWorkbench();
							}

							public String getServiceScope() {
								// TODO Auto-generated method stub
								return null;
							}

							public int getServiceLevel() {
								// TODO Auto-generated method stub
								return 0;
							}

							public IWorkbenchPartSite getPartSite() {
								// TODO Auto-generated method stub
								return null;
							}

							public IPageSite getPageSite() {
								// TODO Auto-generated method stub
								return null;
							}

							public IEditorSite getMultiPageEditorSite() {
								// TODO Auto-generated method stub
								return null;
							}
						};
					}
				});
		context.set(IActionCommandMappingService.class.getName(),
				new IContextFunction() {

					public Object compute(IEclipseContext context,
							Object[] arguments) {
						return new ActionCommandMappingService();
					}
				});
		readActionSets();
	}

	/**
	 * 
	 */
	private void readActionSets() {
		ICommandService cs = (ICommandService) context
				.get(ICommandService.class.getName());
		IConfigurationElement[] actionSetElements = ExtensionUtils
				.getExtensions(IWorkbenchRegistryConstants.PL_ACTION_SETS);
		for (IConfigurationElement ase : actionSetElements) {
			IConfigurationElement[] elements = ase
					.getChildren(IWorkbenchRegistryConstants.TAG_ACTION);
			for (IConfigurationElement configElement : elements) {
				String id = MenuHelper.getId(configElement);
				String cmdId = MenuHelper.getActionSetCommandId(configElement);
				if (id == null || id.length() == 0) {
					continue;
				}
				Command cmd = cs.getCommand(cmdId);
				if (!cmd.isDefined()) {
					continue;
				}
				LegacyHandlerService.registerLegacyHandler(context, id, cmdId,
						new ActionDelegateHandlerProxy(configElement,
								IWorkbenchRegistryConstants.ATT_CLASS, id,
								new ParameterizedCommand(cmd, null), this,
								null, null, null));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindow#close()
	 */
	public boolean close() {
		e4Workbench.closeWindow(workbenchWindow);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindow#getActivePage()
	 */
	public IWorkbenchPage getActivePage() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindow#getExtensionTracker()
	 */
	public IExtensionTracker getExtensionTracker() {
		return (IExtensionTracker) context.get(IExtensionTracker.class
				.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindow#getPages()
	 */
	public IWorkbenchPage[] getPages() {
		IWorkbenchPage[] pages = new IWorkbenchPage[] { this };
		return pages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindow#getPartService()
	 */
	public IPartService getPartService() {
		return (IPartService) context.get(IPartService.class.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindow#getSelectionService()
	 */
	public ISelectionService getSelectionService() {
		return (ISelectionService) context.get(ISelectionService.class
				.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindow#getShell()
	 */
	public Shell getShell() {
		return (Shell) e4Workbench.getWindow();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindow#getWorkbench()
	 */
	public IWorkbench getWorkbench() {
		return legacyWbImpl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindow#isApplicationMenu(java.lang.String)
	 */
	public boolean isApplicationMenu(String menuId) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindow#openPage(java.lang.String,
	 * org.eclipse.core.runtime.IAdaptable)
	 */
	public IWorkbenchPage openPage(String perspectiveId, IAdaptable input)
			throws WorkbenchException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchWindow#openPage(org.eclipse.core.runtime.IAdaptable
	 * )
	 */
	public IWorkbenchPage openPage(IAdaptable input) throws WorkbenchException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindow#run(boolean, boolean,
	 * org.eclipse.jface.operation.IRunnableWithProgress)
	 */
	public void run(boolean fork, boolean cancelable,
			IRunnableWithProgress runnable) throws InvocationTargetException,
			InterruptedException {
		IProgressMonitor pm = new NullProgressMonitor();
		ModalContext.run(runnable, fork, pm, getShell().getDisplay());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchWindow#setActivePage(org.eclipse.ui.IWorkbenchPage
	 * )
	 */
	public void setActivePage(IWorkbenchPage page) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IPageService#addPageListener(org.eclipse.ui.IPageListener)
	 */
	public void addPageListener(IPageListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.IPageService#addPerspectiveListener(org.eclipse.ui.
	 * IPerspectiveListener)
	 */
	public void addPerspectiveListener(IPerspectiveListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IPageService#removePageListener(org.eclipse.ui.IPageListener
	 * )
	 */
	public void removePageListener(IPageListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IPageService#removePerspectiveListener(org.eclipse.ui.
	 * IPerspectiveListener)
	 */
	public void removePerspectiveListener(IPerspectiveListener listener) {
		// TODO Auto-generated method stub

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
	 * @see
	 * org.eclipse.ui.IWorkbenchPage#activate(org.eclipse.ui.IWorkbenchPart)
	 */
	public void activate(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPage#addPropertyChangeListener(org.eclipse.jface
	 * .util.IPropertyChangeListener)
	 */
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPage#bringToTop(org.eclipse.ui.IWorkbenchPart)
	 */
	public void bringToTop(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#closeAllEditors(boolean)
	 */
	public boolean closeAllEditors(boolean save) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#closeAllPerspectives(boolean, boolean)
	 */
	public void closeAllPerspectives(boolean saveEditors, boolean closePage) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPage#closeEditor(org.eclipse.ui.IEditorPart,
	 * boolean)
	 */
	public boolean closeEditor(IEditorPart editor, boolean save) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPage#closeEditors(org.eclipse.ui.IEditorReference
	 * [], boolean)
	 */
	public boolean closeEditors(IEditorReference[] editorRefs, boolean save) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.IWorkbenchPage#closePerspective(org.eclipse.ui.
	 * IPerspectiveDescriptor, boolean, boolean)
	 */
	public void closePerspective(IPerspectiveDescriptor desc,
			boolean saveParts, boolean closePage) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPage#findEditor(org.eclipse.ui.IEditorInput)
	 */
	public IEditorPart findEditor(IEditorInput input) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPage#findEditors(org.eclipse.ui.IEditorInput,
	 * java.lang.String, int)
	 */
	public IEditorReference[] findEditors(IEditorInput input, String editorId,
			int matchFlags) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#findView(java.lang.String)
	 */
	public IViewPart findView(String viewId) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#findViewReference(java.lang.String)
	 */
	public IViewReference findViewReference(String viewId) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#findViewReference(java.lang.String,
	 * java.lang.String)
	 */
	public IViewReference findViewReference(String viewId, String secondaryId) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#getActiveEditor()
	 */
	public IEditorPart getActiveEditor() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#getAggregateWorkingSet()
	 */
	public IWorkingSet getAggregateWorkingSet() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#getDirtyEditors()
	 */
	public IEditorPart[] getDirtyEditors() {
		return new IEditorPart[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#getEditorReferences()
	 */
	public IEditorReference[] getEditorReferences() {
		return new IEditorReference[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#getEditorReuseThreshold()
	 */
	public int getEditorReuseThreshold() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#getEditors()
	 */
	public IEditorPart[] getEditors() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#getInput()
	 */
	public IAdaptable getInput() {
		IEclipseContext context = e4Workbench.getContext();
		IWorkspace ws = (IWorkspace) context.get(IWorkspace.class.getName());
		return ws.getRoot();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#getLabel()
	 */
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#getNavigationHistory()
	 */
	public INavigationHistory getNavigationHistory() {
		return (INavigationHistory) context.get(INavigationHistory.class
				.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#getNewWizardShortcuts()
	 */
	public String[] getNewWizardShortcuts() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#getOpenPerspectives()
	 */
	public IPerspectiveDescriptor[] getOpenPerspectives() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.IWorkbenchPage#getPartState(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	public int getPartState(IWorkbenchPartReference ref) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#getPerspective()
	 */
	public IPerspectiveDescriptor getPerspective() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#getPerspectiveShortcuts()
	 */
	public String[] getPerspectiveShortcuts() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPage#getReference(org.eclipse.ui.IWorkbenchPart)
	 */
	public IWorkbenchPartReference getReference(IWorkbenchPart part) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#getShowViewShortcuts()
	 */
	public String[] getShowViewShortcuts() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#getSortedPerspectives()
	 */
	public IPerspectiveDescriptor[] getSortedPerspectives() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#getViewReferences()
	 */
	public IViewReference[] getViewReferences() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#getViewStack(org.eclipse.ui.IViewPart)
	 */
	public IViewPart[] getViewStack(IViewPart part) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#getViews()
	 */
	public IViewPart[] getViews() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#getWorkbenchWindow()
	 */
	public IWorkbenchWindow getWorkbenchWindow() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#getWorkingSet()
	 */
	public IWorkingSet getWorkingSet() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#getWorkingSets()
	 */
	public IWorkingSet[] getWorkingSets() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#hideActionSet(java.lang.String)
	 */
	public void hideActionSet(String actionSetId) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPage#hideEditor(org.eclipse.ui.IEditorReference)
	 */
	public void hideEditor(IEditorReference ref) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#hideView(org.eclipse.ui.IViewPart)
	 */
	public void hideView(IViewPart view) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPage#hideView(org.eclipse.ui.IViewReference)
	 */
	public void hideView(IViewReference view) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#isEditorAreaVisible()
	 */
	public boolean isEditorAreaVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPage#isEditorPinned(org.eclipse.ui.IEditorPart)
	 */
	public boolean isEditorPinned(IEditorPart editor) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#isPageZoomed()
	 */
	public boolean isPageZoomed() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPage#isPartVisible(org.eclipse.ui.IWorkbenchPart
	 * )
	 */
	public boolean isPartVisible(IWorkbenchPart part) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPage#openEditor(org.eclipse.ui.IEditorInput,
	 * java.lang.String)
	 */
	public IEditorPart openEditor(IEditorInput input, String editorId)
			throws PartInitException {
		return openEditor(input, editorId, true, MATCH_INPUT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPage#openEditor(org.eclipse.ui.IEditorInput,
	 * java.lang.String, boolean)
	 */
	public IEditorPart openEditor(IEditorInput input, String editorId,
			boolean activate) throws PartInitException {
		return openEditor(input, editorId, activate, MATCH_INPUT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPage#openEditor(org.eclipse.ui.IEditorInput,
	 * java.lang.String, boolean, int)
	 */
	public IEditorPart openEditor(IEditorInput input, String editorId,
			boolean activate, int matchFlags) throws PartInitException {
		// Create the model 'part' in the UI
		MPerspective<?> curPersp = workbenchWindow.getActiveChild();
		EList<?> kids = curPersp.getChildren();
		MPart ea = ModeledPageLayout.findPart(curPersp, ModeledPageLayout
				.internalGetEditorArea());

		MContributedPart<MPart<?>> editorPart = ApplicationFactory.eINSTANCE
				.createMContributedPart();
		editorPart.setId(editorId);
		editorPart.setName(input.getName());
		ea.getChildren().add(editorPart);
		hackInput = input;
		ea.setActiveChild(editorPart);
		hackInput = null;
		System.out.println(kids.toString() + ea.toString());
		// ref = getEditorManager().openEditor(editorID, input, true,
		// editorState);
		//
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPage#removePropertyChangeListener(org.eclipse
	 * .jface.util.IPropertyChangeListener)
	 */
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#resetPerspective()
	 */
	public void resetPerspective() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPage#reuseEditor(org.eclipse.ui.IReusableEditor,
	 * org.eclipse.ui.IEditorInput)
	 */
	public void reuseEditor(IReusableEditor editor, IEditorInput input) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#saveAllEditors(boolean)
	 */
	public boolean saveAllEditors(boolean confirm) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#saveEditor(org.eclipse.ui.IEditorPart,
	 * boolean)
	 */
	public boolean saveEditor(IEditorPart editor, boolean confirm) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#savePerspective()
	 */
	public void savePerspective() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.IWorkbenchPage#savePerspectiveAs(org.eclipse.ui.
	 * IPerspectiveDescriptor)
	 */
	public void savePerspectiveAs(IPerspectiveDescriptor perspective) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#setEditorAreaVisible(boolean)
	 */
	public void setEditorAreaVisible(boolean showEditorArea) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#setEditorReuseThreshold(int)
	 */
	public void setEditorReuseThreshold(int openEditors) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.IWorkbenchPage#setPartState(org.eclipse.ui.
	 * IWorkbenchPartReference, int)
	 */
	public void setPartState(IWorkbenchPartReference ref, int state) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.IWorkbenchPage#setPerspective(org.eclipse.ui.
	 * IPerspectiveDescriptor)
	 */
	public void setPerspective(IPerspectiveDescriptor perspective) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPage#setWorkingSets(org.eclipse.ui.IWorkingSet
	 * [])
	 */
	public void setWorkingSets(IWorkingSet[] sets) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#showActionSet(java.lang.String)
	 */
	public void showActionSet(String actionSetId) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPage#showEditor(org.eclipse.ui.IEditorReference)
	 */
	public void showEditor(IEditorReference ref) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#showView(java.lang.String)
	 */
	public IViewPart showView(String viewId) throws PartInitException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#showView(java.lang.String,
	 * java.lang.String, int)
	 */
	public IViewPart showView(String viewId, String secondaryId, int mode)
			throws PartInitException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.IWorkbenchPage#toggleZoom(org.eclipse.ui.
	 * IWorkbenchPartReference)
	 */
	public void toggleZoom(IWorkbenchPartReference ref) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPage#zoomOut()
	 */
	public void zoomOut() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IPartService#addPartListener(org.eclipse.ui.IPartListener)
	 */
	public void addPartListener(IPartListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IPartService#addPartListener(org.eclipse.ui.IPartListener2
	 * )
	 */
	public void addPartListener(IPartListener2 listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartService#getActivePart()
	 */
	public IWorkbenchPart getActivePart() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPartService#getActivePartReference()
	 */
	public IWorkbenchPartReference getActivePartReference() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IPartService#removePartListener(org.eclipse.ui.IPartListener
	 * )
	 */
	public void removePartListener(IPartListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IPartService#removePartListener(org.eclipse.ui.IPartListener2
	 * )
	 */
	public void removePartListener(IPartListener2 listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.ISelectionService#addPostSelectionListener(org.eclipse
	 * .ui.ISelectionListener)
	 */
	public void addPostSelectionListener(ISelectionListener listener) {
		getSelectionService().addPostSelectionListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.ISelectionService#addPostSelectionListener(java.lang.String
	 * , org.eclipse.ui.ISelectionListener)
	 */
	public void addPostSelectionListener(String partId,
			ISelectionListener listener) {
		getSelectionService().addPostSelectionListener(partId, listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.ISelectionService#addSelectionListener(org.eclipse.ui.
	 * ISelectionListener)
	 */
	public void addSelectionListener(ISelectionListener listener) {
		getSelectionService().addSelectionListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.ISelectionService#addSelectionListener(java.lang.String,
	 * org.eclipse.ui.ISelectionListener)
	 */
	public void addSelectionListener(String partId, ISelectionListener listener) {
		getSelectionService().addSelectionListener(partId, listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISelectionService#getSelection()
	 */
	public ISelection getSelection() {
		return getSelectionService().getSelection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISelectionService#getSelection(java.lang.String)
	 */
	public ISelection getSelection(String partId) {
		return getSelectionService().getSelection(partId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.ISelectionService#removePostSelectionListener(org.eclipse
	 * .ui.ISelectionListener)
	 */
	public void removePostSelectionListener(ISelectionListener listener) {
		getSelectionService().removePostSelectionListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.ISelectionService#removePostSelectionListener(java.lang
	 * .String, org.eclipse.ui.ISelectionListener)
	 */
	public void removePostSelectionListener(String partId,
			ISelectionListener listener) {
		getSelectionService().removePostSelectionListener(partId, listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.ISelectionService#removeSelectionListener(org.eclipse.
	 * ui.ISelectionListener)
	 */
	public void removeSelectionListener(ISelectionListener listener) {
		getSelectionService().removeSelectionListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.ISelectionService#removeSelectionListener(java.lang.String
	 * , org.eclipse.ui.ISelectionListener)
	 */
	public void removeSelectionListener(String partId,
			ISelectionListener listener) {
		getSelectionService().removePostSelectionListener(partId, listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPage#openEditors(org.eclipse.ui.IEditorInput[],
	 * java.lang.String[], int)
	 */
	public IEditorReference[] openEditors(IEditorInput[] inputs,
			String[] editorIDs, int matchFlags) throws MultiPartInitException {
		// TODO Auto-generated method stub
		return null;
	}

}
