/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.tweaklts.workingsets.internal;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.IWorkingSetPage;

public class NewResourceWizardPage extends WizardPage implements
		IWorkingSetPage {

	private Text regularExpressionField;
	private IWorkingSet workingSet;
	private Text workingSetName;

	public NewResourceWizardPage() {
		super("Resource Working Set");
	}
	
	public NewResourceWizardPage(String pageName) {
		super(pageName);
	}

	public NewResourceWizardPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	public void finish() {
		if (workingSet == null) {
			IWorkingSetManager workingSetManager = PlatformUI.getWorkbench()
					.getWorkingSetManager();
			workingSet = workingSetManager.createWorkingSet(
					getWorkingSetName(), new IAdaptable[0]);
		} else {
			workingSet.setName(getWorkingSetName());
			workingSet.setElements(new IAdaptable[0]);
		}
		WorkingSetExpressionManager.getInstance().map(workingSet,
				ResourcesPlugin.getWorkspace().getRoot(),
				Pattern.compile(regularExpressionField.getText()));
	}

	private String getWorkingSetName() {
		return workingSetName.getText();
	}

	public IWorkingSet getSelection() {
		return workingSet;
	}

	public void setSelection(IWorkingSet workingSet) {
		this.workingSet = workingSet;		
	}

	public void createControl(Composite parent) {
		Composite contents = new Composite(parent, SWT.NONE);
		contents.setLayout(new GridLayout(2, false));

		Label label = new Label(contents, SWT.NONE);
		label.setText("Name:");
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false,
				false));

		workingSetName = new Text(contents, SWT.NONE);
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, true,
				false));
		workingSetName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if ("".equals(workingSetName.getText().trim()))
					setErrorMessage("Empty name");
				else
					setErrorMessage("");
			}
		});

		label = new Label(contents, SWT.NONE);
		label.setText("Root:");
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false,
				false));

		Text rootField = new Text(contents, SWT.READ_ONLY);
		rootField.setText("Workspace");
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, true,
				false));

		label = new Label(contents, SWT.NONE);
		label.setText("Regular Expression:");
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false,
				false));

		regularExpressionField = new Text(contents, SWT.NONE);
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, true,
				false));
		regularExpressionField.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				try {
					Pattern.compile(regularExpressionField.getText());
					setErrorMessage("");
				} catch (PatternSyntaxException p) {
					setErrorMessage(p.getMessage());
				}
			}
		});
		
		if (workingSet != null) {
			Pattern pattern = WorkingSetExpressionManager.getInstance().getPattern(workingSet);
			//IAdaptable root = WorkingSetExpressionManager.getInstance().getRoot(workingSet);
			if (pattern != null) 
				regularExpressionField.setText(pattern.pattern());
			workingSetName.setText(workingSet.getName());
		}
		
		setControl(contents);
	}

}
