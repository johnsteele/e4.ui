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

import java.util.Iterator;

import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.MItem;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.model.application.MPartSashContainer;
import org.eclipse.e4.ui.model.application.MPartStack;
import org.eclipse.e4.ui.model.application.MPerspective;
import org.eclipse.e4.ui.model.application.MUIElement;
import org.eclipse.e4.ui.model.application.MWindow;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;

/**
 * Tree displaying contents of the E4 model.
 */
public class ModelExplorer {
	
	private IEclipseContext context;
	private TreeViewer viewer;
	
	MApplicationElement rootElement;
	
	private ImageManagerHelper imageHelper;
	
	private DragSourceListener dragListener = new DragSourceListener() {
		public void dragFinished(DragSourceEvent event) {
		}
		public void dragSetData(DragSourceEvent event) {
//			IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
//			System.out.println("count: " + selection.size());
		}
		public void dragStart(DragSourceEvent event) {
			IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
			if (selection.size() != 1) {
				event.doit = false;
			}
			else {
				event.doit = selection.getFirstElement() instanceof MPart; 
			}
		}
	};

	private DropTargetListener dropListener = new DropTargetListener() {
		public void dragEnter(DropTargetEvent event) {
		}
		public void dragOver(DropTargetEvent event) {
			event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL
					| DND.FEEDBACK_EXPAND;

			Widget curItem = event.item;
			if (curItem != null) {
				Object curElement = curItem.getData();
				if (curElement instanceof MPartStack) {
					event.detail = DND.DROP_MOVE;
				}
				else {
					event.detail = DND.DROP_NONE;
				}
			}
		}
		
		public void dragOperationChanged(DropTargetEvent event) {
			if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
				if (event.detail != DND.DROP_COPY
					&& event.detail != DND.DROP_MOVE) {
					event.detail = DND.DROP_NONE;
				}
			}
		}

		public void drop(DropTargetEvent event) {
			if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
				MPartStack sm = (MPartStack) event.item.getData();
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				MPart view = (MPart) selection.getFirstElement();
				sm.getChildren().add(view);
				ModelUtils.activate(view);
				viewer.refresh();
			}
		}
		public void dragLeave(DropTargetEvent event) {
		}
		public void dropAccept(DropTargetEvent event) {
		}
	};
	
	private ITreeContentProvider contentProvider = new ITreeContentProvider() {	
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
		
		public void dispose() {
		}

		public Object[] getChildren(Object parentElement) {
			return null;
		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object element) {
			return false;
		}

		public Object[] getElements(Object inputElement) {
			return null;
		}
	};
	 
	class ViewLabelProvider implements ILabelProvider {

		public Image getImage(Object element) {
			if (element instanceof MItem) {
				String iconPath = ((MItem) element).getIconURI();
				return imageHelper.getImage(iconPath);
			}
	
			return imageHelper.getElementImage(element);
		}

		public String getText(Object element) {
			if (element instanceof MItem)
				return ((MItem)element).getName();
			if (element instanceof MPartStack)
				return "Stack: \"" + ((MPartStack) element).getId() + "\"";
			if (element instanceof MPartSashContainer)
				return "Sash: \"" + ((MPartSashContainer) element).getId() + "\"";
			if (element instanceof MPerspective)
				return "Perspective \"" + ((MPerspective) element).getId() + "\""; 
			if (element instanceof MWindow)
				return "Workbench Window"; 
			return element.getClass().getSimpleName();
		}

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}
	}

	public ModelExplorer(Composite parent) {
		imageHelper = new ImageManagerHelper();
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);
		GridData data = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		composite.setLayoutData(data);
		
		// add some space around the label
		Label label = new Label(composite, SWT.NONE);
		label.setText("&E4 Model");
		GridData dataLabel = new GridData();
		dataLabel.horizontalIndent = 5;
		dataLabel.verticalIndent = 5;
		
		viewer = new TreeViewer(composite);
		viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(new ViewLabelProvider());
		
		viewer.getControl().setData("viewpart", this);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				handleSelection(event);
			}
		});
		
		// Add DnD support
		int ops = DND.DROP_COPY | DND.DROP_MOVE; 
		Transfer[] xFers = { LocalSelectionTransfer.getTransfer() };
		viewer.addDragSupport(ops, xFers, dragListener);
		viewer.addDropSupport(ops, xFers, dropListener);
		
		// TBD this really need to be replaced by an API 
		// static MApplication getApplication() 
		// on something
		MUIElement element = ModelUtils.getElement(parent);
		EObject topObject = ModelUtils.topObject(element);
		viewer.setInput(topObject);
		
		// double-clicks activate the selected part
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				TreeSelection selection = (TreeSelection) event.getSelection();
				MPart selected = (MPart) selection.getFirstElement();
				// TBD there should be an API to activate MPart 
				ModelUtils.activate(selected);
			}});
		GridLayoutFactory.fillDefaults().generateLayout(parent);
	}
	
	protected void handleSelection(SelectionChangedEvent event) {
		if (event.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection sel = (IStructuredSelection) event.getSelection();
			Object selObj = sel.getFirstElement();
			if (selObj instanceof EObject)
				context.modify(IServiceConstants.SELECTION, selObj);
		}
	}

	public String[] getPropIds(EObject selObj) {
		if (selObj == null)
			return new String[0];
		
		EList<EStructuralFeature> features = selObj.eClass().getEAllStructuralFeatures();
		String[] ids = new String[features.size()];
		int count = 0;
		for (Iterator iterator = features.iterator(); iterator.hasNext();) {
			EStructuralFeature structuralFeature = (EStructuralFeature) iterator
					.next();
			String featureName = structuralFeature.getName();
			ids[count++] = featureName;
		}
		
		return ids;
	}
	
	public Object getProperty(EObject eObj, String id) {
		if (eObj == null)
			return null;
		
	    EStructuralFeature eFeature = eObj.eClass().getEStructuralFeature(id);
	    if (eFeature == null)
	    	return null;
	    
		return eObj.eGet(eFeature);
	}
	
	public void dispose() {
		// TBD any cleanup?
	}
	
	public void contextSet(IEclipseContext context) {
		this.context = context;
	}

}