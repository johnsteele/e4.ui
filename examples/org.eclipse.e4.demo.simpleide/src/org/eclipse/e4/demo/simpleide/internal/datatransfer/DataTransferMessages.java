package org.eclipse.e4.demo.simpleide.internal.datatransfer;

import org.eclipse.osgi.util.NLS;

public class DataTransferMessages {
	static {
		NLS.initializeMessages(DataTransferMessages.class.getName(), DataTransferMessages.class);
	}
	
	public static String WizardProjectsImportPage_projectLabel;
	public static String WizardProjectsImportPage_ImportProjectsTitle;
	public static String WizardProjectsImportPage_ImportProjectsDescription;
	public static String WizardProjectsImportPage_CopyProjectsIntoWorkspace;
	public static String WizardProjectsImportPage_ProjectsListTitle;
	public static String DataTransfer_selectAll;
	public static String DataTransfer_deselectAll;
	public static String DataTransfer_refresh;
	public static String WizardProjectsImportPage_RootSelectTitle;
	public static String DataTransfer_browse;
	public static String WizardProjectsImportPage_ArchiveSelectTitle;
	public static String WizardProjectsImportPage_SearchingMessage;
	public static String WizardProjectsImportPage_ProcessingMessage;
	public static String WizardProjectsImportPage_projectsInWorkspace;
	public static String WizardProjectsImportPage_noProjectsToImport;
	public static String ZipImport_badFormat;
	public static String ZipImport_couldNotRead;
	public static String WizardProjectsImportPage_CheckingMessage;
	public static String WizardProjectsImportPage_SelectDialogTitle;
	public static String WizardProjectsImportPage_SelectArchiveDialogTitle;
	public static String WizardExternalProjectImportPage_errorMessage;
	public static String WizardProjectsImportPage_CreateProjectsTask;
	public static String TarImport_invalid_tar_format;
	public static String TarImport_badFormat;
	public static String ZipImport_couldNotClose;
	public static String DataTransfer_importTask;
	public static String ImportOperation_importProblems;
	public static String ImportOperation_coreImportError;
	public static String ImportOperation_targetSameAsSourceError;
	public static String ImportOperation_openStreamError;
	public static String ImportOperation_closeStreamError;
	public static String ImportOperation_cannotCopy;
	public static String DataTransfer_emptyString; 

}
