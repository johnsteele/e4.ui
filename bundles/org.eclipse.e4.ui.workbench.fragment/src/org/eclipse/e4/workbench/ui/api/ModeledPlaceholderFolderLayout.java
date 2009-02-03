package org.eclipse.e4.workbench.ui.api;

import org.eclipse.e4.ui.model.application.MContributedPart;
import org.eclipse.e4.ui.model.application.MStack;
import org.eclipse.ui.IPlaceholderFolderLayout;


public class ModeledPlaceholderFolderLayout implements IPlaceholderFolderLayout {

	protected MStack folderModel;

	public ModeledPlaceholderFolderLayout(MStack stackModel) {
		folderModel = stackModel;
	}

	public void addPlaceholder(String viewId) {
		MContributedPart viewModel = ModeledPageLayout.createViewModel(viewId, false);
		folderModel.getChildren().add(viewModel);
	}

	public String getProperty(String id) {
		Object propVal = null;
		return propVal == null ? "" : propVal.toString(); //$NON-NLS-1$
	}

	public void setProperty(String id, String value) {
		//folderModel.setProperty(id, value);
	}

}
