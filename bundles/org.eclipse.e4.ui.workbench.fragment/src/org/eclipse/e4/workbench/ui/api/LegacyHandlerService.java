/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.e4.workbench.ui.api;

import java.util.Collection;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.ISourceProvider;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;

/**
 * @since 3.3
 *
 */
public class LegacyHandlerService implements IHandlerService {

	/* (non-Javadoc)
	 * @see org.eclipse.ui.handlers.IHandlerService#activateHandler(org.eclipse.ui.handlers.IHandlerActivation)
	 */
	public IHandlerActivation activateHandler(IHandlerActivation activation) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.handlers.IHandlerService#activateHandler(java.lang.String, org.eclipse.core.commands.IHandler)
	 */
	public IHandlerActivation activateHandler(String commandId, IHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.handlers.IHandlerService#activateHandler(java.lang.String, org.eclipse.core.commands.IHandler, org.eclipse.core.expressions.Expression)
	 */
	public IHandlerActivation activateHandler(String commandId,
			IHandler handler, Expression expression) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.handlers.IHandlerService#activateHandler(java.lang.String, org.eclipse.core.commands.IHandler, org.eclipse.core.expressions.Expression, boolean)
	 */
	public IHandlerActivation activateHandler(String commandId,
			IHandler handler, Expression expression, boolean global) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.handlers.IHandlerService#activateHandler(java.lang.String, org.eclipse.core.commands.IHandler, org.eclipse.core.expressions.Expression, int)
	 */
	public IHandlerActivation activateHandler(String commandId,
			IHandler handler, Expression expression, int sourcePriorities) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.handlers.IHandlerService#createContextSnapshot(boolean)
	 */
	public IEvaluationContext createContextSnapshot(boolean includeSelection) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.handlers.IHandlerService#createExecutionEvent(org.eclipse.core.commands.Command, org.eclipse.swt.widgets.Event)
	 */
	public ExecutionEvent createExecutionEvent(Command command, Event event) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.handlers.IHandlerService#createExecutionEvent(org.eclipse.core.commands.ParameterizedCommand, org.eclipse.swt.widgets.Event)
	 */
	public ExecutionEvent createExecutionEvent(ParameterizedCommand command,
			Event event) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.handlers.IHandlerService#deactivateHandler(org.eclipse.ui.handlers.IHandlerActivation)
	 */
	public void deactivateHandler(IHandlerActivation activation) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.handlers.IHandlerService#deactivateHandlers(java.util.Collection)
	 */
	public void deactivateHandlers(Collection activations) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.handlers.IHandlerService#executeCommand(java.lang.String, org.eclipse.swt.widgets.Event)
	 */
	public Object executeCommand(String commandId, Event event)
			throws ExecutionException, NotDefinedException,
			NotEnabledException, NotHandledException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.handlers.IHandlerService#executeCommand(org.eclipse.core.commands.ParameterizedCommand, org.eclipse.swt.widgets.Event)
	 */
	public Object executeCommand(ParameterizedCommand command, Event event)
			throws ExecutionException, NotDefinedException,
			NotEnabledException, NotHandledException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.handlers.IHandlerService#executeCommandInContext(org.eclipse.core.commands.ParameterizedCommand, org.eclipse.swt.widgets.Event, org.eclipse.core.expressions.IEvaluationContext)
	 */
	public Object executeCommandInContext(ParameterizedCommand command,
			Event event, IEvaluationContext context) throws ExecutionException,
			NotDefinedException, NotEnabledException, NotHandledException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.handlers.IHandlerService#getCurrentState()
	 */
	public IEvaluationContext getCurrentState() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.handlers.IHandlerService#readRegistry()
	 */
	public void readRegistry() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.handlers.IHandlerService#setHelpContextId(org.eclipse.core.commands.IHandler, java.lang.String)
	 */
	public void setHelpContextId(IHandler handler, String helpContextId) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.services.IServiceWithSources#addSourceProvider(org.eclipse.ui.ISourceProvider)
	 */
	public void addSourceProvider(ISourceProvider provider) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.services.IServiceWithSources#removeSourceProvider(org.eclipse.ui.ISourceProvider)
	 */
	public void removeSourceProvider(ISourceProvider provider) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.services.IDisposable#dispose()
	 */
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
