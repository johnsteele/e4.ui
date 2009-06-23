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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.ComputedList;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.MultiList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.e4.ui.model.application.ApplicationPackage;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MContributedPart;
import org.eclipse.e4.ui.model.application.MHandledItem;
import org.eclipse.e4.ui.model.application.MItemContainer;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.model.application.MSashForm;
import org.eclipse.e4.ui.model.application.MStack;
import org.eclipse.e4.ui.model.workbench.MPerspective;
import org.eclipse.e4.ui.model.workbench.MWorkbenchWindow;
import org.eclipse.e4.workbench.ui.renderers.PartFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.TreeStructureAdvisor;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
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

// TBD labels need to be updated on model element updates, 
// such as changing name from the property tabs

public class ModelExplorer {
	
	private ModelObserverContainer editor;
	private TreeViewer viewer;
	
	private ImageManagerHelper imageHelper;
	
	private DragSourceListener dragListener = new DragSourceListener() {
		public void dragFinished(DragSourceEvent event) {
		}
		public void dragSetData(DragSourceEvent event) {
			IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
			System.out.println("count: " + selection.size());
		}
		public void dragStart(DragSourceEvent event) {
			IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
			if (selection.size() != 1) {
				event.doit = false;
			}
			else {
				event.doit = selection.getFirstElement() instanceof MContributedPart<?>; 
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
				if (curElement instanceof MStack) {
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
				MStack sm = (MStack) event.item.getData();
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				MContributedPart<?> view = (MContributedPart<?>) selection.getFirstElement();
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
	 
	class ViewLabelProvider extends ObservableMapLabelProvider {

		public ViewLabelProvider(IObservableMap[] attributeMaps) {
			super(attributeMaps);
		}

		@Override
		public Image getImage(Object element) {
			if (element instanceof MContributedPart<?>) {
				String iconPath = ((MContributedPart<?>) element).getIconURI();
				return imageHelper.getImage(iconPath);
			} else if (element instanceof MWorkbenchWindow)
				return imageHelper.getImage(ImageManagerHelper.IMAGE_WORKBENCH_WINDOW);
			else if (element instanceof MStack)
				return imageHelper.getImage(ImageManagerHelper.IMAGE_STACK);
			else if ((element instanceof MPerspective<?>) ||(element instanceof MSashForm<?>))
				return imageHelper.getImage(ImageManagerHelper.IMAGE_SASH);
            return null;
		}

		@Override
		public String getText(Object element) {
			if (element instanceof MContributedPart<?>)
				return ((MContributedPart<?>)element).getName();
			if (element instanceof MStack)
				return "Stack: \"" + ((MStack) element).getId() + "\"";
			if (element instanceof MSashForm<?>)
				return "Sash: \"" + ((MSashForm<?>) element).getId() + "\"";
			if (element instanceof MPerspective<?>)
				return "Perspective \"" + ((MPerspective<?>) element).getId() + "\""; 
			if (element instanceof MWorkbenchWindow)
				return "Workbench Window"; 
			return element.getClass().getSimpleName();
		}
	}

	public ModelExplorer(Composite parent, ModelObserverContainer editor) {
		this.editor = editor;
		imageHelper = new ImageManagerHelper();
		// Access services...
		// XXX this is bad
		MPart<?> element = (MPart<?>) parent.getParent().getData(PartFactory.OWNING_ME);
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);
		GridData data = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		composite.setLayoutData(data);
		
		new Label(composite, SWT.NONE).setText("&E4 Model");
		
		viewer = new TreeViewer(composite);
		viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		final Realm realm = Realm.getDefault();
		IObservableFactory listFactory = new IObservableFactory() {
			public IObservable createObservable(Object element) {
				if (element instanceof EObject) {
					EObject e = (EObject) element;
					if (e instanceof MPart<?>) {
						IObservableList listChildren = EMFObservables.observeList(e, ApplicationPackage.Literals.MPART__CHILDREN);
						
						// There is 0..1 menus. Need a wrapper in case there is no menu:
						final IObservableValue listMenu = EMFObservables.observeValue(e, ApplicationPackage.Literals.MPART__MENU);
						IObservableList computedMenu = new ComputedList() {
							@Override 
							protected List<?> calculate() {
								Object value = listMenu.getValue();
								return value == null ? Collections.EMPTY_LIST
										: Collections.singletonList(value);
							}
						};
						
						// There is 0..1 toolbar. Need a wrapper in case there is no toolbar:
						final IObservableValue listToolbar = EMFObservables.observeValue(e, ApplicationPackage.Literals.MPART__TOOL_BAR);
						IObservableList computedToolbar = new ComputedList() {
							@Override 
							protected List<?> calculate() {
								Object value = listToolbar.getValue();
								return value == null ? Collections.EMPTY_LIST
										: Collections.singletonList(value);
							}
						};
						
						IObservableList listHandler = EMFObservables.observeList(e, ApplicationPackage.Literals.MPART__HANDLERS);
						return new MultiList(new IObservableList[] { listChildren, computedMenu, computedToolbar, listHandler }); //, listToolbar, /*listMenu, */listHandler });
					} else if (e instanceof MHandledItem) {
						// There is 0..1 commands. Need a wrapper in case there is no command:
						final IObservableValue listCommands = EMFObservables.observeValue(e, ApplicationPackage.Literals.MHANDLED_ITEM__COMMAND);
						IObservableList computedCommands = new ComputedList() {
							@Override 
							protected List<?> calculate() {
								Object value = listCommands.getValue();
								return value == null ? Collections.EMPTY_LIST
										: Collections.singletonList(value);
							}
						};
						// There is 0..1 menus. Need a wrapper in case there is no menu:
						final IObservableValue listMenus = EMFObservables.observeValue(e, ApplicationPackage.Literals.MHANDLED_ITEM__MENU);
						IObservableList computedMenus = new ComputedList() {
							@Override 
							protected List<?> calculate() {
								Object value = listMenus.getValue();
								return value == null ? Collections.EMPTY_LIST
										: Collections.singletonList(value);
							}
						};
						return new MultiList(new IObservableList[] { computedCommands, computedMenus });
					} else if (e instanceof MApplication<?> ) {
						IObservableList listWindows = EMFObservables.observeList(e, ApplicationPackage.Literals.MAPPLICATION__WINDOWS);
						IObservableList listCommands = EMFObservables.observeList(e, ApplicationPackage.Literals.MAPPLICATION__COMMAND);
						return new MultiList(new IObservableList[] { listWindows, listCommands });
					} else if (e instanceof MItemContainer<?>) { // this includes toolbars
						IObservableList listItems = EMFObservables.observeList(e, ApplicationPackage.Literals.MITEM_CONTAINER__ITEMS);
						return listItems;
					}
				} else if (element instanceof Object[]) {
					EObject e = (EObject) ((Object[]) element)[0];
					IObservableList observableList = new WritableList(realm);
					observableList.add(e);
					return observableList;
				}
				return Observables.emptyObservableList();
			}
		};
		
		ObservableListTreeContentProvider contentProvider = new ObservableListTreeContentProvider(
				listFactory, new TreeStructureAdvisor() {
					public Boolean hasChildren(Object element) {
						if (element instanceof EObject)
							return !((EObject) element).eContents().isEmpty();
						return true;
					}
				});
		viewer.setContentProvider(contentProvider);
		
		// The label provider calculates labels as a function of IDs and Names.
		// Here we create observables on those values to tack the model changes.
		IValueProperty propertyName = EMFProperties.value(ApplicationPackage.Literals.MITEM__NAME);
		IValueProperty propertyID = EMFProperties.value(ApplicationPackage.Literals.MAPPLICATION_ELEMENT__ID);
		IObservableSet observableElements = contentProvider.getKnownElements();
		IObservableMap[] observables = new IObservableMap[] { propertyName.observeDetail(observableElements), 
				propertyID.observeDetail(observableElements)};
		viewer.setLabelProvider(new ViewLabelProvider(observables));
		
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
		
		EObject topObject = topObject(element);
		viewer.setInput(topObject);
		
		// double-clicks activate the selected part
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				TreeSelection selection = (TreeSelection) event.getSelection();
				MPart<?> selected = (MPart<?>) selection.getFirstElement();
				// TBD there should be an API to activate MPart 
				ModelUtils.activate(selected);
			}});
	}
	
	private EObject topObject(MPart<?> part) {
		MPart<?> lastTop = part;
		MPart<?> currentTop = part.getParent();
		while (currentTop != null) {
			lastTop = currentTop;
			currentTop = currentTop.getParent();
		}
		return lastTop.eContainer(); 
	}
	
	// TBD propagate selection via context
	protected void handleSelection(SelectionChangedEvent event) {
		if (event.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection sel = (IStructuredSelection) event.getSelection();
			Object selObj = sel.getFirstElement();
			if (selObj instanceof EObject) {
				editor.selected((EObject)selObj);
			}
		}
	}

	public String[] getPropIds(EObject selObj) {
		if (selObj == null)
			return new String[0];
		
		EList<EStructuralFeature> features = selObj.eClass().getEAllStructuralFeatures();
		String[] ids = new String[features.size()];
		int count = 0;
		for (Iterator<?> iterator = features.iterator(); iterator.hasNext();) {
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


}