package org.eclipse.e4.demo.simpleide.editor.handler;

import org.eclipse.core.resources.IFile;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.demo.simpleide.model.simpleide.MEditorPartDescriptor;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MInputPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.workbench.modeling.EModelService;
import org.eclipse.e4.workbench.modeling.EPartService;
import org.eclipse.e4.workbench.modeling.EPartService.PartState;

public class OpenEditor {
	
	@Execute
	public void open(
			IFile file,
			MApplication application, 
			MEditorPartDescriptor model, 
			EModelService modelService, 
			EPartService partService) {
		MPartStack stack = (MPartStack) modelService.find("simpleide.stack.editor", application);
		
		MInputPart part = MBasicFactory.INSTANCE.createInputPart();
		part.setContributionURI(model.getContributionURI());
		part.setInputURI(file.getFullPath().toString());
		part.setIconURI(model.getIconURI());
		part.setLabel(file.getName());
		part.setTooltip(file.getFullPath().toString());
		part.setCloseable(true);
		stack.getChildren().add(part);
		partService.showPart(part, PartState.ACTIVATE);
	}
}
