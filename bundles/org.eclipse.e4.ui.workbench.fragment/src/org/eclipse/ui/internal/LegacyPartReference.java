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
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;

/**
 * Backward compatibility implementation for IWorkbenchPartReference.
 */
public class LegacyPartReference implements IWorkbenchPartReference {

	protected MContributedPart<?> part;

	/**
	 * Constructor.
	 * 
	 * @param part
	 */
	public LegacyPartReference(MContributedPart<?> part) {
		this.part = part;
	}

	public String getContentDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getId() {
		return part.getId();
	}

	public IWorkbenchPage getPage() {
		// TODO Auto-generated method stub
		return null;
	}

	public IWorkbenchPart getPart(boolean restore) {
		Object object = part.getObject();
		if (object instanceof IWorkbenchPart)
			return (IWorkbenchPart) object;
		return null;
	}

	public String getPartName() {
		return part.getName();
	}

	public String getPartProperty(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	public Image getTitleImage() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTitleToolTip() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	// ------------------------- Listeners ------------------------------------

	public void addPartPropertyListener(IPropertyChangeListener listener) {
		// TODO Auto-generated method stub
	}

	public void removePartPropertyListener(IPropertyChangeListener listener) {
		// TODO Auto-generated method stub
	}

	public void addPropertyListener(IPropertyListener listener) {
		// TODO Auto-generated method stub
	}

	public void removePropertyListener(IPropertyListener listener) {
		// TODO Auto-generated method stub
	}

}
