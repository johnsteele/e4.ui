package org.eclipse.e4.workbench.ui.api;

import org.eclipse.e4.ui.model.application.MView;
import org.eclipse.e4.ui.model.application.MViewStack;

import org.eclipse.ui.IFolderLayout;

public class ModeledFolderLayout extends ModeledPlaceholderFolderLayout
		implements IFolderLayout {
	public ModeledFolderLayout(MViewStack stackModel) {
		super(stackModel);
	}

	public void addView(String viewId) {
		MView viewModel = ModeledPageLayout.createViewModel(viewId, true);
		folderModel.getChildren().add(viewModel);
	}
}
