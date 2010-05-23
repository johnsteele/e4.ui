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
package org.eclipse.e4.demo.simpleide.sharedimages;

import org.eclipse.e4.demo.simpleide.sharedimages.internal.SharedImageProviderService;

public class SharedImageKeys {
	private static final String BUNDLE_ID = SharedImageProviderService.BUNDLE.getSymbolicName();
	
	/**
     * Identifies the home image in the enabled state.
     * @since 3.4
     */
	public final static String IMG_TOOL_HOME_NAV = BUNDLE_ID + ".IMG_ETOOL_HOME_NAV"; //$NON-NLS-1$
    
	/**
     * Identifies the back image in the enabled state.
     */
    public final static String IMG_TOOL_BACK = BUNDLE_ID + ".IMG_TOOL_BACK"; //$NON-NLS-1$

    /**
     * Identifies the back image in the disabled state.
     */
    public final static String IMG_TOOL_BACK_DISABLED = BUNDLE_ID + ".IMG_TOOL_BACK_DISABLED"; //$NON-NLS-1$

    /**
     * Identifies the forward image in the enabled state.
     */
    public final static String IMG_TOOL_FORWARD = BUNDLE_ID + ".IMG_TOOL_FORWARD"; //$NON-NLS-1$
    
    /**
     * Identifies the forward image in the disabled state.
     */
    public final static String IMG_TOOL_FORWARD_DISABLED = BUNDLE_ID + ".IMG_TOOL_FORWARD_DISABLED"; //$NON-NLS-1$

}
