package org.eclipse.e4.demo.tools.simpleide3x;

import org.eclipse.e4.demo.simpleide.model.simpleide.MSimpleideFactory;
import org.eclipse.e4.tools.emf.editor3x.wizard.BaseApplicationModelWizard;
import org.eclipse.emf.ecore.EObject;

public class SimpleIDEModelWizard extends BaseApplicationModelWizard {

	@Override
	public String getDefaultFileName() {
		return "Application.e4xmi";
	}

	@Override
	protected EObject createInitialModel() {
		return (EObject) MSimpleideFactory.INSTANCE.createSimpleIDEApplication();
	}

}
