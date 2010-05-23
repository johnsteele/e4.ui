package org.eclipse.e4.demo.simpleide.services;

import org.osgi.framework.FrameworkUtil;

public class OSGiUtil {
	public static final String getBundleName(Class<?> clazz) {
		return FrameworkUtil.getBundle(clazz).getSymbolicName();
	}
}
