package org.eclipse.ui.internal.tweaklets;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.internal.EditorAreaHelper;
import org.eclipse.ui.internal.EditorManager;
import org.eclipse.ui.internal.EditorReference;
import org.eclipse.ui.internal.EditorSite;
import org.eclipse.ui.internal.NavigationHistory;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.internal.registry.EditorDescriptor;

public class TabBehaviourAutoPin extends TabBehaviour {

	public IEditorReference findReusableEditor(WorkbenchPage page) {
		// allow only the active editor to be replaced, and only if it is
		// not dirty or pinned
		IEditorPart activeEditor = page.getActiveEditor();
		if (activeEditor != null) {
			EditorReference activeEditorReference = (EditorReference) page
					.getReference(activeEditor);
			if (activeEditorReference != null
					&& !activeEditorReference.isDirty()
					&& !activeEditorReference.isPinned()) {
				return activeEditorReference;
			}
		}
		return null;
	}

	public boolean alwaysShowPinAction() {
		return true;
	}

	public IEditorReference reuseInternalEditor(WorkbenchPage page,
			EditorManager manager, EditorAreaHelper editorPresentation,
			EditorDescriptor desc, IEditorInput input,
			IEditorReference reusableEditorRef) {
		IEditorPart reusableEditor = reusableEditorRef.getEditor(false);
		if (reusableEditor == null) {
			IEditorReference result = new EditorReference(manager, input, desc);
			page.closeEditor(reusableEditorRef, false);
			return result;
		}

		EditorSite site = (EditorSite) reusableEditor.getEditorSite();
		EditorDescriptor oldDesc = site.getEditorDescriptor();
		if ((desc.getId().equals(oldDesc.getId()))
				&& (reusableEditor instanceof IReusableEditor)) {
			Workbench wb = (Workbench) page.getWorkbenchWindow().getWorkbench();
			editorPresentation.moveEditor(reusableEditor, -1);
			wb.getEditorHistory().add(reusableEditor.getEditorInput(),
					site.getEditorDescriptor());
			page.reuseEditor((IReusableEditor) reusableEditor, input);
			return reusableEditorRef;
		}
		// findReusableEditor(...) checks pinned and saves editor if
		// necessary, so it's OK to close "reusableEditor"
		IEditorReference ref = new EditorReference(manager, input, desc);
		IPreferenceStore store = ((Workbench) page.getWorkbenchWindow()
				.getWorkbench()).getPreferenceStore();
		NavigationHistory history = (NavigationHistory) page
				.getNavigationHistory();
		history.updateCookieForTab(site.getPane(), ((EditorReference) ref)
				.getPane());
		reusableEditor.getEditorSite().getPage().closeEditor(reusableEditor,
				false);
		return ref;
	}
	
	public void setPreferenceVisibility(Composite editorReuseGroup,
			Button showMultipleEditorTabs) {
		editorReuseGroup.setVisible(false);
		showMultipleEditorTabs.setVisible(false);
	}
	
	public boolean autoPinOnDirty() {
		return true;
	}
	
	public boolean isPerTabHistoryEnabled() {
		return true;
	}
	
	public int getReuseEditorMatchFlags(int originalMatchFlags) {
		return IWorkbenchPage.MATCH_ID | IWorkbenchPage.MATCH_INPUT;
	}

	public int getEditorReuseThreshold() {
		return 1;
	}
	
	public boolean enableMRUTabVisibility() {
		return false;
	}
}
