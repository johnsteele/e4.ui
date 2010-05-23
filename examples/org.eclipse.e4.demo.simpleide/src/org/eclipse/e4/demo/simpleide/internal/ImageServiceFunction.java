package org.eclipse.e4.demo.simpleide.internal;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;

public class ImageServiceFunction extends ContextFunction {

	@Override
	public Object compute(IEclipseContext context) {
		return ContextInjectionFactory.make(ImageService.class, context);
	}

}
