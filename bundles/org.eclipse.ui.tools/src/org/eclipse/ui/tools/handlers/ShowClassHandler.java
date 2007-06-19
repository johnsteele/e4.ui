package org.eclipse.ui.tools.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.tools.Messages;

public class ShowClassHandler extends AbstractHandler {

	@SuppressWarnings("unused")
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		Shell shell = HandlerUtil.getActiveShell(event);
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection sel = (IStructuredSelection) selection;
			String msg = null;
			if (sel.isEmpty()) {
				msg = Messages.showClass_no_selection;
			} else {
				msg = sel.getFirstElement().getClass().getName();
			}
			MessageDialog.openInformation(shell, Messages.showClass_info_title, msg);
		}
		return null;
	}
}
