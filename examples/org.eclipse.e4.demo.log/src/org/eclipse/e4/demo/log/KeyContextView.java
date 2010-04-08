package org.eclipse.e4.demo.log;

import java.util.ArrayList;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.commands.contexts.Context;
import org.eclipse.core.commands.contexts.ContextManager;
import org.eclipse.e4.core.contexts.ContextChangeEvent;
import org.eclipse.e4.core.contexts.IRunAndTrack;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.annotations.PostConstruct;
import org.eclipse.e4.ui.bindings.internal.ContextSet;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.services.IStylingEngine;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class KeyContextView {
	final private Composite parent;
	private ListViewer myViewer;
	private ListViewer appViewer;
	private ContextSet myBindingContextSet = ContextSet.EMPTY;
	private ContextSet appBindingContextSet = ContextSet.EMPTY;

	/**
	 * This will not change, and is a good candidate for field injection.
	 */
	@Inject
	private MApplication application;

	/**
	 * This will not change, and is a good candidate for field injection.
	 */
	@Inject
	private IStylingEngine styler;

	private ContextManager contextManager;

	@Inject
	public KeyContextView(final Composite parent) {
		this.parent = new Composite(parent, SWT.NONE);
		this.parent.setLayout(new GridLayout(2, true));
	}

	@PostConstruct
	private void init() {
		Label label = new Label(parent, SWT.NONE);
		styler.setClassname(label, "keyContextView");
		styler.setId(label, "me");
		label.setText("My Binding Contexts");
		GridData data = new GridData(SWT.LEFT, SWT.TOP, true, false);
		label.setLayoutData(data);

		label = new Label(parent, SWT.NONE);
		styler.setClassname(label, "keyContextView");
		styler.setId(label, "app");
		label.setText("Application Binding Contexts");
		data = new GridData(SWT.LEFT, SWT.TOP, true, false);
		label.setLayoutData(data);

		myViewer = new ListViewer(parent);
		data = new GridData(SWT.FILL, SWT.FILL, true, true);
		myViewer.getControl().setLayoutData(data);
		myViewer.setContentProvider(ArrayContentProvider.getInstance());
		LabelProvider labelProvider = new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Context) {
					Context c = (Context) element;
					return c.getId();
				}
				return super.getText(element);
			}
		};
		myViewer.setLabelProvider(labelProvider);
		myViewer.setInput(myBindingContextSet.getContexts());

		data = new GridData(SWT.FILL, SWT.FILL, true, true);
		appViewer = new ListViewer(parent);
		appViewer.getControl().setLayoutData(data);
		appViewer.setContentProvider(ArrayContentProvider.getInstance());
		appViewer.setLabelProvider(labelProvider);

		application.getContext().runAndTrack(new IRunAndTrack() {
			public boolean notify(ContextChangeEvent event) {
				Set<String> set = (Set<String>) application.getContext().get(
						IServiceConstants.ACTIVE_CONTEXTS);
				appBindingContextSet = getContextSet(set);
				appViewer.setInput(appBindingContextSet.getContexts());
				return true;
			}
		}, null);
	}

	/**
	 * This will inject the active contexts, as seen from this view. Because we
	 * need to take action when this value changes, it is a good candidate for
	 * method injection.
	 * 
	 * @param set
	 *            active context IDs
	 */
	@Inject
	public void setBindingContextIds(
			@Named(IServiceConstants.ACTIVE_CONTEXTS) @Optional Set<String> set) {
		myBindingContextSet = getContextSet(set);
		if (myViewer != null) {
			myViewer.setInput(myBindingContextSet.getContexts());
		}
	}

	/**
	 * This is taking advantage of method injection to set a comparator needed
	 * for this view's correct operation.
	 * 
	 * @param manager
	 *            The context object manager for this application.
	 */
	@Inject
	public void setContextManager(ContextManager manager) {
		contextManager = manager;
		if (manager != null && ContextSet.getComparator() == null) {
			ContextSet.setComparator(new ContextSet.CComp(manager));
		}
	}

	private ContextSet getContextSet(Set<String> set) {
		if (set == null || set.isEmpty() || contextManager == null) {
			return ContextSet.EMPTY;
		}
		ArrayList<Context> contexts = new ArrayList<Context>();
		for (String id : set) {
			contexts.add(contextManager.getContext(id));
		}
		return new ContextSet(contexts);
	}
}
