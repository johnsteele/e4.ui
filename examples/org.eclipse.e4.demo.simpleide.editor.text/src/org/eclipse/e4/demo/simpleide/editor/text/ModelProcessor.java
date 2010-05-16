package org.eclipse.e4.demo.simpleide.editor.text;

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
			desc.setContributionURI("platform:/plugin/org.eclipse.e4.demo.simpleide.editor.text/org.eclipse.e4.demo.simpleide.editor.text.PlainTextEditor");
			desc.setIconURI("platform:/plugin/org.eclipse.e4.demo.simpleide.editor.text/icons/file_obj.gif");
			desc.getFileextensions().add("txt");
			app.getEditorPartDescriptors().add(desc);
			
			desc = MSimpleideFactory.INSTANCE.createEditorPartDescriptor();
			desc.setContributionURI("platform:/plugin/org.eclipse.e4.demo.simpleide.editor.text/org.eclipse.e4.demo.simpleide.editor.text.XMLTextEditor");
			desc.setIconURI("platform:/plugin/org.eclipse.e4.demo.simpleide.editor.text/icons/elements_obj.gif");
			desc.getFileextensions().add("xml");
			app.getEditorPartDescriptors().add(desc);
		}
	}
}