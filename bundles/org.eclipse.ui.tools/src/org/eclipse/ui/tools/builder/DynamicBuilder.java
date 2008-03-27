package org.eclipse.ui.tools.builder;

import java.net.URL;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.ui.tools.Activator;
import org.eclipse.ui.tools.dynamic.DynamicTools;

public class DynamicBuilder extends IncrementalProjectBuilder {

	// class SampleDeltaVisitor implements IResourceDeltaVisitor {
	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
	// */
	// public boolean visit(IResourceDelta delta) throws CoreException {
	// IResource resource = delta.getResource();
	// switch (delta.getKind()) {
	// case IResourceDelta.ADDED:
	// // handle added resource
	// checkXML(resource);
	// break;
	// case IResourceDelta.REMOVED:
	// // handle removed resource
	// break;
	// case IResourceDelta.CHANGED:
	// // handle changed resource
	// checkXML(resource);
	// break;
	// }
	// //return true to continue visiting children.
	// return true;
	// }
	// }
	//
	// class SampleResourceVisitor implements IResourceVisitor {
	// public boolean visit(IResource resource) {
	// checkXML(resource);
	// //return true to continue visiting children.
	// return true;
	// }
	// }

	public static final String BUILDER_ID = "org.eclipse.ui.tools.dynamicBuilder"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int,
	 *      java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		String bundleName = PluginRegistry.findModel(getProject())
				.getBundleDescription().getName();
		try {
			DynamicTools.uninstallBundle(bundleName);
			URL url = getProject().getLocationURI().toURL();
			if (url != null)
				DynamicTools.installPlugin(url);

			return null;
		} catch (Exception e) {
			throw new CoreException(new Status(IStatus.ERROR,
					Activator.PLUGIN_ID, e.getMessage(), e));
		}
	}

	//	
	//
	// protected void fullBuild(final IProgressMonitor monitor)
	// throws CoreException {
	// try {
	// getProject().accept(new SampleResourceVisitor());
	// } catch (CoreException e) {
	// }
	// }
	//
	//	
	//
	// protected void incrementalBuild(IResourceDelta delta,
	// IProgressMonitor monitor) throws CoreException {
	// // the visitor does the work.
	// delta.accept(new SampleDeltaVisitor());
	// }
}
