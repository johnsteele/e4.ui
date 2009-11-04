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

import java.util.HashMap;
import java.util.Map;

public class OSGLocale {
	String lang;
	String country;
	String languageDirection;
	String messagesURI;
	Map<String, String> messages = new HashMap<String, String>();

	public OSGLocale(String lang, String country, String languageDirection,
			String messagesURI) {
		this.lang = lang;
		this.country = country;
		this.languageDirection = languageDirection;
		this.messagesURI = messagesURI;
	}
}
