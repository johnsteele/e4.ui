package org.eclipse.e4.demo.simpleide.e4editor;

import org.eclipse.e4.demo.simpleide.model.simpleide.MEditorPartDescriptor;
import org.eclipse.e4.demo.simpleide.model.simpleide.MSimpleIDEApplication;
import org.eclipse.e4.demo.simpleide.model.simpleide.MSimpleideFactory;
import org.eclipse.e4.workbench.modeling.IModelExtension;
import org.eclipse.emf.ecore.EObject;

public class ModelProcessor implements IModelExtension {

	public void processElement(EObject parent) {
		if( parent instanceof MSimpleIDEApplication ) {
			MSimpleIDEApplication app = (MSimpleIDEApplication) parent;
			
			MEditorPartDescriptor desc = MSimpleideFactory.INSTANCE.createEditorPartDescriptor();
			desc.setContributionURI("platform:/plugin/org.eclipse.e4.tools.emf.ui/org.eclipse.e4.tools.emf.ui.internal.wbm.ApplicationModelEditor");
			desc.setIconURI("platform:/plugin/org.eclipse.e4.demo.simpleide.e4editor/icons/application_view_tile.png");
			desc.getFileextensions().add("e4xmi");
			app.getEditorPartDescriptors().add(desc);
		}
	}
}
