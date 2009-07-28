package org.eclipse.e4.extensions;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.e4.core.services.context.EclipseContextFactory;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.core.services.context.spi.IContextConstants;
import org.eclipse.e4.ui.model.application.ApplicationFactory;
import org.eclipse.e4.ui.model.application.MContributedPart;
import org.eclipse.e4.ui.model.application.MMenu;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.model.application.MToolBar;
import org.eclipse.e4.ui.model.workbench.MPerspective;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.services.IStylingEngine;
import org.eclipse.e4.ui.widgets.CTabFolder;
import org.eclipse.e4.ui.widgets.CTabItem;
import org.eclipse.e4.workbench.ui.internal.Activator;
import org.eclipse.e4.workbench.ui.internal.Policy;
import org.eclipse.e4.workbench.ui.internal.Trackable;
import org.eclipse.e4.workbench.ui.internal.UISchedulerStrategy;
import org.eclipse.e4.workbench.ui.menus.MenuHelper;
import org.eclipse.e4.workbench.ui.renderers.AbstractPartRenderer;
import org.eclipse.e4.workbench.ui.renderers.swt.SWTPartRenderer;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.SubActionBars;
import org.eclipse.ui.internal.EditorActionBars;
import org.eclipse.ui.internal.EditorActionBuilder;
import org.eclipse.ui.internal.EditorSite;
import org.eclipse.ui.internal.ViewSite;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.internal.registry.EditorDescriptor;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.menus.IMenuService;

public class LegacyPartRenderer extends SWTPartRenderer {

	private static final String IS_DIRTY = "isDirty"; //$NON-NLS-1$
	private static final String EDITOR_DISPOSED = "editorDisposed"; //$NON-NLS-1$

	private IConfigurationElement findPerspectiveFactory(String id) {
		IConfigurationElement[] factories = ExtensionUtils
				.getExtensions(IWorkbenchRegistryConstants.PL_PERSPECTIVES);
		IConfigurationElement theFactory = ExtensionUtils.findExtension(
				factories, id);
		return theFactory;
	}

	private IConfigurationElement findViewConfig(String id) {
		IConfigurationElement[] views = ExtensionUtils
				.getExtensions(IWorkbenchRegistryConstants.PL_VIEWS);
		IConfigurationElement viewContribution = ExtensionUtils.findExtension(
				views, id);
		return viewContribution;
	}

	private IConfigurationElement findEditorConfig(String id) {
		IConfigurationElement[] editors = ExtensionUtils
				.getExtensions(IWorkbenchRegistryConstants.PL_EDITOR);
		IConfigurationElement editorContribution = ExtensionUtils
				.findExtension(editors, id);
		return editorContribution;
	}

	private Map<IEditorPart, Trackable> trackables = new HashMap<IEditorPart, Trackable>();

