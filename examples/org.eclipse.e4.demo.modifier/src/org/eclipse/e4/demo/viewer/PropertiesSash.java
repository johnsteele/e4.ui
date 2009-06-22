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
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.ui.model.application.ApplicationPackage;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * Basic information for the MStack 
 */
public class PropertiesSash extends PropertiesElement {
	
	private Text policy;
	private Binding policyBinding;
	private Text weights;
	private Binding weightsBinding;
	private Button visible;
	private Binding visibleBinding;
	
	public PropertiesSash(final Composite parent) {
		super(parent);
		policy = createTextControl(comp, "&Policy:", SWT.READ_ONLY);
		weights = createTextControl(comp, "&Weights:", SWT.NONE);
		visible = createButton(comp, "&Visible", SWT.NONE);
		GridData checkboxData = new GridData(SWT.RIGHT, SWT.TOP, false, false, 2, 1);
		visible.setLayoutData(checkboxData);
	}

	public void selected(EObject selected) {
		super.selected(selected);
		policyBinding = bind(policy, ApplicationPackage.Literals.MPART__POLICY);
		weightsBinding = bindList(weights, ApplicationPackage.Literals.MSASH_FORM__WEIGHTS);
		visibleBinding = bind(visible, ApplicationPackage.Literals.MPART__VISIBLE);
	}
	
	protected Binding bindList(Text text, EStructuralFeature feature) {
		IObservableValue observable = EMFObservables.observeValue(selectedObject, feature);
		// NOTE: Do not use SWT.Modify as it might cause StackOverflow
		// when stack is dragged
		return dbc.bindValue(SWTObservables.observeText(text, SWT.FocusOut), observable,
				new UpdateValueStrategy () { // Text to EMF model
					@Override
					public Object convert(Object value) {
						if (!(value instanceof String))
							return super.convert(value);
						
						String[] parts = ((String) value).split(", ");
						EList<Integer> emfList = new BasicEList<Integer>(parts.length); 
						for(String part : parts) {
							try {
								emfList.add(Integer.decode(part));
							} catch (NumberFormatException e) {
								// can't process; pass to superclass
								return super.convert(value);
							}
						}
						return emfList;
					}
				},
				new UpdateValueStrategy () { // EMF model to Text
					@Override
					public Object convert(Object value) {
						if (!(value instanceof EList<?>))
							return super.convert(value);
						StringBuffer result = new StringBuffer();
						for(Object element : (EList<?>) value) {
							if (result.length() != 0)
								result.append(", ");
							result.append(element.toString());
						}
						return result.toString();
					}
				});
	}
	
	
	protected void clearBindings() {
		super.clearBindings();
		if (policyBinding != null) {
			policyBinding.dispose();
			policyBinding = null;
		}
		if (weightsBinding != null) {
			weightsBinding.dispose();
			weightsBinding = null;
		}
		if (visibleBinding != null) {
			visibleBinding.dispose();
			visibleBinding = null;
		}
	}
}
