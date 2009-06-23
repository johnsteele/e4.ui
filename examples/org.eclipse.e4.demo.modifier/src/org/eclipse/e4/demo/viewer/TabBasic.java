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

import org.eclipse.e4.core.services.annotations.Inject;
import org.eclipse.e4.ui.model.application.MContributedPart;
import org.eclipse.e4.ui.model.application.MSashForm;
import org.eclipse.e4.ui.model.application.MStack;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

// TBD contents of this control is an MStack
// TBD pass in selected item via context

public class TabBasic {
	
	private EObject selectedObject;
    private StackLayout stackLayout;
	private Composite comp;
    
    // Pre-made composites for common element types
    private PropertiesElement elementProperties;
    private PropertiesElement contribItemProperties;
    private PropertiesElement stackProperties;
    private PropertiesElement sashProperties;
	private Composite noSelection;
	
	public TabBasic(final Composite parent) {
		comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout());
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));
		
        stackLayout = new StackLayout();
        stackLayout.marginHeight = 0;
        stackLayout.marginWidth = 0;
        comp.setLayout(stackLayout);
        
        noSelection = new Composite(comp, SWT.NONE);
        noSelection.setLayout(new FillLayout());
		new Label(noSelection, SWT.NONE).setText("No selection");
        
        stackLayout.topControl = noSelection;
        
        elementProperties = new PropertiesElement(comp);
		contribItemProperties = new PropertiesPart(comp);
		stackProperties = new PropertiesStack(comp);
		sashProperties = new PropertiesSash(comp);
	}
	
	@Inject
	public void setInput(final EObject selected) {
		if (selected == selectedObject)
			return;
		selectedObject = selected;
		
		if (selectedObject instanceof MStack) {
			stackLayout.topControl = stackProperties;
			stackProperties.selected(selectedObject);
		} else if (selectedObject instanceof MSashForm<?>) {
			stackLayout.topControl = sashProperties;
			sashProperties.selected(selectedObject);
		} else if (selectedObject instanceof MContributedPart<?>){
			stackLayout.topControl = contribItemProperties;
	        contribItemProperties.selected(selected);
		} else {
			stackLayout.topControl = elementProperties;
			elementProperties.selected(selected);
		}
        comp.layout();
	}

	public void dispose() {
		contribItemProperties.dispose();
		stackProperties.dispose();
		sashProperties.dispose();
	}
}