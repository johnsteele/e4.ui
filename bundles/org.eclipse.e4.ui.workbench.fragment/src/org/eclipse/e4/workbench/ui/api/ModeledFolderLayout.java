package org.eclipse.e4.workbench.ui.api;

import org.eclipse.e4.ui.model.application.ContributedPart;
import org.eclipse.e4.ui.model.application.Stack;
import org.eclipse.ui.IFolderLayout;

public class ModeledFolderLayout extends ModeledPlaceholderFolderLayout implements
		IFolderLayout {
	public ModeledFolderLayout(Stack stackModel) {
		super(stackModel);
	}

	public void addView(String viewId) {
		ContributedPart viewModel = ModeledPageLayout.createViewModel(viewId, true);
		folderModel.getChildren().add(viewModel);
	}
}
