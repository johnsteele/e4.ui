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

public class OSGContent {
	String type;
	String view;
	String href;
	String value;

	public OSGContent(String type, String view, String href) {
		this.type = type;
		this.view = view == null ? "" : view;
		this.href = href;
	}

	public String getType() {
		return type;
	}

	public String getView() {
		return view;
	}

	public String getHref() {
		return href;
	}

	public String getValue() {
		return value;
	}
}
