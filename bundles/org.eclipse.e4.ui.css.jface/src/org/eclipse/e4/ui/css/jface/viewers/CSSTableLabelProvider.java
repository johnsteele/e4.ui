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

import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

public abstract class CSSTableLabelProvider extends CSSBaseLabelProvider
		implements ITableLabelProvider, ITableColorProvider, ITableFontProvider {

	public CSSTableLabelProvider(CSSEngine engine, TableViewer tableViewer) {
		super(engine, tableViewer);
	}

	public Color getBackground(Object element, int columnIndex) {
		return getColor(element, "background-color", columnIndex);
	}

	public Font getFont(Object element, int columnIndex) {
		return getFont(element, "font", columnIndex);
	}

	public Color getForeground(Object element, int columnIndex) {
		return getColor(element, "color", columnIndex);
	}

}
