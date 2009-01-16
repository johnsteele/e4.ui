package org.eclipse.e4.compatibility;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class TestPerspectiveFactory implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
 		String editorArea = layout.getEditorArea();

		IFolderLayout folder= layout.createFolder("left", IPageLayout.LEFT, (float)0.25, editorArea); //$NON-NLS-1$
		//folder.addView("org.eclipse.jdt.ui.PackageExplorer"); //$NON-NLS-1$
		//folder.addView("org.eclipse.jdt.ui.TypeHierarchy"); //$NON-NLS-1$
		folder.addView("LegacyViews.ResourceView"); //$NON-NLS-1$
		//folder.addPlaceholder(IPageLayout.ID_RES_NAV);
		folder.addPlaceholder("org.eclipse.ui.navigator.ProjectExplorer"); //$NON-NLS-1$

		IFolderLayout outputfolder= layout.createFolder("bottom", IPageLayout.BOTTOM, (float)0.75, editorArea); //$NON-NLS-1$
		outputfolder.addView("org.eclipse.pde.runtime.LogView"); //$NON-NLS-1$
		outputfolder.addView(IPageLayout.ID_PROBLEM_VIEW);
		outputfolder.addView("org.eclipse.jdt.ui.JavadocView"); //$NON-NLS-1$
		outputfolder.addView("org.eclipse.team.ccvs.ui.RepositoriesView"); //$NON-NLS-1$
		outputfolder.addView("org.eclipse.debug.ui.DebugView"); //$NON-NLS-1$
//		outputfolder.addView("org.eclipse.jdt.ui.SourceView"); //$NON-NLS-1$
//		outputfolder.addPlaceholder("org.eclipse.search.ui.views.SearchView"); //$NON-NLS-1$
//		outputfolder.addPlaceholder("org.eclipse.ui.console.ConsoleView"); //$NON-NLS-1$
//		outputfolder.addPlaceholder(IPageLayout.ID_BOOKMARKS);
		outputfolder.addPlaceholder("org.eclipse.ui.views.ProgressView"); //$NON-NLS-1$
//
//		IFolderLayout outlineFolder = layout.createFolder("right", IPageLayout.RIGHT, (float)0.75, editorArea); //$NON-NLS-1$
//		outlineFolder.addView(IPageLayout.ID_OUTLINE);
//
//		outlineFolder.addPlaceholder("org.eclipse.ui.texteditor.TemplatesView");
//		// XXX: in 3.4 M7 to be replaced by:
////		outlineFolder.addView(TemplatesView.ID);
//
//		layout.addActionSet("org.eclipse.debug.ui.launchActionSet");
//		layout.addActionSet("org.eclipse.jdt.ui.JavaActionSet");
//		layout.addActionSet("org.eclipse.jdt.ui.JavaElementCreationActionSet");
//		layout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);
//
//		// views - java
//		layout.addShowViewShortcut("org.eclipse.jdt.ui.PackageExplorer");
//		layout.addShowViewShortcut("org.eclipse.jdt.ui.TypeHierarchy");
//		layout.addShowViewShortcut("org.eclipse.jdt.ui.SourceView");
//		layout.addShowViewShortcut("org.eclipse.jdt.ui.JavadocView");
//
//
//		// views - search
//		layout.addShowViewShortcut("org.eclipse.search.ui.views.SearchView");
//
//		// views - debugging
//		layout.addShowViewShortcut("org.eclipse.ui.console.ConsoleView");
//
//		// views - standard workbench
//		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
//		layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
//		layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
//		layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
//		layout.addShowViewShortcut("org.eclipse.ui.views.ProgressView");
//		layout.addShowViewShortcut("org.eclipse.ui.navigator.ProjectExplorer");
//		layout.addShowViewShortcut("org.eclipse.ui.texteditor.TemplatesView");
//
//		// new actions - Java project creation wizard
//		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.JavaProjectWizard"); //$NON-NLS-1$
//		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewPackageCreationWizard"); //$NON-NLS-1$
//		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewClassCreationWizard"); //$NON-NLS-1$
//		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewInterfaceCreationWizard"); //$NON-NLS-1$
//		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewEnumCreationWizard"); //$NON-NLS-1$
//		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewAnnotationCreationWizard"); //$NON-NLS-1$
//		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewSourceFolderCreationWizard");	 //$NON-NLS-1$
//		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewSnippetFileCreationWizard"); //$NON-NLS-1$
//		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewJavaWorkingSetWizard"); //$NON-NLS-1$
//		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");//$NON-NLS-1$
//		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");//$NON-NLS-1$
//		layout.addNewWizardShortcut("org.eclipse.ui.editors.wizards.UntitledTextFileWizard");//$NON-NLS-1$
	}

}
