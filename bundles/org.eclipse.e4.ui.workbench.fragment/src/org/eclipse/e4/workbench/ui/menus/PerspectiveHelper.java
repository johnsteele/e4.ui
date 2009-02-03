package org.eclipse.e4.workbench.ui.menus;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.e4.extensions.ExtensionUtils;
import org.eclipse.e4.ui.model.application.MContributedPart;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.model.application.MStack;
import org.eclipse.e4.ui.model.workbench.MPerspective;
import org.eclipse.e4.workbench.ui.api.ModeledPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;

public class PerspectiveHelper {
	
	public static void loadPerspective(MPerspective perspModel) {
		String perspId = perspModel.getId();
		IConfigurationElement[] persps = ExtensionUtils.getExtensions(IWorkbenchRegistryConstants.PL_PERSPECTIVES);
		IConfigurationElement perspContribution = ExtensionUtils.findExtension(persps, perspId);
		if (perspContribution == null)
			return;

		IPerspectiveFactory impl = null;
		try {
			impl = (IPerspectiveFactory) perspContribution.createExecutableExtension("class"); //$NON-NLS-1$
		} catch (CoreException e) {
			e.printStackTrace();
		}
		if (impl == null)
			return;

		ModeledPageLayout layout = new ModeledPageLayout(perspModel);
		impl.createInitialLayout(layout);
		
		loadExtensions(perspModel, layout);
	}
	
	private static void loadExtensions(MPerspective perspModel, ModeledPageLayout layout) {
		String perspId = perspModel.getId();
		if (perspId == null)
			return;
		
		IConfigurationElement[] perspExts = ExtensionUtils.getExtensions(IWorkbenchRegistryConstants.PL_PERSPECTIVE_EXTENSIONS);
		for (int i = 0; i < perspExts.length; i++) {
			String extTarget = perspExts[i].getAttribute(IWorkbenchRegistryConstants.ATT_TARGET_ID);
			if (perspId.equals(extTarget)) {
				IConfigurationElement[] viewExts = perspExts[i].getChildren(IWorkbenchRegistryConstants.TAG_VIEW);
				for (int j = 0; j < viewExts.length; j++) {
					String id = viewExts[j].getAttribute(IWorkbenchRegistryConstants.ATT_ID);
//					String relationship = viewExts[j].getAttribute("relationship");
					String relative = viewExts[j].getAttribute(IWorkbenchRegistryConstants.ATT_RELATIVE);
					String visible = viewExts[j].getAttribute(IWorkbenchRegistryConstants.ATT_VISIBLE);
//					String closeable = viewExts[j].getAttribute("closeable");
//					String showTitle = viewExts[j].getAttribute("showTitle");
					
					MPart relPart = ModeledPageLayout.findPart(perspModel, relative);
					MStack sm = (MStack) relPart.getParent();
					MContributedPart viewModel = ModeledPageLayout.createViewModel(id, Boolean.parseBoolean(visible));
					sm.getChildren().add(viewModel);
				}
			}
		}
	}
	
//	private static int getRelInt(String relationship) {
//		if ("left".equalsIgnoreCase(relationship)) //$NON-NLS-1$
//			return IPageLayout.LEFT;
//		if ("right".equalsIgnoreCase(relationship)) //$NON-NLS-1$
//			return IPageLayout.RIGHT;
//		if ("top".equalsIgnoreCase(relationship)) //$NON-NLS-1$
//			return IPageLayout.TOP;
//		if ("bottom".equalsIgnoreCase(relationship)) //$NON-NLS-1$
//			return IPageLayout.BOTTOM;
//		
//		return IPageLayout.BOTTOM;
//	}
}
