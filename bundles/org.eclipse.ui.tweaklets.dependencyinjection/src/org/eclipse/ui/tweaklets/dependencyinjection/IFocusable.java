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
package org.eclipse.ui.tweaklets.dependencyinjection;

/**
 * Parts can implement or adapt to this interface if they wish to provide custom
 * focus behavior.
 * 
 * @since 3.4
 */
public interface IFocusable {

	/**
	 * Causes the receiver to have the <em>keyboard focus</em>, such that all
	 * keyboard events will be delivered to it. Implementers may return false to
	 * indicate that they do not want to provide non-default keyboard focus
	 * behavior.
	 * 
	 * @return <code>true</code> if the receiver got focus, and
	 *         <code>false</code> if it was unable to.
	 */
	public boolean setFocus();
}
