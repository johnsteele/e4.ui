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

import org.eclipse.e4.ui.model.application.MApplicationPackage;
import org.eclipse.e4.ui.model.application.MEditorStack;
import org.eclipse.e4.ui.model.application.MElementContainer;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.model.application.MPerspective;
import org.eclipse.e4.ui.model.application.MViewStack;

import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.ISaveablesLifecycleListener;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.SubActionBars;

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

		/*
		 * We create partOpened / partClosed events based on both MPartWidget
		 * events.
		 * 
		 * The initial MPartVisible events are sent early and don't have the
		 * actual implementation included. The MPartWidget events are sent as a
		 * final step of "part is created" and therefore we use it to create
		 * partOpened events. When the part is closed the widget is set to null.
		 */
		if (MApplicationPackage.Literals.UI_ELEMENT__WIDGET.equals(notification
				.getFeature())) {
			if (notification.getEventType() != Notification.SET)
				return;
			if (notification.getOldValue() == notification.getNewValue())
				return; // avoid extra notifications
			Object part = notification.getNotifier();
			if (part instanceof MPart) {
				IWorkbenchPartReference ref = toPartRef((MPart) part);
				if (ref != null) {
					boolean isVisible = ((MPart) part).isVisible();
					if (isVisible) {
						if (notification.getNewValue() == null) {
							/*
							 * not sure if this is the right place to fix bug
							 * 283922 but if there is no widget and
							 * isVisible==true, we must be shutting down, and we
							 * should not send partOpened notifications.
							 */
							return;
						}
						SaveablesList modelManager = (SaveablesList) ref
								.getPart(true).getSite().getService(
										ISaveablesLifecycleListener.class);
						modelManager.postOpen(ref.getPart(true));
						partList.firePartOpened(ref);
					}
				}
			}
			return;
		}

		// Interpret E4 activation events:
		if (!MApplicationPackage.Literals.ELEMENT_CONTAINER__ACTIVE_CHILD
				.equals(notification.getFeature()))
			return;

		// at this time we only interpreting SET events
		if (notification.getEventType() != Notification.SET
				|| notification.getEventType() != Notification.CREATE)
			return;

		// make sure something actually changed
		Object oldPart = notification.getOldValue();
		Object newPart = notification.getNewValue();

		// create 3.x visibility events
		if ((newPart != oldPart) && (oldPart instanceof MPart)
				&& (newPart instanceof MPart))
			changeVisibility((MPart) oldPart, (MPart) newPart);

		// create 3.x activation events
		final Object object = e4Context.get(IServiceConstants.ACTIVE_PART);
		if ((newPart != oldPart) && newPart instanceof MPerspective) {
			// let legacy Workbench know about perspective activation
			IWorkbenchPage page = (IWorkbenchPage) e4Context
					.get(IWorkbenchPage.class.getName());
			if (page != null) {
				String id = ((MPerspective) newPart).getId();
				IPerspectiveDescriptor[] descriptors = page
						.getOpenPerspectives();
				for (IPerspectiveDescriptor desc : descriptors) {
					if (!id.equals(desc.getId()))
						continue;
					page.setPerspective(desc);
				}
			}
		}
		if (object instanceof MPart) {
			IWorkbenchPartReference ref = toPartRef((MPart) object);
			if (ref != null) {
				// set the Focus to the newly active part
				IWorkbenchPart part = ref.getPart(true);
				part.setFocus();
				// Update the action bars
				SubActionBars bars = (SubActionBars) ((PartSite) part.getSite())
						.getActionBars();
				bars.partChanged(part);
				partList.setActivePart(ref);
				if (ref instanceof IEditorReference) {
					IEditorReference editorReference = (IEditorReference) ref;
					partList.setActiveEditor(editorReference);
					final IEditorPart editor = editorReference.getEditor(true);
					e4Context.set(ISources.ACTIVE_EDITOR_NAME, editor);
					e4Context.set(ISources.ACTIVE_EDITOR_ID_NAME, editor
							.getSite().getId());
					e4Context.set(ISources.ACTIVE_EDITOR_INPUT_NAME, editor
							.getEditorInput());
				}
			}
		} else {
			partList.setActiveEditor(null);
			e4Context.set(ISources.ACTIVE_EDITOR_NAME, null);
			e4Context.set(ISources.ACTIVE_EDITOR_ID_NAME, null);
			e4Context.set(ISources.ACTIVE_EDITOR_INPUT_NAME, null);
			partList.setActivePart(null);
		}
	}

	// TBD default hidden/shown state
	private void changeVisibility(MPart oldPart, MPart newPart) {
		// TBD old parent vs new parent: should we apply the same logic to both?
		// if parent was a stack: hide the previously active part
		MElementContainer oldParent = oldPart.getParent();
		if (oldParent instanceof MViewStack
				|| oldParent instanceof MEditorStack)
			visiblityChange(oldPart, false);
		// show new part
		visiblityChange(newPart, true);
	}

	private void visiblityChange(MPart part, boolean visible) {
		IWorkbenchPartReference partRef = toPartRef(part);
		if (visible) {
			// TBD do we need to make children/parents visible or will there be
			// separate notifications?
			if (partRef != null)
				partList.firePartVisible(partRef); // make this part visible
		} else {
			if (partRef != null)
				partList.firePartHidden(partRef); // hide this part
		}
	}

	// TBD is this the best method?
	// TBD this should be a general utility method somewhere in the fragment
	private IWorkbenchPartReference toPartRef(MPart part) {
		if (part == null)
			return null;
		Object impl = part.getObject();
		if (!(impl instanceof IWorkbenchPart))
			return null;
		PartSite site = (PartSite) ((IWorkbenchPart) impl).getSite();
		return site.getPartReference();
	}

}
