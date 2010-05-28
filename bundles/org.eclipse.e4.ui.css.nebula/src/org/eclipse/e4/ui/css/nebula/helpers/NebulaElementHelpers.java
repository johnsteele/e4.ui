/*******************************************************************************
 * Copyright (c) 2008, 2009 Angelo Zerr and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.ui.css.nebula.helpers;

import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.nebula.dom.NebulaElement;
import org.eclipse.e4.ui.css.swt.helpers.SWTElementHelpers;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.swt.widgets.Widget;
import org.w3c.dom.Element;

public class NebulaElementHelpers extends SWTElementHelpers {

	/**
	 * Return the w3c Element linked to the SWT widget.
	 * 
	 * @param widget
	 * @return
	 */
	public static Element getElement(Widget widget, CSSEngine engine) {
		try {
			return getElement(widget, engine, NebulaElement.class);
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean isNebulaWidget(Object element) {
		Widget widget = getWidget(element);
		if (widget instanceof GalleryItem)
			return true;
		return false;

	}
}
