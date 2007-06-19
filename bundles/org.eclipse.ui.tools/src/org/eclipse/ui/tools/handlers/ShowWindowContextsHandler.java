/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.ui.tools.handlers;

import java.util.Arrays;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.tools.Messages;

public class ShowWindowContextsHandler extends AbstractHandler {

	@SuppressWarnings("unused")
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindow(event);
		if (window == null) {
			return null;
		}
		final IContextService service = (IContextService) window
				.getService(IContextService.class);
		Shell shell = new Shell(window.getShell().getDisplay());
		service.registerShell(shell, IContextService.TYPE_WINDOW);
		// Shell shell = new Shell(window.getShell());
		shell.setLayout(new FillLayout());
		Button b = new Button(shell, SWT.PUSH);
		final Text text = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		b.setText(Messages.showContexts_button);
		b.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				Object[] array = service.getActiveContextIds().toArray();
				Arrays.sort(array);
				StringBuffer buf = new StringBuffer(Messages.showContexts_title);
				for (int i = 0; i < array.length; i++) {
					buf.append('\n');
					buf.append(array[i].toString());
				}
				text.setText(buf.toString());
			}
		});
		shell.setSize(400, 300);
		shell.layout();
		shell.open();
		return null;
	}

}
