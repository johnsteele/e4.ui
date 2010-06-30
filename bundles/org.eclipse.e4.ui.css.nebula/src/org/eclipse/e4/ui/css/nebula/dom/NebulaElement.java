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
package org.eclipse.e4.ui.css.nebula.dom;

import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.dom.WidgetElement;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.swt.widgets.Widget;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NebulaElement extends WidgetElement {

	public NebulaElement(Widget widget, CSSEngine engine) {
		super(widget, engine);
	}

	public NodeList getChildNodes() {
		return super.getChildNodes();
	}

	protected void computeStaticPseudoInstances() {
		Widget widget = getWidget();
		if (widget instanceof GalleryItem) {
			// it's GalleryItem. Set selected as static pseudo instance.
			// because this widget define methods
			// GalleryItem#setSelectionBackground (Color color)
			// which set background Color when a CTabItem is selected.
			super.addStaticPseudoInstance("selected");
		}
		super.computeStaticPseudoInstances();
	}

	public Node getParentNode() {
		Widget widget = getWidget();
		if (widget instanceof GalleryItem) {
			GalleryItem galleryItem = (GalleryItem) widget;
			Widget parent = galleryItem.getParentItem();
			if (parent == null)
				parent = galleryItem.getParent();
			if (parent != null) {
				Element element = getElement(parent);
				return element;
			}
		}
		return super.getParentNode();
	}

	public int getLength() {
		Widget widget = getWidget();
		if (widget instanceof Gallery) {
			Gallery gallery = (Gallery) widget;
			return gallery.getItemCount();
		}
		if (widget instanceof GalleryItem) {
			GalleryItem galleryItem = (GalleryItem) widget;
			return galleryItem.getItemCount();
		}
		return super.getLength();
	}

	public Node item(int index) {
		Widget widget = getWidget();
		if (widget instanceof Gallery) {
			Gallery gallery = (Gallery) widget;
			GalleryItem galleryItem = gallery.getItem(index);
			return getElement(galleryItem);
		}
		if (widget instanceof GalleryItem) {
			GalleryItem galleryItem = (GalleryItem) widget;
			GalleryItem galleryItemChild = galleryItem.getItem(index);
			return getElement(galleryItemChild);
		}
		return super.item(index);
	}

}
