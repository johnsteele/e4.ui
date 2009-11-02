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

import org.eclipse.e4.ui.internal.gadgets.opensocial.OSGModule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

public class MainPropertyPage extends PropertyPage implements
		IWorkbenchPropertyPage {

	public MainPropertyPage() {
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
		addProperty(composite, "Title:", module.getTitle());
		addProperty(composite, "Author:", module.getAuthor());
		addProperty(composite, "Description:", module.getDescription());

		return composite;
	}

	private void addProperty(Composite parent, String name, String value) {
		Label label = new Label(parent, SWT.NULL);
		label.setText(name);

		Text text = new Text(parent, SWT.READ_ONLY);
		if (value == null)
			value = "";
		text.setText(value.trim());
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(gd);
	}

}
