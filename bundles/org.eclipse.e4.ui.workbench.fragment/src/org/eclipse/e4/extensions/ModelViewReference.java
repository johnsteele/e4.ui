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

package org.eclipse.e4.extensions;

import org.eclipse.e4.ui.model.application.MPart;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.internal.WorkbenchPage;

/**
 * @since 3.3
 * 
 */
public class ModelViewReference extends ModelReference implements
		IViewReference {

	/**
	 * @param model
	 * @param page
	 */
	public ModelViewReference(MPart model, WorkbenchPage page) {
		super(model, page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IViewReference#getSecondaryId()
	 */
	public String getSecondaryId() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IViewReference#getView(boolean)
	 */
	public IViewPart getView(boolean restore) {
		return (IViewPart) getPart(restore);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IViewReference#isFastView()
	 */
	public boolean isFastView() {
		// TODO Auto-generated method stub
		return false;
	}

}
