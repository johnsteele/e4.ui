package org.eclipse.e4.extensions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.e4.core.services.context.EclipseContextFactory;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.core.services.context.spi.IContextConstants;
import org.eclipse.e4.ui.model.application.MContributedPart;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.workbench.ui.internal.UIContextScheduler;
import org.eclipse.e4.workbench.ui.renderers.swt.SWTPartFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.LegacyWBWImpl;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.LegacyWPSImpl;
import org.eclipse.ui.part.ViewPart;

public class LegacyViewFactory extends SWTPartFactory {

	private IConfigurationElement findViewConfig(String id) {
		IConfigurationElement[] views = ExtensionUtils.getExtensions(IWorkbenchRegistryConstants.PL_VIEWS);
		IConfigurationElement viewContribution = ExtensionUtils.findExtension(views, id);
		return viewContribution;
	}

	private IConfigurationElement findEditorConfig(String id) {
		IConfigurationElement[] editors = ExtensionUtils.getExtensions(IWorkbenchRegistryConstants.PL_EDITOR);
		IConfigurationElement editorContribution = ExtensionUtils.findExtension(editors, id);
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

		//part.setPlugin(viewContribution.getContributor().getName());
		part.setIconURI(editorElement.getAttribute("icon")); //$NON-NLS-1$
		//part.setName(editorElement.getAttribute("name")); //$NON-NLS-1$
		EditorPart impl = null;
		try {
			impl = (EditorPart) editorElement.createExecutableExtension("class"); //$NON-NLS-1$
		} catch (CoreException e) {
			e.printStackTrace();
		}
		if (impl == null)
			return null;

		try {
			IEclipseContext parentContext = getContextForParent(part);
			final IEclipseContext localContext = EclipseContextFactory.create(
								parentContext, UIContextScheduler.instance);
						localContext.set(IContextConstants.DEBUG_STRING, "Legacy Editor"); //$NON-NLS-1$
			part.setContext(localContext);
			
			// Assign a 'site' for the newly instantiated part
			LegacyWPSImpl site = new LegacyWPSImpl(part, impl);
			impl.init(site, LegacyWBWImpl.hackInput);  // HACK!! needs an editorInput

			impl.createPartControl(parent);
			if (parent.getChildren().length > 0)
				return parent.getChildren()[parent.getChildren().length-1];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Control createView(MContributedPart<MPart<?>> part, IConfigurationElement viewContribution) {
		Composite parent = (Composite) getParentWidget(part);

		ViewPart impl = null;
		try {
			impl = (ViewPart) viewContribution.createExecutableExtension("class"); //$NON-NLS-1$
		} catch (CoreException e) {
			e.printStackTrace();
		}
		if (impl == null)
			return null;

		try {
			IEclipseContext parentContext = getContextForParent(part);
			final IEclipseContext localContext = EclipseContextFactory.create(
								parentContext, UIContextScheduler.instance);
						localContext.set(IContextConstants.DEBUG_STRING, "Legacy Editor"); //$NON-NLS-1$
			part.setContext(localContext);
			
			// Assign a 'site' for the newly instantiated part
			LegacyWPSImpl site = new LegacyWPSImpl(part, impl);
			impl.init(site, null);

			impl.createPartControl(parent);
			
			// HACK!! presumes it's the -last- child of the parent
			if (parent.getChildren().length > 0)
				return parent.getChildren()[parent.getChildren().length-1];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object createWidget(MPart<?> part) {
		if (part instanceof MContributedPart) {
			MContributedPart cp = (MContributedPart) part;
			
			// HACK!! relies on legacy views -not- having a URI...
			String uri = cp.getURI();
			if (uri != null && uri.length() > 0)
				return null;

			Control newCtrl = null;
			String partId = part.getId();
			
			// if this a view ?
			IConfigurationElement viewElement = findViewConfig(partId);
			if (viewElement != null)
				newCtrl = createView((MContributedPart<MPart<?>>) part, viewElement);
			
			IConfigurationElement editorElement = findEditorConfig(partId);
			if (editorElement != null)
				newCtrl = createEditor((MContributedPart<MPart<?>>) part, editorElement);
			if (newCtrl == null) {
				
			}
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

}
