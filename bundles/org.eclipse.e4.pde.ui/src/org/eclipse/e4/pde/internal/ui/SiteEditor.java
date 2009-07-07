/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Boris Bokowski, IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.pde.internal.ui;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.pde.internal.webui.Activator;
import org.eclipse.e4.pde.internal.webui.PDEServlet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class SiteEditor extends EditorPart {

	private Browser browser;

	public SiteEditor() {
	}

	public void doSaveAs() {
	}

	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		if (!(input instanceof IFileEditorInput)) {
			throw new PartInitException(
					"This editor only works on files in the workspace.");
		}
		setSite(site);
		setInput(input);
	}

	public boolean isDirty() {
		return false;
	}

	public boolean isSaveAsAllowed() {
		return false;
	}

	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		browser = new Browser(parent, SWT.NONE);
		Browser.setCookie("org.eclipse.e4.pde.auth="
				+ PDEServlet.getSessionId(),
				"http://localhost");
		browser
				.setUrl("http://localhost:"
						+ Activator.PORT
						+ "/tree.html#"
						+ ((IFileEditorInput) getEditorInput()).getFile()
								.getFullPath());
	}

	public void setFocus() {
		browser.setFocus();
	}

	public void doSave(IProgressMonitor monitor) {
	}

}
