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
import org.eclipse.core.expressions.ElementHandler;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionConverter;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.services.context.EclipseContextFactory;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.extensions.ExtensionUtils;
import org.eclipse.e4.ui.model.application.MContext;
import org.eclipse.e4.ui.workbench.swt.internal.AbstractPartRenderer;
import org.eclipse.e4.workbench.ui.internal.Activator;
import org.eclipse.e4.workbench.ui.internal.Policy;
import org.eclipse.e4.workbench.ui.internal.UISchedulerStrategy;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.internal.expressions.AndExpression;
import org.eclipse.ui.internal.expressions.WorkbenchWindowExpression;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.internal.services.SourcePriorityNameMapping;

public class LegacyHandlerService implements IHandlerService {
	private static final String PARM_MAP = "legacyParameterMap"; //$NON-NLS-1$
	private static final String HANDLER_PREFIX = "HDL_"; //$NON-NLS-1$

	public static class HandlerProxy {
		IHandler handler = null;
		Command command;
		EHandlerActivation activation = null;

		public HandlerProxy(Command command, IHandler handler) {
			this.command = command;
			this.handler = handler;
		}

		public boolean canExecute(IEclipseContext context) {
			return handler.isEnabled();
		}

		public void execute(IEclipseContext context) {
			Activator.trace(Policy.DEBUG_CMDS, "execute " + command + " and " //$NON-NLS-1$ //$NON-NLS-2$
					+ handler + " with: " + context, null); //$NON-NLS-1$
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

		public IHandler getHandler() {
			return handler;
		}
	}

	static class EHandlerActivation implements IHandlerActivation, Runnable {

		IEclipseContext context;
		private String commandId;
		private IHandler handler;
		HandlerProxy proxy;
		Expression activeWhen;
		boolean active;
		int sourcePriority;
		boolean participating = true;

