package org.eclipse.e4.demo.modifier;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.databinding.viewers.TreeStructureAdvisor;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class ModelView {
	public ModelView(final Composite parent,
			final IEclipseContext outputContext,
			final MApplication application) {
		final Realm realm = Realm.getDefault();
		TreeViewer viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				outputContext.set(IServiceConstants.SELECTION,
						((StructuredSelection) event.getSelection())
								.getFirstElement());
			}
		});
		IObservableFactory listFactory = new IObservableFactory() {
			public IObservable createObservable(Object element) {
				if (element instanceof EObject
						&& !((EObject) element).eContents().isEmpty()) {
					EObject e = (EObject) element;
					IObservableList observableList = new WritableList(realm);
					observableList.addAll(e.eContents());
					return observableList;
				} else if (element instanceof Object[]) {
					EObject e = (EObject) ((Object[]) element)[0];
					IObservableList observableList = new WritableList(realm);
					observableList.add(e);
					return observableList;
				}
				return Observables.emptyObservableList();
			}
		};
		viewer.setContentProvider(new ObservableListTreeContentProvider(
				listFactory, new TreeStructureAdvisor() {
					public Boolean hasChildren(Object element) {
						return Boolean.valueOf(element instanceof EObject);
					}
				}));
		viewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof EObject) {
					return ((EObject) element).eClass().getName();
				}
				return super.getText(element);
			}
		});
		viewer.setInput(new Object[] { application });
		GridLayoutFactory.fillDefaults().generateLayout(parent);
	}
}
