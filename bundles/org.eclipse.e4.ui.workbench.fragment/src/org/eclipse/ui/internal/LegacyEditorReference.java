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

package org.eclipse.ui.internal;

import org.eclipse.e4.ui.model.application.MContributedPart;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;

/**
 * Backward compatibility implementation for IViewReference.
 */
public class LegacyEditorReference extends LegacyPartReference implements
		IEditorReference {

	/**
	 * Constructor.
	 * 
	 * @param part
	 */
	public LegacyEditorReference(MContributedPart<?> part) {
		super(part);
	}

	public IEditorPart getEditor(boolean restore) {
		Object object = part.getObject();
		if (object instanceof IEditorPart)
			return (IEditorPart) object;
		return null;
	}

	public IEditorInput getEditorInput() {
		Object object = part.getObject();
		if (object instanceof IEditorPart)
			return ((IEditorPart) object).getEditorInput();
		return null;
	}

	public String getFactoryId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isPinned() {
		// TODO Auto-generated method stub
		return false;
	}
}
