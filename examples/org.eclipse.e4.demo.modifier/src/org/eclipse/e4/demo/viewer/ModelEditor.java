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
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MContributedPart;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.model.application.MSashForm;
import org.eclipse.e4.ui.model.application.MStack;
import org.eclipse.e4.ui.model.workbench.MPerspective;
import org.eclipse.e4.ui.model.workbench.MWorkbenchWindow;
import org.eclipse.e4.workbench.ui.renderers.PartFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
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

public class ModelEditor extends ViewPart {
	private FormToolkit toolkit;
	private ScrolledForm form;
	private TreeViewer viewer;
	private Section left;
	private Section right;
	private DataBindingContext dbc;
	
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
				event.doit = selection.getFirstElement() instanceof MContributedPart; 
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
				MContributedPart view = (MContributedPart) selection.getFirstElement();
				sm.getChildren().add(view);
				viewer.refresh();
			}
		}
		public void dragLeave(DropTargetEvent event) {
		}
		public void dropAccept(DropTargetEvent event) {
		}
	};
	 
	class ViewContentProvider implements ITreeContentProvider {
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof MPart<?>) {
				MPart<?> element = (MPart<?>) parentElement;
				return element.getChildren().toArray();
			}
			return null;
		}
		public Object getParent(Object element) {
			if (element instanceof MPart<?>) {
				return ((MPart<?>)element).getParent();
			}
			return null;
		}

		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof MApplication<?>) {
				MApplication<?> wbm = (MApplication<?>) inputElement;
				return wbm.getWindows().toArray();
			}
			return new Object[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	class ViewLabelProvider extends LabelProvider {

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
				return "Stack";
			if (element instanceof MSashForm<?>)
				return "Sash";
			if (element instanceof MPerspective<?>)
				return "Perspective"; 
			if (element instanceof MWorkbenchWindow)
				return "Workbench Window"; 
			return element.getClass().getSimpleName();
		}
	}

	/**
	 * The constructor.
	 */
	public ModelEditor() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		imageHelper = new ImageManagerHelper();
		// Access services...
		MPart<?> element = (MPart<?>) parent.getData(PartFactory.OWNING_ME);
		
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		form.setText("Workbench Model Editor");

		GridLayout gl = new GridLayout(2, true);
		form.getBody().setLayout(gl);

		dbc = new DataBindingContext();
		
		left = toolkit.createSection(form.getBody(), Section.TITLE_BAR
				| Section.EXPANDED);
		left.setText("WB Model");
		left.setLayout(new FillLayout());
		left.setLayoutData(new GridData(GridData.FILL_BOTH));

		right = toolkit.createSection(form.getBody(), Section.TITLE_BAR
				| Section.EXPANDED);
		right.setText("Properties");
		right.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL));

		Composite comp = toolkit.createComposite(right);
		comp.setLayout(new GridLayout(2, false));
		right.setClient(comp);
		
		Tree tree = toolkit.createTree(left, SWT.NONE);
		tree.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER);
		left.setClient(tree);
		toolkit.paintBordersFor(left);
		viewer = new TreeViewer(tree);
		viewer.setContentProvider(new ViewContentProvider());
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
		
		EObject topObject = topObject(element);
		viewer.setInput(topObject);
		
		// Track model changes
		final ModelTracker modelTracker = new ModelTracker(viewer);
		topObject.eAdapters().add(modelTracker);
		
		// double-clicks activate the selected part
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				TreeSelection selection = (TreeSelection) event.getSelection();
				MPart<?> selected = (MPart<?>) selection.getFirstElement();
				// TBD there should be an API to activate MPart 
				modelTracker.suspend(); // don't respond to this activation event
				ModelUtils.activate(selected);
				modelTracker.resume();
			}});
		
		form.layout(true);
	}
	
	private EObject topObject(MPart<?> part) {
		MPart<?> lastTop = part;
		MPart<?> currentTop = part.getParent();
		while (currentTop != null) {
			lastTop = currentTop;
			currentTop = currentTop.getParent();
		}
		return lastTop.eContainer(); // TBD why link WorkbenchWindow -> Application is not done via parent?
	}
	
	protected void handleSelection(SelectionChangedEvent event) {
		if (event.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection sel = (IStructuredSelection) event.getSelection();
			Object selObj = sel.getFirstElement();
			if (selObj instanceof EObject) {
				createCellsFor((EObject)selObj);
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

	private void createCellsFor(EObject eObj) {
		Composite curComp = (Composite) right.getClient();

		Composite newComp = toolkit.createComposite(right);
		newComp.setLayout(new GridLayout(2, false));
		right.setClient(newComp);
		
		if (curComp != null)
			curComp.dispose();
		
		EList<EStructuralFeature> features = eObj.eClass().getEAllStructuralFeatures();
		for (Iterator<?> iterator = features.iterator(); iterator.hasNext();) {
			EStructuralFeature feature = (EStructuralFeature) iterator.next();
			String name = feature.getName();
			Label label = toolkit.createLabel(newComp, name);
			label.setLayoutData(new GridData());
			
			Control cellCtrl = createCell(newComp, eObj, feature);
			cellCtrl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
			right.layout(true);
		}
		
		right.layout(true);
	}

	private Control createCell(Composite parent, EObject eObj, EStructuralFeature feature) {
		String name = feature.getName();
		Class<?> clazz = feature.getEType().getInstanceClass();
		String clsStr = clazz.getName();
		Object propVal = getProperty(eObj, feature.getName());
		
		IObservableValue emfObservable = EMFObservables.observeValue(eObj, feature);

		if (clsStr.equals("java.lang.String") || clsStr.equals("int")) {
			Text text = toolkit.createText(parent, "");			
			dbc.bindValue(SWTObservables.observeText(text, SWT.Modify), emfObservable, null, null);
			return text;
		}
		else if (clsStr.equals("boolean")) {
			Button button = toolkit.createButton(parent, "", SWT.CHECK);
			ISWTObservableValue so = SWTObservables.observeSelection(button);
			dbc.bindValue(so, emfObservable, null, null);
			return button;
		}
		else if (clsStr.equals("int")) {
			Text text = toolkit.createText(parent, "");
			
			ISWTObservableValue swtObservable = SWTObservables.observeText(text, SWT.Modify);
			dbc.bindValue(swtObservable, emfObservable, null, null);
			
			return text;
		}
		else if (name.equals("selectedPart")) {
			MStack sm = (MStack) eObj;
			
			CCombo combo = new CCombo(parent, SWT.BORDER | SWT.DROP_DOWN);
			combo.setData(eObj);
			
			List<?> kids = sm.getChildren();
			for (Iterator<?> iterator = kids.iterator(); iterator.hasNext();) {
				MContributedPart<?> uiElement = (MContributedPart<?>) iterator.next();
				if (uiElement.isVisible()) {
					combo.add(uiElement.getName());
					combo.setData(uiElement.getName(), uiElement);
				}
			}
			MPart<?> selPart = sm.getActiveChild();
			if (selPart != null)
				combo.setText(sm.getActiveChild().getName());
			
			combo.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					CCombo combo = (CCombo) e.widget;
					MContributedPart<?> data = (MContributedPart<?>) combo.getData(combo.getText());
					if (data != null) {
						MStack sm = (MStack) combo.getData();
						sm.setActiveChild(data);
					}
				}
			});
			return combo;
		}
		
		return toolkit.createLabel(parent, propVal == null ? "<null> " + clsStr : propVal.getClass().getName()); 
	}
	
	/**
	 * Passing the focus request to the form.
	 */
	public void setFocus() {
		form.setFocus();
	}

	/**
	 * Disposes the toolkit
	 */
	public void dispose() {
		toolkit.dispose();
		super.dispose();
		if (imageHelper != null)
			imageHelper.dispose();
	}
	
}