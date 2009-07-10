package org.eclipse.e4.demo.modifier;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.e4.demo.viewer.ModelUtils;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.model.workbench.MWorkbenchWindow;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.workbench.ui.renderers.PartFactory;
import org.eclipse.e4.workbench.ui.renderers.PartRenderer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;


public class ShowUIEditor extends AbstractHandler {
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("show UI Editor");
		MApplication model = (MApplication) HandlerUtil.getVariable(event, MApplication.class.getName());
		Shell curWindow = (Shell) model.getContext().get(IServiceConstants.ACTIVE_SHELL);
		if (curWindow == null)
			return null;
		
		MWorkbenchWindow winPart = (MWorkbenchWindow) curWindow.getData(PartFactory.OWNING_ME);
		MPart uiEditor = ModelUtils.findPart(winPart, "UI Editor");
		if (uiEditor.isVisible()) {
			Shell editorShell = (Shell) uiEditor.getWidget();
			if(editorShell == null) {
				uiEditor.setVisible(false);
			}
		}
		uiEditor.setVisible(true);
		return null;
	}
	
}
