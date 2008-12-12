package org.eclipse.e4.extensions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.e4.ui.model.application.ContributedPart;
import org.eclipse.e4.ui.model.application.Part;
import org.eclipse.e4.workbench.ui.renderers.swt.PartFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.LegacyWBImpl;
import org.eclipse.ui.LegacyWBWImpl;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.LegacyWPSImpl;
import org.eclipse.ui.part.ViewPart;

public class LegacyViewFactory extends PartFactory {

	private IConfigurationElement findViewConfig(String id) {
		IConfigurationElement[] persps = ExtensionUtils.getExtensions(IWorkbenchRegistryConstants.PL_VIEWS);
		IConfigurationElement viewContribution = ExtensionUtils.findExtension(persps, id);
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
	private Control createEditor(ContributedPart<Part<?>> part,
			IConfigurationElement editorElement) {
		Composite parent = (Composite) getParentWidget(part);

		//part.setPlugin(viewContribution.getContributor().getName());
		part.setIconURI(editorElement.getAttribute("icon")); //$NON-NLS-1$
		part.setName(editorElement.getAttribute("name")); //$NON-NLS-1$
		EditorPart impl = null;
		try {
			impl = (EditorPart) editorElement.createExecutableExtension("class"); //$NON-NLS-1$
		} catch (CoreException e) {
			e.printStackTrace();
		}
		if (impl == null)
			return null;

		try {
			// Assign a 'site' for the newly instantiated part
			LegacyWBImpl wb = (LegacyWBImpl) PlatformUI.getWorkbench();
			LegacyWPSImpl site = new LegacyWPSImpl(wb.getE4Workbench(), part, impl);
			impl.init(site, LegacyWBWImpl.hackInput);  // HACK!! needs an editorInput

			impl.createPartControl(parent);
			if (parent.getChildren().length > 0)
				return parent.getChildren()[parent.getChildren().length-1];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Control createView(ContributedPart<Part<?>> part, IConfigurationElement viewContribution) {
		Composite parent = (Composite) getParentWidget(part);

		//part.setPlugin(viewContribution.getContributor().getName());
		part.setIconURI(viewContribution.getAttribute("icon")); //$NON-NLS-1$
		part.setName(viewContribution.getAttribute("name")); //$NON-NLS-1$
		ViewPart impl = null;
		try {
			impl = (ViewPart) viewContribution.createExecutableExtension("class"); //$NON-NLS-1$
		} catch (CoreException e) {
			e.printStackTrace();
		}
		if (impl == null)
			return null;

		try {
			// Assign a 'site' for the newly instantiated part
			LegacyWBImpl wb = (LegacyWBImpl) PlatformUI.getWorkbench();
			LegacyWPSImpl site = new LegacyWPSImpl(wb.getE4Workbench(), part, impl);
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
	public Object createWidget(Part<?> part) {
		if (part instanceof ContributedPart) {
			ContributedPart cp = (ContributedPart) part;
			
			// HACK!! relies on legacy views -not- having a URI...
			String uri = cp.getURI();
			if (uri != null && uri.length() > 0)
				return null;

			Control newCtrl = null;
			String partId = part.getId();
			
			// if this a view ?
			IConfigurationElement viewElement = findViewConfig(partId);
			if (viewElement != null)
				newCtrl = createView((ContributedPart<Part<?>>) part, viewElement);
			
			IConfigurationElement editorElement = findEditorConfig(partId);
			if (editorElement != null)
				newCtrl = createEditor((ContributedPart<Part<?>>) part, editorElement);
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
