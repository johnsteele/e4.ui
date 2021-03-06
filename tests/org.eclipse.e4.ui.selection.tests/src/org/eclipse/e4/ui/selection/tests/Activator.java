package org.eclipse.e4.ui.selection.tests;

import org.eclipse.e4.core.services.IDisposable;
import org.eclipse.e4.core.services.context.EclipseContextFactory;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.osgi.service.packageadmin.PackageAdmin;

public class Activator implements BundleActivator {

	private static Activator plugin = null;
	private IEclipseContext appContext;
	private IEclipseContext serviceContext;

	public static Activator getDefault() {
		return plugin;
	}

	public void start(BundleContext context) throws Exception {
		plugin = this;
		serviceContext = EclipseContextFactory.getServiceContext(context);
		appContext = EclipseContextFactory.create(serviceContext, null);
		addLogService(appContext);
	}

	private void addLogService(IEclipseContext context) {
		context.set(LogService.class.getName(), new LogService() {

			public void log(int level, String message) {
				System.out.println(level + ": " + message);
			}

			public void log(int level, String message, Throwable exception) {
				System.out.println(level + ": " + message);
				if (exception != null) {
					exception.printStackTrace();
				}
			}

			public void log(ServiceReference sr, int level, String message) {
				// TODO Auto-generated method stub

			}

			public void log(ServiceReference sr, int level, String message,
					Throwable exception) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void stop(BundleContext context) throws Exception {
		if (serviceContext instanceof IDisposable) {
			((IDisposable) serviceContext).dispose();
		}
		plugin = null;
	}

	public IEclipseContext getGlobalContext() {
		return appContext;
	}

	public PackageAdmin getBundleAdmin() {
		return (PackageAdmin) serviceContext.get(PackageAdmin.class.getName());
	}

	public Bundle getBundleForName(String bundleName) {
		Bundle[] bundles = getBundleAdmin().getBundles(bundleName, null);
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

