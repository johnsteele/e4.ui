/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.tools.futureWithMenus.dynamic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.tools.futureWithMenus.dynamic.BundleHistory.BundleRef;

public class BundleListItem extends CompoundContributionItem {

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
			BundleRef ref = (BundleRef) iterator.next();
			Map<String, String> map = new HashMap<String, String>();
			map.put(LoadUnloadHandler.LOCATION, ref.getUrl().toExternalForm());
			CommandContributionItem item = new CommandContributionItem(
					PlatformUI.getWorkbench(), null,
					"org.eclipse.ui.tools.dynamic.bundle", map, null, null,
					null, ref.getLabel(), null, ref.getLabel(),
					CommandContributionItem.STYLE_PUSH);
			items.add(item);
		}
		return items.toArray(new IContributionItem[items.size()]);
	}
}
