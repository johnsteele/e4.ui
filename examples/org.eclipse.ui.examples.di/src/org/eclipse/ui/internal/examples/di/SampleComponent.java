package org.eclipse.ui.internal.examples.di;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class SampleComponent {

	public SampleComponent(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("Hello World");
		GridLayoutFactory.swtDefaults().generateLayout(parent);
	}
}
