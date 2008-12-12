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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.ICompositeOperation;
import org.eclipse.core.commands.operations.IOperationApprover;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IOperationHistoryListener;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.eclipse.e4.ui.model.workbench.WorkbenchWindow;
import org.eclipse.e4.workbench.ui.internal.Workbench;
import org.eclipse.help.IContext;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.commands.IWorkbenchCommandSupport;
import org.eclipse.ui.contexts.IWorkbenchContextSupport;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.ui.internal.SharedImages;
import org.eclipse.ui.internal.activities.ws.WorkbenchActivitySupport;
import org.eclipse.ui.internal.decorators.DecoratorManager;
import org.eclipse.ui.internal.ide.model.WorkbenchAdapterBuilder;
import org.eclipse.ui.internal.registry.EditorRegistry;
import org.eclipse.ui.internal.registry.UIExtensionTracker;
import org.eclipse.ui.internal.themes.WorkbenchThemeManager;
import org.eclipse.ui.intro.IIntroManager;
import org.eclipse.ui.operations.IWorkbenchOperationSupport;
import org.eclipse.ui.progress.IProgressService;
import org.eclipse.ui.themes.IThemeManager;
import org.eclipse.ui.views.IViewRegistry;
import org.eclipse.ui.wizards.IWizardRegistry;

/**
 * @since 4.0
 *
 */
public class LegacyWBImpl implements IWorkbench {
	private Workbench e4Workbench;
	public Workbench getE4Workbench() {
		return e4Workbench;
	}

	private ISharedImages sharedImages;
	private IEditorRegistry editorRegistry;
	private IExtensionTracker tracker;
	private IDecoratorManager decoratorManager;
	private WorkbenchActivitySupport workbenchActivitySupport;
	private IWorkbenchOperationSupport operationSupport;
	protected IOperationHistory operationHistory;
	
	private static Map<WorkbenchWindow, LegacyWBWImpl> wbwModel2LegacyImpl = new HashMap<WorkbenchWindow, LegacyWBWImpl>();