		/**
		 * @param context
		 * @param cmdId
		 * @param handler
		 * @param handlerProxy
		 */
		public EHandlerActivation(IEclipseContext context, String cmdId,
				IHandler handler, HandlerProxy handlerProxy, Expression expr) {
			this.context = context;
			this.commandId = cmdId;
			this.handler = handler;
			this.proxy = handlerProxy;
			this.activeWhen = expr;
			this.sourcePriority = SourcePriorityNameMapping
					.computeSourcePriority(activeWhen);
			proxy.activation = this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.handlers.IHandlerActivation#clearActive()
		 */
		public void clearActive() {
			active = true;
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
			return active;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.ui.internal.services.IEvaluationResultCache#clearResult()
		 */
		public void clearResult() {

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.ui.internal.services.IEvaluationResultCache#evaluate(
		 * org.eclipse.core.expressions.IEvaluationContext)
		 */
		public boolean evaluate(IEvaluationContext context) {
			if (activeWhen == null) {
				active = true;
			} else {
				try {
					active = activeWhen.evaluate(context) != EvaluationResult.FALSE;
				} catch (CoreException e) {
					Activator.trace(Policy.DEBUG_CMDS,
							"Failed to calculate active", e); //$NON-NLS-1$
				}
			}
			return active;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.ui.internal.services.IEvaluationResultCache#getExpression
		 * ()
		 */
		public Expression getExpression() {
			return activeWhen;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.ui.internal.services.IEvaluationResultCache#getSourcePriority
		 * ()
		 */
		public int getSourcePriority() {
			return sourcePriority;
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
			EHandlerActivation activation = (EHandlerActivation) o;
			int difference;

			// Check the priorities
			int thisPriority = this.getSourcePriority();
			int thatPriority = activation.getSourcePriority();

			// rogue bit problem - ISources.ACTIVE_MENU
			int thisLsb = 0;
			int thatLsb = 0;

			if (((thisPriority & ISources.ACTIVE_MENU) | (thatPriority & ISources.ACTIVE_MENU)) != 0) {
				thisLsb = thisPriority & 1;
				thisPriority = (thisPriority >> 1) & 0x7fffffff;
				thatLsb = thatPriority & 1;
				thatPriority = (thatPriority >> 1) & 0x7fffffff;
			}

			difference = thisPriority - thatPriority;
			if (difference != 0) {
				return difference;
			}

			// if all of the higher bits are the same, check the
			// difference of the LSB
			difference = thisLsb - thatLsb;
			if (difference != 0) {
				return difference;
			}

			// Check depth
			final int thisDepth = this.getDepth();
			final int thatDepth = activation.getDepth();
			difference = thisDepth - thatDepth;
			return difference;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "EHA: " + active + ":" + sourcePriority + ":" + commandId + ": " + proxy //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					+ ": " + handler + ": " + context; //$NON-NLS-1$ //$NON-NLS-2$
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			if (!participating) {
				return;
			}
			final EHandlerService hs = (EHandlerService) context
					.get(EHandlerService.class.getName());
			Object obj = context.get(HANDLER_PREFIX + commandId);

			if (evaluate(new LegacyEvalContext(context))) {
				if (obj instanceof HandlerProxy) {
					final EHandlerActivation bestActivation = ((HandlerProxy) obj).activation;
					final int comparison = bestActivation.compareTo(this);
					if (comparison < 0) {
						hs.activateHandler(commandId, proxy);
					}
				} else if (obj == null) {
					hs.activateHandler(commandId, proxy);
				}
			} else {
				if (obj == proxy) {
					hs.deactivateHandler(commandId, proxy);
				}
			}
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
			final IEclipseContext context, String id, final String cmdId,
			IHandler handler, Expression activeWhen) {

		ECommandService cs = (ECommandService) context
				.get(ECommandService.class.getName());
		Command command = cs.getCommand(cmdId);
		HandlerProxy handlerProxy = new HandlerProxy(command, handler);
		final EHandlerActivation activation = new EHandlerActivation(context,
				cmdId, handler, handlerProxy, activeWhen);
		context.runAndTrack(activation);
		return activation;
	}

	private IEclipseContext eclipseContext;
	private IEvaluationContext evalContext;
	private Expression defaultExpression = null;

	public LegacyHandlerService(IEclipseContext context) {
		eclipseContext = context;
		evalContext = new LegacyEvalContext(eclipseContext);
		IWorkbenchWindow window = (IWorkbenchWindow) eclipseContext
				.get(IWorkbenchWindow.class.getName());
		if (window != null) {
			defaultExpression = new WorkbenchWindowExpression(window);
		}
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
		eActivation.participating = true;
		eActivation.context.runAndTrack(eActivation);
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
				handler, defaultExpression);
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
		return activateHandler(commandId, handler, expression, false);
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
		if (global || defaultExpression == null) {
			return registerLegacyHandler(eclipseContext, commandId, commandId,
					handler, expression);
		}
		AndExpression andExpr = new AndExpression();
		andExpr.add(expression);
		andExpr.add(defaultExpression);
		return registerLegacyHandler(eclipseContext, commandId, commandId,
				handler, andExpr);
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
		return activateHandler(commandId, handler, expression, false);
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
		eActivation.participating = false;
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
			Expression activeWhen = null;
			final IConfigurationElement[] awChildren = configElement
					.getChildren(IWorkbenchRegistryConstants.TAG_ACTIVE_WHEN);
			if (awChildren.length > 0) {
				final IConfigurationElement[] subChildren = awChildren[0]
						.getChildren();
				if (subChildren.length != 1) {
					Activator.trace(Policy.DEBUG_CMDS,
							"Incorrect activeWhen element " + commandId, null); //$NON-NLS-1$
					continue;
				}
				final ElementHandler elementHandler = ElementHandler
						.getDefault();
				final ExpressionConverter converter = ExpressionConverter
						.getDefault();
				try {
					activeWhen = elementHandler.create(converter,
							subChildren[0]);
				} catch (CoreException e) {
					Activator.trace(Policy.DEBUG_CMDS,
							"Incorrect activeWhen element " + commandId, e); //$NON-NLS-1$
				}
			}
			registerLegacyHandler(eclipseContext, commandId, commandId,
					new org.eclipse.ui.internal.handlers.HandlerProxy(
							commandId, configElement,
							IWorkbenchRegistryConstants.ATT_CLASS), activeWhen);
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
							IWorkbenchRegistryConstants.ATT_DEFAULT_HANDLER),
					null);

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
		Object modelObj = null;
		while (control != null) {
			modelObj = control.getData(AbstractPartRenderer.OWNING_ME);
			if (modelObj instanceof MContext)
				return ((MContext) modelObj).getContext();
			control = control.getParent();
		}
		return eclipseContext;
	}

}
