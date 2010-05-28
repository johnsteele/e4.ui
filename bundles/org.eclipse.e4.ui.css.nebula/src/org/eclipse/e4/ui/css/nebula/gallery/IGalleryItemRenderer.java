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

import org.eclipse.swt.graphics.Color;

public interface IGalleryItemRenderer {

	public Color getForegroundColor();

	public void setForegroundColor(Color foregroundColor);

	public Color getSelectionForegroundColor();

	public void setSelectionForegroundColor(Color selectionForegroundColor);

	public Color getSelectionBackgroundColor();

	public void setSelectionBackgroundColor(Color selectionBackgroundColor);

	public Color getBackgroundColor();

	public void setBackgroundColor(Color backgroundColor);
}
