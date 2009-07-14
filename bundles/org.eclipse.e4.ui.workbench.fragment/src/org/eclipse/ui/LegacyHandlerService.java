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

package org.eclipse.ui;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
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
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.e4.core.services.context.EclipseContextFactory;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.extensions.ExtensionUtils;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.services.ECommandService;
import org.eclipse.e4.ui.services.EHandlerService;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.workbench.ui.internal.UISchedulerStrategy;
import org.eclipse.e4.workbench.ui.renderers.AbstractPartRenderer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;

/**
 * @since 3.3
 * 
 */
public class LegacyHandlerService implements IHandlerService {
	private static final String PARM_MAP = "legacyParameterMap"; //$NON-NLS-1$

	public static class HandlerProxy {
		IHandler handler = null;
		private Command command;

		public HandlerProxy(Command command, IHandler handler) {
			this.command = command;
			this.handler = handler;
		}

		public boolean canExecute(IEclipseContext context) {
			return handler.isEnabled();
		}

		public void execute(IEclipseContext context) {
			Object shell = context.get(IServiceConstants.ACTIVE_SHELL);
			context.set(ISources.ACTIVE_WORKBENCH_WINDOW_SHELL_NAME, shell);
			context.set(ISources.ACTIVE_SHELL_NAME, shell);
			LegacyEvalContext legacy = new LegacyEvalContext(context);
			ExecutionEvent event = new ExecutionEvent(command, (Map) context
					.get(PARM_MAP), null, legacy);
			try {
				handler.execute(event);
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	static class EHandlerActivation implements IHandlerActivation {

		IEclipseContext context;
		private String commandId;
		private IHandler handler;
		HandlerProxy proxy;

		/**
		 * @param context
		 * @param cmdId
		 * @param handler
		 * @param handlerProxy
		 */
		public EHandlerActivation(IEclipseContext context, String cmdId,
				IHandler handler, HandlerProxy handlerProxy) {
			this.context = context;
			this.commandId = cmdId;
			this.handler = handler;
			this.proxy = handlerProxy;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.handlers.IHandlerActivation#clearActive()
		 */
		public void clearActive() {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.handlers.IHandlerActivation#getCommandId()
		 */
		public String getCommandId() {
			return commandId;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.handlers.IHandlerActivation#getDepth()
		 */
		public int getDepth() {
			// TODO Auto-generated method stub
			return 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.handlers.IHandlerActivation#getHandler()
		 */
		public IHandler getHandler() {
			return handler;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.handlers.IHandlerActivation#getHandlerService()
		 */
		public IHandlerService getHandlerService() {
			return (IHandlerService) context.get(IHandlerService.class
					.getName());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.ui.handlers.IHandlerActivation#isActive(org.eclipse.core
		 * .expressions.IEvaluationContext)
		 */
		public boolean isActive(IEvaluationContext context) {
			// TODO Auto-generated method stub
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.ui.internal.services.IEvaluationResultCache#clearResult()
		 */
		public void clearResult() {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.ui.internal.services.IEvaluationResultCache#evaluate(
		 * org.eclipse.core.expressions.IEvaluationContext)
		 */
		public boolean evaluate(IEvaluationContext context) {
			// TODO Auto-generated method stub
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.ui.internal.services.IEvaluationResultCache#getExpression
		 * ()
		 */
		public Expression getExpression() {
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.ui.internal.services.IEvaluationResultCache#getSourcePriority
		 * ()
		 */
		public int getSourcePriority() {
			// TODO Auto-generated method stub
			return 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.ui.internal.services.IEvaluationResultCache#setResult
		 * (boolean)
		 */
		public void setResult(boolean result) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		public int compareTo(Object o) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.application.IActionBarConfigurer#registerGlobalAction(
	 * org.eclipse.jface.action.IAction)
	 */
	public static IHandlerActivation registerLegacyHandler(
			IEclipseContext context, String id, String cmdId, IHandler handler) {
		EHandlerService hs = (EHandlerService) context
				.get(EHandlerService.class.getName());
		ECommandService cs = (ECommandService) context
				.get(ECommandService.class.getName());
		Command command = cs.getCommand(cmdId);
		HandlerProxy handlerProxy = new HandlerProxy(command, handler);
		EHandlerActivation activation = new EHandlerActivation(context, cmdId,
				handler, handlerProxy);
		hs.activateHandler(cmdId, handlerProxy);
		return activation;
	}

	private IEclipseContext eclipseContext;
	private IEvaluationContext evalContext;

	public LegacyHandlerService(IEclipseContext context) {
		eclipseContext = context;
		evalContext = new LegacyEvalContext(eclipseContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.handlers.IHandlerService#activateHandler(org.eclipse.ui
	 * .handlers.IHandlerActivation)
	 */
	public IHandlerActivation activateHandler(IHandlerActivation activation) {
		EHandlerActivation eActivation = (EHandlerActivation) activation;
		EHandlerService hs = (EHandlerService) eActivation.context
				.get(EHandlerService.class.getName());
		hs.activateHandler(activation.getCommandId(), eActivation.proxy);
		return activation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.handlers.IHandlerService#activateHandler(java.lang.String,
	 * org.eclipse.core.commands.IHandler)
	 */
	public IHandlerActivation activateHandler(String commandId, IHandler handler) {
		return registerLegacyHandler(eclipseContext, commandId, commandId,
				handler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.handlers.IHandlerService#activateHandler(java.lang.String,
	 * org.eclipse.core.commands.IHandler,
	 * org.eclipse.core.expressions.Expression)
	 */
	public IHandlerActivation activateHandler(String commandId,
			IHandler handler, Expression expression) {
		return registerLegacyHandler(eclipseContext, commandId, commandId,
				handler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.handlers.IHandlerService#activateHandler(java.lang.String,
	 * org.eclipse.core.commands.IHandler,
	 * org.eclipse.core.expressions.Expression, boolean)
	 */
	public IHandlerActivation activateHandler(String commandId,
			IHandler handler, Expression expression, boolean global) {
		return registerLegacyHandler(eclipseContext, commandId, commandId,
				handler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.handlers.IHandlerService#activateHandler(java.lang.String,
	 * org.eclipse.core.commands.IHandler,
	 * org.eclipse.core.expressions.Expression, int)
	 */
	public IHandlerActivation activateHandler(String commandId,
			IHandler handler, Expression expression, int sourcePriorities) {
		return registerLegacyHandler(eclipseContext, commandId, commandId,
				handler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.handlers.IHandlerService#createContextSnapshot(boolean)
	 */
	public IEvaluationContext createContextSnapshot(boolean includeSelection) {
		return new LegacyEvalContext(EclipseContextFactory.create(
				getFocusContext(PlatformUI.getWorkbench().getDisplay()),
				UISchedulerStrategy.getInstance()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.handlers.IHandlerService#createExecutionEvent(org.eclipse
	 * .core.commands.Command, org.eclipse.swt.widgets.Event)
	 */
	public ExecutionEvent createExecutionEvent(Command command, Event event) {
		LegacyEvalContext legacy = new LegacyEvalContext(
				getFocusContext(PlatformUI.getWorkbench().getDisplay()));
		ExecutionEvent e = new ExecutionEvent(command, Collections.EMPTY_MAP,
				event, legacy);
		return e;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.handlers.IHandlerService#createExecutionEvent(org.eclipse
	 * .core.commands.ParameterizedCommand, org.eclipse.swt.widgets.Event)
	 */
	public ExecutionEvent createExecutionEvent(ParameterizedCommand command,
			Event event) {
		LegacyEvalContext legacy = new LegacyEvalContext(
				getFocusContext(PlatformUI.getWorkbench().getDisplay()));
		ExecutionEvent e = new ExecutionEvent(command.getCommand(), command
				.getParameterMap(), event, legacy);
		return e;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.handlers.IHandlerService#deactivateHandler(org.eclipse
	 * .ui.handlers.IHandlerActivation)
	 */
	public void deactivateHandler(IHandlerActivation activation) {
		EHandlerActivation eActivation = (EHandlerActivation) activation;
		EHandlerService hs = (EHandlerService) eActivation.context
				.get(EHandlerService.class.getName());
		hs.deactivateHandler(eActivation.getCommandId(), eActivation.proxy);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.handlers.IHandlerService#deactivateHandlers(java.util.
	 * Collection)
	 */
	public void deactivateHandlers(Collection activations) {
		Object[] array = activations.toArray();
		for (int i = 0; i < array.length; i++) {
			deactivateHandler((IHandlerActivation) array[i]);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.handlers.IHandlerService#executeCommand(java.lang.String,
	 * org.eclipse.swt.widgets.Event)
	 */
	public Object executeCommand(String commandId, Event event)
			throws ExecutionException, NotDefinedException,
			NotEnabledException, NotHandledException {
		ECommandService cs = (ECommandService) getFocusContext(
				PlatformUI.getWorkbench().getDisplay()).get(
				ECommandService.class.getName());
		final Command command = cs.getCommand(commandId);
		return executeCommand(ParameterizedCommand.generateCommand(command,
				null), event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.handlers.IHandlerService#executeCommand(org.eclipse.core
	 * .commands.ParameterizedCommand, org.eclipse.swt.widgets.Event)
	 */
	public Object executeCommand(ParameterizedCommand command, Event event)
			throws ExecutionException, NotDefinedException,
			NotEnabledException, NotHandledException {
		EHandlerService hs = (EHandlerService) getFocusContext(
				PlatformUI.getWorkbench().getDisplay()).get(
				EHandlerService.class.getName());
		hs.getContext().set(PARM_MAP, command.getParameterMap());
		return hs.executeHandler(command);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.handlers.IHandlerService#executeCommandInContext(org.eclipse
	 * .core.commands.ParameterizedCommand, org.eclipse.swt.widgets.Event,
	 * org.eclipse.core.expressions.IEvaluationContext)
	 */
	public Object executeCommandInContext(ParameterizedCommand command,
			Event event, IEvaluationContext context) throws ExecutionException,
			NotDefinedException, NotEnabledException, NotHandledException {
		if (context instanceof LegacyEvalContext) {
			EHandlerService hs = (EHandlerService) ((LegacyEvalContext) context).eclipseContext
					.get(EHandlerService.class.getName());
			return hs.executeHandler(command);
		}
		return executeCommand(command, event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.handlers.IHandlerService#getCurrentState()
	 */
	public IEvaluationContext getCurrentState() {
		return evalContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.handlers.IHandlerService#readRegistry()
	 */
	public void readRegistry() {
		readDefaultHandlers();
		readHandlers();
	}

	private void readHandlers() {
		IConfigurationElement[] elements = ExtensionUtils
				.getExtensions(IWorkbenchRegistryConstants.PL_HANDLERS);
		for (IConfigurationElement configElement : elements) {
			String commandId = configElement
					.getAttribute(IWorkbenchRegistryConstants.ATT_COMMAND_ID);
			if (commandId == null || commandId.length() == 0) {
				continue;
			}
			String defaultHandler = configElement
					.getAttribute(IWorkbenchRegistryConstants.ATT_CLASS);
			if ((defaultHandler == null)
					&& (configElement
							.getChildren(IWorkbenchRegistryConstants.TAG_CLASS).length == 0)) {
				continue;
			}
			registerLegacyHandler(eclipseContext, commandId, commandId,
					new org.eclipse.ui.internal.handlers.HandlerProxy(
							commandId, configElement,
							IWorkbenchRegistryConstants.ATT_CLASS));
		}
	}

	private void readDefaultHandlers() {
		IConfigurationElement[] elements = ExtensionUtils
				.getExtensions(IWorkbenchRegistryConstants.PL_COMMANDS);
		for (IConfigurationElement configElement : elements) {
			String id = configElement
					.getAttribute(IWorkbenchRegistryConstants.ATT_ID);
			if (id == null || id.length() == 0) {
				continue;
			}
			String defaultHandler = configElement
					.getAttribute(IWorkbenchRegistryConstants.ATT_DEFAULT_HANDLER);
			if ((defaultHandler == null)
					&& (configElement
							.getChildren(IWorkbenchRegistryConstants.TAG_DEFAULT_HANDLER).length == 0)) {
				continue;
			}
			registerLegacyHandler(eclipseContext, id, id,
					new org.eclipse.ui.internal.handlers.HandlerProxy(id,
							configElement,
							IWorkbenchRegistryConstants.ATT_DEFAULT_HANDLER));

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.handlers.IHandlerService#setHelpContextId(org.eclipse.
	 * core.commands.IHandler, java.lang.String)
	 */
	public void setHelpContextId(IHandler handler, String helpContextId) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.services.IServiceWithSources#addSourceProvider(org.eclipse
	 * .ui.ISourceProvider)
	 */
	public void addSourceProvider(ISourceProvider provider) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.services.IServiceWithSources#removeSourceProvider(org.
	 * eclipse.ui.ISourceProvider)
	 */
	public void removeSourceProvider(ISourceProvider provider) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.services.IDisposable#dispose()
	 */
	public void dispose() {
		// TODO Auto-generated method stub

	}

	private IEclipseContext getFocusContext(Display display) {
		// find the first useful part in the model
		Control control = display.getFocusControl();
		Object partObj = null;
		while (control != null && !(partObj instanceof MPart<?>)) {
			partObj = control.getData(AbstractPartRenderer.OWNING_ME);
			control = control.getParent();
		}
		if (partObj == null) {
			return eclipseContext;
		}
		// get the applicable context (or parent)
		MPart<?> part = (MPart<?>) partObj;
		return getContext(part);
	}

	private IEclipseContext getContext(MPart<?> part) {
		IEclipseContext c = null;
		while (c == null && part != null) {
			c = part.getContext();
			part = part.getParent();
		}
		return c == null ? eclipseContext : c;
	}

}
