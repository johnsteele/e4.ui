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

package org.eclipse.e4.workbench.ui.api;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.ui.model.application.MContributedPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.WorkbenchPlugin;

/**
 * @since 3.3
 * 
 */
public class LegacySelectionService implements ISelectionService {

	ListenerList selectionListeners = new ListenerList(ListenerList.IDENTITY);
	ListenerList postSelectionListeners = new ListenerList(
			ListenerList.IDENTITY);

	private IEclipseContext windowContext;

	public LegacySelectionService(IEclipseContext context) {
		windowContext = context;
		windowContext.runAndTrack(new Runnable() {

			public void run() {
				fireSelectionChange();
			}

			/*
			 * for debugging purposes only
			 */
			@Override
			public String toString() {
				return IServiceConstants.SELECTION;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.ISelectionService#addPostSelectionListener(org.eclipse
	 * .ui.ISelectionListener)
	 */
	public void addPostSelectionListener(ISelectionListener listener) {
		postSelectionListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.ISelectionService#addPostSelectionListener(java.lang.String
	 * , org.eclipse.ui.ISelectionListener)
	 */
	public void addPostSelectionListener(String partId,
			ISelectionListener listener) {
		postSelectionListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.ISelectionService#addSelectionListener(org.eclipse.ui.
	 * ISelectionListener)
	 */
	public void addSelectionListener(ISelectionListener listener) {
		selectionListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.ISelectionService#addSelectionListener(java.lang.String,
	 * org.eclipse.ui.ISelectionListener)
	 */
	public void addSelectionListener(String partId, ISelectionListener listener) {
		selectionListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISelectionService#getSelection()
	 */
	public ISelection getSelection() {
		Object obj = windowContext.get(IServiceConstants.SELECTION);
		if (obj instanceof ISelection) {
			return (ISelection) obj;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISelectionService#getSelection(java.lang.String)
	 */
	public ISelection getSelection(String partId) {
		return getSelection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.ISelectionService#removePostSelectionListener(org.eclipse
	 * .ui.ISelectionListener)
	 */
	public void removePostSelectionListener(ISelectionListener listener) {
		postSelectionListeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.ISelectionService#removePostSelectionListener(java.lang
	 * .String, org.eclipse.ui.ISelectionListener)
	 */
	public void removePostSelectionListener(String partId,
			ISelectionListener listener) {
		postSelectionListeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.ISelectionService#removeSelectionListener(org.eclipse.
	 * ui.ISelectionListener)
	 */
	public void removeSelectionListener(ISelectionListener listener) {
		selectionListeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.ISelectionService#removeSelectionListener(java.lang.String
	 * , org.eclipse.ui.ISelectionListener)
	 */
	public void removeSelectionListener(String partId,
			ISelectionListener listener) {
		selectionListeners.remove(listener);
	}

	protected void fireSelectionChange() {
		fireSelection(getActivePart(), getSelection());
		firePostSelection(getActivePart(), getSelection());
	}

	private IWorkbenchPart getActivePart() {
		IEclipseContext currentActive = windowContext;
		IEclipseContext child;
		while ((child = (IEclipseContext) currentActive
				.get(IServiceConstants.ACTIVE_CHILD)) != null
				&& child != currentActive) {
			currentActive = child;
		}
		if (child != null) {
			MContributedPart p = (MContributedPart) child
					.get(MContributedPart.class.getName());
			if (p == null)
				return null;
			Object object = p.getObject();
			if (object == null)
				return null;
			if (object instanceof IWorkbenchPart)
				return (IWorkbenchPart) p.getObject();
		}
		return null;
	}

	protected void fireSelection(final IWorkbenchPart part, final ISelection sel) {
		Object[] array = selectionListeners.getListeners();
		for (int i = 0; i < array.length; i++) {
			final ISelectionListener l = (ISelectionListener) array[i];
			if ((part != null && sel != null)
					|| l instanceof INullSelectionListener) {

				try {
					l.selectionChanged(part, sel);
				} catch (Exception e) {
					WorkbenchPlugin.log(e);
				}
			}
		}
	}

	protected void firePostSelection(final IWorkbenchPart part,
			final ISelection sel) {
		Object[] array = postSelectionListeners.getListeners();
		for (int i = 0; i < array.length; i++) {
			final ISelectionListener l = (ISelectionListener) array[i];
			if ((part != null && sel != null)
					|| l instanceof INullSelectionListener) {

				try {
					l.selectionChanged(part, sel);
				} catch (Exception e) {
					WorkbenchPlugin.log(e);
				}
			}
		}
	}
}
