/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.e4.ui.selection.internal;

import javax.inject.Inject;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.ui.selection.ESelectionService;

/**
 *
 */
public class SelectionServiceImpl implements ESelectionService {

	static final String OUT_SELECTION = "output.selection"; //$NON-NLS-1$

	private IEclipseContext context;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.ui.selection.ESelectionService#setSelection(java.lang.Object)
	 */
	public void setSelection(Object selection) {
		if (selection != null) {
			context.set(OUT_SELECTION, selection);
		} else {
			context.remove(OUT_SELECTION);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.ui.selection.ESelectionService#getSelection()
	 */
	public Object getSelection() {
		return context.get(ESelectionService.SELECTION);
	}

	@Inject
	public void setContext(IEclipseContext c) {
		context = c;
	}

}
