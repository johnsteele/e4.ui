package org.eclipse.e4.demo.simpleide.navigator.internal.handler;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.demo.simpleide.navigator.IProjectCreator;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
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
	private Map<IConfigurationElement, Image> images = new HashMap<IConfigurationElement, Image>();
	
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
				viewer.setContentProvider(new ArrayContentProvider());
				viewer.setLabelProvider(new LabelProvider() {
					@Override
					public String getText(Object element) {
						IConfigurationElement el = (IConfigurationElement) element;
						return el.getAttribute("label");
					}
					
					@Override
					public Image getImage(Object element) {
						IConfigurationElement el = (IConfigurationElement) element;
						Image img = images.get(el);
						if( img == null ) {
							try {
								img = ((IProjectCreator)el.createExecutableExtension("delegateClass")).createIcon(getShell().getDisplay());
								images.put(el, img);
							} catch (CoreException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						return img;
					}
				});
				
				IExtensionRegistry reg = RegistryFactory.getRegistry();
				viewer.setInput(reg.getConfigurationElementsFor("org.eclipse.e4.demo.simpleide.navigator.projectcreators"));
				
				viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
				
				return comp; 
			}
		};
		dialog.open();
	}
}
