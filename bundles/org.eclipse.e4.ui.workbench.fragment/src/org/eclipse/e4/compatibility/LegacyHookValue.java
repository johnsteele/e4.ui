package org.eclipse.e4.compatibility;

import org.eclipse.e4.core.services.context.IContextFunction;
import org.eclipse.e4.core.services.context.IEclipseContext;

public class LegacyHookValue implements IContextFunction {
	public Object compute(IEclipseContext context, Object[] arguments) {
		LegacyHook lh = new LegacyHook(context);
		return lh;
	}
}
