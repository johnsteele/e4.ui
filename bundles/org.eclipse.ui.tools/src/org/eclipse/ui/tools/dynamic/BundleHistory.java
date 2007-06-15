/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.tools.dynamic;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.tools.Activator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

/**
 * @since 3.1
 */
public class BundleHistory {

	private class BundleRef extends Action {
		private Bundle bundle;

		private String label;

		private URL url;

		/**
		 * @param label
		 * @param url
		 * @throws MalformedURLException
		 */
		public BundleRef(String label, String url) throws MalformedURLException {
			super("", IAction.AS_CHECK_BOX);
			setLabel(label);
			this.url = new URL(url);
			setText(label);
			setBundle(DynamicTools.getBundle("reference:"
					+ this.url.toExternalForm()));
		}

		/**
		 * @param label
		 */
		private void setLabel(String label) {
			this.label = label;
			setText(label);			
		}

		public BundleRef(URL url) throws BundleException {
			this.url = url;
			install();
			setLabel((String) bundle.getHeaders().get("Bundle-Name"));
		}

		/**
		 * @return
		 */
		public Action getAction() {
			return this;
		}

		public String getLabel() {
			return label;
		}

		public URL getUrl() {
			return url;
		}

		public Bundle install() throws BundleException {
			if (bundle == null) {
				String pluginLocation = "reference:" + url.toExternalForm();
				setBundle(DynamicTools.installBundle(pluginLocation));
			}
			return bundle;
		}
		
		public void run() {
			try {
				if (bundle == null) 
					install();
				else
					uninstall();
			}
			catch (BundleException e) {
				MessageDialog.openError(null, "Problem in run", e.getMessage());
			}
		}

		/**
		 * @param bundle
		 */
		private void setBundle(Bundle bundle) {
			this.bundle = bundle;
			setChecked(bundle != null);
		}

		public void uninstall() throws BundleException {
			if (bundle != null) {
				DynamicTools.uninstallBundle(bundle);
				setBundle(null);
			}
		}
	}

	private final static String PREF = "org.eclipse.ui.internal.dynamic.entries"; //$NON-NLS-1$

	private static BundleHistory singleton;

	public static BundleHistory getInstance() {
		if (singleton == null) {
			singleton = new BundleHistory();
		}
		return singleton;
	}

	private List bundles = new ArrayList();

	/**
	 *  
	 */
	public BundleHistory() {
		super();
		load();
	}

	public void addBundleReference(URL url) throws BundleException {
		BundleRef ref = new BundleRef(url);
		bundles.add(ref);
		save();
	}

	/**
	 * @param menuManager
	 * @return
	 */
	public boolean createMenu(MenuManager menuManager) {
		for (Iterator i = bundles.iterator(); i.hasNext();) {
			BundleRef ref = (BundleRef) i.next();
			menuManager.add(ref.getAction());
		}
		return !bundles.isEmpty();
	}

	/**
	 *  
	 */
	private void load() {
		String bundleString = Activator.getDefault().getPluginPreferences().getString(PREF);
		if (bundleString == null)
			return;

		for (StringTokenizer toker = new StringTokenizer(bundleString, "\n"); toker
				.hasMoreTokens();) {
			String token = toker.nextToken();
			String[] parts = token.split("\t"); //$NON-NLS-1$
			if (parts.length != 2)
				continue;

			try {
				BundleRef ref = new BundleRef(parts[0], parts[1]);
				bundles.add(ref);
			} catch (Exception e) {
				// do nothing ATM
			}
		}
	}

	/**
	 *  
	 */
	private void save() {
		IPreferenceStore store = Activator.getDefault()
				.getPreferenceStore();
		
		StringBuffer buffer = new StringBuffer();
		for (Iterator i = bundles.iterator(); i.hasNext();) {
			BundleRef ref = (BundleRef) i.next();
			buffer.append(ref.getLabel() + "\t" + ref.getUrl().toString());
			buffer.append('\n');
		}
		Activator.getDefault().getPluginPreferences().setValue(PREF, buffer.toString());
	}
}
