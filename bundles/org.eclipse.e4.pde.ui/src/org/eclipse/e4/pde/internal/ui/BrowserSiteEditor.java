/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Boris Bokowski, IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.pde.internal.ui;

import org.eclipse.e4.pde.internal.webui.Activator;
import org.eclipse.e4.pde.internal.webui.PDEServlet;
import org.eclipse.e4.ui.web.BrowserEditorPart;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;

public class BrowserSiteEditor extends BrowserEditorPart {

	public BrowserSiteEditor() {
	}

	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
	}

	protected void configureBrowser(Browser b) {
		Browser.setCookie("org.eclipse.e4.pde.auth="
				+ PDEServlet.getSessionId(), "http://localhost");
		b
				.setUrl("http://localhost:"
						+ Activator.PORT
						+ "/tree.html#"
						+ ((IFileEditorInput) getEditorInput()).getFile()
								.getFullPath());
	}

	protected IEditorInput getNewWindowEditorInput(WindowEvent event) {
		return null;
	}

}
