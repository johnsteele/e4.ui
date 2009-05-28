package org.eclipse.e4.extensions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.e4.core.services.context.EclipseContextFactory;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.core.services.context.spi.IContextConstants;
import org.eclipse.e4.ui.model.application.MContributedPart;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.model.workbench.MPerspective;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.workbench.ui.internal.UISchedulerStrategy;
import org.eclipse.e4.workbench.ui.menus.PerspectiveHelper;
import org.eclipse.e4.workbench.ui.renderers.swt.SWTPartFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.internal.EditorSite;
import org.eclipse.ui.internal.ViewSite;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;

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
	private Control createEditor(MContributedPart<MPart<?>> part,
			IConfigurationElement editorElement) {
		Composite parent = (Composite) getParentWidget(part);

		// part.setPlugin(viewContribution.getContributor().getName());
		part.setIconURI(editorElement.getAttribute("icon")); //$NON-NLS-1$
		//part.setName(editorElement.getAttribute("name")); //$NON-NLS-1$
		IEditorPart impl = null;
		try {
			impl = (IEditorPart) editorElement
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
			ModelEditorReference ref = new ModelEditorReference(part, page);
			EditorSite site = new EditorSite(ref, impl, page);
			site.setConfigurationElement(editorElement);
			impl.init(site, (IEditorInput) localContext.get(IEditorInput.class
					.getName()));

			impl.createPartControl(parent);
			if (parent.getChildren().length > 0)
				return parent.getChildren()[parent.getChildren().length - 1];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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

			impl.createPartControl(parent);

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

		if (part.getChildren().size() == 0)
			PerspectiveHelper.loadPerspective(part, perspFactory);

		return perspArea;
	}

}
