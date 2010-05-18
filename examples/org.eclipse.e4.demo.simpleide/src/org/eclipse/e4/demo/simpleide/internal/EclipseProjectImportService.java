package org.eclipse.e4.demo.simpleide.internal;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.statusreporter.StatusReporter;
import org.eclipse.e4.demo.simpleide.internal.datatransfer.ExternalProjectImportWizard;
import org.eclipse.e4.demo.simpleide.services.IImportResourceService;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.FrameworkUtil;

public class EclipseProjectImportService implements IImportResourceService {

	public String getIconURI() {
		return "platform:/plugin/" + FrameworkUtil.getBundle(getClass()).getSymbolicName() + "/icons/newprj_wiz.gif";
	}

	public String getLabel() {
		return "Existing Projects Into Workspace";
	}

	public void importResource(Shell shell, IWorkspace workspace, StatusReporter reporter, Logger logger) {
		ExternalProjectImportWizard wz = new ExternalProjectImportWizard(workspace, reporter, logger);
		WizardDialog dialog = new WizardDialog(shell, wz);
		dialog.open();
	}

	public String getCategoryName() {
		return "General";
	}

}
