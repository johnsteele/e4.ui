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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;

/**
 * @since 3.3
 * 
 */
public class RunMultipleCommands extends AbstractHandler {
	private static final String PARM_ID_LIST = "commandId.list"; //$NON-NLS-1$

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		if (window == null) {
			return null;
		}
		IHandlerService handlerService = (IHandlerService) window
				.getService(IHandlerService.class);
		String idParm = event.getParameter(PARM_ID_LIST);
		if (idParm == null) {
			throw new ExecutionException("Must supply command parameter"); //$NON-NLS-1$
		}
		String[] idList = idParm.split(","); //$NON-NLS-1$
		for (String id : idList) {
			try {
				handlerService.executeCommand(id, null);
			} catch (NotDefinedException e) {
				throw new ExecutionException(e.getMessage(), e);
			} catch (NotEnabledException e) {
				throw new ExecutionException(e.getMessage(), e);
			} catch (NotHandledException e) {
				throw new ExecutionException(e.getMessage(), e);
			}
		}
		return null;
	}
}
