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
package org.eclipse.e4.ui.css.nebula.properties.css2;

import org.eclipse.e4.ui.css.core.dom.properties.css2.AbstractCSSPropertyBackgroundHandler;
import org.eclipse.e4.ui.css.core.dom.properties.css2.ICSSPropertyBackgroundHandler;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.nebula.helpers.GalleryHelpers;
import org.eclipse.e4.ui.css.nebula.helpers.NebulaElementHelpers;
import org.eclipse.e4.ui.css.swt.helpers.SWTElementHelpers;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;
import org.w3c.dom.css.CSSValue;

public class CSSPropertyBackgroundNebulaHandler extends
		AbstractCSSPropertyBackgroundHandler {

	public final static ICSSPropertyBackgroundHandler INSTANCE = new CSSPropertyBackgroundNebulaHandler();

	public boolean applyCSSProperty(Object element, String property,
			CSSValue value, String pseudo, CSSEngine engine) throws Exception {
		if (!NebulaElementHelpers.isNebulaWidget(element))
			return false;
		Widget widget = SWTElementHelpers.getWidget(element);
		if (widget != null) {
			super.applyCSSProperty(widget, property, value, pseudo, engine);
			return true;
		}
		return false;
	}

	public String retrieveCSSProperty(Object element, String property,
			String pseudo, CSSEngine engine) throws Exception {
		if (!NebulaElementHelpers.isNebulaWidget(element))
			return null;
		Widget widget = SWTElementHelpers.getWidget(element);
		if (widget != null) {
			return super.retrieveCSSProperty(widget, property, pseudo, engine);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.ui.core.css.dom.properties.css2.AbstractCSSPropertyBackgroundHandler#applyCSSPropertyBackgroundColor(java.lang.Object,
	 *      org.w3c.dom.css.CSSValue, java.lang.String,
	 *      org.eclipse.e4.ui.core.css.engine.CSSEngine)
	 */
	public void applyCSSPropertyBackgroundColor(Object element, CSSValue value,
			String pseudo, CSSEngine engine) throws Exception {
		Widget widget = (Widget) element;
		if (value.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE) {
			Color newColor = (Color) engine.convert(value, Color.class, widget
					.getDisplay());
			if (widget instanceof GalleryItem) {
				GalleryItem galleryItem = (GalleryItem) widget;
				if ("selected".equals(pseudo)) {
					GalleryHelpers.setSelectionBackgroundColor(galleryItem
							.getParent(), newColor);
				} else
					galleryItem.setBackground(newColor);

			} else {
				if (widget instanceof Control)
					((Control) widget).setBackground(newColor);
			}
		}
	}

	public String retrieveCSSPropertyBackgroundAttachment(Object element,
			String pseudo, CSSEngine engine) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String retrieveCSSPropertyBackgroundColor(Object element,
			String pseudo, CSSEngine engine) throws Exception {
		Widget widget = (Widget) element;
		Color color = null;
		if (widget instanceof GalleryItem) {
			GalleryItem galleryItem = (GalleryItem) widget;
			if ("selected".equals(pseudo)) {
				color = GalleryHelpers.getSelectionBackgroundColor(galleryItem
						.getParent());
			} else {
				color = galleryItem.getBackground();
			}
		}
		if (color != null) {
			return engine.convert(color, Color.class, null);
		}
		return null;
	}

	public String retrieveCSSPropertyBackgroundImage(Object element,
			String pseudo, CSSEngine engine) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String retrieveCSSPropertyBackgroundPosition(Object element,
			String pseudo, CSSEngine engine) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String retrieveCSSPropertyBackgroundRepeat(Object element,
			String pseudo, CSSEngine engine) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
