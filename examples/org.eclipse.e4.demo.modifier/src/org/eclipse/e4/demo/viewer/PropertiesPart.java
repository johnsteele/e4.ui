/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.demo.viewer;

import org.eclipse.core.databinding.Binding;
import org.eclipse.e4.ui.model.application.ApplicationPackage;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * Basic information for the MContributedPart 
 */
public class PropertiesPart extends PropertiesElement {
	
	private Text name;
	private Binding nameBinding;
	private Text tooltip;
	private Binding tooltipBinding;
	private Text icon;
	private Binding iconBinding;
	private Button visible;
	private Binding visibleBinding;
	
	public PropertiesPart(final Composite parent) {
		super(parent);

		name = createTextControl(comp, "&Name:", SWT.NONE);
		tooltip = createTextControl(comp, "&Tootip:", SWT.NONE);
		icon = createTextControl(comp, "Icon &URI:", SWT.NONE);

		visible = createButton(comp, "&Visible", SWT.NONE);
		GridData checkboxData = new GridData(SWT.RIGHT, SWT.TOP, false, false, 2, 1);
		visible.setLayoutData(checkboxData);
	}

	public void selected(EObject selected) {
		super.selected(selected);
		
		nameBinding = bind(name, ApplicationPackage.Literals.MITEM__NAME);
		tooltipBinding = bind(tooltip, ApplicationPackage.Literals.MITEM__TOOLTIP);
		iconBinding = bind(icon, ApplicationPackage.Literals.MITEM__ICON_URI);
		visibleBinding = bind(visible, ApplicationPackage.Literals.MPART__VISIBLE);
	}
	
	protected void clearBindings() {
		super.clearBindings();

		if (nameBinding != null) {
			nameBinding.dispose();
			nameBinding = null;
		}
		if (tooltipBinding != null) {
			tooltipBinding.dispose();
			tooltipBinding = null;
		}
		if (iconBinding != null) {
			iconBinding.dispose();
			iconBinding = null;
		}
		if (visibleBinding != null) {
			visibleBinding.dispose();
			visibleBinding = null;
		}
	}
}