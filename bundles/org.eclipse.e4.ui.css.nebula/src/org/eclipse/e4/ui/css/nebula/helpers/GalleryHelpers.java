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

import org.eclipse.e4.ui.css.nebula.gallery.DefaultGalleryItemRendererDelegate;
import org.eclipse.e4.ui.css.nebula.gallery.IGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.AbstractGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.swt.graphics.Color;

public class GalleryHelpers {

	private static final String ITEM_RENDERER_KEY = "org.eclipse.e4.ui.css.nebula.helpers.ITEM_RENDERER_KEY";

	public static AbstractGalleryItemRenderer getItemRenderer(
			GalleryItem galleryItem) {
		return getItemRenderer(galleryItem.getParent());
	}

	public static AbstractGalleryItemRenderer getItemRenderer(Gallery gallery) {

		return gallery.getItemRenderer();
	}

	public static Color getSelectionBackgroundColor(Gallery gallery) {
		IGalleryItemRenderer itemRenderer = getGalleryItemRenderer(gallery);
		if (itemRenderer != null) {
			return itemRenderer.getSelectionBackgroundColor();
		}
		return null;
	}

	public static void setSelectionBackgroundColor(Gallery gallery, Color color) {
		IGalleryItemRenderer itemRenderer = getGalleryItemRenderer(gallery);
		if (itemRenderer != null) {
			itemRenderer.setSelectionBackgroundColor(color);
		}
	}

	public static Color getSelectionForegroundColor(Gallery gallery) {
		IGalleryItemRenderer itemRenderer = getGalleryItemRenderer(gallery);
		if (itemRenderer != null) {
			return itemRenderer.getSelectionForegroundColor();
		}
		return null;
	}

	public static void setSelectionForegroundColor(Gallery gallery, Color color) {
		IGalleryItemRenderer itemRenderer = getGalleryItemRenderer(gallery);
		if (itemRenderer != null) {
			itemRenderer.setSelectionForegroundColor(color);
		}
	}

	public static IGalleryItemRenderer getGalleryItemRenderer(Gallery gallery) {
		AbstractGalleryItemRenderer itemRenderer = gallery.getItemRenderer();
		if (itemRenderer instanceof IGalleryItemRenderer)
			return (IGalleryItemRenderer) itemRenderer;
		IGalleryItemRenderer galleryItemRenderer = (IGalleryItemRenderer) gallery
				.getData(ITEM_RENDERER_KEY);
		if (galleryItemRenderer != null)
			return galleryItemRenderer;
		// DefaultGalleryItemRenderer
		if (itemRenderer instanceof DefaultGalleryItemRenderer) {
			galleryItemRenderer = new DefaultGalleryItemRendererDelegate(
					(DefaultGalleryItemRenderer) itemRenderer);			
		}
		// DefaultGalleryItemRenderer
		if (itemRenderer instanceof DefaultGalleryItemRenderer) {
			galleryItemRenderer = new DefaultGalleryItemRendererDelegate(
					(DefaultGalleryItemRenderer) itemRenderer);			
		}
		if (galleryItemRenderer != null)
			gallery.setData(ITEM_RENDERER_KEY, galleryItemRenderer);
		return galleryItemRenderer;
	}
}
