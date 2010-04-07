/*******************************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.demo.javascript;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.WeakHashMap;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.contributions.IContributionFactorySpi;
import org.mozilla.javascript.Scriptable;
import org.osgi.framework.Bundle;

public class JSContributionFactory implements IContributionFactorySpi {

	private JSUtil js;
	private WeakHashMap contributionDatas = new WeakHashMap();

	public JSContributionFactory() {
		js = new JSUtil();
	}

	private static class ContributionWrapper {
		Object contribution;

		public ContributionWrapper(Object c) {
			contribution = c;
		}
	}

	private static class ContributionData {
		Bundle bundle;
		String name;
		String script;
		IEclipseContext context;

		private Object updateContribution(JSUtil js, Object contribution) {
			InputStream stream;
			try {
				stream = bundle.getResource(name).openStream();
				String newScript = readString(stream);
				if (script != null && script.equals(newScript)) {
					return contribution;
				}
				script = newScript;
				contribution = js.eval(script);
				Object initargs = js.get(contribution, "initargs");
				if (initargs instanceof Scriptable) {
					int len = js.length(initargs);
					Object[] args = new Object[len];
					for (int i = 0; i < len; i++) {
						args[i] = context.get((String) js.get(initargs, i));
					}
					js.call(js.get(contribution, "init"), contribution, args);
				}
				return contribution;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	}

	private static String readString(InputStream is) throws IOException {
		StringBuffer result = new StringBuffer();
		BufferedInputStream bis = new BufferedInputStream(is);
		int ch;
		while ((ch = bis.read()) != -1) {
			result.append((char) ch);
		}
		bis.close();
		return result.toString();
	}

	public Object create(Bundle bundle, String name,
			IEclipseContext context) {
		ContributionData cd = new ContributionData();
		cd.bundle = bundle;
		cd.name = name;
		cd.context = context;
		Object result = cd.updateContribution(js, null);
		ContributionWrapper wrapper = new ContributionWrapper(result);
		contributionDatas.put(wrapper, cd);
		return wrapper;
	}

	public Object call(Object o, String methodName,
			IEclipseContext context, Object defaultValue) {
		ContributionWrapper wrapper = (ContributionWrapper) o;
		ContributionData cd = (ContributionData) contributionDatas.get(wrapper);
		wrapper.contribution = cd.updateContribution(js, wrapper.contribution);
		Object runargs = js.get(wrapper.contribution, methodName + "args");
		int len = js.length(runargs);
		Object[] args = new Object[len];
		for (int i = 0; i < len; i++) {
			args[i] = context.get((String) js.get(runargs, i));
		}
		Object function = js.get(wrapper.contribution, methodName);

		if (function== null || !(function instanceof Scriptable)) {
			if (defaultValue != null) {
				return defaultValue;
			}
			throw new RuntimeException(
					"could not find function " + methodName + " in " + cd.name); //$NON-NLS-1$//$NON-NLS-2$
		}

		Object result = js.call(function,
				wrapper.contribution, args);
		return result;
	}

}
