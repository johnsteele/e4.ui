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
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.ui.model.application.MApplicationPackage;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

/**
 * Basic control for model items 
 */
public class PropertiesElement extends Composite {
	
	protected EObject selectedObject;

	protected DataBindingContext dbc;
	
	private Text id;
	private Binding idBinding;
	
	protected FormToolkit toolkit;
	protected ScrolledForm form;
	protected Section right;
	
	protected Composite comp;

	public PropertiesElement(final Composite parent) {
		super(parent, SWT.NONE);
		
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(this);
		form.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));
		form.getBody().setLayout(new GridLayout());
		
		right = toolkit.createSection(form.getBody(), Section.TITLE_BAR
				| Section.EXPANDED);
		right.setText("Properties");
		right.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL));

		comp = toolkit.createComposite(right);
		comp.setLayout(new GridLayout(2, false));
		comp.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));
		right.setClient(comp);

		dbc = new DataBindingContext();
		
		id = createTextControl(comp, "&ID:", SWT.NONE);
		
		GridLayoutFactory.fillDefaults().generateLayout(this);
	}
	
	/**
	 * Passing the focus request to the form.
	 */
	public boolean setFocus() {
		boolean result = super.setFocus();
		form.setFocus();
		return result;
	}

	protected Text createTextControl(Composite parent, String labelText, int style) {
		
		Label label = toolkit.createLabel(parent, labelText);
		label.setLayoutData(new GridData());
		Text result = toolkit.createText(parent, "", style);
		result.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
		return result;
	}
	
	protected Button createButton(Composite parent, String labelText, int style) {
		Button result = toolkit.createButton(parent, labelText, SWT.CHECK | style);
		return result;
	}
	
	protected Binding bind(Text text, EStructuralFeature feature) {
		if (selectedObject == null)
			return null;
		IObservableValue observable = EMFObservables.observeValue(selectedObject, feature);
		return dbc.bindValue(SWTObservables.observeText(text, SWT.Modify), observable, null, null);
	}

	protected Binding bind(Button button, EStructuralFeature feature) {
		if (selectedObject == null)
			return null;
		IObservableValue observable = EMFObservables.observeValue(selectedObject, feature);
		return dbc.bindValue(SWTObservables.observeSelection(button), observable, null, null);
	}
	
	public void selected(EObject selected) {
		if (selected == selectedObject)
			return;
		clearBindings();
		selectedObject = selected;
		
		idBinding = bind(id, MApplicationPackage.Literals.APPLICATION_ELEMENT__ID);
	}
	
	protected void clearBindings() {
		if (idBinding != null) {
			idBinding.dispose();
			idBinding = null;
		}
	}
	
	public void dispose() {
		clearBindings();
		toolkit.dispose();
	}
	
}