/*******************************************************************************
 * Copyright (c) 2010 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.demo.simpleide.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.workbench.ui.IPresentationEngine;

public class ExitHandler {
	@Execute
	public void exitWorkbench(IPresentationEngine engine) {
		System.err.println("Shut down");
		engine.stop();
	}
}
