package org.eclipse.e4.workbench.ui.api;

import org.eclipse.e4.ui.model.application.MView;

import org.eclipse.ui.IViewLayout;

public class ModeledViewLayout implements IViewLayout {

	// private MContributedPart viewME;

	public ModeledViewLayout(MView view) {
		// viewME = view;
	}

	public boolean getShowTitle() {
		return true;// viewME.getShowTitle();
	}

	public boolean isCloseable() {
		return true;// viewME.isCloseable();
	}

	public boolean isMoveable() {
		return true;// viewME.isMoveable();
	}

	public boolean isStandalone() {
		return false;// viewME.isStandAlone();
	}

	public void setCloseable(boolean closeable) {
		// viewME.setCloseable(closeable);
	}

	public void setMoveable(boolean moveable) {
		// viewME.setMoveable(moveable);
	}

}
