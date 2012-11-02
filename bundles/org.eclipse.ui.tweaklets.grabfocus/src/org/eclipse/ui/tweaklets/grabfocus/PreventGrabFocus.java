/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     sxenos@ca.ibm.com - initial code
 ******************************************************************************/
package org.eclipse.ui.tweaklets.grabfocus;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.tweaklets.GrabFocus;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;

/**
 * 
 */
public class PreventGrabFocus extends GrabFocus {
	private static int[] eventIds = { SWT.KeyDown, SWT.KeyUp, SWT.MouseDown,
			SWT.MouseUp, SWT.MouseDoubleClick, SWT.Selection, SWT.MenuDetect,
			SWT.DragDetect };

	private Display display;

	private boolean allowFocus = false;
	private boolean filterAdded = false;

	private Listener listener = new Listener() {
		public void handleEvent(Event event) {
			enableInput();
		}
	};

	private Runnable disabler = new Runnable() {
		public void run() {
			allowFocus = false;
			addFilters();
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.tweaklets.GrabFocusManager#allowGrabFocus(org.eclipse.ui.IWorkbenchPart)
	 */
	public boolean grabFocusAllowed(IWorkbenchPart part) {
		if (!allowFocus) {
			IWorkbenchSiteProgressService progressService = (IWorkbenchSiteProgressService) part
					.getSite().getAdapter(IWorkbenchSiteProgressService.class);

			// Bold the tab if possible
			if (progressService != null) {
				progressService.warnOfContentChange();
			}
		}
		return allowFocus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.tweaklets.GrabFocusManager#init(org.eclipse.swt.widgets.Display)
	 */
	public void init(Display theDisplay) {
		display = theDisplay;
		addFilters();
	}

	public void dispose() {
		removeFilters();
		listener = null;
		allowFocus = false;
	}

	private void addFilters() {
		if (filterAdded) {
			return;
		}
		filterAdded = true;

		for (int i = 0; i < eventIds.length; i++) {
			int id = eventIds[i];
			display.addFilter(id, listener);
		}
	}

	private void removeFilters() {
		if (!filterAdded) {
			return;
		}
		filterAdded = false;

		for (int i = 0; i < eventIds.length; i++) {
			int id = eventIds[i];
			display.removeFilter(id, listener);
		}
	}

	private void enableInput() {
		if (allowFocus) {
			return;
		}
		allowFocus = true;
		removeFilters();
		display.asyncExec(disabler);
	}

}
