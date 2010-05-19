package org.eclipse.e4.ui.examples.legacy.workbench;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.core.contexts.IContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;

public class WorkspaceValue implements IContextFunction {

	/* (non-Javadoc)
	 * @see org.eclipse.e4.core.contexts.IContextFunction#compute(org.eclipse.e4.core.contexts.IEclipseContext)
	 */
	public Object compute(IEclipseContext context) {
		ResourcesPlugin.getPlugin().getPluginPreferences().setValue(ResourcesPlugin.PREF_AUTO_REFRESH, true);
		return ResourcesPlugin.getWorkspace();
	}

}
