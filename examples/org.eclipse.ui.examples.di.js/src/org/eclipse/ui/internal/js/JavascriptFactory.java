package org.eclipse.ui.internal.js;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.js.SWTFactory;
import org.eclipse.ui.services.IServiceLocator;
import org.eclipse.ui.tweaklets.dependencyinjection.DIFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;
import org.osgi.framework.Bundle;

public class JavascriptFactory extends DIFactory implements
		IExecutableExtension {

	private String bundleName;
	private String filename;

	public Object createObject(IServiceLocator services) {
		Context context = Context.enter();
		try {
			final Bundle bundle = Platform.getBundle(bundleName);

			context
					.setApplicationClassLoader(new BundleProxyClassLoader(
							bundle));
			Scriptable scope = new ImporterTopLevel(context);

			URLConnection connection = bundle.getResource(filename)
					.openConnection();
			String encoding = connection.getContentEncoding();
			Reader reader = encoding == null ? new InputStreamReader(connection
					.getInputStream()) : new InputStreamReader(connection
					.getInputStream(), encoding);
			context.evaluateReader(scope, reader, filename, 0, null);
			reader.close();

			Scriptable array = (Scriptable) scope.get("argumentTypes", scope);

			List arguments = new ArrayList();
			Object element;
			int i = 0;
			while ((element = array.get(i++, array)) != Scriptable.NOT_FOUND) {
				final String typeName = Context.toString(element);
				Class c = bundle.loadClass(typeName);
				Object argument;
				if (c == SWTFactory.class) {
					argument = new SWTFactory();
				} else {
					argument = services.getService(c);
				}
				arguments.add(argument);
			}

			Function constructorFunction = (Function) scope.get("construct",
					scope);

			Object result = constructorFunction.call(context, scope, scope,
					arguments.toArray());

			return result;

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			Context.exit();
		}
	}

	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {

		if (!(data instanceof String)) {
			throw new CoreException(
					new Status(
							IStatus.ERROR,
							"org.eclipse.ui.js", "initialization data must be a string")); //$NON-NLS-1$
		}

		this.bundleName = config.getContributor().getName();
		this.filename = (String) data;

	}

}
