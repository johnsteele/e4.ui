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

import org.eclipse.core.commands.Category;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.CommandManager;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.commands.contexts.ContextManager;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.eclipse.e4.core.services.context.IContextFunction;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.core.services.context.spi.ContextFunction;
import org.eclipse.e4.extensions.ExtensionUtils;
import org.eclipse.e4.ui.model.application.ApplicationFactory;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MCommand;
import org.eclipse.e4.ui.model.application.MWindow;
import org.eclipse.e4.ui.model.workbench.MWorkbenchWindow;
import org.eclipse.e4.ui.services.ECommandService;
import org.eclipse.e4.ui.services.EContextService;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.workbench.ui.menus.MenuHelper;
import org.eclipse.help.IContext;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.LegacyActionTools;
import org.eclipse.jface.bindings.BindingManager;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.commands.ICommandImageService;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IWorkbenchCommandSupport;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.contexts.IWorkbenchContextSupport;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.SharedImages;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.WorkingSetManager;
import org.eclipse.ui.internal.activities.ws.WorkbenchActivitySupport;
import org.eclipse.ui.internal.commands.CommandImageManager;
import org.eclipse.ui.internal.commands.CommandImageService;
import org.eclipse.ui.internal.commands.CommandService;
import org.eclipse.ui.internal.contexts.ContextService;
import org.eclipse.ui.internal.decorators.DecoratorManager;
import org.eclipse.ui.internal.keys.BindingService;
import org.eclipse.ui.internal.operations.WorkbenchOperationSupport;
import org.eclipse.ui.internal.progress.ProgressManager;
import org.eclipse.ui.internal.registry.EditorRegistry;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.internal.registry.UIExtensionTracker;
import org.eclipse.ui.internal.registry.ViewRegistry;
import org.eclipse.ui.internal.services.IWorkbenchLocationService;
import org.eclipse.ui.internal.themes.WorkbenchThemeManager;
import org.eclipse.ui.intro.IIntroManager;
import org.eclipse.ui.intro.IIntroPart;
import org.eclipse.ui.keys.IBindingService;
import org.eclipse.ui.model.ContributionComparator;
import org.eclipse.ui.model.IContributionService;
import org.eclipse.ui.operations.IWorkbenchOperationSupport;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.progress.IProgressService;
import org.eclipse.ui.themes.IThemeManager;
import org.eclipse.ui.views.IViewRegistry;
import org.eclipse.ui.wizards.IWizardRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * @since 4.0
 * 
 */
public class LegacyWBImpl implements IWorkbench {
	private IEclipseContext context;
	private CommandManager commandManager;
	private BindingManager bindingManager;
	private CommandImageManager commandImageManager;

	private static Map<MWorkbenchWindow, LegacyWBWImpl> wbwModel2LegacyImpl = new HashMap<MWorkbenchWindow, LegacyWBWImpl>();
	public HashMap<String, MCommand> commandsById;
	protected ContextManager contextManager;

	/**
	 * @param e4Workbench
	 * @param workbench
	 */
	public LegacyWBImpl(IEclipseContext context) {
		this.context = context;

		// Add myself to the context
		context.set(LegacyWBImpl.class.getName(), this);

		// register workspace adapters
		IDE.registerAdapters();

		// Register necessary services in the context
		registerServices();
	}

