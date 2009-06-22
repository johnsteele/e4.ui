/*******************************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.demo.viewer;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

// TBD this view is added in a 3.x way: plugin.xml + ViewPart.
// It would be better to provide complete E4 implementation of it
// and remove dependency on the "org.eclipse.ui.workbench".

/*
 * NOTE: this is work-in-progress, don't take those notes too serious. Including this note.
 *  
 * Things that need to be clarified:
 * 1) What is the E4 way for the plug-in to contribute a view 
 * 2) Notifications of model events. It would be nice to have an analog of IPartService;
 * otherwise everybody would have to start adding global listeners
 * 2b) "Global" listeners (extends EContentAdapter) are likely to produce too much overhead. Their
 * use somehow should be restricted to the UI itself.   
 * 2c) EMF notification code generation: calls to the listeners should be wrapped into SafeRunnable
 * 3) Need an API to find currently active leaf, currently active path, tree of active children
 * 3b) Currently "global" listeners get a series of activation events: leaf activates, then its parent,
 * then grandparent, and so on. 
 * 4) Need a standard way of getting a specific model element in the model: 
 * String MPart.toPath() <-> Model.findElement(path)
 * otherwise everybody will be writing tree walkers. XPath?
 * 4b) Derivative of (4): all model elements must have IDs unique for the branch. Even better
 * would be unique ID for the model, when we don't have to walk the tree every time we need 
 * a model element.
 * 5) There should be an API to activate MPart. [Currently PartFactory#activate().]
 * 6) The link between MWorkbenchWindow and MApplication is done differently from other elements.
 * (Not #getParent().) While I understand why it is this way in the current model, it breaks the rhyme. 
 * 
 */

// TBD convert this into E4 elements

public class ModelObserverContainer extends ViewPart {
	
	private TabAdvanced propertiesArea;
	private TabBasic basicArea;
	private ModelExplorer modelViewer;
	
	private ImageManagerHelper imageHelper;

	public void createPartControl(Composite parent) {
		imageHelper = new ImageManagerHelper();
		
		Composite composite = new Composite(parent, SWT.BORDER);
		composite.setLayout(new GridLayout(2, true));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		modelViewer = new ModelExplorer(composite, this);
		
		CTabFolder folder = new CTabFolder(composite, SWT.BOTTOM | SWT.BORDER);

		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.horizontalSpan = 1;
		folder.setLayoutData(gd);
		
		CTabItem basicTab = new CTabItem(folder, 0);
		basicTab.setText("Basic");
		Composite basicPage = new Composite(folder, SWT.NONE);
		basicPage.setLayout(new GridLayout());
		basicPage.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));
		basicTab.setControl(basicPage);
		
		CTabItem advancedTab = new CTabItem(folder, 1);
		advancedTab.setText("Advanced");
		Composite advancedPage = new Composite(folder, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		advancedPage.setLayout(layout);
		advancedPage.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));
		advancedTab.setControl(advancedPage);
		
		basicArea = new TabBasic(basicPage);
		propertiesArea = new TabAdvanced(advancedPage);
		
		folder.setSelection(0);
	}
	
	public void setFocus() {
		// TBD anything to do?
	}

	public void dispose() {
		if (propertiesArea != null)
			propertiesArea.dispose();
		if (basicArea != null)
			basicArea.dispose();
		if (imageHelper != null)
			imageHelper.dispose();
		if (modelViewer != null)
			modelViewer.dispose();
		super.dispose();
	}
	
	// TBD use context to propagate changes in the selected element
	public void selected(EObject object) {
		if (propertiesArea != null)
			propertiesArea.selected(object);
		if (basicArea != null)
			basicArea.selected(object);
	}

	
}