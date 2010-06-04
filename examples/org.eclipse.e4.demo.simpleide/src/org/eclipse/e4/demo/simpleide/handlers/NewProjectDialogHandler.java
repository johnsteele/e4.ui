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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.inject.Named;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.statusreporter.StatusReporter;
import org.eclipse.e4.demo.simpleide.internal.ServiceRegistryComponent;
import org.eclipse.e4.demo.simpleide.services.INLSLookupFactoryService;
import org.eclipse.e4.demo.simpleide.services.IProjectService;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
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
	private Map<IProjectService, Image> images = new HashMap<IProjectService, Image>();
	private String projectName = ""; //$NON-NLS-1$
	private IProjectService creator;

	@Execute
	public void openNewProjectDialog(
			@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell,
			IWorkspace workspace, IProgressMonitor monitor,
			final ServiceRegistryComponent serviceRegistry,
			StatusReporter reporter, Logger logger,
			final INLSLookupFactoryService nlsFactory) {
		
		TitleAreaDialog dialog = new TitleAreaDialog(parentShell) {
			private Text projectName;
			private TableViewer projectType;
			private Messages messages = nlsFactory
					.createNLSLookup(Messages.class);

			@Override
			protected int getShellStyle() {
				return super.getShellStyle() | SWT.SHEET;
			}

			@Override
			protected Control createDialogArea(Composite parent) {
				Composite comp = (Composite) super.createDialogArea(parent);
				getShell().setText(messages.NewProjectDialogHandler_ShellTitle());
				setTitle(messages.NewProjectDialogHandler_Title());
				setMessage(messages.NewProjectDialogHandler_Message());

				final Image titleImage = new Image(parent.getDisplay(),
						getClass().getClassLoader().getResourceAsStream(
								"/icons/wizard/newprj_wiz.png"));

				setTitleImage(titleImage);

				final Image shellImg = new Image(parent.getDisplay(),
						getClass().getClassLoader().getResourceAsStream(
								"/icons/newprj_wiz.gif"));
				getShell().setImage(shellImg);
				getShell().addDisposeListener(new DisposeListener() {

					public void widgetDisposed(DisposeEvent e) {
						shellImg.dispose();
						titleImage.dispose();
					}
				});

				Composite container = new Composite(comp, SWT.NONE);
				container.setLayoutData(new GridData(GridData.FILL_BOTH));
				container.setLayout(new GridLayout(2, false));

				Label l = new Label(container, SWT.NONE);
				l.setText(messages.NewProjectDialogHandler_Name());

				projectName = new Text(container, SWT.BORDER);
				projectName
						.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

				l = new Label(container, SWT.NONE);
				l.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
				l.setText(messages.NewProjectDialogHandler_Type());

				projectType = new TableViewer(container);
				projectType.setContentProvider(new ArrayContentProvider());
				projectType.setLabelProvider(new LabelProvider() {
					@Override
					public String getText(Object element) {
						IProjectService el = (IProjectService) element;
						return el.getLabel();
					}

					@Override
					public Image getImage(Object element) {
						IProjectService el = (IProjectService) element;
						Image img = images.get(el);
						if (img == null) {
							URL url;
							InputStream in = null;
							try {
								url = FileLocator.find(new URL(el.getIconURI()));
								in = url.openStream();
								img = new Image(getShell().getDisplay(), in);
								images.put(el, img);
							} catch (MalformedURLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} finally {
								if (in != null) {
									try {
										in.close();
									} catch (IOException e) {
									}
								}
							}
						}
						return img;
					}
				});

				Vector<IProjectService> creators = serviceRegistry
						.getCreators();
				projectType.setInput(creators);
				if (creators.size() > 0) {
					projectType.setSelection(new StructuredSelection(creators
							.get(0)));
				}
				projectType.getControl().setLayoutData(
						new GridData(GridData.FILL_BOTH));

				getShell().addDisposeListener(new DisposeListener() {

					public void widgetDisposed(DisposeEvent e) {
						for (Image img : images.values()) {
							img.dispose();
						}
						images.clear();
					}
				});

				return comp;
			}

			@Override
			protected void okPressed() {
				if (projectType.getSelection().isEmpty()) {
					setMessage("Please select a project type",
							IMessageProvider.ERROR);
					return;
				}

				if (projectName.getText().trim().length() == 0) {
					setMessage("Please enter a projectname",
							IMessageProvider.ERROR);
					return;
				}

				NewProjectDialogHandler.this.creator = (IProjectService) ((IStructuredSelection) projectType
						.getSelection()).getFirstElement();
				NewProjectDialogHandler.this.projectName = projectName
						.getText();

				super.okPressed();
			}
		};

		if (dialog.open() == IDialogConstants.OK_ID) {
			creator.createProject(parentShell, workspace, reporter, logger,
					monitor, projectName);
		}
	}
}
