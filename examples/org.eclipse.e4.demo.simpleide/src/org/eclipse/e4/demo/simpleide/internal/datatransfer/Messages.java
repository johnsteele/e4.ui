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
package org.eclipse.e4.demo.simpleide.internal.datatransfer;

public interface Messages {
	public String WizardProjectsImportPage_ProjectLabel();
	public String WizardProjectsImportPage_ImportProjectsTitle();
	public String WizardProjectsImportPage_ImportProjectsDescription();
	public String WizardProjectsImportPage_CopyProjectsIntoWorkspace();
	public String WizardProjectsImportPage_ProjectsListTitle();
	public String WizardProjectsImportPage_RootSelectTitle();
	public String WizardProjectsImportPage_ArchiveSelectTitle();
	public String WizardProjectsImportPage_SearchingMessage();
	public String WizardProjectsImportPage_ProcessingMessage();
	public String WizardProjectsImportPage_ProjectsInWorkspace();
	public String WizardProjectsImportPage_NoProjectsToImport();
	public String WizardProjectsImportPage_CheckingMessage();
	public String WizardProjectsImportPage_SelectDialogTitle();
	public String WizardProjectsImportPage_SelectArchiveDialogTitle();
	public String WizardProjectsImportPage_CreateProjectsTask();
	
	public String WizardProjectsImportPage_SelectAll();
	public String WizardProjectsImportPage_DeselectAll();
	public String WizardProjectsImportPage_Refresh();
	public String WizardProjectsImportPage_browse();
	
	public String WizardExternalProjectImportPage_ErrorMessage();
	
	
	public String ImportOperation_ImportTask();
	public String ImportOperation_EmptyString();
	public String ImportOperation_importProblems();
	public String ImportOperation_coreImportError();
	public String ImportOperation_targetSameAsSourceError();
	public String ImportOperation_openStreamError();
	public String ImportOperation_closeStreamError();
	public String ImportOperation_cannotCopy();
	
	public String ZipImport_badFormat();
	public String ZipImport_couldNotRead();
	public String ZipImport_couldNotClose();
	
	public String TarImport_invalid_tar_format();
	public String TarImport_badFormat();

}
