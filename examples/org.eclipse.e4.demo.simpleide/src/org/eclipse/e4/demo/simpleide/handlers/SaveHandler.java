package org.eclipse.e4.demo.simpleide.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.workbench.modeling.EPartService;

public class SaveHandler {
	@Execute
	public void save(EPartService partService) {
		MPart part = partService.getActivePart();
		if( part != null && part.isDirty() ) {
			partService.savePart(part, false);
		}
	}
}
