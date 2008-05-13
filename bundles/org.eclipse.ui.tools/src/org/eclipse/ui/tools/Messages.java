/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.tools;

import org.eclipse.osgi.util.NLS;

/**
 * @since 3.3
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.ui.tools.messages"; //$NON-NLS-1$
	public static String loadlUnload_selection_location;
	public static String SelectionView_adaptableHeader;
	public static String SelectionView_classHeader;
	public static String SelectionView_toStringHeader;
	public static String showClass_info_title;
	public static String showClass_no_selection;
	public static String showContexts_button;
	public static String showContexts_title;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
