package org.eclipse.e4.workbench.ui.api;

import org.eclipse.e4.ui.model.application.MContributedPart;
import org.eclipse.e4.ui.model.application.MStack;
import org.eclipse.ui.IFolderLayout;

public class ModeledFolderLayout extends ModeledPlaceholderFolderLayout implements
		IFolderLayout {
	public ModeledFolderLayout(MStack stackModel) {
		super(stackModel);
	}

	public void addView(String viewId) {
		MContributedPart viewModel = ModeledPageLayout.createViewModel(viewId, true);
		folderModel.getChildren().add(viewModel);
	}
}
