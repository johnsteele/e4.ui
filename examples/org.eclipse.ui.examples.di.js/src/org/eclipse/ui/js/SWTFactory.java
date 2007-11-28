package org.eclipse.ui.js;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class SWTFactory {
	public Label createLabel(Composite parent, int style) {
		return new Label(parent, style);
	}

	public FillLayout createFillLayout() {
		return new FillLayout();
	}

}
