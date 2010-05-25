/*******************************************************************************
 * Copyright (c) 2010 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.demo.simpleide.outline.internal;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.demo.simpleide.outline.IOutlinePageProvider;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class OutlineView {
	private Composite parent;

	private Control pageControl;

	@Inject
	private IEclipseContext context;

	private IEclipseContext pageContext;

	@Inject
	public OutlineView(Composite parent) {
		this.parent = parent;
	}

	@Inject
	public void setActivePart(
			@Optional @Named(IServiceConstants.ACTIVE_PART) MPart activePart) {
		IEclipseContext oldPageContext = pageContext;
		Control oldPageControl = pageControl;

		if (activePart != null) {
			if (activePart.getObject() instanceof IOutlinePageProvider) {
				pageContext = context.createChild();
				Composite comp = new Composite(parent, SWT.NONE);
				comp.setLayout(new FillLayout());
				pageControl = comp;
				pageContext.set(Composite.class, pageControl);
				ContextInjectionFactory.make(((IOutlinePageProvider) activePart
						.getObject()).getOutlineClass(), pageContext);
			} else {
				oldPageContext = null;
				oldPageControl = null;
			}
		} else {
			Label label = new Label(parent, SWT.NONE);
			label.setText("No outline available");
			pageControl = label;
		}
		
		// Dispose afterwards which helps potential image resource pooling
		if (oldPageContext != null) {
			oldPageContext.dispose();
		}

		if (oldPageControl != null) {
			oldPageControl.dispose();
		}
		
		parent.layout(true, true);
	}
}