	/**
	 * @param part
	 * @param editorElement
	 * @return
	 */
	private Control createEditor(final MContributedPart<MPart<?>> part,
			IConfigurationElement editorElement, final Composite parent) {
		EditorDescriptor desc = new EditorDescriptor(part.getId(),
				editorElement);

		// Convert the relative path into a bundle URI
		String imagePath = editorElement.getAttribute("icon"); //$NON-NLS-1$
		String imageURI = imagePath;
		if (!imagePath.startsWith("platform:")) { //$NON-NLS-1$
			imagePath = imagePath.replace("$nl$", ""); //$NON-NLS-1$//$NON-NLS-2$
			if (imagePath.charAt(0) != '/') {
				imagePath = '/' + imagePath;
			}
			String bundleId = editorElement.getContributor().getName();
			imageURI = "platform:/plugin/" + bundleId + imagePath; //$NON-NLS-1$
		}
		// part.setPlugin(viewContribution.getContributor().getName());
		part.setIconURI(imageURI);
		//part.setName(editorElement.getAttribute("name")); //$NON-NLS-1$
		IEditorPart impl = null;

		try {
			impl = desc.createEditor();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		if (impl == null)
			return null;

		try {
			IEclipseContext parentContext = getContextForParent(part);
			final IEclipseContext localContext = part.getContext();
			localContext.set(IContextConstants.DEBUG_STRING, "Legacy Editor(" //$NON-NLS-1$
					+ desc.getLabel() + ")"); //$NON-NLS-1$
			final IEclipseContext outputContext = EclipseContextFactory.create(
					null, UISchedulerStrategy.getInstance());
			outputContext.set(IContextConstants.DEBUG_STRING,
					"ContributedPart-output"); //$NON-NLS-1$
			localContext.set(IContextConstants.OUTPUTS, outputContext);
			localContext.set(IEclipseContext.class.getName(), outputContext);
			parentContext.set(IServiceConstants.ACTIVE_CHILD, localContext);

			part.setObject(impl);
			// Assign a 'site' for the newly instantiated part
			WorkbenchPage page = (WorkbenchPage) localContext
					.get(WorkbenchPage.class.getName());
			ModelEditorReference ref = new ModelEditorReference(part, page);
			EditorSite site = new EditorSite(ref, impl, page);
			EditorActionBars bars = getEditorActionBars(desc, page, page
					.getWorkbenchWindow(), part.getId());
			site.setActionBars(bars);
			site.setConfigurationElement(editorElement);
			impl.init(site, (IEditorInput) localContext.get(IEditorInput.class
					.getName()));

			impl.createPartControl(parent);
			localContext.set(MContributedPart.class.getName(), part);

			// Manage the 'dirty' state
			final IEditorPart implementation = impl;
			localContext.set(IS_DIRTY, implementation.isDirty());
			localContext.set(EDITOR_DISPOSED, Boolean.FALSE);
			Trackable updateDirty = new Trackable(localContext) {
				private CTabItem findItemForPart(CTabFolder ctf) {
					CTabItem[] items = ctf.getItems();
					for (int i = 0; i < items.length; i++) {
						if (items[i].getData(AbstractPartRenderer.OWNING_ME) == part) {
							return items[i];
						}
					}

					return null;
				}

				public void run() {
					if (!participating) {
						return;
					}
					trackingContext.get(EDITOR_DISPOSED);
					boolean dirty = (Boolean) trackingContext.get(IS_DIRTY);
					if (parent instanceof CTabFolder) {
						CTabFolder ctf = (CTabFolder) parent;
						CTabItem partItem = findItemForPart(ctf);
						if (partItem == null)
							return;

						String itemText = partItem.getText();
						if (dirty && itemText.indexOf('*') != 0) {
							itemText = '*' + itemText;
						} else if (itemText.indexOf('*') == 0) {
							itemText = itemText.substring(1);
						}
						partItem.setText(itemText);
					}
				}
			};
			trackables.put(implementation, updateDirty);
			localContext.runAndTrack(updateDirty);
			impl.addPropertyListener(new IPropertyListener() {
				public void propertyChanged(Object source, int propId) {
					localContext.set(IS_DIRTY, implementation.isDirty());
				}
			});

			if (parent.getChildren().length > 0)
				return parent.getChildren()[parent.getChildren().length - 1];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	Map<String, EditorActionBars> actionCache = new HashMap<String, EditorActionBars>();

	private EditorActionBars getEditorActionBars(EditorDescriptor desc,
			WorkbenchPage page, IWorkbenchWindow workbenchWindow, String type) {
		// Get the editor type.

		// If an action bar already exists for this editor type return it.
		EditorActionBars actionBars = actionCache.get(type);
		if (actionBars != null) {
			actionBars.addRef();
			return actionBars;
		}

		// Create a new action bar set.
		actionBars = new EditorActionBars(page, workbenchWindow, type);
		actionBars.addRef();
		actionCache.put(type, actionBars);

		// Read base contributor.
		IEditorActionBarContributor contr = desc.createActionBarContributor();
		if (contr != null) {
			actionBars.setEditorContributor(contr);
			contr.init(actionBars, page);
		}

		// Read action extensions.
		EditorActionBuilder builder = new EditorActionBuilder();
		contr = builder.readActionExtensions(desc);
		if (contr != null) {
			actionBars.setExtensionContributor(contr);
			contr.init(actionBars, page);
		}

		// Return action bars.
		return actionBars;
	}

	private void disposeEditorActionBars(EditorActionBars actionBars) {
		actionBars.removeRef();
		if (actionBars.getRef() <= 0) {
			String type = actionBars.getEditorType();
			actionCache.remove(type);
			actionBars.dispose();
		}
	}

	private Control createView(MContributedPart<MPart<?>> part,
			IConfigurationElement viewContribution, Composite parent) {
		IViewPart impl = null;
		try {
			impl = (IViewPart) viewContribution
					.createExecutableExtension("class"); //$NON-NLS-1$
		} catch (CoreException e) {
			e.printStackTrace();
		}
		if (impl == null)
			return null;

		try {
			IEclipseContext parentContext = getContextForParent(part);
			final IEclipseContext localContext = part.getContext();
			localContext.set(IContextConstants.DEBUG_STRING, "Legacy View(" //$NON-NLS-1$
					+ part.getName() + ")"); //$NON-NLS-1$
			final IEclipseContext outputContext = EclipseContextFactory.create(
					null, UISchedulerStrategy.getInstance());
			outputContext.set(IContextConstants.DEBUG_STRING,
					"ContributedPart-output"); //$NON-NLS-1$
			localContext.set(IContextConstants.OUTPUTS, outputContext);
			localContext.set(IEclipseContext.class.getName(), outputContext);
			parentContext.set(IServiceConstants.ACTIVE_CHILD, localContext);

			part.setObject(impl);
			// Assign a 'site' for the newly instantiated part
			WorkbenchPage page = (WorkbenchPage) localContext
					.get(WorkbenchPage.class.getName());
			ModelViewReference ref = new ModelViewReference(part, page);
			ViewSite site = new ViewSite(ref, impl, page);
			site.setConfigurationElement(viewContribution);
			impl.init(site, null);
			// final ToolBarManager tbm = (ToolBarManager) site.getActionBars()
			// .getToolBarManager();
			// if (parent instanceof CTabFolder) {
			// final ToolBar tb = tbm.createControl(parent);
			// ((CTabFolder) parent).setTopRight(tb);
			// }

			// We need to create the actual TB (some parts call 'getControl')
			ToolBarManager tbMgr = (ToolBarManager) site.getActionBars()
					.getToolBarManager();
			tbMgr.createControl(parent);

			impl.createPartControl(parent);

			localContext.set(MContributedPart.class.getName(), part);

			// Populate and scrape the old-style contributions...
			IMenuService menuSvc = (IMenuService) localContext
					.get(IMenuService.class.getName());

			String tbURI = "toolbar:" + part.getId(); //$NON-NLS-1$
			menuSvc.populateContributionManager(tbMgr, tbURI);
			MToolBar viewTB = ApplicationFactory.eINSTANCE.createMToolBar();
			MenuHelper.processToolbarManager(localContext, viewTB, tbMgr
					.getItems());
			part.setToolBar(viewTB);
			tbMgr.getControl().dispose();

			String menuURI = "menu:" + part.getId(); //$NON-NLS-1$
			MenuManager menuMgr = (MenuManager) site.getActionBars()
					.getMenuManager();
			menuSvc.populateContributionManager(menuMgr, menuURI);
			MMenu viewMenu = ApplicationFactory.eINSTANCE.createMMenu();
			MenuHelper.processMenuManager(localContext, viewMenu, menuMgr
					.getItems());
			part.setMenu(viewMenu);

			// HACK!! presumes it's the -last- child of the parent
			if (parent.getChildren().length > 0)
				return parent.getChildren()[parent.getChildren().length - 1];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object createWidget(MPart<?> part, Object parent) {
		String partId = part.getId();

		if (!(parent instanceof Composite))
			return null;

		Composite parentComposite = (Composite) parent;

		Control newCtrl = null;
		if (part instanceof MPerspective) {
			IConfigurationElement perspFactory = findPerspectiveFactory(partId);
			if (perspFactory != null || part.getChildren().size() > 0) {
				newCtrl = createPerspective((MPerspective<MPart<?>>) part,
						perspFactory, parentComposite);
			}
			return newCtrl;
		} else if (part instanceof MContributedPart) {
			MContributedPart cp = (MContributedPart) part;

			// HACK!! relies on legacy views -not- having a URI...
			String uri = cp.getURI();
			if (uri != null && uri.length() > 0)
				return null;

			// if this a view ?
			IConfigurationElement viewElement = findViewConfig(partId);
			if (viewElement != null)
				newCtrl = createView((MContributedPart<MPart<?>>) part,
						viewElement, parentComposite);

			IConfigurationElement editorElement = findEditorConfig(partId);
			if (editorElement != null)
				newCtrl = createEditor((MContributedPart<MPart<?>>) part,
						editorElement, parentComposite);
			if (newCtrl == null) {
				Composite pc = (Composite) getParentWidget(part);
				Label lbl = new Label(pc, SWT.BORDER);
				lbl.setText(part.getId());
				newCtrl = lbl;
			}

			return newCtrl;
		}
		return null;
	}

	/**
	 * @param part
	 * @param perspFactory
	 * @param parentComposite
	 * @return
	 */
	private Control createPerspective(MPerspective<MPart<?>> part,
			IConfigurationElement perspFactory, Composite parentComposite) {
		Composite perspArea = new Composite(parentComposite, SWT.NONE);
		IStylingEngine stylingEngine = (IStylingEngine) part.getContext().get(
				IStylingEngine.SERVICE_NAME);
		stylingEngine.setClassname(perspArea, "perspectiveLayout"); //$NON-NLS-1$
		perspArea.setLayout(new FillLayout());

		return perspArea;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.workbench.ui.renderers.swt.SWTPartRenderer#disposeWidget
	 * (org.eclipse.e4.ui.model.application.MPart)
	 */
	@Override
	public void disposeWidget(MPart<?> part) {
		if (part instanceof MContributedPart) {
			MContributedPart mpart = (MContributedPart) part;
			Object obj = mpart.getObject();
			if (obj instanceof IWorkbenchPart) {
				((IWorkbenchPart) obj).dispose();
				if (obj instanceof IEditorPart) {
					Activator.trace(Policy.DEBUG_RENDERER,
							"Disposing tracker for " + obj, null); //$NON-NLS-1$
					Trackable t = trackables.remove(obj);
					t.participating = false;
					part.getContext().set(EDITOR_DISPOSED, Boolean.TRUE);
				}
			}
		}
		super.disposeWidget(part);
		if (part instanceof MContributedPart) {
			MContributedPart mpart = (MContributedPart) part;
			Object obj = mpart.getObject();
			if (obj instanceof IWorkbenchPart) {
				if (obj instanceof IEditorPart) {
					EditorSite site = (EditorSite) ((IEditorPart) obj)
							.getEditorSite();
					disposeEditorActionBars((EditorActionBars) site
							.getActionBars());
					site.dispose();
				} else if (obj instanceof IViewPart) {
					ViewSite site = (ViewSite) ((IViewPart) obj).getViewSite();
					SubActionBars bars = (SubActionBars) site.getActionBars();
					bars.dispose();
					site.dispose();
				}
			}
			mpart.setObject(null);
		}
	}

}
