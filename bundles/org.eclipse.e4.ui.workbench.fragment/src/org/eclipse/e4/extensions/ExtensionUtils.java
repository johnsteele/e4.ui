package org.eclipse.e4.extensions;

import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;

public class ExtensionUtils {
	public static IConfigurationElement[] getExtensions(String extensionId) {
		IExtensionRegistry registry = InternalPlatform.getDefault().getRegistry();
		String extId = "org.eclipse.ui." + extensionId; //$NON-NLS-1$
		IConfigurationElement[] exts = registry.getConfigurationElementsFor(extId);
		return exts;
	}
	
	public static  IConfigurationElement findExtension(IConfigurationElement[] extensions, String attId, String id) {
		if (id == null || id.length() == 0)
			return null;
		
		for (int i = 0; i < extensions.length; i++) {
			String extId = extensions[i].getAttribute(attId);
			if (id.equals(extId))
				return extensions[i];
		}
		
		return null;
	}
	
	public static  IConfigurationElement findExtension(IConfigurationElement[] extensions, String id) {
		return findExtension(extensions, "id", id); //$NON-NLS-1$
	}
}
