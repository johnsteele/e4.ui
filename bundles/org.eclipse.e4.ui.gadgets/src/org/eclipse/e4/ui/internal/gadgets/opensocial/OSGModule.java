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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OSGModule {
	String title;
	String author;
	String description;
	Map<LocaleKey, OSGLocale> locales = new HashMap<LocaleKey, OSGLocale>();
	List<OSGUserPref> userPrefs = new ArrayList<OSGUserPref>();
	Map<String, OSGContent> contents = new HashMap<String, OSGContent>();

	public OSGModule(String title, String author, String description) {
		this.title = title;
		this.author = author;
		this.description = description;
	}

	public String getAuthor() {
		return author;
	}

	public Map<String, OSGContent> getContents() {
		return contents;
	}

	public String getDescription() {
		return description;
	}

	public String getTitle() {
		return title;
	}

	public List<OSGUserPref> getUserPrefs() {
		return userPrefs;
	}

	public void setUserPrefValue(String key, String value) {
		for (OSGUserPref pref : getUserPrefs()) {
			if (pref.getName().equals(key)) {
				pref.setValue((String) value);
			}
		}
	}

	public String getUserPrefValue(String key) {
		for (OSGUserPref pref : getUserPrefs()) {
			if (pref.getName().equals(key))
				return (pref.getValue() == null) ? "" : pref.getValue();
		}
		return null;
	}

	public String getUserPrefDefaultValue(String key) {
		for (OSGUserPref pref : getUserPrefs()) {
			if (pref.getName().equals(key))
				return (pref.getDefaultValue() == null) ? "" : pref.getValue();
		}
		return null;
	}

}
