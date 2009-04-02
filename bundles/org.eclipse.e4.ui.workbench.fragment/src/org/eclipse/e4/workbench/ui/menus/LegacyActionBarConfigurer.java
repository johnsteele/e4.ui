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

package org.eclipse.e4.workbench.ui.menus;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.presentations.AbstractPresentationFactory;

/**
 * @since 3.3
 * 
 */
public class LegacyActionBarConfigurer implements IActionBarConfigurer {
	CoolBarManager cbm = null;
	MenuManager mm = null;
	StatusLineManager slm = null;
	private IWorkbenchWindow window;

	/**
	 * @param window
	 */
	public LegacyActionBarConfigurer(IWorkbenchWindow window) {
		this.window = window;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.application.IActionBarConfigurer#getCoolBarManager()
	 */
	public ICoolBarManager getCoolBarManager() {
		if (cbm == null) {
			cbm = new CoolBarManager();
		}
		return cbm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.application.IActionBarConfigurer#getMenuManager()
	 */
	public IMenuManager getMenuManager() {
		if (mm == null) {
			mm = new MenuManager();
		}
		return mm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.application.IActionBarConfigurer#getStatusLineManager()
	 */
	public IStatusLineManager getStatusLineManager() {
		if (slm == null) {
			slm = new StatusLineManager();
		}
		return slm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.application.IActionBarConfigurer#getWindowConfigurer()
	 */
	public IWorkbenchWindowConfigurer getWindowConfigurer() {
		return new IWorkbenchWindowConfigurer(){
		
			public void setTitle(String title) {
				// TODO Auto-generated method stub
				
			}
		
			public void setShowStatusLine(boolean show) {
				// TODO Auto-generated method stub
				
			}
		
			public void setShowProgressIndicator(boolean show) {
				// TODO Auto-generated method stub
				
			}
		
			public void setShowPerspectiveBar(boolean show) {
				// TODO Auto-generated method stub
				
			}
		
			public void setShowMenuBar(boolean show) {
				// TODO Auto-generated method stub
				
			}
		
			public void setShowFastViewBars(boolean enable) {
				// TODO Auto-generated method stub
				
			}
		
			public void setShowCoolBar(boolean show) {
				// TODO Auto-generated method stub
				
			}
		
			public void setShellStyle(int shellStyle) {
				// TODO Auto-generated method stub
				
			}
		
			public void setPresentationFactory(AbstractPresentationFactory factory) {
				// TODO Auto-generated method stub
				
			}
		
			public void setInitialSize(Point initialSize) {
				// TODO Auto-generated method stub
				
			}
		
			public void setData(String key, Object data) {
				// TODO Auto-generated method stub
				
			}
		
			public IStatus saveState(IMemento memento) {
				// TODO Auto-generated method stub
				return null;
			}
		
			public IWorkbenchConfigurer getWorkbenchConfigurer() {
				// TODO Auto-generated method stub
				return null;
			}
		
			public IWorkbenchWindow getWindow() {
				return window;
			}
		
			public String getTitle() {
				// TODO Auto-generated method stub
				return null;
			}
		
			public boolean getShowStatusLine() {
				// TODO Auto-generated method stub
				return false;
			}
		
			public boolean getShowProgressIndicator() {
				// TODO Auto-generated method stub
				return false;
			}
		
			public boolean getShowPerspectiveBar() {
				// TODO Auto-generated method stub
				return false;
			}
		
			public boolean getShowMenuBar() {
				// TODO Auto-generated method stub
				return false;
			}
		
			public boolean getShowFastViewBars() {
				// TODO Auto-generated method stub
				return false;
			}
		
			public boolean getShowCoolBar() {
				// TODO Auto-generated method stub
				return false;
			}
		
			public int getShellStyle() {
				// TODO Auto-generated method stub
				return 0;
			}
		
			public AbstractPresentationFactory getPresentationFactory() {
				// TODO Auto-generated method stub
				return null;
			}
		
			public Point getInitialSize() {
				// TODO Auto-generated method stub
				return null;
			}
		
			public Object getData(String key) {
				// TODO Auto-generated method stub
				return null;
			}
		
			public IActionBarConfigurer getActionBarConfigurer() {
				// TODO Auto-generated method stub
				return null;
			}
		
			public Control createStatusLineControl(Composite parent) {
				// TODO Auto-generated method stub
				return null;
			}
		
			public Control createPageComposite(Composite parent) {
				// TODO Auto-generated method stub
				return null;
			}
		
			public Menu createMenuBar() {
				// TODO Auto-generated method stub
				return null;
			}
		
			public Control createCoolBarControl(Composite parent) {
				// TODO Auto-generated method stub
				return null;
			}
		
			public void configureEditorAreaDropListener(
					DropTargetListener dropTargetListener) {
				// TODO Auto-generated method stub
				
			}
		
			public void addEditorAreaTransfer(Transfer transfer) {
				// TODO Auto-generated method stub
				
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.application.IActionBarConfigurer#registerGlobalAction(
	 * org.eclipse.jface.action.IAction)
	 */
	public void registerGlobalAction(IAction action) {
		System.err.println("Better register a handler for: " + action); //$NON-NLS-1$
	}

}
