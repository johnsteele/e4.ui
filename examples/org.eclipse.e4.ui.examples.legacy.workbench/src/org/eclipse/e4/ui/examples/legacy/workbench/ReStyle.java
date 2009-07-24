package org.eclipse.e4.ui.examples.legacy.workbench;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.workbench.ui.internal.Workbench;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

public class ReStyle extends AbstractHandler implements IHandler {


	public Object execute(ExecutionEvent event) throws ExecutionException {
		Workbench workbench = (Workbench) HandlerUtil.getVariable(event, Workbench.class.getName());
		
		final Shell shell = (Shell) workbench.getWindow();
		final Display display = shell.getDisplay();

		display.syncExec(new Runnable() {
			public void run() {
				try {
					final CSSEngine engine = (CSSEngine) display
						.getData("org.eclipse.e4.ui.css.core.engine"); //$NON-NLS-1$

					//Read in the style sheet again since it may have changed
					URL url = (URL) display.getData("org.eclipse.e4.ui.css.core.cssURL"); //$NON-NLS-1$
					InputStream stream = url.openStream();
					InputStreamReader streamReader = new InputStreamReader(stream);
					
					engine.reset();
					engine.parseStyleSheet(streamReader);
					stream.close();
					streamReader.close();
					engine.applyStyles(shell, true, false);
					shell.layout(true, true);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		return null;
	}
}
