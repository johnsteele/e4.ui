package org.eclipse.e4.demo.simpleide.editor.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.demo.simpleide.editor.IInput;
import org.eclipse.e4.demo.simpleide.editor.IInput.Listener;
import org.eclipse.e4.ui.model.application.ui.basic.MInputPart;

public class IDocumentInputProviderFunction extends ContextFunction {

	@Override
	public Object compute(IEclipseContext context, Object[] arguments) {
		final MInputPart inputPart = context.get(MInputPart.class);
		
		IWorkspace workspace = context.get(IWorkspace.class);
		
		String uri = inputPart.getInputURI();
		Path p = new Path(uri);
		IFile file = workspace.getRoot().getFile(p);
		FileDocumentInput input = new FileDocumentInput(file);
		
		input.addListener(new Listener() {
			
			public void propertyChanged(String property, Object oldValue,
					Object newValue) {
				if( IInput.DIRTY == property ) {
					inputPart.setDirty((Boolean)newValue);
				}
			}
		});
		
		return input;
	}

}
