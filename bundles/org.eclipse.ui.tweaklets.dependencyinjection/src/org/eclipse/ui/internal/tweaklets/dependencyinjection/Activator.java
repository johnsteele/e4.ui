package org.eclipse.ui.internal.tweaklets.dependencyinjection;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

	private static final String ID = "org.eclipse.ui.tweaklets.dependencyinjection";
	private static BundleContext context;
	private static ServiceTracker bundleTracker;

	public Activator() {
	}
	
	private static PackageAdmin getPackageAdmin() {
		if (bundleTracker == null) {
			if (context == null)
				return null;
			bundleTracker = new ServiceTracker(context, PackageAdmin.class
					.getName(), null);
			bundleTracker.open();
		}
		return (PackageAdmin) bundleTracker.getService();
	}

	public void start(BundleContext context) throws Exception {
		Activator.context = context;
	}

	public void stop(BundleContext context) throws Exception {
		bundleTracker.close();
		bundleTracker = null;
		context = null;
	}

	public static Class loadClass(String bundleName, String className)
			throws ClassNotFoundException {
		Bundle bundle = getBundle(bundleName);
		if (bundle == null) {
			return null;
		}
		return bundle.loadClass(className);
	}

	/**
	 * @param bundleName
	 * @return
	 */
	private static Bundle getBundle(String bundleName) {
		PackageAdmin packageAdmin = getPackageAdmin();
		Bundle[] bundles = packageAdmin.getBundles(bundleName, null);
		if (bundles == null)
			return null;
		// Return the first bundle that is not installed or uninstalled
		for (int i = 0; i < bundles.length; i++) {
			if ((bundles[i].getState() & (Bundle.INSTALLED | Bundle.UNINSTALLED)) == 0) {
				return bundles[i];
			}
		}
		return null;
	}

}
