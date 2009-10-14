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

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.internal.WorkbenchPage;

/**
 */
public class ModelEditorReference extends ModelReference implements
		IEditorReference {

	/**
	 * @param model
	 * @param page
	 */
	public ModelEditorReference(MPart model, WorkbenchPage page) {
		super(model, page);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorReference#getEditor(boolean)
	 */
	public IEditorPart getEditor(boolean restore) {
		return (IEditorPart) getPart(restore);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorReference#getEditorInput()
	 */
	public IEditorInput getEditorInput() {
		// the "compatibility" way
		Object object = getPart(false);
		if (object instanceof IEditorPart)
			return ((IEditorPart) object).getEditorInput();
		// the E4 way
		return (IEditorInput) getModel().getContext().get(
				IEditorInput.class.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorReference#getFactoryId()
	 */
	public String getFactoryId() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorReference#getName()
	 */
	public String getName() {
		return getModel().getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorReference#isPinned()
	 */
	public boolean isPinned() {
		// TODO Auto-generated method stub
		return false;
	}

}
