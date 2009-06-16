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

import java.util.ArrayList;

import org.eclipse.e4.ui.model.application.ApplicationPackage;
import org.eclipse.e4.ui.model.application.MContributedPart;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * Tracks model events.
 */
public class ModelTracker extends EContentAdapter {

	final private TreeViewer viewer;
	private boolean processNotifications = true;
	

	public ModelTracker(TreeViewer viewer) {
		this.viewer = viewer;
	}
	
	public void suspend() {
		processNotifications = false;
	}

	public void resume() {
		processNotifications = true;
	}

	public void notifyChanged(Notification notification) {
		if (!processNotifications)
			return;
		
		int type = notification.getEventType();
		switch (type) {
			case Notification.SET:
				processSet(notification);
				break;
			case Notification.ADD:			// intentionally fall through
			case Notification.ADD_MANY:		// intentionally fall through
			case Notification.REMOVE:		// intentionally fall through
			case Notification.REMOVE_MANY:	// intentionally fall through
				processStructure(notification);
				break;
			default:
		}
	}
	
	/**
	 * Tracks activation events
	 */
	private void processSet(Notification notification) {
		if (!ApplicationPackage.Literals.MPART__ACTIVE_CHILD.equals(notification.getFeature()))
			return;
		// TBD we are getting successive activation events starting from leaves and going up.
		// What would be the way to differentiate the original event from all propagated
		// events?
		Object newPart = notification.getNewValue();
		if (!(newPart instanceof MContributedPart<?>))
			return;
		TreePath path = formPath((MPart<?>) newPart);
		viewer.setSelection(new TreeSelection(path), true);
	}

	/**
	 * Tracks structural changes
	 */
	private void processStructure(Notification notification) {
		if (!(ApplicationPackage.Literals.MPART__CHILDREN.equals(notification.getFeature())))
			return;
		viewer.refresh();
	}

	/**
	 * Creates a selection path for the tree viewer
	 */
	private TreePath formPath(MPart<?> part) {
		ArrayList<MPart<?>> path = new ArrayList<MPart<?>>();
		do {
			path.add(part);
			part = part.getParent();
		} while (part != null);
		// write segments in the reverse order
		Object[] result = new Object[path.size()];
		int pos = result.length - 1;
		for(MPart<?> segment : path) {
			result[pos] = segment;
			pos--;
		}
		return new TreePath(result);
	}

}