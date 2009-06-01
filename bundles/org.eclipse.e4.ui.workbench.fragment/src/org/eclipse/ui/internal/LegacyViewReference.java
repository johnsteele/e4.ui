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
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;

/**
 * Backward compatibility implementation for IViewReference.
 */
public class LegacyViewReference extends LegacyPartReference implements
		IViewReference {

	/**
	 * Constructor.
	 * 
	 * @param part
	 */
	public LegacyViewReference(MContributedPart<?> part) {
		super(part);
	}

	public String getSecondaryId() {
		// TODO Auto-generated method stub
		return null;
	}

	public IViewPart getView(boolean restore) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isFastView() {
		// TODO Auto-generated method stub
		return false;
	}

}