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
package org.eclipse.e4.demo.simpleide.jdt.internal.editor;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class JDTOutlinePage {
	
	@Inject
	public JDTOutlinePage(Composite parent, @Named(IServiceConstants.ACTIVE_PART) MPart part) {
		parent.setLayout(new FillLayout());
		Label l = new Label(parent, SWT.NONE);
		l.setText("JDT OUTLINE: " + part.getLabel());
	}
	
	@Inject
	public void setSelection(@Optional @Named(IServiceConstants.SELECTION) IJDTSelection selection) {
		if( selection != null ) {
			System.err.println("Updating to JavaSelection '"+selection+"'");
		}
	}
	
	@PreDestroy
	public void dispose() {
		System.err.println("Destroying!!!!");
	}
}
