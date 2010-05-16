package org.eclipse.e4.demo.simpleide.navigator.internal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableSetTreeContentProvider;
import org.eclipse.jface.databinding.viewers.TreeStructureAdvisor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class ResourceNavigator {
	private Map<IContainer, IObservableSet> observableSets = new HashMap<IContainer, IObservableSet>();

	private IResourceChangeListener listener = new IResourceChangeListener() {
		public void resourceChanged(IResourceChangeEvent event) {
			if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
				try {
					event.getDelta().accept(new IResourceDeltaVisitor() {
						public boolean visit(IResourceDelta delta)
								throws CoreException {
							if (delta.getKind() == IResourceDelta.ADDED) {
								handleChange(delta.getResource(), delta
										.getResource().getParent(), true);
							} else if (delta.getKind() == IResourceDelta.REMOVED) {
								handleChange(delta.getResource(), delta
										.getResource().getParent(), false);
							}
							return true;
						}

						private void handleChange(final IResource resource,
								final IContainer parent, final boolean added) {
							final IObservableSet set = observableSets
									.get(parent);
							Realm realm = set != null ? set.getRealm() : null;
							if (realm != null) {
								realm.asyncExec(new Runnable() {
									public void run() {
										if (added) {
											set.add(resource);
										} else {
											set.remove(resource);
										}
									}
								});
							}
						}
					});
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	@Inject
	public ResourceNavigator(Composite parent, IWorkspace workspace) {
		final Realm realm = SWTObservables.getRealm(parent.getDisplay());
		parent.setLayout(new FillLayout());
		TreeViewer viewer = new TreeViewer(parent);
		IObservableFactory setFactory = new IObservableFactory() {
			public IObservable createObservable(Object element) {
				if (element instanceof IContainer
						&& ((IContainer) element).exists()) {
					IObservableSet observableSet = observableSets.get(element);
					if (observableSet == null) {
						observableSet = new WritableSet(realm);
						try {
							observableSet.addAll(Arrays
									.asList(((IContainer) element).members()));
						} catch (CoreException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						observableSets.put((IContainer) element, observableSet);
					}
					return observableSet;
				}
				return Observables.emptyObservableSet();
			}
		};
		viewer.setContentProvider(new ObservableSetTreeContentProvider(
				setFactory, new TreeStructureAdvisor() {
					public Boolean hasChildren(Object element) {
						return Boolean.valueOf(element instanceof IContainer);
					}
				}));

		viewer.setLabelProvider(new LabelProvider() {
			public String getText(Object element) {
				if (element instanceof IResource)
					return ((IResource) element).getName();
				return element == null ? "" : element.toString();
			}
		});
		viewer.setSorter(new ViewerSorter());
		viewer.setInput(workspace.getRoot());
		workspace.addResourceChangeListener(listener);
	}
}
