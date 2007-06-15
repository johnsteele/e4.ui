package org.eclipse.ui.tools.futureWithMenus.dynamic;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

public class LoadUnloadHandler extends AbstractHandler implements
		IElementUpdater {

	public static final String LOCATION = "bundleLocation"; //$NON-NLS-1$

	public void updateElement(UIElement element, Map parameters) {
		String location = (String) parameters.get(LOCATION);
		if (location == null)
			return;

		element.setChecked(DynamicTools.getBundle(location) != null);
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);

		String location = (String) event.getParameter(LOCATION);
		try {
			if (location == null) {
				DirectoryDialog dd = new DirectoryDialog(window.getShell());
				dd.setMessage("Select bundle location.");
				String desiredLocation = dd.open();
				if (desiredLocation == null)
					return null;

				File file = new File(desiredLocation);
				try {
					URL url = file.toURL();
					Bundle bundle = DynamicTools.installBundle("reference:"
							+ url.toExternalForm());
					BundleHistory.getInstance().addBundleReference(
							bundle.getSymbolicName(),
							"reference:" + url.toExternalForm());
					BundleHistory.getInstance().save();
				} catch (MalformedURLException e) {
					throw new ExecutionException(file.toString()
							+ " is not a valid path.", e);
				} catch (BundleException e) {
					throw new ExecutionException(e.getMessage(), e);
				}

			} else {
				Bundle bundle = DynamicTools.getBundle(location);

				if (bundle == null) {
					load(location);
				} else {
					unload(bundle);
				}
			}
		} finally {
			ICommandService cs = (ICommandService) window
					.getService(ICommandService.class);
			cs.refreshElements(event.getCommand().getId(), null);
		}
		return null;
	}

	private void unload(Bundle bundle) throws ExecutionException {
		try {
			DynamicTools.uninstallBundle(bundle);
		} catch (BundleException e) {
			throw new ExecutionException("", e);
		}
	}

	private void load(String location) throws ExecutionException {
		try {
			DynamicTools.installBundle(location);
		} catch (IllegalStateException e) {
			throw new ExecutionException("", e);
		} catch (BundleException e) {
			throw new ExecutionException("", e);
		}
	}
}
