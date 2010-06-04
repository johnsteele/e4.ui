package org.eclipse.e4.demo.simpleide.jdt.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.e4.demo.simpleide.services.AbstractBundleImageProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class JDTImageServiceProvider extends AbstractBundleImageProvider {
	private static final Bundle BUNDLE = FrameworkUtil.getBundle(JDTImageServiceProvider.class);
	private static final String BUNDLE_NAME = BUNDLE.getSymbolicName();
	
	public static final String IMG_PACKAGE_DECL = BUNDLE_NAME + ".IMG_PACKAGE_DECL";
	public static final String IMG_IMPORTCONTAINER_DECL = BUNDLE_NAME + ".IMG_IMPORTCONTAINER_DECL";
	
	private static final Map<String, String> IMAGEMAP = Collections.unmodifiableMap(new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put(JDTImageServiceProvider.IMG_PACKAGE_DECL, "/icons/outline/packd_obj.gif");
			put(JDTImageServiceProvider.IMG_IMPORTCONTAINER_DECL, "/icons/impc_obj.gif");
		}
	});
	
	@Override
	protected Map<String, String> getImageMap() {
		return IMAGEMAP;
	}

	@Override
	protected Bundle getBundle(String imageKey) {
		return BUNDLE;
	}
	
}
