package org.eclipse.e4.demo.simpleide.navigator.internal;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ResourceNavigator {
	
	@Inject
	public ResourceNavigator(Composite parent) {
		parent.setLayout(new FillLayout());
		Label l = new Label(parent, SWT.NONE);
		l.setText("Hello World");
	}
}
