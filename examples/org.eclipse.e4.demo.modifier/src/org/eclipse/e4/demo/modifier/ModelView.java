package org.eclipse.e4.demo.modifier;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MWindow;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.databinding.viewers.ObservableSetTreeContentProvider;
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
			final MApplication<MWindow<?>> application) {
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
		IObservableFactory setFactory = new IObservableFactory() {
			public IObservable createObservable(Object element) {
				if (element instanceof EObject
						&& !((EObject) element).eContents().isEmpty()) {
					EObject e = (EObject) element;
					IObservableSet observableSet = new WritableSet(realm);
					observableSet.addAll(e.eContents());
					return observableSet;
				} else if (element instanceof Object[]) {
					EObject e = (EObject) ((Object[]) element)[0];
					IObservableSet observableSet = new WritableSet(realm);
					observableSet.add(e);
					return observableSet;
				}
				return Observables.emptyObservableSet();
			}
		};
		viewer.setContentProvider(new ObservableSetTreeContentProvider(
				setFactory, new TreeStructureAdvisor() {
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
