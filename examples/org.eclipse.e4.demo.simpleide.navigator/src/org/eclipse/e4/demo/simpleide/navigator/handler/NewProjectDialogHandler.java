package org.eclipse.e4.demo.simpleide.navigator.handler;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class NewProjectDialogHandler {
	
	@Execute
	public void openNewProjectEditor(@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell) {
		TitleAreaDialog dialog = new TitleAreaDialog(parentShell) {
			@Override
			protected Control createDialogArea(Composite parent) {
				Composite comp =  (Composite) super.createDialogArea(parent);
				getShell().setText("New Project");
				setTitle("New Project");
				setMessage("Create a new project by entering a name and select a project type");
				
				final Image titleImage = new Image(parent.getDisplay(), getClass().getClassLoader().getResourceAsStream("/icons/wizard/newjprj_wiz.png"));
				
				setTitleImage(titleImage);
				
				final Image shellImg = new Image(parent.getDisplay(), getClass().getClassLoader().getResourceAsStream("/icons/newjprj_wiz.gif"));
				getShell().setImage(shellImg);
				getShell().addDisposeListener(new DisposeListener() {
					
					public void widgetDisposed(DisposeEvent e) {
						shellImg.dispose();
						titleImage.dispose();
					}
				});
				
				Composite container = new Composite(comp, SWT.NONE);
				container.setLayoutData(new GridData(GridData.FILL_BOTH));
				container.setLayout(new GridLayout(2,false));
				
				Label l = new Label(container, SWT.NONE);
				l.setText("Name");
				
				Text t = new Text(container, SWT.BORDER);
				t.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				
				l = new Label(container, SWT.NONE);
				l.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
				l.setText("Type"); 
				
				TableViewer viewer = new TableViewer(container);
				viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
				
				return comp; 
			}
		};
		dialog.open();
	}
}