	/**
	 * @param e4Workbench
	 */
	public LegacyWBImpl(Workbench e4Workbench) {
		this.e4Workbench = e4Workbench;
		
		// register workspace adapters
		WorkbenchAdapterBuilder.registerAdapters();

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#addWindowListener(org.eclipse.ui.IWindowListener)
	 */
	public void addWindowListener(IWindowListener listener) {
		if (e4Workbench == null)
			return;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#addWorkbenchListener(org.eclipse.ui.IWorkbenchListener)
	 */
	public void addWorkbenchListener(IWorkbenchListener listener) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#close()
	 */
	public boolean close() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#createLocalWorkingSetManager()
	 */
	public ILocalWorkingSetManager createLocalWorkingSetManager() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getActiveWorkbenchWindow()
	 */
	public IWorkbenchWindow getActiveWorkbenchWindow() {
		//TODO: ensure windows list is in z-order
		WorkbenchWindow workbenchWindow = e4Workbench.getModelElement().getWindows().get(0);
		return getWBWImpl(workbenchWindow);
	}

	/**
	 * @return
	 */
	private LegacyWBWImpl getWBWImpl(WorkbenchWindow workbenchWindow) {
		if(!wbwModel2LegacyImpl.containsKey(workbenchWindow)) {
			LegacyWBWImpl impl = new LegacyWBWImpl(e4Workbench, this, workbenchWindow);
			wbwModel2LegacyImpl.put(workbenchWindow, impl);
		}
			
		return wbwModel2LegacyImpl.get(workbenchWindow);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getActivitySupport()
	 */
	public IWorkbenchActivitySupport getActivitySupport() {
		if (workbenchActivitySupport == null) {
			workbenchActivitySupport = new WorkbenchActivitySupport();
			//activityHelper = ActivityPersistanceHelper.getInstance();
		}
		
		return workbenchActivitySupport;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getBrowserSupport()
	 */
	public IWorkbenchBrowserSupport getBrowserSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getCommandSupport()
	 */
	public IWorkbenchCommandSupport getCommandSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getContextSupport()
	 */
	public IWorkbenchContextSupport getContextSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getDecoratorManager()
	 */
	public IDecoratorManager getDecoratorManager() {
        if (decoratorManager == null) {
            decoratorManager = new DecoratorManager();
        }
        return decoratorManager;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getDisplay()
	 */
	public Display getDisplay() {
		return Display.getCurrent();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getEditorRegistry()
	 */
	public IEditorRegistry getEditorRegistry() {
        if (editorRegistry == null) {
            editorRegistry = new EditorRegistry();
        }
        return editorRegistry;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getElementFactory(java.lang.String)
	 */
	public IElementFactory getElementFactory(String factoryId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getExportWizardRegistry()
	 */
	public IWizardRegistry getExportWizardRegistry() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getExtensionTracker()
	 */
	public IExtensionTracker getExtensionTracker() {
		if (tracker == null) {
			tracker = new UIExtensionTracker(getDisplay());
		}
		return tracker;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getHelpSystem()
	 */
	public IWorkbenchHelpSystem getHelpSystem() {
		//HACK!! fake this for now
		return new IWorkbenchHelpSystem() {
			public void displayContext(IContext context, int x, int y) {
			}

			public void displayDynamicHelp() {
			}

			public void displayHelp() {
			}

			public void displayHelp(String contextId) {
			}

			public void displayHelp(IContext context) {
			}

			public void displayHelpResource(String href) {
			}

			public void displaySearch() {
			}

			public boolean hasHelpUI() {
				return false;
			}

			public boolean isContextHelpDisplayed() {
				return false;
			}

			public URL resolve(String href, boolean documentOnly) {
				return null;
			}

			public void search(String expression) {
			}

			public void setHelp(IAction action, String contextId) {
				System.out.println("setHelp(IAction)"); //$NON-NLS-1$
			}

			public void setHelp(Control control, String contextId) {
				System.out.println("setHelp(Control)"); //$NON-NLS-1$
			}

			public void setHelp(Menu menu, String contextId) {
				System.out.println("setHelpMenu)"); //$NON-NLS-1$
			}

			public void setHelp(MenuItem item, String contextId) {
				System.out.println("setHelp(MenuItem)"); //$NON-NLS-1$
			}
			
		};
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getImportWizardRegistry()
	 */
	public IWizardRegistry getImportWizardRegistry() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getIntroManager()
	 */
	public IIntroManager getIntroManager() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getNewWizardRegistry()
	 */
	public IWizardRegistry getNewWizardRegistry() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getOperationSupport()
	 */
	public IWorkbenchOperationSupport getOperationSupport() {
		if (operationSupport == null) {
			operationSupport = new IWorkbenchOperationSupport() {
	
				public IOperationHistory getOperationHistory() {
					if (operationHistory == null) {
						operationHistory = new IOperationHistory() {

							public void add(IUndoableOperation operation) {
								// TODO Auto-generated method stub
								
							}

							public void addOperationApprover(
									IOperationApprover approver) {
								// TODO Auto-generated method stub
								
							}

							public void addOperationHistoryListener(
									IOperationHistoryListener listener) {
								// TODO Auto-generated method stub
								
							}

							public boolean canRedo(IUndoContext context) {
								// TODO Auto-generated method stub
								return false;
							}

							public boolean canUndo(IUndoContext context) {
								// TODO Auto-generated method stub
								return false;
							}

							public void closeOperation(boolean operationOk,
									boolean addToHistory, int mode) {
								// TODO Auto-generated method stub
								
							}

							public void dispose(IUndoContext context,
									boolean flushUndo, boolean flushRedo,
									boolean flushContext) {
								// TODO Auto-generated method stub
								
							}

							public IStatus execute(
									IUndoableOperation operation,
									IProgressMonitor monitor, IAdaptable info)
									throws ExecutionException {
								// TODO Auto-generated method stub
								return null;
							}

							public int getLimit(IUndoContext context) {
								// TODO Auto-generated method stub
								return 0;
							}

							public IUndoableOperation[] getRedoHistory(
									IUndoContext context) {
								// TODO Auto-generated method stub
								return null;
							}

							public IUndoableOperation getRedoOperation(
									IUndoContext context) {
								// TODO Auto-generated method stub
								return null;
							}

							public IUndoableOperation[] getUndoHistory(
									IUndoContext context) {
								// TODO Auto-generated method stub
								return null;
							}

							public IUndoableOperation getUndoOperation(
									IUndoContext context) {
								// TODO Auto-generated method stub
								return null;
							}

							public void openOperation(
									ICompositeOperation operation, int mode) {
								// TODO Auto-generated method stub
								
							}

							public void operationChanged(
									IUndoableOperation operation) {
								// TODO Auto-generated method stub
								
							}

							public IStatus redo(IUndoContext context,
									IProgressMonitor monitor, IAdaptable info)
									throws ExecutionException {
								// TODO Auto-generated method stub
								return null;
							}

							public IStatus redoOperation(
									IUndoableOperation operation,
									IProgressMonitor monitor, IAdaptable info)
									throws ExecutionException {
								// TODO Auto-generated method stub
								return null;
							}

							public void removeOperationApprover(
									IOperationApprover approver) {
								// TODO Auto-generated method stub
								
							}

							public void removeOperationHistoryListener(
									IOperationHistoryListener listener) {
								// TODO Auto-generated method stub
								
							}

							public void replaceOperation(
									IUndoableOperation operation,
									IUndoableOperation[] replacements) {
								// TODO Auto-generated method stub
								
							}

							public void setLimit(IUndoContext context, int limit) {
								// TODO Auto-generated method stub
								
							}

							public IStatus undo(IUndoContext context,
									IProgressMonitor monitor, IAdaptable info)
									throws ExecutionException {
								// TODO Auto-generated method stub
								return null;
							}

							public IStatus undoOperation(
									IUndoableOperation operation,
									IProgressMonitor monitor, IAdaptable info)
									throws ExecutionException {
								// TODO Auto-generated method stub
								return null;
							}
							
						};
					}
					
					return operationHistory;
				}
	
				public IUndoContext getUndoContext() {
					// TODO Auto-generated method stub
					return null;
				}
				
			};
		}
		
		return operationSupport;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getPerspectiveRegistry()
	 */
	public IPerspectiveRegistry getPerspectiveRegistry() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getPreferenceManager()
	 */
	public PreferenceManager getPreferenceManager() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getPreferenceStore()
	 */
	public IPreferenceStore getPreferenceStore() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getProgressService()
	 */
	public IProgressService getProgressService() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getSharedImages()
	 */
	public ISharedImages getSharedImages() {
        if (sharedImages == null) {
			sharedImages = new SharedImages();
		}
        return sharedImages;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getThemeManager()
	 */
	public IThemeManager getThemeManager() {
		return WorkbenchThemeManager.getInstance();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getViewRegistry()
	 */
	public IViewRegistry getViewRegistry() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getWorkbenchWindowCount()
	 */
	public int getWorkbenchWindowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getWorkbenchWindows()
	 */
	public IWorkbenchWindow[] getWorkbenchWindows() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#getWorkingSetManager()
	 */
	public IWorkingSetManager getWorkingSetManager() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#isClosing()
	 */
	public boolean isClosing() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#openWorkbenchWindow(java.lang.String, org.eclipse.core.runtime.IAdaptable)
	 */
	public IWorkbenchWindow openWorkbenchWindow(String perspectiveId,
			IAdaptable input) throws WorkbenchException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#openWorkbenchWindow(org.eclipse.core.runtime.IAdaptable)
	 */
	public IWorkbenchWindow openWorkbenchWindow(IAdaptable input)
			throws WorkbenchException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#removeWindowListener(org.eclipse.ui.IWindowListener)
	 */
	public void removeWindowListener(IWindowListener listener) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#removeWorkbenchListener(org.eclipse.ui.IWorkbenchListener)
	 */
	public void removeWorkbenchListener(IWorkbenchListener listener) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#restart()
	 */
	public boolean restart() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#saveAll(org.eclipse.jface.window.IShellProvider, org.eclipse.jface.operation.IRunnableContext, org.eclipse.ui.ISaveableFilter, boolean)
	 */
	public boolean saveAll(IShellProvider shellProvider,
			IRunnableContext runnableContext, ISaveableFilter filter,
			boolean confirm) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#saveAllEditors(boolean)
	 */
	public boolean saveAllEditors(boolean confirm) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#showPerspective(java.lang.String, org.eclipse.ui.IWorkbenchWindow)
	 */
	public IWorkbenchPage showPerspective(String perspectiveId,
			IWorkbenchWindow window) throws WorkbenchException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#showPerspective(java.lang.String, org.eclipse.ui.IWorkbenchWindow, org.eclipse.core.runtime.IAdaptable)
	 */
	public IWorkbenchPage showPerspective(String perspectiveId,
			IWorkbenchWindow window, IAdaptable input)
			throws WorkbenchException {
		// TODO Auto-generated method stub
		return null;
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
		return e4Workbench.getService(api);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.services.IServiceLocator#hasService(java.lang.Class)
	 */
	public boolean hasService(Class api) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbench#isStarting()
	 */
	public boolean isStarting() {
		// TODO Auto-generated method stub
		return false;
	}

}
