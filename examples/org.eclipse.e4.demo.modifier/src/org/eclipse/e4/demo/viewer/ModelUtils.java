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

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.contexts.IContextConstants;
import org.eclipse.e4.ui.model.application.MContext;
import org.eclipse.e4.ui.model.application.MElementContainer;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.model.application.MUIElement;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.swt.internal.AbstractPartRenderer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

/**
 * Pieces of code that really should be available as APIs somewhere.
 */
public class ModelUtils {

	/**
	 * Return a parent context for this part.
	 * 
	 * @param part
	 *            the part to start searching from
	 * @return the parent's closest context, or global context if none in the
	 *         hierarchy
	 */
	public static IEclipseContext getContextForParent(MUIElement part) {
		MContext pwc = getParentWithContext(part);
		return pwc != null ? pwc.getContext() : null;
	}

	/**
	 * Return a parent context for this part.
	 * 
	 * @param part
	 *            the part to start searching from
	 * @return the parent's closest context, or global context if none in the
	 *         hierarchy
	 */
	public static MContext getParentWithContext(MUIElement part) {
		MElementContainer<MUIElement> parent = part.getParent();
		while (parent != null) {
			if (parent instanceof MContext) {
				if (((MContext) parent).getContext() != null)
					return (MContext) parent;
			}
			parent = parent.getParent();
		}
		return null;
	}

	/**
	 * Return a context for this part.
	 * 
	 * @param element
	 *            the part to start searching from
	 * @return the closest context, or global context if none in the hierarchy
	 */
	public static IEclipseContext getContext(MUIElement element) {
		if (element instanceof MContext) {
			IEclipseContext theContext = ((MContext) element).getContext();
			if (theContext != null)
				return theContext;
		}
		return getContextForParent(element);
	}
	
	/**
	 * Activate the part in the hierarchy. This should either still be internal
	 * or be a public method somewhere else.
	 * 
	 * @param element
	 */
	public static void activate(MUIElement element) {
		IEclipseContext curContext = getContext(element);
		MUIElement curElement = element;
		MContext pwc = getParentWithContext(element);
		while (pwc != null) {
			IEclipseContext parentContext = pwc.getContext();
			if (parentContext != null) {
				parentContext.set(IContextConstants.ACTIVE_CHILD, curContext);
				curContext = parentContext;
			}

			// Ensure that the UI model has the part 'on top'
			while (curElement != pwc) {
				MElementContainer<MUIElement> parent = curElement.getParent();
				if (parent.getActiveChild() != element)
					parent.setActiveChild(curElement);
				curElement = parent;
			}

			pwc = getParentWithContext((MUIElement) pwc);
		}
	}
	
	/**
	 * Get model element associated with the composite or
	 * its containers.
	 * This method may return null.
	 */
	static MUIElement getElement(Composite composite) {
		for (Composite container = composite; container != null; container = container.getParent()) {
			Object owner = container.getData(AbstractPartRenderer.OWNING_ME);
			if (owner == null)
				continue;
			if (owner instanceof MUIElement)
				return (MUIElement) owner;
		}
		return null;
	}
	
	/**
	 * Get context associated with the composite or its containers.
	 * This method may return null.
	 */
	static IEclipseContext getContext(Composite composite) {
		MUIElement element = getElement(composite);
		return getContext(element);
	}
	
	/**
	 * Returns a model root for a model tree element.
	 */
	static public EObject topObject(MUIElement part) {
		MUIElement lastTop = part;
		MUIElement currentTop = part.getParent();
		while (currentTop != null) {
			lastTop = currentTop;
			currentTop = currentTop.getParent();
		}
		return ((EObject) lastTop).eContainer(); // MWindow -> MApp 
	}

	private static MUIElement findElementById(MUIElement element, String id) {
		if (id == null || id.length() == 0)
			return null;

		// is it me?
		if (id.equals(element.getId()))
			return element;

		// Recurse
		if (element instanceof MElementContainer<?>) {
			MUIElement foundPart = null;
			EList<MUIElement> children = ((MElementContainer<MUIElement>) element).getChildren();
			for (MUIElement childPart : children) {
				foundPart = findElementById(childPart, id);
				if (foundPart != null)
					return foundPart;
			}
		}

		return null;
	}

	public static MPart findPart(MUIElement toSearch, String id) {
		MPart found = (MPart) findElementById(toSearch, id);
		if (found instanceof MPart)
			return (MPart) found;

		return null;
	}

}