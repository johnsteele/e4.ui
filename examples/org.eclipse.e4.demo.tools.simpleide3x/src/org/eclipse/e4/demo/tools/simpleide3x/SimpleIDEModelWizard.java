package org.eclipse.e4.demo.tools.simpleide3x;

import org.eclipse.e4.demo.simpleide.model.simpleide.MSimpleideFactory;
import org.eclipse.e4.internal.tools.wizards.model.BaseApplicationModelWizard;
import org.eclipse.e4.internal.tools.wizards.model.NewModelFilePage;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;

public class SimpleIDEModelWizard extends BaseApplicationModelWizard {

	@Override
	public String getDefaultFileName() {
		return "Application.e4xmi";
	}

	@Override
	protected EObject createInitialModel() {
		return (EObject) MSimpleideFactory.INSTANCE.createSimpleIDEApplication();
	}

	@Override
	protected NewModelFilePage createWizardPage(ISelection selection) {
		// TODO Auto-generated method stub
		return null;
	}

}
