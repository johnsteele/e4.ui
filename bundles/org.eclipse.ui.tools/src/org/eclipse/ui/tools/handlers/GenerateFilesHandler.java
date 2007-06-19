/**
 * 
 */
package org.eclipse.ui.tools.handlers;

import java.io.ByteArrayInputStream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.internal.util.Util;
import org.eclipse.ui.tools.Activator;

/**
 * 
 */
public class GenerateFilesHandler extends AbstractHandler {
	private static final String contents = "prop.info = Hello\nprop.user = me\n"; //$NON-NLS-1$

	private static final String GEN_PERF_EXTENSION = "genPerf.extension"; //$NON-NLS-1$
	private static final String GEN_PERF_NUM = "genPerf.num"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@SuppressWarnings("restriction")
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String extension = event.getParameter(GEN_PERF_EXTENSION);
		if (extension == null || extension.length() == 0) {
			throw new ExecutionException("Command incorrectly configured: " //$NON-NLS-1$
					+ GEN_PERF_EXTENSION);
		}
		int number = 100;
		String num = event.getParameter(GEN_PERF_NUM);
		if (num != null) {
			try {
				number = Integer.parseInt(num);
			} catch (NumberFormatException e) {
				// do nothing, we'll default to 100
			}
		}
		ISelection sel = HandlerUtil.getCurrentSelectionChecked(event);
		if (!(sel instanceof IStructuredSelection)) {
			throw new ExecutionException("Must select a project"); //$NON-NLS-1$
		}
		IStructuredSelection selection = (IStructuredSelection) sel;
		Object obj = selection.getFirstElement();
		IProject proj = (IProject) Util.getAdapter(obj, IProject.class);
		if (proj == null) {
			throw new ExecutionException("Must select a project"); //$NON-NLS-1$
		}

		new CreateFiles(proj, extension, number).schedule();

		return null;
	}

	private static class CreateFiles extends Job {
		private IProject project;
		private String extension;
		private int number;

		public CreateFiles(IProject proj, String ext, int num) {
			super("CreateFiles"); //$NON-NLS-1$
			project = proj;
			extension = ext;
			number = num;
		}

		protected IStatus run(IProgressMonitor monitor) {
			IStatus rc = Status.OK_STATUS;
			try {
				IFolder folder = project.getFolder(extension);
				if (!folder.exists()) {
					folder.create(true, true, monitor);
				}
				for (int i = 0; i < number && !monitor.isCanceled(); i++) {
					IFile file = folder.getFile("testing_" + i + "." //$NON-NLS-1$ //$NON-NLS-2$
							+ extension);
					if (!file.exists()) {
						ByteArrayInputStream source = new ByteArrayInputStream(
								contents.getBytes());
						file.create(source, true, monitor);
					}
				}
			} catch (CoreException e) {
				rc = new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0,
						"file creation", e); //$NON-NLS-1$
			}
			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}
			return rc;
		}
	}
}
