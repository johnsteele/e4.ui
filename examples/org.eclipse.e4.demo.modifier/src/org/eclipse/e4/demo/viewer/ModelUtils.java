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

import java.util.Iterator;

import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.workbench.ui.renderers.PartFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

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
	
	/**
	 * Get model element associated with the composite or
	 * its containers.
	 * This method may return null.
	 */
	static MPart<?> getElement(Composite composite) {
		for (Composite container = composite; container != null; container = container.getParent()) {
			Object owner = container.getData(PartFactory.OWNING_ME);
			if (owner == null)
				continue;
			if (owner instanceof MPart<?>)
				return (MPart<?>) owner;
		}
		return null;
	}
	
	/**
	 * Get context associated with the composite or its containers.
	 * This method may return null.
	 */
	static IEclipseContext getContext(Composite composite) {
		MPart<?> part = getElement(composite);
		while (part != null) {
			IEclipseContext result = part.getContext();
			if (result != null)
				return result;
			part = part.getParent();
		};
		return null;
	}
	
	/**
	 * Returns a model root for a model tree element.
	 */
	static public EObject topObject(MPart<?> part) {
		MPart<?> lastTop = part;
		MPart<?> currentTop = part.getParent();
		while (currentTop != null) {
			lastTop = currentTop;
			currentTop = currentTop.getParent();
		}
		return lastTop.eContainer(); // MWindow -> MApp 
	}

	private static MPart findElementById(MPart part, String id) {
		if (id == null || id.length() == 0)
			return null;

		// is it me?
		if (id.equals(part.getId()))
			return part;

		// Recurse
		EList children = part.getChildren();
		MPart foundPart = null;
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			MPart childPart = (MPart) iterator.next();
			foundPart = findElementById(childPart, id);
			if (foundPart != null)
				return foundPart;
		}

		return null;
	}

	public static MPart findPart(MPart toSearch, String id) {
		MPart found = findElementById(toSearch, id);
		if (found instanceof MPart)
			return (MPart) found;

		return null;
	}

}