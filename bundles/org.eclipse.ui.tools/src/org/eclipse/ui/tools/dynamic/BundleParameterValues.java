/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.tools.dynamic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.IParameterValues;
import org.eclipse.ui.tools.dynamic.BundleHistory.BundleRef;

public class BundleParameterValues implements IParameterValues {

	public Map<String, String> getParameterValues() {
		Map<String, String> result = new HashMap<String, String>();
		List<BundleRef> bundles = BundleHistory.getInstance().getBundles();
		for (Iterator<BundleRef> i = bundles.iterator(); i.hasNext();) {
			BundleRef ref = i.next();
			result.put(ref.getLabel(), ref.getLocation());
		}
		
		return result;
	}
}
