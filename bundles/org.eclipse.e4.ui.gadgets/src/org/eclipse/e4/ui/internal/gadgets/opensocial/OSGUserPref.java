/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Boris Bokowski, IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.ui.internal.gadgets.opensocial;

public class OSGUserPref {
	String name;
	String value;
	String defaultValue;

	public OSGUserPref(String name, String defaultValue) {
		this.name = name;
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public String toString() {
		return "OSGUserPref [name=" + name + ", value=" + value
				+ ", defaultValue=" + defaultValue + "]";
	}
	
	
}
