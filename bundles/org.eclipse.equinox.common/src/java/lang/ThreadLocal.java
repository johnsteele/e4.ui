/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package java.lang;

/**
 * Simplistic replacement for ThreadLocal assuming there is only one thread,
 * like in GWT.
 * 
 */
public class ThreadLocal {
	private Object value;

	public void set(Object value) {
		this.value = value;
	}

	public Object get() {
		return value;
	}
}
