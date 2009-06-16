/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.demo.viewer;

import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.workbench.ui.renderers.PartFactory;

/**
 * Pieces of code that really should be available as APIs somewhere.
 */
public class ModelUtils {
	
	/**
	 * This code is copied from the {@link PartFactory#activate(MPart)}
	 */
	static public void activate(MPart<?> part) {
		MPart<MPart<?>> parent = (MPart<MPart<?>>) part.getParent();
		IEclipseContext partContext = part.getContext();
		while (parent != null) {
			IEclipseContext parentContext = parent.getContext();
			parent.setActiveChild(part);
			if (parentContext != null) {
				parentContext.set(IServiceConstants.ACTIVE_CHILD, partContext);
				partContext = parentContext;
			}
			part = parent;
			parent = (MPart<MPart<?>>) parent.getParent();
		}
	}

}