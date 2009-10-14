package org.eclipse.e4.workbench.ui.api;

import org.eclipse.e4.ui.model.application.MView;
import org.eclipse.e4.ui.model.application.MViewStack;

import org.eclipse.ui.IPlaceholderFolderLayout;

public class ModeledPlaceholderFolderLayout implements IPlaceholderFolderLayout {

	protected MViewStack folderModel;

	public ModeledPlaceholderFolderLayout(MViewStack stackModel) {
		folderModel = stackModel;
	}

	public void addPlaceholder(String viewId) {
		MView viewModel = ModeledPageLayout.createViewModel(viewId, false);
		folderModel.getChildren().add(viewModel);
	}

	public String getProperty(String id) {
		Object propVal = null;
		return propVal == null ? "" : propVal.toString(); //$NON-NLS-1$
	}

	public void setProperty(String id, String value) {
		// folderModel.setProperty(id, value);
	}

}
