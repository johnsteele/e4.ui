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


import javax.inject.Named;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.demo.simpleide.internal.Messages;
import org.eclipse.e4.demo.simpleide.services.INLSLookupFactoryService;
import org.eclipse.e4.demo.simpleide.ui.ResourceViewerControl;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class NewFolderDialogHandler {
	private IContainer parentContainer;
	private String name;
	
	@Execute
	public void openNewTextFileDialog(
			@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell,
			@Optional @Named(IServiceConstants.SELECTION) final IResource resource,
			final IWorkspace workspace,
			IProgressMonitor monitor,
			final INLSLookupFactoryService nlsFactory) {
		
		TitleAreaDialog dialog = new TitleAreaDialog(parentShell) {
			private ResourceViewerControl viewer;
			private Text text;
			
			@Override
			protected Control createDialogArea(Composite parent) {
				Messages messages = nlsFactory.createNLSLookup(Messages.class);
				
				getShell().setText(messages.NewFolderDialogHandler_ShellTitle());
				setTitle(messages.NewFolderDialogHandler_Title());
				setMessage(messages.NewFolderDialogHandler_Message());
				
				Composite comp = (Composite) super.createDialogArea(parent);
				Composite container = new Composite(comp,SWT.NONE);
				container.setLayoutData(new GridData(GridData.FILL_BOTH));
				container.setLayout(new GridLayout(2,false));
				
				Label label = new Label(container, SWT.NONE);
				label.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
				label.setText(messages.NewFolderDialogHandler_ParentFolder());
				
				viewer = new ResourceViewerControl(container, SWT.NONE, workspace, resource);
				viewer.setLayoutData(new GridData(GridData.FILL_BOTH));
				
				label = new Label(container, SWT.NONE);
				label.setText(messages.NewFolderDialogHandler_FolderName());
				
				text = new Text(container, SWT.BORDER);
				text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				
				return comp;
			}
			
			@Override
			protected void okPressed() {
				IResource resource = viewer.getResource();
				if( resource == null ) {
					setMessage("Select a parent folder", IMessageProvider.ERROR);
					return;
				}
				
				if( text.getText().trim().length() == 0 ) {
					setMessage("Enter a folder name", IMessageProvider.ERROR);
					return;
				}
				
				parentContainer = (IContainer) resource;
				name = text.getText();
				super.okPressed();
			}
		};
		
		if( dialog.open() == IDialogConstants.OK_ID ) {
			IPath newFolderPath = parentContainer.getFullPath().append(name);
			IFolder folder = workspace.getRoot().getFolder(newFolderPath);
			try {
				folder.create(false, true, monitor);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
