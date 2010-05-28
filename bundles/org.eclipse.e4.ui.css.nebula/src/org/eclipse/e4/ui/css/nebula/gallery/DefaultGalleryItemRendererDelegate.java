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
package org.eclipse.e4.ui.css.nebula.gallery;

import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.swt.graphics.Color;

public class DefaultGalleryItemRendererDelegate implements IGalleryItemRenderer {

	private DefaultGalleryItemRenderer itemRenderer;

	public DefaultGalleryItemRendererDelegate(
			DefaultGalleryItemRenderer itemRenderer) {
		this.itemRenderer = itemRenderer;
	}

	public Color getBackgroundColor() {
		return itemRenderer.getBackgroundColor();
	}

	public Color getForegroundColor() {
		return itemRenderer.getForegroundColor();
	}

	public Color getSelectionBackgroundColor() {
		return itemRenderer.getSelectionBackgroundColor();
	}

	public Color getSelectionForegroundColor() {
		return itemRenderer.getSelectionForegroundColor();
	}

	public void setBackgroundColor(Color backgroundColor) {
		itemRenderer.setBackgroundColor(backgroundColor);
	}

	public void setForegroundColor(Color foregroundColor) {
		itemRenderer.setForegroundColor(foregroundColor);
	}

	public void setSelectionBackgroundColor(Color selectionBackgroundColor) {
		itemRenderer.setSelectionBackgroundColor(selectionBackgroundColor);
	}

	public void setSelectionForegroundColor(Color selectionForegroundColor) {
		itemRenderer.setSelectionForegroundColor(selectionForegroundColor);
	}

}
