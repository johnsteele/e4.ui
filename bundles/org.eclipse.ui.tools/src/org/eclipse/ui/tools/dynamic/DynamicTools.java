/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.tools.dynamic;

import java.net.URL;

import org.eclipse.ui.tools.Activator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;

public class DynamicTools {

	public static final Bundle installPlugin(URL dataURL) throws 
			BundleException {
		// Programmatically install a new plugin
		String pluginLocation = "reference:" + dataURL.toExternalForm(); //$NON-NLS-1$
		return installBundle(pluginLocation);
	}

	public static Bundle installBundle(String pluginLocation)
			throws BundleException, IllegalStateException {
		Bundle target = Activator.getDefault().getContext().installBundle(
				pluginLocation);
		int state = target.getState();
		if (state != Bundle.INSTALLED)
			throw new IllegalStateException("Bundle " + target //$NON-NLS-1$
					+ " is in a wrong state: " + state); //$NON-NLS-1$
		refreshPackages(new Bundle[] { target });
		return target;
	}
	
	public static Bundle getBundle(String pluginLocation) {
		Bundle [] bundles = Activator.getDefault().getContext().getBundles();
		for (int i = 0; i < bundles.length; i++) {
			if (pluginLocation.equals(bundles[i].getLocation()))
				return bundles[i];
		}
		return null;
	}

	public static void refreshPackages(Bundle[] bundles) {
		BundleContext context = Activator.getDefault().getContext();
		ServiceReference packageAdminRef = context
				.getServiceReference(PackageAdmin.class.getName());
		PackageAdmin packageAdmin = null;
		if (packageAdminRef != null) {
			packageAdmin = (PackageAdmin) context.getService(packageAdminRef);
			if (packageAdmin == null)
				return;
		}

		final boolean[] flag = new boolean[] { false };
		FrameworkListener listener = new FrameworkListener() {
			public void frameworkEvent(FrameworkEvent event) {
				if (event.getType() == FrameworkEvent.PACKAGES_REFRESHED)
					synchronized (flag) {
						flag[0] = true;
						flag.notifyAll();
					}
			}
		};
		context.addFrameworkListener(listener);
		packageAdmin.refreshPackages(bundles);
		synchronized (flag) {
			while (!flag[0]) {
				try {
					flag.wait();
				} catch (InterruptedException e) {
				}
			}
		}
		context.removeFrameworkListener(listener);
		context.ungetService(packageAdminRef);
	}

	public static void uninstallBundle(Bundle target) throws BundleException {
		target.uninstall();
		refreshPackages(null);
	}

	/**
	 * @param bundleName
	 * @throws BundleException 
	 */
	public static void uninstallBundle(String bundleName) throws BundleException {
		BundleContext context = Activator.getDefault().getContext();
		Bundle [] bundles = context.getBundles();
		for (int i = 0; i < bundles.length; i++) {
			if (bundleName.equals(bundles[i].getSymbolicName()))
					uninstallBundle(bundles[i]);
		}
		
	}
}
