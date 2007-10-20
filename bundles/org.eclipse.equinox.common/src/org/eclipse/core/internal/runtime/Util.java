/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl<tom.schindl@bestsolution.at> - initial API and implementation
 ******************************************************************************/

package org.eclipse.core.internal.runtime;

import com.google.gwt.core.client.GWT;

/**
 * @since 3.3
 *
 */
public class Util {

	/**
	 * Extracts the classname from the class
	 *
	 * @param instance
	 *            the instance the classname is extracted for
	 * @return the classname
	 */
	public static String getClassNameAsString(Object instance) {
		return GWT.getTypeName(instance);
	}

	/**
	 * Checks if both classes are the same
	 *
	 * @param a
	 *            the first class
	 * @param b
	 *            the second class
	 * @return <code>true</code> if both classes are the same
	 */
	public static boolean isSameClass(Object a, Object b) {
		return getClassNameAsString(a).equals(getClassNameAsString(b));
	}
}
