/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.internal;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.core.services.context.spi.ContextFunction;
import org.eclipse.e4.extensions.ExtensionUtils;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationFactory;
import org.eclipse.e4.ui.model.application.MMenu;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.model.application.MWindow;
import org.eclipse.e4.ui.services.EContextService;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.workbench.ui.api.LegacySelectionService;
import org.eclipse.e4.workbench.ui.internal.Activator;
import org.eclipse.e4.workbench.ui.internal.Policy;
import org.eclipse.e4.workbench.ui.menus.ActionSet;
import org.eclipse.e4.workbench.ui.menus.MenuHelper;
import org.eclipse.jface.action.ContributionManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.internal.provisional.action.ICoolBarManager2;
import org.eclipse.jface.internal.provisional.action.IToolBarManager2;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.CBanner;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ActiveShellExpression;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IPersistable;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.LegacyHandlerService;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.branding.IProductConstants;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.contexts.IWorkbenchContextSupport;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.internal.StartupThreading.StartupRunnable;
import org.eclipse.ui.internal.actions.CommandAction;
import org.eclipse.ui.internal.dialogs.CustomizePerspectiveDialog;
import org.eclipse.ui.internal.dnd.DragUtil;
import org.eclipse.ui.internal.dnd.SwtUtil;
import org.eclipse.ui.internal.expressions.WorkbenchWindowExpression;
import org.eclipse.ui.internal.handlers.ActionCommandMappingService;
import org.eclipse.ui.internal.handlers.ActionDelegateHandlerProxy;
import org.eclipse.ui.internal.handlers.IActionCommandMappingService;
import org.eclipse.ui.internal.layout.ITrimManager;
import org.eclipse.ui.internal.layout.IWindowTrim;
import org.eclipse.ui.internal.layout.TrimLayout;
import org.eclipse.ui.internal.menus.IActionSetsListener;
import org.eclipse.ui.internal.menus.LegacyActionPersistence;
import org.eclipse.ui.internal.menus.TrimBarManager2;
import org.eclipse.ui.internal.menus.TrimContributionManager;
import org.eclipse.ui.internal.misc.UIListenerLogging;
import org.eclipse.ui.internal.misc.UIStats;
import org.eclipse.ui.internal.presentations.DefaultActionBarPresentationFactory;
import org.eclipse.ui.internal.progress.ProgressRegion;
import org.eclipse.ui.internal.provisional.application.IActionBarConfigurer2;
import org.eclipse.ui.internal.provisional.presentations.IActionBarPresentationFactory;
import org.eclipse.ui.internal.registry.ActionSetRegistry;
import org.eclipse.ui.internal.registry.IActionSetDescriptor;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.internal.registry.UIExtensionTracker;
import org.eclipse.ui.internal.services.IServiceLocatorCreator;
import org.eclipse.ui.internal.services.IWorkbenchLocationService;
import org.eclipse.ui.internal.services.ServiceLocator;
import org.eclipse.ui.internal.services.WorkbenchLocationService;
import org.eclipse.ui.internal.tweaklets.Tweaklets;
import org.eclipse.ui.internal.tweaklets.WorkbenchImplementation;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.presentations.AbstractPresentationFactory;
import org.eclipse.ui.services.IDisposable;
import org.eclipse.ui.services.IServiceScopes;

/**
 * A window within the workbench.
 */
