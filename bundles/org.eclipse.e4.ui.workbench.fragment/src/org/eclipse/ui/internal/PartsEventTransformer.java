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
package org.eclipse.ui.internal;

import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.ui.model.application.ApplicationPackage;
import org.eclipse.e4.ui.model.application.MContributedPart;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.model.application.MStack;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;

/**
 * Transforms E4 MPart events into 3.x legacy events.
 */
public class PartsEventTransformer extends EContentAdapter {

	final private IEclipseContext e4Context;
	final private WorkbenchPagePartList partList;

	/**
	 * Constructor.
	 * 
	 * @param e4Context
	 * @param partList
	 */
	public PartsEventTransformer(IEclipseContext e4Context,
			WorkbenchPagePartList partList) {
		this.e4Context = e4Context;
		this.partList = partList;
	}

	public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);

		// at this time we only interpreting E4 activation events
		if (!ApplicationPackage.Literals.MPART__ACTIVE_CHILD
				.equals(notification.getFeature()))
			return;

		// at this time we only interpreting SET events
		if (notification.getEventType() != Notification.SET)
			return;

		// make sure something actually changed
		Object oldPart = notification.getOldValue();
		Object newPart = notification.getNewValue();

		// create 3.x visibility events
		if ((newPart != oldPart) && (oldPart instanceof MContributedPart<?>)
				&& (newPart instanceof MContributedPart<?>))
			changeVisibility((MContributedPart<?>) oldPart,
					(MContributedPart<?>) newPart);

		// create 3.x activation events
		final Object object = e4Context.get(IServiceConstants.ACTIVE_PART);
		if (object instanceof MContributedPart<?>) {
			IWorkbenchPartReference ref = toPartRef((MContributedPart<?>) object);
			if (ref != null) {
				partList.setActivePart(ref);
				if (ref instanceof IEditorReference)
					partList.setActiveEditor((IEditorReference) ref);
			}
		} else {
			partList.setActiveEditor(null);
			partList.setActivePart(null);
		}
	}

	// TBD default hidden/shown state
	private void changeVisibility(MContributedPart<?> oldPart,
			MContributedPart<?> newPart) {
		// TBD old parent vs new parent: should we apply the same logic to both?
		// if parent was a stack: hide the previously active part
		MPart<?> oldParent = ((MContributedPart<?>) oldPart).getParent();
		if (oldParent instanceof MStack)
			visiblityChange(oldPart, false);
		// show new part
		visiblityChange(newPart, true);
	}

	private void visiblityChange(MContributedPart<?> part, boolean visible) {
		IWorkbenchPartReference partRef = toPartRef(part);
		if (visible) {
			// TBD do we need to make children/parents visible or will there be
			// separate notifications?
			if (partRef != null)
				partList.firePartVisible(partRef); // make this part visible
		} else {
			// hide children
			for (Object child : part.getChildren()) {
				if (child instanceof MContributedPart<?>)
					visiblityChange((MContributedPart<?>) child, visible);
			}
			if (partRef != null)
				partList.firePartHidden(partRef); // hide this part
		}
	}

	// TBD is this the best method?
	// TBD this should be a general utility method somewhere in the fragment
	private IWorkbenchPartReference toPartRef(MContributedPart<?> part) {
		if (part == null)
			return null;
		Object impl = part.getObject();
		if (!(impl instanceof IWorkbenchPart))
			return null;
		PartSite site = (PartSite) ((IWorkbenchPart) impl).getSite();
		return site.getPartReference();
	}

}
