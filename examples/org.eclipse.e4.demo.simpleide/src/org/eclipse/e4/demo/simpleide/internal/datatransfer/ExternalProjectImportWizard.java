/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.demo.simpleide.internal.datatransfer;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.statusreporter.StatusReporter;
//import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;

/**
 * Standard workbench wizard for importing projects defined
 * outside of the currently defined projects into Eclipse.
 * <p>
 * This class may be instantiated and used without further configuration;
 * this class is not intended to be subclassed.
 * </p>
 * <p>
 * Example:
 * <pre>
 * IWizard wizard = new ExternalProjectImportWizard();
 * wizard.init(workbench, selection);
 * WizardDialog dialog = new WizardDialog(shell, wizard);
 * dialog.open();
 * </pre>
 * During the call to <code>open</code>, the wizard dialog is presented to the
 * user. When the user hits Finish, a project is created with the location
 * specified by the user.
 * </p>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class ExternalProjectImportWizard extends Wizard {
//    private static final String EXTERNAL_PROJECT_SECTION = "ExternalProjectImportWizard";//$NON-NLS-1$
	private WizardProjectsImportPage mainPage;
	private IStructuredSelection currentSelection = null;
	private String initialPath = null;
	
	private IWorkspace workspace;
	private StatusReporter statusReporter;
	private Logger logger;
	
    /**
     * Constructor for ExternalProjectImportWizard.
     */
    public ExternalProjectImportWizard(IWorkspace workspace, StatusReporter statusReporter, Logger logger) {
    	this(workspace,statusReporter,logger,null);
    }

    /**
     * Constructor for ExternalProjectImportWizard.
     * 
     * @param initialPath Default path for wizard to import
     * @since 3.5
     */
    public ExternalProjectImportWizard(IWorkspace workspace, StatusReporter statusReporter, Logger logger, String initialPath)
    {
        super();
        this.workspace = workspace;
        this.statusReporter = statusReporter;
        this.logger = logger;
        this.initialPath = initialPath;
        setNeedsProgressMonitor(true);
//        IDialogSettings workbenchSettings = IDEWorkbenchPlugin.getDefault().getDialogSettings();
//        
//		IDialogSettings wizardSettings = workbenchSettings
//		        .getSection(EXTERNAL_PROJECT_SECTION);
//		if (wizardSettings == null) {
//			wizardSettings = workbenchSettings
//		            .addNewSection(EXTERNAL_PROJECT_SECTION);
//		}
//		setDialogSettings(wizardSettings);        
    }

    /* (non-Javadoc)
     * Method declared on IWizard.
     */
    public void addPages() {
        super.addPages();
		mainPage = new WizardProjectsImportPage(
				"wizardExternalProjectsPage", initialPath, currentSelection,workspace,statusReporter,logger); //$NON-NLS-1$
        addPage(mainPage);
    }
    
//TODO SimpleIDE
//    /* (non-Javadoc)
//     * Method declared on IWorkbenchWizard.
//     */
//    public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
//        setWindowTitle(DataTransferMessages.DataTransfer_importTitle);
//        setDefaultPageImageDescriptor(
//				IDEWorkbenchPlugin.getIDEImageDescriptor("wizban/importproj_wiz.png")); //$NON-NLS-1$
//        this.currentSelection = currentSelection;
//    }

    /* (non-Javadoc)
     * Method declared on IWizard.
     */
    public boolean performCancel() {
    	mainPage.performCancel();
        return true;
    }

    /* (non-Javadoc)
     * Method declared on IWizard.
     */
    public boolean performFinish() {
        return mainPage.createProjects();
    }

}