public class WorkbenchWindow extends ApplicationWindow implements
		IWorkbenchWindow {

	private WorkbenchWindowAdvisor windowAdvisor;

	private ActionBarAdvisor actionBarAdvisor;

	private int number;

	private PageList pageList = new PageList();

	private PageListenerList pageListeners = new PageListenerList();

	private PerspectiveListenerList perspectiveListeners = new PerspectiveListenerList();

	private WWinPartService partService = new WWinPartService(this);

	private WWinActionBars actionBars;

	private boolean closing = false;

	private boolean shellActivated = false;

	private FastViewBar fastViewBar;

	private PerspectiveSwitcher perspectiveSwitcher = null;

	private TrimLayout defaultLayout;

	ProgressRegion progressRegion = null;

	// Legacy (3.2) contribution handling
	private TrimBarManager2 trimMgr2 = null;

	// 3.3 Trim Contribution handling
	private TrimContributionManager trimContributionMgr = null;

	/**
	 * The map of services maintained by the workbench window. These services
	 * are initialized during workbench window during the
	 * {@link #configureShell(Shell)}.
	 */
	private final ServiceLocator serviceLocator;

	private CBanner topBar;

	// Previous shell size. Used to prevent the CBanner from triggering
	// redundant layouts
	// private Point lastShellSize = new Point(0, 0);

	/**
	 * The composite under which workbench pages create their controls.
	 * 
	 * @since 3.0
	 */
	private Composite pageComposite;

	/**
	 * Bit flags indication which submenus (New, Show Views, ...) this window
	 * contains. Initially none.
	 * 
	 * @since 3.0
	 */
	private int submenus = 0x00;

	/**
	 * Object for configuring this workbench window. Lazily initialized to an
	 * instance unique to this window.
	 * 
	 * @since 3.0
	 */
	private WorkbenchWindowConfigurer windowConfigurer = null;

	/**
	 * List of generic property listeners.
	 * 
	 * @since 3.3
	 */
	private ListenerList genericPropertyListeners = new ListenerList();

	static final String TEXT_DELIMITERS = TextProcessor.getDefaultDelimiters()
			+ "-"; //$NON-NLS-1$

	// constants for shortcut bar group ids
	static final String GRP_PAGES = "pages"; //$NON-NLS-1$

	static final String GRP_PERSPECTIVES = "perspectives"; //$NON-NLS-1$

	static final String GRP_FAST_VIEWS = "fastViews"; //$NON-NLS-1$

	// static fields for inner classes.
	static final int VGAP = 0;

	static final int CLIENT_INSET = 3;

	static final int BAR_SIZE = 23;

	/**
	 * Coolbar visibility change property.
	 * 
	 * @since 3.3
	 */
	public static final String PROP_COOLBAR_VISIBLE = "coolbarVisible"; //$NON-NLS-1$

	/**
	 * Perspective bar visibility change property.
	 * 
	 * @since 3.3
	 */
	public static final String PROP_PERSPECTIVEBAR_VISIBLE = "perspectiveBarVisible"; //$NON-NLS-1$

	/**
	 * The status line visibility change property. for internal use only.
	 * 
	 * @since 3.4
	 */
	public static final String PROP_STATUS_LINE_VISIBLE = "statusLineVisible"; //$NON-NLS-1$

	/**
	 * Constant (bit mask) indicating which the Show View submenu is probably
	 * present somewhere in this window.
	 * 
	 * @see #addSubmenu
	 * @since 3.0
	 */
	public static final int SHOW_VIEW_SUBMENU = 0x01;

	/**
	 * Constant (bit mask) indicating which the Open Perspective submenu is
	 * probably present somewhere in this window.
	 * 
	 * @see #addSubmenu
	 * @since 3.0
	 */
	public static final int OPEN_PERSPECTIVE_SUBMENU = 0x02;

	/**
	 * Constant (bit mask) indicating which the New Wizard submenu is probably
	 * present somewhere in this window.
	 * 
	 * @see #addSubmenu
	 * @since 3.0
	 */
	public static final int NEW_WIZARD_SUBMENU = 0x04;

	/**
	 * Remembers that this window contains the given submenu.
	 * 
	 * @param type
	 *            the type of submenu, one of: {@link #NEW_WIZARD_SUBMENU
	 *            NEW_WIZARD_SUBMENU}, {@link #OPEN_PERSPECTIVE_SUBMENU
	 *            OPEN_PERSPECTIVE_SUBMENU}, {@link #SHOW_VIEW_SUBMENU
	 *            SHOW_VIEW_SUBMENU}
	 * @see #containsSubmenu
	 * @since 3.0
	 */
	public void addSubmenu(int type) {
		submenus |= type;
	}

	/**
	 * Checks to see if this window contains the given type of submenu.
	 * 
	 * @param type
	 *            the type of submenu, one of: {@link #NEW_WIZARD_SUBMENU
	 *            NEW_WIZARD_SUBMENU}, {@link #OPEN_PERSPECTIVE_SUBMENU
	 *            OPEN_PERSPECTIVE_SUBMENU}, {@link #SHOW_VIEW_SUBMENU
	 *            SHOW_VIEW_SUBMENU}
	 * @return <code>true</code> if window contains submenu, <code>false</code>
	 *         otherwise
	 * @see #addSubmenu
	 * @since 3.0
	 */
	public boolean containsSubmenu(int type) {
		return ((submenus & type) != 0);
	}

	/**
	 * Constant indicating that all the actions bars should be filled.
	 * 
	 * @since 3.0
	 */
	private static final int FILL_ALL_ACTION_BARS = ActionBarAdvisor.FILL_MENU_BAR
			| ActionBarAdvisor.FILL_COOL_BAR
			| ActionBarAdvisor.FILL_STATUS_LINE;

	/**
	 * Creates and initializes a new workbench window.
	 * 
	 * @param number
	 *            the number for the window
	 */
	public WorkbenchWindow(int number) {
		super(null);
		this.number = number;

		// Make sure there is a workbench. This call will throw
		// an exception if workbench not created yet.
		final Workbench workbench = (Workbench) PlatformUI.getWorkbench();
		createE4Model(workbench);
		e4Context = e4Window.getContext();

		IServiceLocatorCreator slc = (IServiceLocatorCreator) workbench
				.getService(IServiceLocatorCreator.class);
		this.serviceLocator = (ServiceLocator) slc.createServiceLocator(
				workbench, null, new IDisposable() {
					public void dispose() {
						final Shell shell = getShell();
						if (shell != null && !shell.isDisposed()) {
							close();
						}
					}
				});
		serviceLocator.setContext(e4Context);

		initializeDefaultServices();

		// Add contribution managers that are exposed to other plugins.
		addMenuBar();
		addCoolBar(SWT.NONE); // style is unused
		addStatusLine();

		fireWindowOpening();

		// set the shell style
		setShellStyle(getWindowConfigurer().getShellStyle());

		// Fill the action bars
		fillActionBars(FILL_ALL_ACTION_BARS);
		MenuHelper.loadMainMenu(e4Window.getContext(), e4Window.getMainMenu(),
				getMenuBarManager());
		e4ActionSets = MenuHelper.processActionSets(e4Window.getContext(),
				e4Window.getMainMenu());
	}

	/**
	 * @param workbench
	 */
	private void createE4Model(Workbench workbench) {
		e4Window = MApplicationFactory.eINSTANCE.createWindow();
		MApplication app = (MApplication) workbench
				.getService(MApplication.class);
		app.getChildren().add(e4Window);
		e4Window.setWidth(1024);
		e4Window.setHeight(768);
		e4Window.setName("MyWindow"); //$NON-NLS-1$
		initializeWindowImages(e4Window);
		final MMenu mainMenu = MApplicationFactory.eINSTANCE.createMenu();
		mainMenu.setId(MenuHelper.MAIN_MENU_ID);
		e4Window.setMainMenu(mainMenu);
	}

	/**
	 * Set the images corresponding to the workbench window
	 */
	private void initializeWindowImages(MWindow window) {
		IProduct product = Platform.getProduct();
		String images = product == null ? null : product
				.getProperty(IProductConstants.WINDOW_IMAGES);
		if (images == null) {
			// backwards compatibility
			images = product == null ? null : product
					.getProperty(IProductConstants.WINDOW_IMAGE);
		}
		if (images == null)
			return;
		// for now just take the first image - the model currently only accepts
		// one image
		if (images.indexOf(',') > 0)
			images = images.substring(0, images.indexOf(','));
		URL iconURL = FileLocator.find(product.getDefiningBundle(), new Path(
				images), null);
		if (iconURL != null)
			e4Window.setIconURI(iconURL.toExternalForm());
	}

	/**
	 * Return the style bits for the shortcut bar.
	 * 
	 * @return int
	 */
	protected int perspectiveBarStyle() {
		return SWT.FLAT | SWT.WRAP | SWT.RIGHT | SWT.HORIZONTAL;
	}

	private TrimDropTarget trimDropTarget;

	private boolean coolBarVisible = true;

	private boolean perspectiveBarVisible = true;

	private boolean fastViewBarVisible = true;

	private boolean statusLineVisible = true;

	private IWindowTrim statusLineTrim = null;

	/**
	 * The handlers for global actions that were last submitted to the workbench
	 * command support. This is a map of command identifiers to
	 * <code>ActionHandler</code>. This map is never <code>null</code>, and is
	 * never empty as long as at least one global action has been registered.
	 */
	private Map globalActionHandlersByCommandId = new HashMap();

	/**
	 * The list of handler submissions submitted to the workbench command
	 * support. This list may be empty, but it is never <code>null</code>.
	 */
	private List handlerActivations = new ArrayList();

	/**
	 * The number of large updates that are currently going on. If this is
	 * number is greater than zero, then UI updateActionBars is a no-op.
	 * 
	 * @since 3.1
	 */
	private int largeUpdates = 0;

	private IExtensionTracker tracker;

	void registerGlobalAction(IAction globalAction) {
		String commandId = globalAction.getActionDefinitionId();

		if (commandId != null) {
			final Object value = globalActionHandlersByCommandId.get(commandId);
			if (value instanceof ActionHandler) {
				// This handler is about to get clobbered, so dispose it.
				final ActionHandler handler = (ActionHandler) value;
				handler.dispose();
			}

			if (globalAction instanceof CommandAction) {
				final String actionId = globalAction.getId();
				if (actionId != null) {
					final IActionCommandMappingService mappingService = (IActionCommandMappingService) serviceLocator
							.getService(IActionCommandMappingService.class);
					mappingService.map(actionId, commandId);
				}
			} else {
				globalActionHandlersByCommandId.put(commandId,
						new ActionHandler(globalAction));
			}
		}

		submitGlobalActions();
	}

	/**
	 * <p>
	 * Submits the action handlers for action set actions and global actions.
	 * Global actions are given priority, so that if a global action and an
	 * action set action both handle the same command, the global action is
	 * given priority.
	 * </p>
	 * <p>
	 * These submissions are submitted as <code>Priority.LEGACY</code>, which
	 * means that they are the lowest priority. This means that if a higher
	 * priority submission handles the same command under the same conditions,
	 * that that submission will become the handler.
	 * </p>
	 */
	void submitGlobalActions() {
		final IHandlerService handlerService = (IHandlerService) serviceLocator
				.getService(IHandlerService.class);

		/*
		 * Mash the action sets and global actions together, with global actions
		 * taking priority.
		 */
		Map handlersByCommandId = new HashMap();
		handlersByCommandId.putAll(globalActionHandlersByCommandId);

		List newHandlers = new ArrayList(handlersByCommandId.size());

		Iterator existingIter = handlerActivations.iterator();
		while (existingIter.hasNext()) {
			IHandlerActivation next = (IHandlerActivation) existingIter.next();

			String cmdId = next.getCommandId();

			Object handler = handlersByCommandId.get(cmdId);
			if (handler == next.getHandler()) {
				handlersByCommandId.remove(cmdId);
				newHandlers.add(next);
			} else {
				handlerService.deactivateHandler(next);
			}
		}

		final Shell shell = getShell();
		if (shell != null) {
			final Expression expression = new ActiveShellExpression(shell);
			for (Iterator iterator = handlersByCommandId.entrySet().iterator(); iterator
					.hasNext();) {
				Map.Entry entry = (Map.Entry) iterator.next();
				String commandId = (String) entry.getKey();
				IHandler handler = (IHandler) entry.getValue();
				newHandlers.add(handlerService.activateHandler(commandId,
						handler, expression));
			}
		}

		handlerActivations = newHandlers;
	}

	/**
	 * Add a generic property listener.
	 * 
	 * @param listener
	 *            the listener to add
	 * @since 3.3
	 */
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		genericPropertyListeners.add(listener);
	}

	/**
	 * Removes a generic property listener.
	 * 
	 * @param listener
	 *            the listener to remove
	 * @since 3.3
	 */
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		genericPropertyListeners.remove(listener);
	}

	/*
	 * Adds an listener to the part service.
	 */
	public void addPageListener(IPageListener l) {
		pageListeners.addPageListener(l);
	}

	/**
	 * @see org.eclipse.ui.IPageService
	 */
	public void addPerspectiveListener(org.eclipse.ui.IPerspectiveListener l) {
		perspectiveListeners.addPerspectiveListener(l);
	}

	/**
	 * Configures this window to have a perspecive bar. Does nothing if it
	 * already has one.
	 */
	protected void addPerspectiveBar(int style) {
		Assert.isTrue(perspectiveSwitcher == null);
		perspectiveSwitcher = new PerspectiveSwitcher(this, topBar, style);
	}

	/**
	 * Close the window.
	 * 
	 * Assumes that busy cursor is active.
	 */
	private boolean busyClose() {
		// Whether the window was actually closed or not
		boolean windowClosed = false;

		// Setup internal flags to indicate window is in
		// progress of closing and no update should be done.
		closing = true;

		try {
			// Only do the check if it is OK to close if we are not closing
			// via the workbench as the workbench will check this itself.
			Workbench workbench = getWorkbenchImpl();
			int count = workbench.getWorkbenchWindowCount();
			// also check for starting - if the first window dies on startup
			// then we'll need to open a default window.
			if (!workbench.isStarting()
					&& !workbench.isClosing()
					&& count <= 1
					&& workbench.getWorkbenchConfigurer()
							.getExitOnLastWindowClose()) {
				windowClosed = workbench.close();
			} else {
				if (okToClose()) {
					windowClosed = hardClose();
				}
			}
		} finally {
			if (!windowClosed) {
				// Reset the internal flags if window was not closed.
				closing = false;
			}
		}

		if (windowClosed && tracker != null) {
			tracker.close();
		}

		return windowClosed;
	}

	/**
	 * Opens a new page. Assumes that busy cursor is active.
	 * <p>
	 * <b>Note:</b> Since release 2.0, a window is limited to contain at most
	 * one page. If a page exist in the window when this method is used, then
	 * another window is created for the new page. Callers are strongly
	 * recommended to use the <code>IWorkbench.openPerspective</code> APIs to
	 * programmatically show a perspective.
	 * </p>
	 */
	protected IWorkbenchPage busyOpenPage(String perspID, IAdaptable input)
			throws WorkbenchException {
		IWorkbenchPage newPage = null;

		if (pageList.isEmpty()) {
			newPage = ((WorkbenchImplementation) Tweaklets
					.get(WorkbenchImplementation.KEY)).createWorkbenchPage(
					this, perspID, input);
			pageList.add(newPage);
			firePageOpened(newPage);
			setActivePage(newPage);
		} else {
			IWorkbenchWindow window = getWorkbench().openWorkbenchWindow(
					perspID, input);
			newPage = window.getActivePage();
		}

		return newPage;
	}

	/**
	 * @see Window
	 */
	public int open() {
		fireWindowCreated();
		// getWindowAdvisor().openIntro();
		// int result = super.open();
		org.eclipse.e4.workbench.ui.internal.Workbench e4Workbench = (org.eclipse.e4.workbench.ui.internal.Workbench) e4Window
				.getContext().get(
						org.eclipse.e4.workbench.ui.internal.Workbench.class
								.getName());
		e4Workbench.createGUI(e4Window);

		// It's time for a layout ... to insure that if TrimLayout
		// is in play, it updates all of the trim it's responsible
		// for. We have to do this before updating in order to get
		// the PerspectiveBar management correct...see defect 137334
		if (getShell().getChildren()[0] instanceof Composite) {
			try {
				Field field = Window.class.getDeclaredField("contents"); //$NON-NLS-1$
				field.setAccessible(true);
				field.set(this, getShell().getChildren()[0]);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		e4Context.set(ISources.ACTIVE_WORKBENCH_WINDOW_SHELL_NAME, getShell());
		// createStatusLine(getShell());
		getShell().layout();
		getShell().addShellListener(getShellListener());
		getShell().setData(this);
		String title = getWindowConfigurer().basicGetTitle();
		if (title != null) {
			getShell().setText(TextProcessor.process(title, TEXT_DELIMITERS));
		}

		submitGlobalActions();

		fireWindowOpened();
		if (perspectiveSwitcher != null) {
			perspectiveSwitcher.updatePerspectiveBar();
			perspectiveSwitcher.updateBarParent();
		}

		return OK;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#getShell()
	 */
	@Override
	public Shell getShell() {
		return (Shell) e4Window.getWidget();
	}

	/*
	 * (non-Javadoc) Method declared on Window.
	 */
	protected boolean canHandleShellCloseEvent() {
		if (!super.canHandleShellCloseEvent()) {
			return false;
		}
		// let the advisor or other interested parties
		// veto the user's explicit request to close the window
		return fireWindowShellClosing();
	}

	/**
	 * @see IWorkbenchWindow
	 */
	public boolean close() {
		final boolean[] ret = new boolean[1];
		BusyIndicator.showWhile(null, new Runnable() {
			public void run() {
				ret[0] = busyClose();
			}
		});
		return ret[0];
	}

	protected boolean isClosing() {
		return closing || getWorkbenchImpl().isClosing();
	}

	/**
	 * Return whether or not the coolbar layout is locked.
	 */
	protected boolean isCoolBarLocked() {
		ICoolBarManager cbm = getCoolBarManager2();
		return cbm != null && cbm.getLockLayout();
	}

	/**
	 * Close all of the pages.
	 */
	private void closeAllPages() {
		// Deactivate active page.
		setActivePage(null);

		// Clone and deref all so that calls to getPages() returns
		// empty list (if call by pageClosed event handlers)
		PageList oldList = pageList;
		pageList = new PageList();

		// Close all.
		Iterator itr = oldList.iterator();
		while (itr.hasNext()) {
			WorkbenchPage page = (WorkbenchPage) itr.next();
			firePageClosed(page);
			page.dispose();
		}
	}

	/**
	 * Save and close all of the pages.
	 */
	public void closeAllPages(boolean save) {
		if (save) {
			boolean ret = saveAllPages(true);
			if (!ret) {
				return;
			}
		}
		closeAllPages();
	}

	/**
	 * closePerspective method comment.
	 */
	protected boolean closePage(IWorkbenchPage in, boolean save) {

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets
	 * .Shell)
	 */
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
	}

	/* package */ShellPool getDetachedWindowPool() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.window.ApplicationWindow#createTrimWidgets(org.eclipse
	 * .swt.widgets.Shell)
	 */
	protected void createTrimWidgets(Shell shell) {
		// do nothing -- trim widgets are created in createDefaultContents
	}

	/**
	 * Creates and remembers the client composite, under which workbench pages
	 * create their controls.
	 * 
	 * @since 3.0
	 */
	protected Composite createPageComposite(Composite parent) {
		pageComposite = new Composite(parent, SWT.NONE);
		// use a StackLayout instead of a FillLayout (see bug 81460 [Workbench]
		// (regression) Close all perspectives, open Java perspective, layout
		// wrong)
		pageComposite.setLayout(new StackLayout());
		return pageComposite;
	}

	/**
	 * Creates the contents of the workbench window, including trim controls and
	 * the client composite. This MUST create the client composite via a call to
	 * <code>createClientComposite</code>.
	 * 
	 * @since 3.0
	 */
	protected Control createContents(Composite parent) {
		return pageComposite;
	}

	/**
	 * If the perspective bar is drawn on the top right corner of the window,
	 * then this method changes its appearance from curved to square. This
	 * should have its own preference, but for now it piggy-backs on the
	 * SHOW_TRADITIONAL_STYLE_TABS preference.
	 * 
	 * @param square
	 *            true for a square banner and false otherwise
	 */
	public void setBannerCurve(boolean square) {
	}

	/**
	 * Creates the default contents and layout of the shell.
	 * 
	 * @param shell
	 *            the shell
	 */
	protected void createDefaultContents(final Shell shell) {
		defaultLayout = new TrimLayout();
		shell.setLayout(defaultLayout);

		// System.err.println(defaultLayout.displayTrim());
	}

	/**
	 * <p>
	 * Returns a new menu manager for this workbench window. This menu manager
	 * will just be a proxy to the new command-based menu service.
	 * </p>
	 * <p>
	 * Subclasses may override this method to customize the menu manager.
	 * </p>
	 * 
	 * @return a menu manager for this workbench window; never <code>null</code>
	 *         .
	 */
	protected MenuManager createMenuManager() {
		MenuManager manager = super.createMenuManager();
		return manager;
	}

	/**
	 * Set the perspective bar location
	 * 
	 * @param location
	 *            the location to place the bar
	 */
	public void setPerspectiveBarLocation(String location) {
	}

	/**
	 * Notifies interested parties (namely the advisor) that the window is about
	 * to be opened.
	 * 
	 * @since 3.1
	 */
	private void fireWindowOpening() {
		// let the org.eclipse.e4.ui.model.application do further configuration
		getWindowAdvisor().preWindowOpen();
	}

	/**
	 * Notifies interested parties (namely the advisor) that the window has been
	 * restored from a previously saved state.
	 * 
	 * @throws WorkbenchException
	 *             passed through from the advisor
	 * @since 3.1
	 */
	void fireWindowRestored() throws WorkbenchException {
		StartupThreading.runWithWorkbenchExceptions(new StartupRunnable() {
			public void runWithException() throws Throwable {
				getWindowAdvisor().postWindowRestore();
			}
		});
	}

	/**
	 * Notifies interested parties (namely the advisor) that the window has been
	 * created.
	 * 
	 * @since 3.1
	 */
	private void fireWindowCreated() {
		getWindowAdvisor().postWindowCreate();
	}

	/**
	 * Notifies interested parties (namely the advisor and the window listeners)
	 * that the window has been opened.
	 * 
	 * @since 3.1
	 */
	private void fireWindowOpened() {
		getWorkbenchImpl().fireWindowOpened(this);
		getWindowAdvisor().postWindowOpen();
	}

	/**
	 * Notifies interested parties (namely the advisor) that the window's shell
	 * is closing. Allows the close to be vetoed.
	 * 
	 * @return <code>true</code> if the close should proceed, <code>false</code>
	 *         if it should be canceled
	 * @since 3.1
	 */
	private boolean fireWindowShellClosing() {
		return getWindowAdvisor().preWindowShellClose();
	}

	/**
	 * Notifies interested parties (namely the advisor and the window listeners)
	 * that the window has been closed.
	 * 
	 * @since 3.1
	 */
	private void fireWindowClosed() {
		// let the org.eclipse.e4.ui.model.application do further
		// deconfiguration
		getWindowAdvisor().postWindowClose();
		getWorkbenchImpl().fireWindowClosed(this);
	}

	/**
	 * Fires page activated
	 */
	void firePageActivated(IWorkbenchPage page) {
		String label = null; // debugging only
		if (UIStats.isDebugging(UIStats.NOTIFY_PAGE_LISTENERS)) {
			label = "activated " + page.getLabel(); //$NON-NLS-1$
		}
		try {
			UIStats.start(UIStats.NOTIFY_PAGE_LISTENERS, label);
			UIListenerLogging.logPageEvent(this, page,
					UIListenerLogging.WPE_PAGE_ACTIVATED);
			pageListeners.firePageActivated(page);
			partService.pageActivated(page);
		} finally {
			UIStats.end(UIStats.NOTIFY_PAGE_LISTENERS, page.getLabel(), label);
		}
	}

	/**
	 * Fires page closed
	 */
	private void firePageClosed(IWorkbenchPage page) {
		String label = null; // debugging only
		if (UIStats.isDebugging(UIStats.NOTIFY_PAGE_LISTENERS)) {
			label = "closed " + page.getLabel(); //$NON-NLS-1$
		}
		try {
			UIStats.start(UIStats.NOTIFY_PAGE_LISTENERS, label);
			UIListenerLogging.logPageEvent(this, page,
					UIListenerLogging.WPE_PAGE_CLOSED);
			pageListeners.firePageClosed(page);
			partService.pageClosed(page);
		} finally {
			UIStats.end(UIStats.NOTIFY_PAGE_LISTENERS, page.getLabel(), label);
		}

	}

	/**
	 * Fires page opened
	 */
	private void firePageOpened(IWorkbenchPage page) {
		String label = null; // debugging only
		if (UIStats.isDebugging(UIStats.NOTIFY_PAGE_LISTENERS)) {
			label = "opened " + page.getLabel(); //$NON-NLS-1$
		}
		try {
			UIStats.start(UIStats.NOTIFY_PAGE_LISTENERS, label);
			UIListenerLogging.logPageEvent(this, page,
					UIListenerLogging.WPE_PAGE_OPENED);
			pageListeners.firePageOpened(page);
			partService.pageOpened(page);
		} finally {
			UIStats.end(UIStats.NOTIFY_PAGE_LISTENERS, page.getLabel(), label);
		}
	}

	/**
	 * Fires perspective activated
	 */
	void firePerspectiveActivated(IWorkbenchPage page,
			IPerspectiveDescriptor perspective) {
		UIListenerLogging.logPerspectiveEvent(this, page, perspective,
				UIListenerLogging.PLE_PERSP_ACTIVATED);
		perspectiveListeners.firePerspectiveActivated(page, perspective);
	}

	/**
	 * Fires perspective deactivated.
	 * 
	 * @since 3.2
	 */
	void firePerspectivePreDeactivate(IWorkbenchPage page,
			IPerspectiveDescriptor perspective) {
		UIListenerLogging.logPerspectiveEvent(this, page, perspective,
				UIListenerLogging.PLE_PERSP_PRE_DEACTIVATE);
		perspectiveListeners.firePerspectivePreDeactivate(page, perspective);
	}

	/**
	 * Fires perspective deactivated.
	 * 
	 * @since 3.1
	 */
	void firePerspectiveDeactivated(IWorkbenchPage page,
			IPerspectiveDescriptor perspective) {
		UIListenerLogging.logPerspectiveEvent(this, page, perspective,
				UIListenerLogging.PLE_PERSP_DEACTIVATED);
		perspectiveListeners.firePerspectiveDeactivated(page, perspective);
	}

	/**
	 * Fires perspective changed
	 */
	public void firePerspectiveChanged(IWorkbenchPage page,
			IPerspectiveDescriptor perspective, String changeId) {
		// Some callers call this even when there is no active perspective.
		// Just ignore this case.
		if (perspective != null) {
			UIListenerLogging.logPerspectiveChangedEvent(this, page,
					perspective, null, changeId);
			perspectiveListeners.firePerspectiveChanged(page, perspective,
					changeId);
		}
	}

	/**
	 * Fires perspective changed for an affected part
	 */
	public void firePerspectiveChanged(IWorkbenchPage page,
			IPerspectiveDescriptor perspective,
			IWorkbenchPartReference partRef, String changeId) {
		// Some callers call this even when there is no active perspective.
		// Just ignore this case.
		if (perspective != null) {
			UIListenerLogging.logPerspectiveChangedEvent(this, page,
					perspective, partRef, changeId);
			perspectiveListeners.firePerspectiveChanged(page, perspective,
					partRef, changeId);
		}
	}

	/**
	 * Fires perspective closed
	 */
	void firePerspectiveClosed(IWorkbenchPage page,
			IPerspectiveDescriptor perspective) {
		UIListenerLogging.logPerspectiveEvent(this, page, perspective,
				UIListenerLogging.PLE_PERSP_CLOSED);
		perspectiveListeners.firePerspectiveClosed(page, perspective);
	}

	/**
	 * Fires perspective opened
	 */
	void firePerspectiveOpened(IWorkbenchPage page,
			IPerspectiveDescriptor perspective) {
		UIListenerLogging.logPerspectiveEvent(this, page, perspective,
				UIListenerLogging.PLE_PERSP_OPENED);
		perspectiveListeners.firePerspectiveOpened(page, perspective);
	}

	/**
	 * Fires perspective saved as.
	 * 
	 * @since 3.1
	 */
	void firePerspectiveSavedAs(IWorkbenchPage page,
			IPerspectiveDescriptor oldPerspective,
			IPerspectiveDescriptor newPerspective) {
		UIListenerLogging.logPerspectiveSavedAs(this, page, oldPerspective,
				newPerspective);
		perspectiveListeners.firePerspectiveSavedAs(page, oldPerspective,
				newPerspective);
	}

	/**
	 * Returns the action bars for this window.
	 */
	public WWinActionBars getActionBars() {
		if (actionBars == null) {
			actionBars = new WWinActionBars(this);
		}
		return actionBars;
	}

	/**
	 * Returns the active page.
	 * 
	 * @return the active page
	 */
	public IWorkbenchPage getActivePage() {
		return pageList.getActive();
	}

	/**
	 * Returns the active workbench page.
	 * 
	 * @return the active workbench page
	 */
	/* package */
	WorkbenchPage getActiveWorkbenchPage() {
		return pageList.getActive();
	}

	/**
	 * Returns the page composite, under which the window's pages create their
	 * controls.
	 */
	protected Composite getPageComposite() {
		return pageComposite;
	}

	/**
	 * Answer the menu manager for this window.
	 */
	public MenuManager getMenuManager() {
		return getMenuBarManager();
	}

	/**
	 * Returns the number. This corresponds to a page number in a window or a
	 * window number in the workbench.
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Returns an array of the pages in the workbench window.
	 * 
	 * @return an array of pages
	 */
	public IWorkbenchPage[] getPages() {
		return pageList.getPages();
	}

	/**
	 * @see IWorkbenchWindow
	 */
	public IPartService getPartService() {
		return partService;
	}

	/**
	 * Returns the layout for the shell.
	 * 
	 * @return the layout for the shell
	 */
	protected Layout getLayout() {
		return null;
	}

	/**
	 * @see IWorkbenchWindow
	 */
	public ISelectionService getSelectionService() {
		return (ISelectionService) e4Window.getContext().get(
				ISelectionService.class.getName());
	}

	/**
	 * Returns <code>true</code> when the window's shell is activated,
	 * <code>false</code> when it's shell is deactivated
	 * 
	 * @return boolean <code>true</code> when shell activated,
	 *         <code>false</code> when shell deactivated
	 */
	public boolean getShellActivated() {
		return shellActivated;
	}

	/**
	 * Returns the status line manager for this window (if it has one).
	 * 
	 * @return the status line manager, or <code>null</code> if this window does
	 *         not have a status line
	 * @see ApplicationWindow#addStatusLine
	 */
	public StatusLineManager getStatusLineManager() {
		return super.getStatusLineManager();
	}

	private IWindowTrim getStatusLineTrim() {
		if (statusLineTrim == null) {
			statusLineTrim = new WindowTrimProxy(
					getStatusLineManager().getControl(),
					"org.eclipse.jface.action.StatusLineManager", //$NON-NLS-1$
					WorkbenchMessages.TrimCommon_StatusLine_TrimName, SWT.NONE,
					true);
		}
		return statusLineTrim;
	}

	/**
	 * @see IWorkbenchWindow
	 */
	public IWorkbench getWorkbench() {
		return PlatformUI.getWorkbench();
	}

	/*
	 * The implementation of this method is copied from the 3.x
	 * WorkbenchWindow's implementation. This is for rendering the group labels
	 * within the 'Customize Perspective' dialog, amongst other places.
	 */
	public String getToolbarLabel(String actionSetId) {
		ActionSetRegistry registry = WorkbenchPlugin.getDefault()
				.getActionSetRegistry();
		IActionSetDescriptor actionSet = registry.findActionSet(actionSetId);
		if (actionSet != null) {
			return actionSet.getLabel();
		}

		if (IWorkbenchActionConstants.TOOLBAR_FILE
				.equalsIgnoreCase(actionSetId)) {
			return WorkbenchMessages.WorkbenchWindow_FileToolbar;
		}

		if (IWorkbenchActionConstants.TOOLBAR_NAVIGATE
				.equalsIgnoreCase(actionSetId)) {
			return WorkbenchMessages.WorkbenchWindow_NavigateToolbar;
		}

		return null;
	}

	/**
	 * Unconditionally close this window. Assumes the proper flags have been set
	 * correctly (e.i. closing and updateDisabled)
	 */
	private boolean hardClose() {
		boolean result;
		try {
			// Remove the handler submissions. Bug 64024.
			final IWorkbench workbench = getWorkbench();
			final IHandlerService handlerService = (IHandlerService) workbench
					.getService(IHandlerService.class);
			handlerService.deactivateHandlers(handlerActivations);
			final Iterator activationItr = handlerActivations.iterator();
			while (activationItr.hasNext()) {
				final IHandlerActivation activation = (IHandlerActivation) activationItr
						.next();
				activation.getHandler().dispose();
			}
			handlerActivations.clear();
			globalActionHandlersByCommandId.clear();

			// Remove the enabled submissions. Bug 64024.
			final IContextService contextService = (IContextService) workbench
					.getService(IContextService.class);
			contextService.unregisterShell(getShell());

			closeAllPages();

			fireWindowClosed();

			// time to wipe our our populate
			IMenuService menuService = (IMenuService) workbench
					.getService(IMenuService.class);
			menuService
					.releaseContributions(((ContributionManager) getActionBars()
							.getMenuManager()));
			ICoolBarManager coolbar = getActionBars().getCoolBarManager();
			if (coolbar != null) {
				menuService
						.releaseContributions(((ContributionManager) coolbar));
			}

			getActionBarAdvisor().dispose();
			getWindowAdvisor().dispose();

			// Null out the progress region. Bug 64024.
			progressRegion = null;

			// Remove drop targets
			DragUtil.removeDragTarget(null, trimDropTarget);
			DragUtil.removeDragTarget(getShell(), trimDropTarget);
			trimDropTarget = null;

			if (trimMgr2 != null) {
				trimMgr2.dispose();
				trimMgr2 = null;
			}

			if (trimContributionMgr != null) {
				trimContributionMgr.dispose();
				trimContributionMgr = null;
			}
		} finally {
			result = super.close();

			if (result) {
				// explicitly dispose the shell here, our superclasses does not
				// handle this scenario because they don't have a reference to
				// the real shell, see bug 279731
				Shell shell = getShell();
				if (shell != null && !shell.isDisposed()) {
					shell.dispose();
				}
			}

			// Clear the action sets, fix for bug 27416.
			// getActionPresentation().clearActionSets();
			try {
				// Bring down all of the services ... after the window goes away
				serviceLocator.dispose();
			} catch (Exception ex) {
				WorkbenchPlugin.log(ex);
			}
			menuRestrictions.clear();
		}
		return result;
	}

	/**
	 * @see IWorkbenchWindow
	 */
	public boolean isApplicationMenu(String menuID) {
		// delegate this question to the action bar advisor
		return getActionBarAdvisor().isApplicationMenu(menuID);
	}

	/**
	 * Return whether or not the given id matches the id of the coolitems that
	 * the org.eclipse.e4.ui.model.application creates.
	 */
	/* package */
	boolean isWorkbenchCoolItemId(String id) {
		return windowConfigurer.containsCoolItem(id);
	}

	/**
	 * Locks/unlocks the CoolBar for the workbench.
	 * 
	 * @param lock
	 *            whether the CoolBar should be locked or unlocked
	 */
	/* package */
	void lockCoolBar(boolean lock) {
		getCoolBarManager2().setLockLayout(lock);
	}

	/**
	 * Makes the window visible and frontmost.
	 */
	void makeVisible() {
		Shell shell = getShell();
		if (shell != null && !shell.isDisposed()) {
			// see bug 96700 and bug 4414 for a discussion on the use of open()
			// here
			shell.open();
		}
	}

	/**
	 * Called when this window is about to be closed.
	 * 
	 * Subclasses may overide to add code that returns <code>false</code> to
	 * prevent closing under certain conditions.
	 */
	public boolean okToClose() {
		// Save all of the editors.
		if (!getWorkbenchImpl().isClosing()) {
			if (!saveAllPages(true)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Opens a new page.
	 * <p>
	 * <b>Note:</b> Since release 2.0, a window is limited to contain at most
	 * one page. If a page exist in the window when this method is used, then
	 * another window is created for the new page. Callers are strongly
	 * recommended to use the <code>IWorkbench.openPerspective</code> APIs to
	 * programmatically show a perspective.
	 * </p>
	 */
	public IWorkbenchPage openPage(final String perspId, final IAdaptable input)
			throws WorkbenchException {
		Assert.isNotNull(perspId);

		// Run op in busy cursor.
		final Object[] result = new Object[1];
		BusyIndicator.showWhile(null, new Runnable() {
			public void run() {
				try {
					result[0] = busyOpenPage(perspId, input);
				} catch (WorkbenchException e) {
					result[0] = e;
				}
			}
		});

		if (result[0] instanceof IWorkbenchPage) {
			return (IWorkbenchPage) result[0];
		} else if (result[0] instanceof WorkbenchException) {
			throw (WorkbenchException) result[0];
		} else {
			throw new WorkbenchException(
					WorkbenchMessages.WorkbenchWindow_exceptionMessage);
		}
	}

	/**
	 * Opens a new page.
	 * <p>
	 * <b>Note:</b> Since release 2.0, a window is limited to contain at most
	 * one page. If a page exist in the window when this method is used, then
	 * another window is created for the new page. Callers are strongly
	 * recommended to use the <code>IWorkbench.openPerspective</code> APIs to
	 * programmatically show a perspective.
	 * </p>
	 */
	public IWorkbenchPage openPage(IAdaptable input) throws WorkbenchException {
		String perspId = getWorkbenchImpl().getPerspectiveRegistry()
				.getDefaultPerspective();
		return openPage(perspId, input);
	}

	/*
	 * Removes an listener from the part service.
	 */
	public void removePageListener(IPageListener l) {
		pageListeners.removePageListener(l);
	}

	/**
	 * @see org.eclipse.ui.IPageService
	 */
	public void removePerspectiveListener(org.eclipse.ui.IPerspectiveListener l) {
		perspectiveListeners.removePerspectiveListener(l);
	}

	public IStatus restoreState(IMemento memento,
			IPerspectiveDescriptor activeDescriptor) {

		return Status.OK_STATUS;
	}

	/*
	 * (non-Javadoc) Method declared on IRunnableContext.
	 */
	public void run(boolean fork, boolean cancelable,
			IRunnableWithProgress runnable) throws InvocationTargetException,
			InterruptedException {
		IWorkbenchContextSupport contextSupport = getWorkbench()
				.getContextSupport();
		final boolean keyFilterEnabled = contextSupport.isKeyFilterEnabled();

		Control fastViewBarControl = getFastViewBar() == null ? null
				: getFastViewBar().getControl();
		boolean fastViewBarWasEnabled = fastViewBarControl == null ? false
				: fastViewBarControl.getEnabled();

		Control perspectiveBarControl = getPerspectiveBar() == null ? null
				: getPerspectiveBar().getControl();
		boolean perspectiveBarWasEnabled = perspectiveBarControl == null ? false
				: perspectiveBarControl.getEnabled();

		// Cache for any diabled trim controls
		List disabledControls = null;

		try {
			if (fastViewBarControl != null && !fastViewBarControl.isDisposed()) {
				fastViewBarControl.setEnabled(false);
			}

			if (perspectiveBarControl != null
					&& !perspectiveBarControl.isDisposed()) {
				perspectiveBarControl.setEnabled(false);
			}

			if (keyFilterEnabled) {
				contextSupport.setKeyFilterEnabled(false);
			}

			// Disable all trim -except- the StatusLine
			if (defaultLayout != null)
				disabledControls = defaultLayout
						.disableTrim(getStatusLineTrim());

			super.run(fork, cancelable, runnable);
		} finally {
			if (fastViewBarControl != null && !fastViewBarControl.isDisposed()) {
				fastViewBarControl.setEnabled(fastViewBarWasEnabled);
			}

			if (perspectiveBarControl != null
					&& !perspectiveBarControl.isDisposed()) {
				perspectiveBarControl.setEnabled(perspectiveBarWasEnabled);
			}

			if (keyFilterEnabled) {
				contextSupport.setKeyFilterEnabled(true);
			}

			// Re-enable any disabled trim
			if (defaultLayout != null && disabledControls != null)
				defaultLayout.enableTrim(disabledControls);
		}
	}

	/**
	 * Save all of the pages. Returns true if the operation succeeded.
	 */
	private boolean saveAllPages(boolean bConfirm) {
		boolean bRet = true;
		Iterator itr = pageList.iterator();
		while (bRet && itr.hasNext()) {
			WorkbenchPage page = (WorkbenchPage) itr.next();
			bRet = page.saveAllEditors(bConfirm);
		}
		return bRet;
	}

	/**
	 * @see IPersistable
	 */
	public IStatus saveState(IMemento memento) {

		MultiStatus result = new MultiStatus(PlatformUI.PLUGIN_ID, IStatus.OK,
				WorkbenchMessages.WorkbenchWindow_problemsSavingWindow, null);

		return result;
	}

	/**
	 * Sets the active page within the window.
	 * 
	 * @param in
	 *            identifies the new active page, or <code>null</code> for no
	 *            active page
	 */
	public void setActivePage(final IWorkbenchPage in) {
		if (getActiveWorkbenchPage() == in) {
			return;
		}
		pageList.setActive(in);
		updateActionSets();
		if (isClosing()) {
			return;
		}
		firePageActivated(in);
	}

	/**
	 * Returns whether or not children exist for the Window's toolbar control.
	 * Overridden for coolbar support.
	 * <p>
	 * 
	 * @return boolean true if children exist, false otherwise
	 */
	protected boolean toolBarChildrenExist() {
		CoolBar coolBarControl = (CoolBar) getCoolBarControl();
		return coolBarControl.getItemCount() > 0;
	}

	private Set menuRestrictions = new HashSet();

	public Set getMenuRestrictions() {
		return menuRestrictions;
	}

	void liftRestrictions() {

	}

	void imposeRestrictions() {

	}

	/**
	 * update the action bars.
	 */
	public void updateActionBars() {

	}

	/**
	 * <p>
	 * Indicates the start of a large update within this window. This is used to
	 * disable CPU-intensive, change-sensitive services that were temporarily
	 * disabled in the midst of large changes. This method should always be
	 * called in tandem with <code>largeUpdateEnd</code>, and the event loop
	 * should not be allowed to spin before that method is called.
	 * </p>
	 * <p>
	 * Important: always use with <code>largeUpdateEnd</code>!
	 * </p>
	 * 
	 * @since 3.1
	 */
	public final void largeUpdateStart() {
		largeUpdates++;
	}

	/**
	 * <p>
	 * Indicates the end of a large update within this window. This is used to
	 * re-enable services that were temporarily disabled in the midst of large
	 * changes. This method should always be called in tandem with
	 * <code>largeUpdateStart</code>, and the event loop should not be allowed
	 * to spin before this method is called.
	 * </p>
	 * <p>
	 * Important: always protect this call by using <code>finally</code>!
	 * </p>
	 * 
	 * @since 3.1
	 */
	public final void largeUpdateEnd() {
		if (--largeUpdates == 0) {
			updateActionBars();
		}
	}

	/**
	 * Update the visible action sets. This method is typically called from a
	 * page when the user changes the visible action sets within the
	 * prespective.
	 */
	public void updateActionSets() {
		WorkbenchPage page = pageList.getActive();
		if (page == null) {
			return;
		}
		final IActionSetDescriptor[] actionSets = page.getActionSets();
		e4Context.set(ISources.ACTIVE_ACTION_SETS_NAME, actionSets);
		final HashSet<String> set = new HashSet<String>();
		for (int i = 0; i < actionSets.length; i++) {
			set.add(actionSets[i].getId());
		}
		for (int i = 0; i < e4ActionSets.length; i++) {
			if (set.contains(e4ActionSets[i].getId())) {
				e4ActionSets[i].setVisible(true);
			} else {
				e4ActionSets[i].setVisible(false);
			}
		}
		Shell shell = getShell();
		if (shell != null) {
			Menu bar = shell.getMenuBar();
			if (bar != null) {
				final MenuManager manager = (MenuManager) bar.getData();
				if (manager != null) {
					manager.update(true);
				}
			}
		}
	}

	private ListenerList actionSetListeners = null;

	private ListenerList backgroundSaveListeners = new ListenerList(
			ListenerList.IDENTITY);

	private MWindow e4Window;

	private IEclipseContext e4Context;

	private ActionSet[] e4ActionSets;

	final void addActionSetsListener(final IActionSetsListener listener) {
		if (actionSetListeners == null) {
			actionSetListeners = new ListenerList();
		}

		actionSetListeners.add(listener);
	}

	final void removeActionSetsListener(final IActionSetsListener listener) {
		if (actionSetListeners != null) {
			actionSetListeners.remove(listener);
			if (actionSetListeners.isEmpty()) {
				actionSetListeners = null;
			}
		}
	}

	/**
	 * Create the progress indicator for the receiver.
	 * 
	 * @param shell
	 *            the parent shell
	 */
	void createProgressIndicator(Shell shell) {
		if (getWindowConfigurer().getShowProgressIndicator()) {
			progressRegion = new ProgressRegion();
			progressRegion.createContents(shell, this);
		}

	}

	class PageList {
		// List of pages in the order they were created;
		private List pagesInCreationOrder;

		// List of pages where the top is the last activated.
		private List pageInActivationOrder;

		// The page explicitly activated
		private Object active;

		public PageList() {
			pagesInCreationOrder = new ArrayList(4);
			pageInActivationOrder = new ArrayList(4);
		}

		public boolean add(Object object) {
			pagesInCreationOrder.add(object);
			pageInActivationOrder.add(0, object);
			// It will be moved to top only when activated.
			return true;
		}

		public Iterator iterator() {
			return pagesInCreationOrder.iterator();
		}

		public boolean contains(Object object) {
			return pagesInCreationOrder.contains(object);
		}

		public boolean remove(Object object) {
			if (active == object) {
				active = null;
			}
			pageInActivationOrder.remove(object);
			return pagesInCreationOrder.remove(object);
		}

		public boolean isEmpty() {
			return pagesInCreationOrder.isEmpty();
		}

		public IWorkbenchPage[] getPages() {
			int nSize = pagesInCreationOrder.size();
			IWorkbenchPage[] retArray = new IWorkbenchPage[nSize];
			pagesInCreationOrder.toArray(retArray);
			return retArray;
		}

		public void setActive(Object page) {
			if (active == page) {
				return;
			}

			active = page;

			if (page != null) {
				pageInActivationOrder.remove(page);
				pageInActivationOrder.add(page);
			}
		}

		public WorkbenchPage getActive() {
			return (WorkbenchPage) active;
		}

		public WorkbenchPage getNextActive() {
			if (active == null) {
				if (pageInActivationOrder.isEmpty()) {
					return null;
				}

				return (WorkbenchPage) pageInActivationOrder
						.get(pageInActivationOrder.size() - 1);
			}

			if (pageInActivationOrder.size() < 2) {
				return null;
			}

			return (WorkbenchPage) pageInActivationOrder
					.get(pageInActivationOrder.size() - 2);
		}
	}

	/**
	 * Returns the unique object that applications use to configure this window.
	 * <p>
	 * IMPORTANT This method is declared package-private to prevent regular
	 * plug-ins from downcasting IWorkbenchWindow to WorkbenchWindow and getting
	 * hold of the workbench window configurer that would allow them to tamper
	 * with the workbench window. The workbench window configurer is available
	 * only to the org.eclipse.e4.ui.model.application.
	 * </p>
	 */
	/* package - DO NOT CHANGE */
	WorkbenchWindowConfigurer getWindowConfigurer() {
		if (windowConfigurer == null) {
			// lazy initialize
			windowConfigurer = new WorkbenchWindowConfigurer(this);
		}
		return windowConfigurer;
	}

	/**
	 * Returns the workbench advisor. Assumes the workbench has been created
	 * already.
	 * <p>
	 * IMPORTANT This method is declared private to prevent regular plug-ins
	 * from downcasting IWorkbenchWindow to WorkbenchWindow and getting hold of
	 * the workbench advisor that would allow them to tamper with the workbench.
	 * The workbench advisor is internal to the
	 * org.eclipse.e4.ui.model.application.
	 * </p>
	 */
	private/* private - DO NOT CHANGE */
	WorkbenchAdvisor getAdvisor() {
		return getWorkbenchImpl().getAdvisor();
	}

	/**
	 * Returns the window advisor, creating a new one for this window if needed.
	 * <p>
	 * IMPORTANT This method is declared package private to prevent regular
	 * plug-ins from downcasting IWorkbenchWindow to WorkbenchWindow and getting
	 * hold of the window advisor that would allow them to tamper with the
	 * window. The window advisor is internal to the
	 * org.eclipse.e4.ui.model.application.
	 * </p>
	 */
	/* package private - DO NOT CHANGE */
	WorkbenchWindowAdvisor getWindowAdvisor() {
		if (windowAdvisor == null) {
			windowAdvisor = getAdvisor().createWorkbenchWindowAdvisor(
					getWindowConfigurer());
			Assert.isNotNull(windowAdvisor);
		}
		return windowAdvisor;
	}

	/**
	 * Returns the action bar advisor, creating a new one for this window if
	 * needed.
	 * <p>
	 * IMPORTANT This method is declared private to prevent regular plug-ins
	 * from downcasting IWorkbenchWindow to WorkbenchWindow and getting hold of
	 * the action bar advisor that would allow them to tamper with the window's
	 * action bars. The action bar advisor is internal to the
	 * org.eclipse.e4.ui.model.application.
	 * </p>
	 */
	private/* private - DO NOT CHANGE */
	ActionBarAdvisor getActionBarAdvisor() {
		if (actionBarAdvisor == null) {
			actionBarAdvisor = getWindowAdvisor().createActionBarAdvisor(
					getWindowConfigurer().getActionBarConfigurer());
			Assert.isNotNull(actionBarAdvisor);
		}
		return actionBarAdvisor;
	}

	/*
	 * Returns the IWorkbench implementation.
	 */
	private Workbench getWorkbenchImpl() {
		return Workbench.getInstance();
	}

	/**
	 * Fills the window's real action bars.
	 * 
	 * @param flags
	 *            indicate which bars to fill
	 */
	public void fillActionBars(int flags) {
		Workbench workbench = getWorkbenchImpl();
		workbench.largeUpdateStart();
		try {
			getActionBarAdvisor().fillActionBars(flags);
			//
			// 3.3 start
			// final IMenuService menuService = (IMenuService) serviceLocator
			// .getService(IMenuService.class);
			// menuService.populateContributionManager(
			// (ContributionManager) getActionBars().getMenuManager(),
			// MenuUtil.MAIN_MENU);
			// ICoolBarManager coolbar = getActionBars().getCoolBarManager();
			// if (coolbar != null) {
			// menuService.populateContributionManager(
			// (ContributionManager) coolbar, MenuUtil.MAIN_TOOLBAR);
			// }
			// 3.3 end
		} finally {
			workbench.largeUpdateEnd();
		}
	}

	/**
	 * Fills the window's proxy action bars.
	 * 
	 * @param proxyBars
	 *            the proxy configurer
	 * @param flags
	 *            indicate which bars to fill
	 */
	public void fillActionBars(IActionBarConfigurer2 proxyBars, int flags) {
		Assert.isNotNull(proxyBars);
		WorkbenchWindowConfigurer.WindowActionBarConfigurer wab = (WorkbenchWindowConfigurer.WindowActionBarConfigurer) getWindowConfigurer()
				.getActionBarConfigurer();
		wab.setProxy(proxyBars);
		try {
			getActionBarAdvisor().fillActionBars(
					flags | ActionBarAdvisor.FILL_PROXY);
		} finally {
			wab.setProxy(null);
		}
	}

	/**
	 * The <code>WorkbenchWindow</code> implementation of this method has the
	 * same logic as <code>Window</code>'s implementation, but without the
	 * resize check. We don't want to skip setting the bounds if the shell has
	 * been resized since a free resize event occurs on Windows when the menubar
	 * is set in configureShell.
	 */
	protected void initializeBounds() {
		Point size = getInitialSize();
		Point location = getInitialLocation(size);
		getShell().setBounds(
				getConstrainedShellBounds(new Rectangle(location.x, location.y,
						size.x, size.y)));
	}

	/*
	 * Unlike dialogs, the position of the workbench window is set by the user
	 * and persisted across sessions. If the user wants to put the window
	 * offscreen or spanning multiple monitors, let them (bug 74762)
	 */
	protected void constrainShellSize() {
		// As long as the shell is visible on some monitor, don't change it.
		Rectangle bounds = getShell().getBounds();
		if (!SwtUtil.intersectsAnyMonitor(Display.getCurrent(), bounds)) {
			super.constrainShellSize();
		}
	}

	/*
	 * Unlike dialogs, the position of the workbench window is set by the user
	 * and persisted across sessions. If the user wants to put the window
	 * offscreen or spanning multiple monitors, let them (bug 74762)
	 */
	protected Point getInitialLocation(Point size) {
		Shell shell = getShell();
		if (shell != null) {
			return shell.getLocation();
		}

		return super.getInitialLocation(size);
	}

	/**
	 * The <code>WorkbenchWindow</code> implementation of this method delegates
	 * to the window configurer.
	 * 
	 * @since 3.0
	 */
	protected Point getInitialSize() {
		return getWindowConfigurer().getInitialSize();
	}

	/**
	 * @param visible
	 *            whether the cool bar should be shown. This is only applicable
	 *            if the window configurer also wishes either the cool bar to be
	 *            visible.
	 * @since 3.0
	 */
	public void setCoolBarVisible(boolean visible) {
	}

	/**
	 * @return whether the cool bar should be shown. This is only applicable if
	 *         the window configurer also wishes either the cool bar to be
	 *         visible.
	 * @since 3.0
	 */
	public boolean getCoolBarVisible() {
		return coolBarVisible;
	}

	/**
	 * @param visible
	 *            whether the perspective bar should be shown. This is only
	 *            applicable if the window configurer also wishes either the
	 *            perspective bar to be visible.
	 * @since 3.0
	 */
	public void setPerspectiveBarVisible(boolean visible) {
	}

	/**
	 * @return whether the perspective bar should be shown. This is only
	 *         applicable if the window configurer also wishes either the
	 *         perspective bar to be visible.
	 * @since 3.0
	 */
	public boolean getPerspectiveBarVisible() {
		return perspectiveBarVisible;
	}

	/**
	 * Tell the workbench window a visible state for the fastview bar. This is
	 * only applicable if the window configurer also wishes the fast view bar to
	 * be visible.
	 * 
	 * @param visible
	 *            <code>true</code> or <code>false</code>
	 * @since 3.2
	 */
	public void setFastViewBarVisible(boolean visible) {
	}

	/**
	 * The workbench window take on the fastview bar. This is only applicable if
	 * the window configurer also wishes the fast view bar to be visible.
	 * 
	 * @return <code>true</code> if the workbench window thinks the fastview bar
	 *         should be visible.
	 * @since 3.2
	 */
	public boolean getFastViewBarVisible() {
		return fastViewBarVisible;
	}

	/**
	 * @param visible
	 *            whether the perspective bar should be shown. This is only
	 *            applicable if the window configurer also wishes either the
	 *            perspective bar to be visible.
	 * @since 3.0
	 */
	public void setStatusLineVisible(boolean visible) {
	}

	/**
	 * @return whether the perspective bar should be shown. This is only
	 *         applicable if the window configurer also wishes either the
	 *         perspective bar to be visible.
	 * @since 3.0
	 */
	public boolean getStatusLineVisible() {
		return statusLineVisible;
	}

	public boolean getShowFastViewBars() {
		return getWindowConfigurer().getShowFastViewBars();
	}

	/**
	 * Set the layout data for the contents of the window.
	 */
	void setLayoutDataForContents() {

	}

	/**
	 * Returns the fast view bar.
	 */
	public FastViewBar getFastViewBar() {
		return fastViewBar;
	}

	/**
	 * Returns the perspective bar.
	 * 
	 * @return Returns the perspective bar, or <code>null</code> if it has not
	 *         been initialized.
	 */
	public PerspectiveBarManager getPerspectiveBar() {
		return perspectiveSwitcher == null ? null : perspectiveSwitcher
				.getPerspectiveBar();
	}

	/**
	 * Returns the action presentation for dynamic UI
	 * 
	 * @return action presentation
	 */
	public ActionPresentation getActionPresentation() {
		return null;
	}

	/**
	 * Return the action bar presentation used for creating toolbars. This is
	 * for internal use only, used for consistency with the window.
	 * 
	 * @return the presentation used.
	 */
	public IActionBarPresentationFactory getActionBarPresentationFactory() {
		// allow replacement of the actionbar presentation
		IActionBarPresentationFactory actionBarPresentation;
		AbstractPresentationFactory presentationFactory = getWindowConfigurer()
				.getPresentationFactory();
		if (presentationFactory instanceof IActionBarPresentationFactory) {
			actionBarPresentation = ((IActionBarPresentationFactory) presentationFactory);
		} else {
			actionBarPresentation = new DefaultActionBarPresentationFactory();
		}

		return actionBarPresentation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.ApplicationWindow#showTopSeperator()
	 */
	protected boolean showTopSeperator() {
		return false;
	}

	/**
	 * Returns a new cool bar manager for the window.
	 * <p>
	 * Subclasses may override this method to customize the cool bar manager.
	 * </p>
	 * 
	 * @return a cool bar manager
	 * @since 3.2
	 */
	protected ICoolBarManager createCoolBarManager2(int style) {
		final ICoolBarManager2 coolBarManager = getActionBarPresentationFactory()
				.createCoolBarManager();
		return coolBarManager;
	}

	/**
	 * Returns a new tool bar manager for the window.
	 * <p>
	 * Subclasses may override this method to customize the tool bar manager.
	 * </p>
	 * 
	 * @return a tool bar manager
	 * @since 3.2
	 */
	protected IToolBarManager createToolBarManager2(int style) {
		final IToolBarManager2 toolBarManager = getActionBarPresentationFactory()
				.createToolBarManager();
		return toolBarManager;
	}

	/**
	 * Delegate to the presentation factory.
	 * 
	 * @see org.eclipse.jface.window.ApplicationWindow#createStatusLineManager
	 * @since 3.0
	 */
	protected StatusLineManager createStatusLineManager() {
		// @issue ApplicationWindow and WorkbenchWindow should allow full
		// IStatusLineManager
		return new StatusLineManager() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.jface.action.StatusLineManager#getProgressMonitor()
			 */
			@Override
			public IProgressMonitor getProgressMonitor() {
				return new NullProgressMonitor();
			}
		};
	}

	/**
	 * Delegate to the presentation factory.
	 * 
	 * @see org.eclipse.jface.window.ApplicationWindow#createStatusLine
	 * @since 3.0
	 */
	protected void createStatusLine(Shell shell) {
		getWindowConfigurer().getPresentationFactory().createStatusLineControl(
				getStatusLineManager(), shell);
	}

	/**
	 * Updates the fast view bar, if present. TODO: The fast view bar should
	 * update itself as necessary. All calls to this should be cleaned up.
	 * 
	 * @since 3.0
	 */
	public void updateFastViewBar() {
	}

	/**
	 * @return Returns the progressRegion.
	 */
	public ProgressRegion getProgressRegion() {
		return progressRegion;
	}

	/**
	 * Adds the given control to the specified side of this window's trim.
	 * 
	 * @param trim
	 *            the bar's IWindowTrim
	 * @param side
	 *            one of <code>SWT.LEFT</code>,<code>SWT.BOTTOM</code>, or
	 *            <code>SWT.RIGHT</code> (only LEFT has been tested)
	 * @since 3.0
	 */
	public void addToTrim(IWindowTrim trim, int side) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindow#getExtensionTracker()
	 */
	public IExtensionTracker getExtensionTracker() {
		return (IExtensionTracker) e4Window.getContext().get(
				IExtensionTracker.class.getName());
	}

	/**
	 * Creates the perspective customization dialog.
	 * 
	 * @param persp
	 *            perspective to customize
	 * 
	 * @return a new perspective customization dialog
	 * @since 3.1
	 */
	public CustomizePerspectiveDialog createCustomizePerspectiveDialog(
			Perspective persp) {
		return new CustomizePerspectiveDialog(getWindowConfigurer(), persp);
	}

	/**
	 * Returns the default page input for workbench pages opened in this window.
	 * 
	 * @return the default page input or <code>null</code> if none
	 * @since 3.1
	 */
	IAdaptable getDefaultPageInput() {
		return getWorkbenchImpl().getDefaultPageInput();
	}

	/**
	 * Add a listener for perspective reordering.
	 * 
	 * @param listener
	 */
	public void addPerspectiveReorderListener(IReorderListener listener) {
		if (perspectiveSwitcher != null) {
			perspectiveSwitcher.addReorderListener(listener);
		}
	}

	/**
	 * Show the heap status
	 * 
	 * @param selection
	 */
	public void showHeapStatus(boolean selection) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindow#getTrimManager()
	 */
	public ITrimManager getTrimManager() {
		return defaultLayout;
	}

	/**
	 * Initializes all of the default command-based services for the workbench
	 * window.
	 */
	private final void initializeDefaultServices() {
		e4Context.declareModifiable(IServiceConstants.SELECTION);

		e4Context.set(IExtensionTracker.class.getName(), new ContextFunction() {
			@Override
			public Object compute(IEclipseContext context, Object[] arguments) {
				if (tracker == null) {
					tracker = new UIExtensionTracker(getWorkbench()
							.getDisplay());
				}
				return tracker;
			}
		});
		e4Context.set(IPartService.class.getName(), getPartService());
		e4Context.set(ISelectionService.class.getName(),
				new LegacySelectionService(e4Context));
		// END: e4 services
		serviceLocator.registerService(IWorkbenchLocationService.class,
				new WorkbenchLocationService(IServiceScopes.WINDOW_SCOPE,
						getWorkbench(), this, null, null, null, 1));
		// added back for legacy reasons
		serviceLocator.registerService(IWorkbenchWindow.class, this);
		serviceLocator.registerService(getClass(), this);

		final ActionCommandMappingService mappingService = new ActionCommandMappingService();
		serviceLocator.registerService(IActionCommandMappingService.class,
				mappingService);

		final LegacyActionPersistence actionPersistence = new LegacyActionPersistence(
				this);
		serviceLocator.registerService(LegacyActionPersistence.class,
				actionPersistence);
		actionPersistence.read();

		// BEGIN: e4 registration
		EContextService cs = (EContextService) e4Context
				.get(EContextService.class.getName());
		cs.activateContext("org.eclipse.ui.contexts.window"); //$NON-NLS-1$
		cs.getActiveContextIds();
		e4Context.set(ISources.ACTIVE_WORKBENCH_WINDOW_NAME, this);
		e4Context.set(ISources.ACTIVE_PART_NAME, new ContextFunction() {
			@Override
			public Object compute(IEclipseContext context, Object[] arguments) {
				Object o = context.get(IServiceConstants.ACTIVE_PART);
				if (o instanceof MPart) {
					Object impl = ((MPart) o).getObject();
					if (impl instanceof IWorkbenchPart) {
						return impl;
					}
				}
				return null;
			}
		});
		e4Context.set(ISources.ACTIVE_SITE_NAME, new ContextFunction() {
			@Override
			public Object compute(IEclipseContext context, Object[] arguments) {
				Object o = context.get(IServiceConstants.ACTIVE_PART);
				if (o instanceof MPart) {
					Object impl = ((MPart) o).getObject();
					if (impl instanceof IWorkbenchPart) {
						return ((IWorkbenchPart) impl).getSite();
					}
				}
				return null;
			}
		});
		// local handler service for local handlers
		IHandlerService handlerService = new LegacyHandlerService(e4Context);
		serviceLocator.registerService(IHandlerService.class, handlerService);
		readActionSets();
	}

	private void readActionSets() {
		WorkbenchWindowExpression windowExpression = new WorkbenchWindowExpression(
				this);
		ICommandService cs = (ICommandService) e4Context
				.get(ICommandService.class.getName());
		IConfigurationElement[] actionSetElements = ExtensionUtils
				.getExtensions(IWorkbenchRegistryConstants.PL_ACTION_SETS);
		for (IConfigurationElement ase : actionSetElements) {
			IConfigurationElement[] elements = ase
					.getChildren(IWorkbenchRegistryConstants.TAG_ACTION);
			for (IConfigurationElement configElement : elements) {
				String id = MenuHelper.getId(configElement);
				String cmdId = MenuHelper.getActionSetCommandId(configElement);
				if (id == null || id.length() == 0
						|| MenuHelper.getRetarget(configElement)) {
					continue;
				}
				Command cmd = cs.getCommand(cmdId);
				if (!cmd.isDefined()) {
					Activator.trace(Policy.DEBUG_CMDS, "Still no command for " //$NON-NLS-1$
							+ cmdId, null);
					continue;
				}
				LegacyHandlerService.registerLegacyHandler(e4Context, id,
						cmdId, new ActionDelegateHandlerProxy(configElement,
								IWorkbenchRegistryConstants.ATT_CLASS, id,
								new ParameterizedCommand(cmd, null), this,
								null, null, null), windowExpression);
			}
		}
	}

	public final Object getService(final Class key) {
		return serviceLocator.getService(key);
	}

	public final boolean hasService(final Class key) {
		return serviceLocator.hasService(key);
	}

	/**
	 * Toggle the visibility of the coolbar/perspective bar. This method
	 * respects the window configurer and will only toggle visibility if the
	 * item in question was originally declared visible by the window advisor.
	 * 
	 * @since 3.3
	 */
	public void toggleToolbarVisibility() {
		boolean coolbarVisible = getCoolBarVisible();
		boolean perspectivebarVisible = getPerspectiveBarVisible();
		// only toggle the visibility of the components that
		// were on initially
		if (getWindowConfigurer().getShowCoolBar()) {
			setCoolBarVisible(!coolbarVisible);
		}
		if (getWindowConfigurer().getShowPerspectiveBar()) {
			setPerspectiveBarVisible(!perspectivebarVisible);
		}
		getShell().layout();
	}

	/* package */void addBackgroundSaveListener(IBackgroundSaveListener listener) {
		backgroundSaveListeners.add(listener);
	}

	/* package */void fireBackgroundSaveStarted() {
		Object[] listeners = backgroundSaveListeners.getListeners();
		for (int i = 0; i < listeners.length; i++) {
			IBackgroundSaveListener listener = (IBackgroundSaveListener) listeners[i];
			listener.handleBackgroundSaveStarted();
		}
	}

	/* package */void removeBackgroundSaveListener(
			IBackgroundSaveListener listener) {
		backgroundSaveListeners.remove(listener);
	}

	public MWindow getModelWindow() {
		return e4Window;
	}
}
