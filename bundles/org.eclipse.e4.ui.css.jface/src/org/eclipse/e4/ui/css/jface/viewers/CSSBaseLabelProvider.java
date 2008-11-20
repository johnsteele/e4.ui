/*******************************************************************************
 * Copyright (c) 2008 Angelo Zerr and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.ui.css.jface.viewers;

import org.eclipse.e4.ui.css.core.css2.CSS2FontPropertiesHelpers;
import org.eclipse.e4.ui.css.core.dom.properties.css2.CSS2FontProperties;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Widget;
import org.w3c.dom.Element;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;

public class CSSBaseLabelProvider extends LabelProvider {

	protected CSSEngine engine;
	protected StructuredViewer viewer;

	public CSSBaseLabelProvider(CSSEngine engine, StructuredViewer viewer) {
		this.engine = engine;
		this.viewer = viewer;
	}

	protected Font getFont(Object element, String propertyName, int columnIndex) {
		Widget widget = getWidget(element, columnIndex);
		return getFont(widget, propertyName);
	}

	protected Font getFont(Object element, String propertyName) {
		Widget widget = getWidget(element);
		return getFont(widget, propertyName);
	}

	protected Font getFont(Widget widget, String propertyName) {
		CSSValue value = getCSSValue(widget, propertyName);
		if (value == null)
			return null;
		CSS2FontProperties fontProperties = CSS2FontPropertiesHelpers
				.createCSS2FontProperties(value, propertyName);
		try {
			return (Font) engine.convert(fontProperties, Font.class, widget
					.getDisplay());
		} catch (Exception e) {
			engine.handleExceptions(e);
		}
		return null;
	}

	protected Image getImage(Object element, String propertyName,
			int columnIndex) {
		Widget widget = getWidget(element, columnIndex);
		return getImage(widget, propertyName);
	}

	protected Image getImage(Object element, String propertyName) {
		Widget widget = getWidget(element);
		return getImage(widget, propertyName);
	}

	protected Image getImage(Widget widget, String propertyName) {
		return (Image) getResource(widget, propertyName, Image.class);
	}

	protected Color getColor(Object element, String propertyName,
			int columnIndex) {
		Widget widget = getWidget(element, columnIndex);
		return getColor(widget, propertyName);
	}

	protected Color getColor(Object element, String propertyName) {
		Widget widget = getWidget(element);
		return getColor(widget, propertyName);
	}

	protected Color getColor(Widget widget, String propertyName) {
		return (Color) getResource(widget, propertyName, Color.class);
	}

	protected Object getResource(Widget widget, String propertyName,
			Object toType) {
		CSSValue value = getCSSValue(widget, propertyName);
		if (value != null) {
			try {
				return engine.convert(value, toType, widget.getDisplay());
			} catch (Exception e) {
				engine.handleExceptions(e);
			}
		}
		return null;
	}

	protected CSSValue getCSSValue(Widget widget, String propertyName) {
		if (widget == null)
			return null;
		Element elt = engine.getElement(widget);
		if (elt == null)
			return null;
		CSSStyleDeclaration styleDeclaration = engine.getViewCSS()
				.getComputedStyle(elt, null);
		if (styleDeclaration == null)
			return null;

		return styleDeclaration.getPropertyCSSValue(propertyName);
	}

	protected Widget getWidget(Object element) {
		return viewer.testFindItem(element);
	}

	protected Widget getWidget(Object element, int columnIndex) {
//		Widget[] widgets = viewer.testFindItems(element);
//		if (widgets.length > columnIndex)
//			return widgets[columnIndex];
		return null;
	}
}
