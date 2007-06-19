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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.tools.dynamic.BundleHistory.BundleRef;

public class BundleListItem extends CompoundContributionItem {

	private static final String BUNDLE_COMMAND_ID = "org.eclipse.ui.tools.dynamic.bundle"; //$NON-NLS-1$

	public BundleListItem() {
	}

	public BundleListItem(String id) {
		super(id);
	}

	@Override
	protected IContributionItem[] getContributionItems() {
		List<BundleRef> bundles = BundleHistory.getInstance().getBundles();
		List<IContributionItem> items = new ArrayList<IContributionItem>(
				bundles.size());
		for (Iterator<BundleRef> iterator = bundles.iterator(); iterator
				.hasNext();) {
			BundleRef ref = iterator.next();
			Map<String, String> map = new HashMap<String, String>();
			map.put(LoadUnloadHandler.LOCATION, ref.getLocation());
			CommandContributionItem item = new CommandContributionItem(
					PlatformUI.getWorkbench(), null,
					BUNDLE_COMMAND_ID, map, null, null,
					null, ref.getLabel(), null, ref.getLabel(),
					CommandContributionItem.STYLE_CHECK);
			items.add(item);
		}
		return items.toArray(new IContributionItem[items.size()]);
	}
}
