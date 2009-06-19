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
import org.eclipse.e4.workbench.ui.internal.UISchedulerStrategy;
import org.eclipse.e4.workbench.ui.menus.MenuHelper;
import org.eclipse.e4.workbench.ui.renderers.PartFactory;
import org.eclipse.e4.workbench.ui.renderers.swt.SWTPartFactory;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.EditorActionBars;
import org.eclipse.ui.internal.EditorActionBuilder;
import org.eclipse.ui.internal.EditorSite;
import org.eclipse.ui.internal.ViewSite;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.internal.registry.EditorDescriptor;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.menus.IMenuService;

public class LegacyViewFactory extends SWTPartFactory {

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

	/**
	 * @param part
	 * @param editorElement
	 * @return
	 */
	private Control createEditor(final MContributedPart<MPart<?>> part,
			IConfigurationElement editorElement) {
		final Composite parent = (Composite) getParentWidget(part);
		EditorDescriptor desc = new EditorDescriptor(part.getId(),
				editorElement);

		// Convert the relative path into a bundle URI
		String imagePath = editorElement.getAttribute("icon"); //$NON-NLS-1$
		imagePath = imagePath.replace("$nl$", ""); //$NON-NLS-1$//$NON-NLS-2$
		if (imagePath.charAt(0) != '/') {
			imagePath = '/' + imagePath;
		}
		String bundleId = editorElement.getContributor().getName();
		String imageURI = "platform:/plugin/" + bundleId + imagePath; //$NON-NLS-1$
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
			localContext.set(IContextConstants.DEBUG_STRING, "Legacy Editor"); //$NON-NLS-1$
			final IEclipseContext outputContext = EclipseContextFactory.create(
					null, UISchedulerStrategy.getInstance());
			outputContext.set(IContextConstants.DEBUG_STRING,
					"ContributedPart-output"); //$NON-NLS-1$
			localContext.set(IServiceConstants.OUTPUTS, outputContext);
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
			impl.addPropertyListener(new IPropertyListener() {
				private CTabItem findItemForPart(CTabFolder ctf) {
					CTabItem[] items = ctf.getItems();
					for (int i = 0; i < items.length; i++) {
						if (items[i].getData(PartFactory.OWNING_ME) == part) {
							return items[i];
						}
					}

					return null;
				}

				public void propertyChanged(Object source, int propId) {
					if (parent instanceof CTabFolder) {
						CTabFolder ctf = (CTabFolder) parent;
						CTabItem partItem = findItemForPart(ctf);
						String itemText = partItem.getText();
						if (implementation.isDirty()
								&& itemText.indexOf('*') != 0) {
							itemText = '*' + itemText;
						} else if (itemText.indexOf('*') == 0) {
							itemText = itemText.substring(1);
						}
						partItem.setText(itemText);
					}
					// DebugUITools.openLaunchConfigurationDialogOnGroup(parent
					// .getShell(), null,
					//							"org.eclipse.debug.ui.launchGroup.debug"); //$NON-NLS-1$
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

	private Control createView(MContributedPart<MPart<?>> part,
			IConfigurationElement viewContribution) {
		Composite parent = (Composite) getParentWidget(part);

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
			localContext.set(IContextConstants.DEBUG_STRING, "Legacy Editor"); //$NON-NLS-1$
			final IEclipseContext outputContext = EclipseContextFactory.create(
					null, UISchedulerStrategy.getInstance());
			outputContext.set(IContextConstants.DEBUG_STRING,
					"ContributedPart-output"); //$NON-NLS-1$
			localContext.set(IServiceConstants.OUTPUTS, outputContext);
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

			// Popupate and scrape the old-style contributions...
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
	public Object createWidget(MPart<?> part) {
		String partId = part.getId();

		Control newCtrl = null;
		if (part instanceof MPerspective) {
			IConfigurationElement perspFactory = findPerspectiveFactory(partId);
			if (perspFactory != null || part.getChildren().size() > 0) {
				newCtrl = createPerspective((MPerspective<MPart<?>>) part,
						perspFactory);
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
						viewElement);

			IConfigurationElement editorElement = findEditorConfig(partId);
			if (editorElement != null)
				newCtrl = createEditor((MContributedPart<MPart<?>>) part,
						editorElement);
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
	 * @return
	 */
	private Control createPerspective(MPerspective<MPart<?>> part,
			IConfigurationElement perspFactory) {
		Widget parentWidget = getParentWidget(part);
		if (!(parentWidget instanceof Composite))
			return null;

		Composite perspArea = new Composite((Composite) parentWidget, SWT.NONE);
		perspArea.setLayout(new FillLayout());

		return perspArea;
	}

}