	/**
	 * Adds ComputedValues for all the services to the context
	 */
	private void registerServices() {
		context.set(ISharedImages.class.getName(), new IContextFunction() {
			public Object compute(IEclipseContext context, Object[] arguments) {
				return new SharedImages();
			}
		});
		context.set(IEditorRegistry.class.getName(), new IContextFunction() {
			public Object compute(IEclipseContext context, Object[] arguments) {
				return new EditorRegistry();
			}
		});
		context.set(IExtensionTracker.class.getName(), new IContextFunction() {
			public Object compute(IEclipseContext context, Object[] arguments) {
				return new UIExtensionTracker(getDisplay());
			}
		});
		context.set(IDecoratorManager.class.getName(), new IContextFunction() {
			public Object compute(IEclipseContext context, Object[] arguments) {
				return new DecoratorManager();
			}
		});
		context.set(IWorkbenchActivitySupport.class.getName(),
				new IContextFunction() {
					public Object compute(IEclipseContext context,
							Object[] arguments) {
						return new WorkbenchActivitySupport();
					}
				});
		// private IWorkbenchOperationSupport operationSupport;
		context.set(IWorkbenchOperationSupport.class.getName(),
				new IContextFunction() {
					public Object compute(IEclipseContext context,
							Object[] arguments) {
						return new WorkbenchOperationSupport();
					}
				});
		// private IWorkingSetManager wsMgr;
		context.set(IWorkingSetManager.class.getName(), new IContextFunction() {
			public Object compute(IEclipseContext context, Object[] arguments) {
				Bundle bundle = Platform
						.getBundle("org.eclipse.e4.ui.model.workbench"); //$NON-NLS-1$
				BundleContext bc = bundle.getBundleContext();
				return new WorkingSetManager(bc);
			}
		});
		// private IContextService contextService;
		context.set(IContextService.class.getName(), new ContextFunction() {
			public Object compute(IEclipseContext context, Object[] arguments) {
				if (contextManager == null) {
					contextManager = (ContextManager) context
							.get(ContextManager.class.getName());
					ContextService cs = new ContextService(contextManager);
					cs.readRegistry();
					EContextService ecs = (EContextService) context
							.get(EContextService.class.getName());
					ecs
							.activateContext("org.eclipse.ui.contexts.dialogAndWindow"); //$NON-NLS-1$
				}
				return new ContextService(contextManager);
			}
		});
		context.set(PreferenceManager.class.getName(), new IContextFunction() {
			public Object compute(IEclipseContext context, Object[] arguments) {
				return WorkbenchPlugin.getDefault().getPreferenceManager();
			}
		});
		context.set(IViewRegistry.class.getName(), new ContextFunction() {
			@Override
			public Object compute(IEclipseContext context, Object[] arguments) {
				return new ViewRegistry();
			}
		});
		context.set(ICommandService.class.getName(), new IContextFunction() {
			public Object compute(IEclipseContext context, Object[] arguments) {
				if (commandManager == null) {
					commandManager = (CommandManager) context
							.get(CommandManager.class.getName());
					CommandService cs = new CommandService(commandManager);
					cs.readRegistry();
					populateCommands();
					populateActionSets();
					return cs;
				}
				return new CommandService(commandManager);
			}
		});
		context.set(IHandlerService.class.getName(), new IContextFunction() {

			public Object compute(IEclipseContext context, Object[] arguments) {
				// make sure the command service is initialized
				context.get(ICommandService.class.getName());
				LegacyHandlerService hs = new LegacyHandlerService(context);
				hs.readRegistry();
				return hs;
			}
		});
		context.set(IBindingService.class.getName(), new ContextFunction() {
			@Override
			public Object compute(IEclipseContext context, Object[] arguments) {
				if (bindingManager == null) {
					System.err.println("Initialized legacy binding manager"); //$NON-NLS-1$
					commandManager = (CommandManager) context
							.get(CommandManager.class.getName());
					ContextManager contextManager = (ContextManager) context
							.get(ContextManager.class.getName());
					bindingManager = new BindingManager(contextManager,
							commandManager);
					ICommandService cs = (ICommandService) context
							.get(ICommandService.class.getName());
					IBindingService bindingService = new BindingService(
							bindingManager, cs, LegacyWBImpl.this);
					bindingService.readRegistryAndPreferences(cs);
				}
				ICommandService cs = (ICommandService) context
						.get(ICommandService.class.getName());
				return new BindingService(bindingManager, cs, LegacyWBImpl.this);
			}
		});
		context.set(ICommandImageService.class.getName(),
				new IContextFunction() {

					public Object compute(IEclipseContext context,
							Object[] arguments) {
						commandImageManager = new CommandImageManager();
						ICommandService cs = (ICommandService) context
								.get(ICommandService.class.getName());
						CommandImageService cis = new CommandImageService(
								commandImageManager, cs);
						cis.readRegistry();
						return cis;
					}
				});
		context.set(IWorkbenchLocationService.class.getName(),
				new IContextFunction() {

					public Object compute(IEclipseContext context,
							Object[] arguments) {
						return new IWorkbenchLocationService() {

							public IWorkbenchWindow getWorkbenchWindow() {
								return null;
							}

							public IWorkbench getWorkbench() {
								return LegacyWBImpl.this;
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
		context.set(IContributionService.class.getName(),
				new IContextFunction() {

					public Object compute(IEclipseContext context,
							Object[] arguments) {
						return new IContributionService() {

							public ContributionComparator getComparatorFor(
									String contributionType) {
								return new ContributionComparator();
							}
						};
					}
				});
	}

	protected void populateCommands() {
		ECommandService cs = (ECommandService) context
				.get(ECommandService.class.getName());
		commandsById = new HashMap<String, MCommand>();
		MApplication<MWindow<?>> app = (MApplication<MWindow<?>>) context
				.get(MApplication.class.getName());
		Command[] cmds = commandManager.getAllCommands();
		for (int i = 0; i < cmds.length; i++) {
			Command cmd = cmds[i];
			cs.getCommand(cmd.getId());
			MCommand mcmd = ApplicationFactory.eINSTANCE.createMCommand();
			mcmd.setId(cmd.getId());
			try {
				mcmd.setName(cmd.getName());
			} catch (NotDefinedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			app.getCommand().add(mcmd);
			commandsById.put(cmd.getId(), mcmd);
		}
	}

	protected void populateActionSets() {
		ECommandService cs = (ECommandService) context
				.get(ECommandService.class.getName());
		Category category = cs
				.getCategory(IWorkbenchRegistryConstants.PL_ACTION_SETS);
		category.define("Action Sets", null); //$NON-NLS-1$
		MApplication<MWindow<?>> app = (MApplication<MWindow<?>>) context
				.get(MApplication.class.getName());
		IConfigurationElement[] actionSetElements = ExtensionUtils
				.getExtensions(IWorkbenchRegistryConstants.PL_ACTION_SETS);
		for (IConfigurationElement ase : actionSetElements) {
			IConfigurationElement[] elements = ase
					.getChildren(IWorkbenchRegistryConstants.TAG_ACTION);

			for (IConfigurationElement element : elements) {
				String id = MenuHelper.getActionSetCommandId(element);
				MCommand mcmd = ApplicationFactory.eINSTANCE.createMCommand();
				mcmd.setId(id);
				mcmd.setName(LegacyActionTools.removeMnemonics(MenuHelper
						.getLabel(element)));
				app.getCommand().add(mcmd);
				commandsById.put(mcmd.getId(), mcmd);
				Command command = cs.getCommand(id);
				if (!command.isDefined()) {
					command.define(mcmd.getName(), null, category);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbench#addWindowListener(org.eclipse.ui.IWindowListener
	 * )
	 */
	public void addWindowListener(IWindowListener listener) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.IWorkbench#addWorkbenchListener(org.eclipse.ui.
	 * IWorkbenchListener)
	 */
	public void addWorkbenchListener(IWorkbenchListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#close()
	 */
	public boolean close() {
		org.eclipse.e4.workbench.ui.IWorkbench wb = (org.eclipse.e4.workbench.ui.IWorkbench) context
				.get(org.eclipse.e4.workbench.ui.IWorkbench.class.getName());
		wb.close();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#createLocalWorkingSetManager()
	 */
	public ILocalWorkingSetManager createLocalWorkingSetManager() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getActiveWorkbenchWindow()
	 */
	public IWorkbenchWindow getActiveWorkbenchWindow() {
		// TODO: ensure windows list is in z-order
		MApplication<MWorkbenchWindow> application = (MApplication<MWorkbenchWindow>) context
				.get(MApplication.class.getName());
		MWorkbenchWindow workbenchWindow = application.getWindows().get(0);
		return getWBWImpl(workbenchWindow);
	}

	/**
	 * @return
	 */
	private LegacyWBWImpl getWBWImpl(MWorkbenchWindow workbenchWindow) {
		if (!wbwModel2LegacyImpl.containsKey(workbenchWindow)) {
			LegacyWBWImpl impl = new LegacyWBWImpl(workbenchWindow);
			wbwModel2LegacyImpl.put(workbenchWindow, impl);
		}

		return wbwModel2LegacyImpl.get(workbenchWindow);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getActivitySupport()
	 */
	public IWorkbenchActivitySupport getActivitySupport() {
		return (IWorkbenchActivitySupport) context
				.get(IWorkbenchActivitySupport.class.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getBrowserSupport()
	 */
	public IWorkbenchBrowserSupport getBrowserSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getCommandSupport()
	 */
	public IWorkbenchCommandSupport getCommandSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getContextSupport()
	 */
	public IWorkbenchContextSupport getContextSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getDecoratorManager()
	 */
	public IDecoratorManager getDecoratorManager() {
		return (IDecoratorManager) context.get(IDecoratorManager.class
				.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getDisplay()
	 */
	public Display getDisplay() {
		if (Display.getCurrent() != null)
			return Display.getCurrent();

		Shell shell = (Shell) context.get(IServiceConstants.ACTIVE_SHELL);
		return shell.getDisplay();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getEditorRegistry()
	 */
	public IEditorRegistry getEditorRegistry() {
		return (IEditorRegistry) context.get(IEditorRegistry.class.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getElementFactory(java.lang.String)
	 */
	public IElementFactory getElementFactory(String factoryId) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getExportWizardRegistry()
	 */
	public IWizardRegistry getExportWizardRegistry() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getExtensionTracker()
	 */
	public IExtensionTracker getExtensionTracker() {
		return (IExtensionTracker) context.get(IExtensionTracker.class
				.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getHelpSystem()
	 */
	public IWorkbenchHelpSystem getHelpSystem() {
		// HACK!! fake this for now
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
			}

			public void setHelp(Control control, String contextId) {
			}

			public void setHelp(Menu menu, String contextId) {
			}

			public void setHelp(MenuItem item, String contextId) {
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getImportWizardRegistry()
	 */
	public IWizardRegistry getImportWizardRegistry() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getIntroManager()
	 */
	public IIntroManager getIntroManager() {
		return new IIntroManager() {

			public IIntroPart showIntro(IWorkbenchWindow preferredWindow,
					boolean standby) {
				// TODO Auto-generated method stub
				return null;
			}

			public void setIntroStandby(IIntroPart part, boolean standby) {
				// TODO Auto-generated method stub

			}

			public boolean isNewContentAvailable() {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean isIntroStandby(IIntroPart part) {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean hasIntro() {
				// TODO Auto-generated method stub
				return false;
			}

			public IIntroPart getIntro() {
				// TODO Auto-generated method stub
				return null;
			}

			public boolean closeIntro(IIntroPart part) {
				// TODO Auto-generated method stub
				return false;
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getNewWizardRegistry()
	 */
	public IWizardRegistry getNewWizardRegistry() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getOperationSupport()
	 */
	public IWorkbenchOperationSupport getOperationSupport() {
		return (IWorkbenchOperationSupport) context
				.get(IWorkbenchOperationSupport.class.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getPerspectiveRegistry()
	 */
	public IPerspectiveRegistry getPerspectiveRegistry() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getPreferenceManager()
	 */
	public PreferenceManager getPreferenceManager() {
		return (PreferenceManager) context.get(PreferenceManager.class
				.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getPreferenceStore()
	 */
	public IPreferenceStore getPreferenceStore() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getProgressService()
	 */
	public IProgressService getProgressService() {
		return ProgressManager.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getSharedImages()
	 */
	public ISharedImages getSharedImages() {
		return (ISharedImages) context.get(ISharedImages.class.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getThemeManager()
	 */
	public IThemeManager getThemeManager() {
		return WorkbenchThemeManager.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getViewRegistry()
	 */
	public IViewRegistry getViewRegistry() {
		return (IViewRegistry) context.get(IViewRegistry.class.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getWorkbenchWindowCount()
	 */
	public int getWorkbenchWindowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getWorkbenchWindows()
	 */
	public IWorkbenchWindow[] getWorkbenchWindows() {
		IWorkbenchWindow[] wbws = { getActiveWorkbenchWindow() };
		return wbws;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#getWorkingSetManager()
	 */
	public IWorkingSetManager getWorkingSetManager() {
		return (IWorkingSetManager) context.get(IWorkingSetManager.class
				.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#isClosing()
	 */
	public boolean isClosing() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#openWorkbenchWindow(java.lang.String,
	 * org.eclipse.core.runtime.IAdaptable)
	 */
	public IWorkbenchWindow openWorkbenchWindow(String perspectiveId,
			IAdaptable input) throws WorkbenchException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbench#openWorkbenchWindow(org.eclipse.core.runtime
	 * .IAdaptable)
	 */
	public IWorkbenchWindow openWorkbenchWindow(IAdaptable input)
			throws WorkbenchException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbench#removeWindowListener(org.eclipse.ui.IWindowListener
	 * )
	 */
	public void removeWindowListener(IWindowListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.IWorkbench#removeWorkbenchListener(org.eclipse.ui.
	 * IWorkbenchListener)
	 */
	public void removeWorkbenchListener(IWorkbenchListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#restart()
	 */
	public boolean restart() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbench#saveAll(org.eclipse.jface.window.IShellProvider
	 * , org.eclipse.jface.operation.IRunnableContext,
	 * org.eclipse.ui.ISaveableFilter, boolean)
	 */
	public boolean saveAll(IShellProvider shellProvider,
			IRunnableContext runnableContext, ISaveableFilter filter,
			boolean confirm) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#saveAllEditors(boolean)
	 */
	public boolean saveAllEditors(boolean confirm) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#showPerspective(java.lang.String,
	 * org.eclipse.ui.IWorkbenchWindow)
	 */
	public IWorkbenchPage showPerspective(String perspectiveId,
			IWorkbenchWindow window) throws WorkbenchException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#showPerspective(java.lang.String,
	 * org.eclipse.ui.IWorkbenchWindow, org.eclipse.core.runtime.IAdaptable)
	 */
	public IWorkbenchPage showPerspective(String perspectiveId,
			IWorkbenchWindow window, IAdaptable input)
			throws WorkbenchException {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbench#isStarting()
	 */
	public boolean isStarting() {
		// TODO Auto-generated method stub
		return false;
	}

}
