package org.eclipse.e4.demo.javascript;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.WeakHashMap;

import org.eclipse.e4.core.services.IContributionFactorySpi;
import org.eclipse.e4.core.services.IServiceLocator;
import org.osgi.framework.Bundle;

import org.mozilla.javascript.Scriptable;

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
		IServiceLocator serviceLocator;

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
						args[i] = serviceLocator.getService(bundle
								.loadClass((String) js.get(initargs, i)));
					}
					js.call(js.get(contribution, "init"), contribution, args);
				}
				return contribution;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
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
			IServiceLocator serviceLocator) {
		ContributionData cd = new ContributionData();
		cd.bundle = bundle;
		cd.name = name;
		cd.serviceLocator = serviceLocator;
		Object result = cd.updateContribution(js, null);
		ContributionWrapper wrapper = new ContributionWrapper(result);
		contributionDatas.put(wrapper, cd);
		return wrapper;
	}

	public Object call(Object o, String methodName,
			IServiceLocator serviceLocator, Object defaultValue) {
		ContributionWrapper wrapper = (ContributionWrapper) o;
		ContributionData cd = (ContributionData) contributionDatas.get(wrapper);
		wrapper.contribution = cd.updateContribution(js, wrapper.contribution);
		Object runargs = js.get(wrapper.contribution, methodName + "args");
		int len = js.length(runargs);
		Object[] args = new Object[len];
		for (int i = 0; i < len; i++) {
			try {
				args[i] = serviceLocator.getService(cd.bundle
						.loadClass((String) js.get(runargs, i)));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
