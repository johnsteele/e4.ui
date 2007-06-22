/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.sound;

/**
 * @since 1.0
 */
public interface ISoundManager {

	/**
	 * 
	 * @param id
	 * @return
	 */
	public ISound getSound(String id);

	/**
	 * @return
	 */
	ISound[] getSounds();
}
