package org.eclipse.e4.compatibility;

import org.eclipse.e4.core.services.context.IComputedValue;
import org.eclipse.e4.core.services.context.IEclipseContext;

public class LegacyHookValue implements IComputedValue {
	public Object compute(IEclipseContext context, Object[] arguments) {
		LegacyHook lh = new LegacyHook(context);
		return lh;
	}
}
