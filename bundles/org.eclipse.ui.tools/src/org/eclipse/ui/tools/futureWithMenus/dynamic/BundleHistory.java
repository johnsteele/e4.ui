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
package org.eclipse.ui.tools.futureWithMenus.dynamic;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.ui.tools.Activator;

/**
 * @since 3.1
 */
public class BundleHistory {

	public static class BundleRef {
		private String label;

		private URL url;

		/**
		 * @param label
		 * @param url
		 * @throws MalformedURLException
		 */
		public BundleRef(String label, String url) throws MalformedURLException {
			this.label = label;
			this.url = new URL(url); 
		}

		public String getLabel() {
			return label;
		}

		public URL getUrl() {
			return url;
		}
	}

	private final static String PREF = "org.eclipse.ui.tools.dynamic.entries"; //$NON-NLS-1$

	private static BundleHistory singleton;

	public static BundleHistory getInstance() {
		if (singleton == null) {
			singleton = new BundleHistory();
		}
		return singleton;
	}

	private List<BundleRef> bundles = new ArrayList<BundleRef>();

	/**
	 *  
	 */
	public BundleHistory() {
		super();
		load();
	}

	public List<BundleRef> getBundles() {
		return Collections.unmodifiableList(bundles);
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

			addBundleReference(parts[1], parts[0]);
		}
	}

	/**
	 *  
	 */
	public void save() {
		StringBuffer buffer = new StringBuffer();
		for (Iterator<BundleRef> i = bundles.iterator(); i.hasNext();) {
			BundleRef ref = i.next();
			buffer.append(ref.getLabel() + "\t" + ref.getUrl().toString());
			buffer.append('\n');
		}
		Activator.getDefault().getPluginPreferences().setValue(PREF, buffer.toString());
	}

	public void addBundleReference(String symbolicName, String location) {
		try {
			BundleRef ref = new BundleRef(symbolicName, location);
			bundles.add(ref);
		} catch (Exception e) {
			// do nothing ATM
		}
		
	}
}
