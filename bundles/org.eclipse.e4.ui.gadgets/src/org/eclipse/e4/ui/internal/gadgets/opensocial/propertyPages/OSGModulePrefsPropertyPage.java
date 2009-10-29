/*******************************************************************************
 * Copyright (c) 2009 Sierra Wireless Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Benjamin Cabe, Sierra Wireless - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.ui.internal.gadgets.opensocial.propertyPages;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.ui.internal.gadgets.opensocial.OSGModule;
import org.eclipse.e4.ui.internal.gadgets.opensocial.OSGUserPref;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

public class OSGModulePrefsPropertyPage extends PropertyPage implements
		IWorkbenchPropertyPage {

	private Map<String, Text> preferenceEditors = new HashMap<String, Text>();

	public OSGModulePrefsPropertyPage() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);
		GridData data = new GridData(GridData.FILL);
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);

		OSGModule module = (OSGModule) getElement().getAdapter(OSGModule.class);
		for (OSGUserPref pref : module.getUserPrefs()) {
			addProperty(composite, pref.getName(), pref.getValue());
		}

		return composite;
	}

	@Override
	public boolean performOk() {
		OSGModule module = (OSGModule) getElement().getAdapter(OSGModule.class);
		for (Map.Entry<String, Text> entry : preferenceEditors.entrySet()) {
			module.setUserPrefValue(entry.getKey(), entry.getValue().getText());
		}

		return true;
	}

	@Override
	protected void performDefaults() {
		OSGModule module = (OSGModule) getElement().getAdapter(OSGModule.class);
		for (Map.Entry<String, Text> entry : preferenceEditors.entrySet()) {
			String userPrefDefaultValue = module.getUserPrefDefaultValue(entry
					.getKey());
			entry.getValue().setText(userPrefDefaultValue);
		}
	}

	private void addProperty(Composite parent, String key, String value) {
		Label label = new Label(parent, SWT.NULL);
		label.setText(key);

		Text text = new Text(parent, SWT.BORDER);
		if (value == null)
			value = "";
		text.setText(value);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(gd);
		preferenceEditors.put(key, text);
	}

}
