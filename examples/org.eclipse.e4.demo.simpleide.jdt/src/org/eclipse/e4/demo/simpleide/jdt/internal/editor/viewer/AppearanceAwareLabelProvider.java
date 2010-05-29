/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Guven Demir <guven.internet+eclipse@gmail.com> - [package explorer] Alternative package name shortening: abbreviation - https://bugs.eclipse.org/bugs/show_bug.cgi?id=299514
 *******************************************************************************/
package org.eclipse.e4.demo.simpleide.jdt.internal.editor.viewer;

import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.demo.simpleide.jdt.internal.JavaUIMessages;

public class AppearanceAwareLabelProvider extends JavaUILabelProvider {
	public final static long DEFAULT_TEXTFLAGS = JavaElementLabels.ROOT_VARIABLE
			| JavaElementLabels.T_TYPE_PARAMETERS
			| JavaElementLabels.M_PARAMETER_TYPES
			| JavaElementLabels.M_APP_TYPE_PARAMETERS
			| JavaElementLabels.M_APP_RETURNTYPE
			| JavaElementLabels.REFERENCED_ROOT_POST_QUALIFIED;
	public final static int DEFAULT_IMAGEFLAGS= JavaElementImageProvider.OVERLAY_ICONS;
	
//	private long fTextFlagMask;
	
	public AppearanceAwareLabelProvider(long textFlags, int imageFlags, Logger logger,
			JavaUIMessages messages) {
		super(textFlags, imageFlags, logger, messages);
//		initMasks();
	}

	/**
	 * Creates a labelProvider with DEFAULT_TEXTFLAGS and DEFAULT_IMAGEFLAGS
	 */
	public AppearanceAwareLabelProvider(Logger logger, JavaUIMessages messages) {
		this(DEFAULT_TEXTFLAGS, DEFAULT_IMAGEFLAGS, logger, messages);
	}
	
//	private void initMasks() {
//		IPreferenceStore store= PreferenceConstants.getPreferenceStore();
//		fTextFlagMask= -1;
//		if (!store.getBoolean(PreferenceConstants.APPEARANCE_METHOD_RETURNTYPE)) {
//			fTextFlagMask ^= JavaElementLabels.M_APP_RETURNTYPE;
//		}
//		if (!store.getBoolean(PreferenceConstants.APPEARANCE_METHOD_TYPEPARAMETERS)) {
//			fTextFlagMask ^= JavaElementLabels.M_APP_TYPE_PARAMETERS;
//		}
//		if (!(store.getBoolean(PreferenceConstants.APPEARANCE_COMPRESS_PACKAGE_NAMES)
//				|| store.getBoolean(JavaElementLabelComposer.APPEARANCE_ABBREVIATE_PACKAGE_NAMES))) {
//			fTextFlagMask ^= JavaElementLabels.P_COMPRESSED;
//		}
//		if (!store.getBoolean(PreferenceConstants.APPEARANCE_CATEGORY)) {
//			fTextFlagMask ^= JavaElementLabels.ALL_CATEGORY;
//		}
//
//		fImageFlagMask= -1;
//	}
}
