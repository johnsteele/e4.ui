package org.eclipse.e4.demo.modifier;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.e4.demo.viewer.ModelUtils;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.model.application.MWindow;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.swt.internal.AbstractPartRenderer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;


public class ShowUIEditor extends AbstractHandler {
	public Object execute(ExecutionEvent event) throws ExecutionException {
		MApplication model = (MApplication) HandlerUtil.getVariable(event, MApplication.class.getName());
		Shell curWindow = (Shell) model.getContext().get(IServiceConstants.ACTIVE_SHELL);
		if (curWindow == null)
			return null;
		
		MWindow winPart = (MWindow) curWindow.getData(AbstractPartRenderer.OWNING_ME);
		MPart uiEditor = ModelUtils.findPart(winPart, "UI Editor");
		if (uiEditor.isVisible()) {
			Shell editorShell = (Shell) uiEditor.getWidget();
			if(editorShell == null) {
				uiEditor.setVisible(false);
			}
			else {
				if (!editorShell.isDisposed())
					return null;
			}
		}
		uiEditor.setVisible(true);
		return null;
	}
	
}
