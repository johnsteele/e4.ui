package org.eclipse.e4.demo.simpleide.navigator.internal.handler;


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
			IProgressMonitor monitor) {
		
		TitleAreaDialog dialog = new TitleAreaDialog(parentShell) {
			private ResourceViewerControl viewer;
			private Text text;
			
			@Override
			protected Control createDialogArea(Composite parent) {
				Composite comp = (Composite) super.createDialogArea(parent);
				Composite container = new Composite(comp,SWT.NONE);
				container.setLayoutData(new GridData(GridData.FILL_BOTH));
				container.setLayout(new GridLayout(2,false));
				
				Label label = new Label(container, SWT.NONE);
				label.setText("Parent Folder");
				
				viewer = new ResourceViewerControl(container, SWT.NONE, workspace, resource);
				viewer.setLayoutData(new GridData(GridData.FILL_BOTH));
				
				label = new Label(container, SWT.NONE);
				label.setText("Folder name");
				
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