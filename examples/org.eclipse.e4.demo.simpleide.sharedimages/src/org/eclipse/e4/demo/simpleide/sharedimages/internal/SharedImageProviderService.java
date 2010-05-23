/*******************************************************************************
 * Copyright (c) 2010 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.demo.simpleide.sharedimages.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.demo.simpleide.services.AbstractBundleImageProvider;
import org.eclipse.e4.demo.simpleide.sharedimages.SharedImageKeys;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class SharedImageProviderService extends AbstractBundleImageProvider {
	public static final Bundle BUNDLE = FrameworkUtil.getBundle(SharedImageKeys.class);
	
	private static final Map<String, String> IMAGEMAP = Collections.unmodifiableMap(new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put(SharedImageKeys.IMG_TOOL_HOME_NAV, "/icons/home_nav_enabled.gif");
			put(SharedImageKeys.IMG_TOOL_BACK, "/icons/backward_nav_enabled.gif");
			put(SharedImageKeys.IMG_TOOL_BACK_DISABLED, "/icons/backward_nav_disabled.gif");
			put(SharedImageKeys.IMG_TOOL_FORWARD, "/icons/forward_nav_enabled.gif");
			put(SharedImageKeys.IMG_TOOL_FORWARD_DISABLED, "/icons/forward_nav_disabled.gif");
		}
	});
	

	@Override
	protected Map<String, String> getImageMap() {
		return IMAGEMAP;
	}

	@Override
	protected Bundle getBundle(String imageKey) {
		return BUNDLE;
	}
}