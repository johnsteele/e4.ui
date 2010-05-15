package org.eclipse.e4.demo.simpleide.editor.handler;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.demo.simpleide.model.simpleide.MEditorPartDescriptor;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.workbench.modeling.EModelService;
import org.eclipse.e4.workbench.modeling.EPartService;

public class OpenEditor {
	
	@Execute
	public void open(MApplication application, MEditorPartDescriptor model, EModelService modelService, EPartService partService) {
		MPartStack stack = (MPartStack) modelService.find("simpleide.editorstack", application);
	}
}
