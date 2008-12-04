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
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.part.ViewPart;

public class LegacyViewFactory extends PartFactory {

	private IConfigurationElement findViewConfig(String id) {
		IConfigurationElement[] persps = ExtensionUtils.getExtensions(IWorkbenchRegistryConstants.PL_VIEWS);
		IConfigurationElement viewContribution = ExtensionUtils.findExtension(persps, id);
		
		return viewContribution;
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
			impl.createPartControl(parent);
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
			String partId = part.getId();
			IConfigurationElement viewElement = findViewConfig(partId);
			
			// HACK!! relies on legacy views -not- having a URI...
			String uri = cp.getURI();
			if (viewElement == null && uri != null && uri.length() > 0)
				return null;
			
			Composite pc = (Composite) getParentWidget(part);
			Control newView = null;
			if (viewElement != null)
				newView = createView((ContributedPart<Part<?>>) part, viewElement);
			if (newView == null) {
				Label lbl = new Label(pc, SWT.BORDER);
				lbl.setText(part.getId());
				newView = lbl;
			}
			
			return newView;
		}
		return null;
	}
//
//	@Override
//	public Object createWidget(Part<?> part) {
//		if (!(part instanceof ContributedPart))
//			return null;
//		
//		ContributedPart<?> itemPart = (ContributedPart<?>) part;
//			
//		String partId = part.getId();
//		IConfigurationElement[] persps = ExtensionUtils.getExtensions(IWorkbenchRegistryConstants.PL_VIEWS);
//		IConfigurationElement viewContribution = ExtensionUtils.findExtension(persps, partId);
//		if (viewContribution == null)
//			return null;
//
//		String bundleId = viewContribution.getContributor().getName();
//		String iconPath = viewContribution.getAttribute("icon");
//		//itemPart.setPlugin(viewContribution.getContributor().getName());
//		
//		itemPart.setIconURI(viewContribution.getAttribute("icon"));
//		itemPart.setName(viewContribution.getAttribute("name"));
//		ViewPart impl = null;
//		try {
//			impl = (ViewPart) viewContribution.createExecutableExtension("class");
//		} catch (CoreException e) {
//			//e.printStackTrace();
//		}
//		if (impl == null)
//			return null;
//
//		try {
//			impl.createPartControl(parent);
//			return parent.getChildren()[parent.getChildren().length-1];
//		} catch (Exception e) {
//			//e.printStackTrace();
//		}
//		return null;
//	}

}
