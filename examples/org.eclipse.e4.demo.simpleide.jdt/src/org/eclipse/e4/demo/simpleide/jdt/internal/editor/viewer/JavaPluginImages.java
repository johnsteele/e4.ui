package org.eclipse.e4.demo.simpleide.jdt.internal.editor.viewer;

import java.net.URL;
import java.util.HashMap;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.ImageData;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class JavaPluginImages {
	public static final IPath ICONS_PATH= new Path("$nl$/icons/full"); //$NON-NLS-1$
	
	private static final String T_OBJ= "obj16"; 		//$NON-NLS-1$
	private static final String T_OVR= "ovr16"; 		//$NON-NLS-1$

	private static final String NAME_PREFIX= "org.eclipse.e4.demo.simpleide.jdt."; //$NON-NLS-1$
	private static final int    NAME_PREFIX_LENGTH= NAME_PREFIX.length();
	
	private static ImageRegistry fgImageRegistry= null;
	private static HashMap<String,ImageDescriptor> fgAvoidSWTErrorMap= null;
	public static ImageDescriptorRegistry IMAGE_DESCRIPTOR_REGISTRY = new ImageDescriptorRegistry();
	
	public static final String IMG_MISC_PRIVATE= NAME_PREFIX + "methpri_obj.gif"; 		//$NON-NLS-1$
	public static final String IMG_OBJS_LOCAL_VARIABLE= NAME_PREFIX + "localvariable_obj.gif"; //$NON-NLS-1$
	public static final String IMG_OBJS_PACKDECL= NAME_PREFIX + "packd_obj.gif"; 			//$NON-NLS-1$
	public static final String IMG_OBJS_IMPDECL= NAME_PREFIX + "imp_obj.gif"; 			//$NON-NLS-1$
	public static final String IMG_OBJS_IMPCONT= NAME_PREFIX + "impc_obj.gif"; 			//$NON-NLS-1$
	public static final String IMG_OBJS_EXTJAR= NAME_PREFIX + "jar_l_obj.gif"; 			//$NON-NLS-1$
	public static final String IMG_OBJS_EXTJAR_WSRC= NAME_PREFIX + "jar_lsrc_obj.gif";	//$NON-NLS-1$
	public static final String IMG_OBJS_JAR= NAME_PREFIX + "jar_obj.gif"; 				//$NON-NLS-1$
	public static final String IMG_OBJS_JAR_WSRC= NAME_PREFIX + "jar_src_obj.gif"; 		//$NON-NLS-1$
	public static final String IMG_OBJS_CLASSFOLDER= NAME_PREFIX + "cf_obj.gif"; 			//$NON-NLS-1$
	public static final String IMG_OBJS_CLASSFOLDER_WSRC= NAME_PREFIX + "cf_src_obj.gif"; 		//$NON-NLS-1$
	public static final String IMG_OBJS_PACKFRAG_ROOT= NAME_PREFIX + "packagefolder_obj.gif"; //$NON-NLS-1$
	public static final String IMG_OBJS_CUNIT= NAME_PREFIX + "jcu_obj.gif"; 				//$NON-NLS-1$
	public static final String IMG_OBJS_CFILE= NAME_PREFIX + "classf_obj.gif";  			//$NON-NLS-1$
	public static final String IMG_OBJS_JAVA_MODEL= NAME_PREFIX + "java_model_obj.gif"; //$NON-NLS-1$
	public static final String IMG_OBJS_TYPEVARIABLE= NAME_PREFIX + "typevariable_obj.gif"; 		//$NON-NLS-1$
	public static final String IMG_OBJS_ANNOTATION= NAME_PREFIX + "annotation_obj.gif"; //$NON-NLS-1$
	public static final String IMG_OBJS_GHOST= NAME_PREFIX + "ghost.gif"; 				//$NON-NLS-1$
	public static final String IMG_OBJS_UNKNOWN= NAME_PREFIX + "unknown_obj.gif"; //$NON-NLS-1$
	public static final String IMG_MISC_PUBLIC= NAME_PREFIX + "methpub_obj.gif"; 			//$NON-NLS-1$
	public static final String IMG_MISC_PROTECTED= NAME_PREFIX + "methpro_obj.gif"; 		//$NON-NLS-1$
	public static final String IMG_MISC_DEFAULT= NAME_PREFIX + "methdef_obj.gif"; 		//$NON-NLS-1$
	public static final String IMG_FIELD_PUBLIC= NAME_PREFIX + "field_public_obj.gif"; 			//$NON-NLS-1$
	public static final String IMG_FIELD_PROTECTED= NAME_PREFIX + "field_protected_obj.gif"; 		//$NON-NLS-1$
	public static final String IMG_FIELD_PRIVATE= NAME_PREFIX + "field_private_obj.gif"; 		//$NON-NLS-1$
	public static final String IMG_FIELD_DEFAULT= NAME_PREFIX + "field_default_obj.gif"; 		//$NON-NLS-1$
	public static final String IMG_OBJS_EMPTY_PACK_RESOURCE= NAME_PREFIX + "empty_pack_fldr_obj.gif"; //$NON-NLS-1$
	public static final String IMG_OBJS_EMPTY_PACKAGE= NAME_PREFIX + "empty_pack_obj.gif"; //$NON-NLS-1$
	public static final String IMG_OBJS_PACKAGE= NAME_PREFIX + "package_obj.gif"; 		//$NON-NLS-1$
	public static final String IMG_OBJS_ENUM_ALT= NAME_PREFIX + "enum_alt_obj.gif"; //$NON-NLS-1$
	public static final String IMG_OBJS_ANNOTATION_ALT= NAME_PREFIX + "annotation_alt_obj.gif"; //$NON-NLS-1$
	public static final String IMG_OBJS_INTERFACEALT= NAME_PREFIX + "intf_obj.gif"; 			//$NON-NLS-1$
	public static final String IMG_OBJS_CLASSALT= NAME_PREFIX + "classfo_obj.gif"; 			//$NON-NLS-1$
	public static final String IMG_OBJS_ENUM= NAME_PREFIX + "enum_obj.gif"; //$NON-NLS-1$
	public static final String IMG_OBJS_ENUM_DEFAULT= NAME_PREFIX + "enum_default_obj.gif"; //$NON-NLS-1$
	public static final String IMG_OBJS_ENUM_PROTECTED= NAME_PREFIX + "enum_protected_obj.gif"; //$NON-NLS-1$
	public static final String IMG_OBJS_ENUM_PRIVATE= NAME_PREFIX + "enum_private_obj.gif"; //$NON-NLS-1$
	public static final String IMG_OBJS_ANNOTATION_DEFAULT= NAME_PREFIX + "annotation_default_obj.gif"; //$NON-NLS-1$
	public static final String IMG_OBJS_ANNOTATION_PROTECTED= NAME_PREFIX + "annotation_protected_obj.gif"; //$NON-NLS-1$
	public static final String IMG_OBJS_ANNOTATION_PRIVATE= NAME_PREFIX + "annotation_private_obj.gif"; //$NON-NLS-1$
	public static final String IMG_OBJS_INNER_INTERFACE_PUBLIC= NAME_PREFIX + "innerinterface_public_obj.gif"; //$NON-NLS-1$
	public static final String IMG_OBJS_INNER_INTERFACE_DEFAULT= NAME_PREFIX + "innerinterface_default_obj.gif"; //$NON-NLS-1$
	public static final String IMG_OBJS_INNER_INTERFACE_PROTECTED= NAME_PREFIX + "innerinterface_protected_obj.gif"; //$NON-NLS-1$
	public static final String IMG_OBJS_INNER_INTERFACE_PRIVATE= NAME_PREFIX + "innerinterface_private_obj.gif"; //$NON-NLS-1$
	public static final String IMG_OBJS_INTERFACE_DEFAULT= NAME_PREFIX + "int_default_obj.gif"; 		//$NON-NLS-1$
	public static final String IMG_OBJS_INTERFACE= NAME_PREFIX + "int_obj.gif"; 			//$NON-NLS-1$
	public static final String IMG_OBJS_INNER_CLASS_PUBLIC= NAME_PREFIX + "innerclass_public_obj.gif"; //$NON-NLS-1$
	public static final String IMG_OBJS_INNER_CLASS_DEFAULT= NAME_PREFIX + "innerclass_default_obj.gif"; //$NON-NLS-1$
	public static final String IMG_OBJS_INNER_CLASS_PROTECTED= NAME_PREFIX + "innerclass_protected_obj.gif"; //$NON-NLS-1$
	public static final String IMG_OBJS_INNER_CLASS_PRIVATE= NAME_PREFIX + "innerclass_private_obj.gif"; //$NON-NLS-1$
	public static final String IMG_OBJS_CLASS= NAME_PREFIX + "class_obj.gif"; 			//$NON-NLS-1$
	public static final String IMG_OBJS_CLASS_DEFAULT= NAME_PREFIX + "class_default_obj.gif"; 			//$NON-NLS-1$
	public static final String IMG_OBJ_PROJECT_CLOSED = NAME_PREFIX + "cprj_obj.gif";			//$NON-NLS-1$
	public static final String IMG_OBJ_PROJECT = NAME_PREFIX + "prj_obj.gif";			//$NON-NLS-1$

	public static final ImageDescriptor DESC_MISC_PRIVATE= createManagedFromKey(T_OBJ, IMG_MISC_PRIVATE);
	public static final ImageDescriptor DESC_OBJS_LOCAL_VARIABLE= createManagedFromKey(T_OBJ, IMG_OBJS_LOCAL_VARIABLE);
	public static final ImageDescriptor DESC_OBJS_PACKDECL= createManagedFromKey(T_OBJ, IMG_OBJS_PACKDECL);
	public static final ImageDescriptor DESC_OBJS_IMPDECL= createManagedFromKey(T_OBJ, IMG_OBJS_IMPDECL);
	public static final ImageDescriptor DESC_OBJS_IMPCONT= createManagedFromKey(T_OBJ, IMG_OBJS_IMPCONT);
	public static final ImageDescriptor DESC_OBJS_EXTJAR= createManagedFromKey(T_OBJ, IMG_OBJS_EXTJAR);
	public static final ImageDescriptor DESC_OBJS_EXTJAR_WSRC= createManagedFromKey(T_OBJ, IMG_OBJS_EXTJAR_WSRC);
	public static final ImageDescriptor DESC_OBJS_JAR= createManagedFromKey(T_OBJ, IMG_OBJS_JAR);
	public static final ImageDescriptor DESC_OBJS_JAR_WSRC= createManagedFromKey(T_OBJ, IMG_OBJS_JAR_WSRC);
	public static final ImageDescriptor DESC_OBJS_CLASSFOLDER= createManagedFromKey(T_OBJ, IMG_OBJS_CLASSFOLDER);
	public static final ImageDescriptor DESC_OBJS_CLASSFOLDER_WSRC= createManagedFromKey(T_OBJ, IMG_OBJS_CLASSFOLDER_WSRC);
	public static final ImageDescriptor DESC_OBJS_PACKFRAG_ROOT= createManagedFromKey(T_OBJ, IMG_OBJS_PACKFRAG_ROOT);
	public static final ImageDescriptor DESC_OBJS_CUNIT= createManagedFromKey(T_OBJ, IMG_OBJS_CUNIT);
	public static final ImageDescriptor DESC_OBJS_CFILE= createManagedFromKey(T_OBJ, IMG_OBJS_CFILE);
	public static final ImageDescriptor DESC_OBJS_JAVA_MODEL= createManagedFromKey(T_OBJ, IMG_OBJS_JAVA_MODEL);
	public static final ImageDescriptor DESC_OBJS_TYPEVARIABLE= createManagedFromKey(T_OBJ, IMG_OBJS_TYPEVARIABLE);
	public static final ImageDescriptor DESC_OBJS_ANNOTATION= createManagedFromKey(T_OBJ, IMG_OBJS_ANNOTATION);
	public static final ImageDescriptor DESC_OBJS_GHOST= createManagedFromKey(T_OBJ, IMG_OBJS_GHOST);
	public static final ImageDescriptor DESC_OBJS_UNKNOWN= createManagedFromKey(T_OBJ, IMG_OBJS_UNKNOWN);
	public static final ImageDescriptor DESC_MISC_PUBLIC= createManagedFromKey(T_OBJ, IMG_MISC_PUBLIC);
	public static final ImageDescriptor DESC_MISC_PROTECTED= createManagedFromKey(T_OBJ, IMG_MISC_PROTECTED);
	public static final ImageDescriptor DESC_MISC_DEFAULT= createManagedFromKey(T_OBJ, IMG_MISC_DEFAULT);
	public static final ImageDescriptor DESC_FIELD_PUBLIC= createManagedFromKey(T_OBJ, IMG_FIELD_PUBLIC);
	public static final ImageDescriptor DESC_FIELD_PROTECTED= createManagedFromKey(T_OBJ, IMG_FIELD_PROTECTED);
	public static final ImageDescriptor DESC_FIELD_PRIVATE= createManagedFromKey(T_OBJ, IMG_FIELD_PRIVATE);
	public static final ImageDescriptor DESC_FIELD_DEFAULT= createManagedFromKey(T_OBJ, IMG_FIELD_DEFAULT);
	public static final ImageDescriptor DESC_OBJS_EMPTY_PACKAGE_RESOURCES= createManagedFromKey(T_OBJ, IMG_OBJS_EMPTY_PACK_RESOURCE);
	public static final ImageDescriptor DESC_OBJS_EMPTY_PACKAGE= createManagedFromKey(T_OBJ, IMG_OBJS_EMPTY_PACKAGE);
	public static final ImageDescriptor DESC_OBJS_PACKAGE= createManagedFromKey(T_OBJ, IMG_OBJS_PACKAGE);
	public static final ImageDescriptor DESC_OBJS_ENUM_ALT= createManagedFromKey(T_OBJ, IMG_OBJS_ENUM_ALT);
	public static final ImageDescriptor DESC_OBJS_ANNOTATION_ALT= createManagedFromKey(T_OBJ, IMG_OBJS_ANNOTATION_ALT);
	public static final ImageDescriptor DESC_OBJS_INTERFACEALT= createManagedFromKey(T_OBJ, IMG_OBJS_INTERFACEALT);
	public static final ImageDescriptor DESC_OBJS_CLASSALT= createManagedFromKey(T_OBJ, IMG_OBJS_CLASSALT);
	public static final ImageDescriptor DESC_OBJS_ENUM= createManagedFromKey(T_OBJ, IMG_OBJS_ENUM);
	public static final ImageDescriptor DESC_OBJS_ENUM_DEFAULT= createManagedFromKey(T_OBJ, IMG_OBJS_ENUM_DEFAULT);
	public static final ImageDescriptor DESC_OBJS_ENUM_PROTECTED= createManagedFromKey(T_OBJ, IMG_OBJS_ENUM_PROTECTED);
	public static final ImageDescriptor DESC_OBJS_ENUM_PRIVATE= createManagedFromKey(T_OBJ, IMG_OBJS_ENUM_PRIVATE);
	public static final ImageDescriptor DESC_OBJS_ANNOTATION_DEFAULT= createManagedFromKey(T_OBJ, IMG_OBJS_ANNOTATION_DEFAULT);
	public static final ImageDescriptor DESC_OBJS_ANNOTATION_PROTECTED= createManagedFromKey(T_OBJ, IMG_OBJS_ANNOTATION_PROTECTED);
	public static final ImageDescriptor DESC_OBJS_ANNOTATION_PRIVATE= createManagedFromKey(T_OBJ, IMG_OBJS_ANNOTATION_PRIVATE);
	public static final ImageDescriptor DESC_OBJS_INNER_INTERFACE_PUBLIC= createManagedFromKey(T_OBJ, IMG_OBJS_INNER_INTERFACE_PUBLIC);
	public static final ImageDescriptor DESC_OBJS_INNER_INTERFACE_DEFAULT= createManagedFromKey(T_OBJ, IMG_OBJS_INNER_INTERFACE_DEFAULT);
	public static final ImageDescriptor DESC_OBJS_INNER_INTERFACE_PROTECTED= createManagedFromKey(T_OBJ, IMG_OBJS_INNER_INTERFACE_PROTECTED);
	public static final ImageDescriptor DESC_OBJS_INNER_INTERFACE_PRIVATE= createManagedFromKey(T_OBJ, IMG_OBJS_INNER_INTERFACE_PRIVATE);
	public static final ImageDescriptor DESC_OBJS_INTERFACE_DEFAULT= createManagedFromKey(T_OBJ, IMG_OBJS_INTERFACE_DEFAULT);
	public static final ImageDescriptor DESC_OBJS_INTERFACE= createManagedFromKey(T_OBJ, IMG_OBJS_INTERFACE);
	public static final ImageDescriptor DESC_OBJS_INNER_CLASS_PUBLIC= createManagedFromKey(T_OBJ, IMG_OBJS_INNER_CLASS_PUBLIC);
	public static final ImageDescriptor DESC_OBJS_INNER_CLASS_DEFAULT= createManagedFromKey(T_OBJ, IMG_OBJS_INNER_CLASS_DEFAULT);
	public static final ImageDescriptor DESC_OBJS_INNER_CLASS_PROTECTED= createManagedFromKey(T_OBJ, IMG_OBJS_INNER_CLASS_PROTECTED);
	public static final ImageDescriptor DESC_OBJS_INNER_CLASS_PRIVATE= createManagedFromKey(T_OBJ, IMG_OBJS_INNER_CLASS_PRIVATE);
	public static final ImageDescriptor DESC_OBJS_CLASS= createManagedFromKey(T_OBJ, IMG_OBJS_CLASS);
	public static final ImageDescriptor DESC_OBJS_CLASS_DEFAULT= createManagedFromKey(T_OBJ, IMG_OBJS_CLASS_DEFAULT);
	public static final ImageDescriptor DESC_OVR_DEPRECATED= createUnManagedCached(T_OVR, "deprecated.gif");			//$NON-NLS-1$
	public static final ImageDescriptor DESC_OVR_ABSTRACT= createUnManagedCached(T_OVR, "abstract_co.gif"); 					//$NON-NLS-1$
	public static final ImageDescriptor DESC_OVR_CONSTRUCTOR= createUnManagedCached(T_OVR, "constr_ovr.gif");			//$NON-NLS-1$
	public static final ImageDescriptor DESC_OVR_FINAL= createUnManagedCached(T_OVR, "final_co.gif"); 						//$NON-NLS-1$
	public static final ImageDescriptor DESC_OVR_VOLATILE= createUnManagedCached(T_OVR, "volatile_co.gif"); 						//$NON-NLS-1$
	public static final ImageDescriptor DESC_OVR_STATIC= createUnManagedCached(T_OVR, "static_co.gif"); 						//$NON-NLS-1$
	public static final ImageDescriptor DESC_OVR_SYNCH_AND_OVERRIDES= createUnManagedCached(T_OVR, "sync_over.gif");  	//$NON-NLS-1$
	public static final ImageDescriptor DESC_OVR_SYNCH_AND_IMPLEMENTS= createUnManagedCached(T_OVR, "sync_impl.gif");   //$NON-NLS-1$
	public static final ImageDescriptor DESC_OVR_OVERRIDES= createUnManagedCached(T_OVR, "over_co.gif");  					//$NON-NLS-1$
	public static final ImageDescriptor DESC_OVR_IMPLEMENTS= createUnManagedCached(T_OVR, "implm_co.gif");  				//$NON-NLS-1$
	public static final ImageDescriptor DESC_OVR_SYNCH= createUnManagedCached(T_OVR, "synch_co.gif"); 						//$NON-NLS-1$
	public static final ImageDescriptor DESC_OVR_RUN= createUnManagedCached(T_OVR, "run_co.gif"); 							//$NON-NLS-1$
	public static final ImageDescriptor DESC_OVR_TRANSIENT= createUnManagedCached(T_OVR, "transient_co.gif"); 						//$NON-NLS-1$
	public static final ImageDescriptor DESC_OVR_ERROR= createUnManagedCached(T_OVR, "error_co.gif"); 						//$NON-NLS-1$
	public static final ImageDescriptor DESC_OVR_WARNING= createUnManagedCached(T_OVR, "warning_co.gif"); 					//$NON-NLS-1$
	public static final ImageDescriptor DESC_OBJ_PROJECT_CLOSED= createManagedFromKey(T_OBJ, IMG_OBJ_PROJECT_CLOSED);
	public static final ImageDescriptor DESC_OBJ_PROJECT= createManagedFromKey(T_OBJ, IMG_OBJ_PROJECT);
	
	private static final class CachedImageDescriptor extends ImageDescriptor {
		private ImageDescriptor fDescriptor;
		private ImageData fData;

		public CachedImageDescriptor(ImageDescriptor descriptor) {
			fDescriptor = descriptor;
		}

		public ImageData getImageData() {
			if (fData == null) {
				fData= fDescriptor.getImageData();
			}
			return fData;
		}
	}
	
	private static ImageDescriptor createManagedFromKey(String prefix, String key) {
		return createManaged(prefix, key.substring(NAME_PREFIX_LENGTH), key);
	}
	
	private static ImageDescriptor createManaged(String prefix, String name, String key) {
		try {
			ImageDescriptor result= create(prefix, name, true);

			if (fgAvoidSWTErrorMap == null) {
				fgAvoidSWTErrorMap= new HashMap<String,ImageDescriptor>();
			}
			fgAvoidSWTErrorMap.put(key, result);
			if (fgImageRegistry != null) {
				
	//FIXME			JavaPlugin.logErrorMessage("Image registry already defined"); //$NON-NLS-1$
			}
			return result;			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ImageDescriptor.getMissingImageDescriptor();
	}
	
	/*
	 * Creates an image descriptor for the given prefix and name in the JDT UI bundle. The path can
	 * contain variables like $NL$.
	 * If no image could be found, <code>useMissingImageDescriptor</code> decides if either
	 * the 'missing image descriptor' is returned or <code>null</code>.
	 * or <code>null</code>.
	 */
	private static ImageDescriptor create(String prefix, String name, boolean useMissingImageDescriptor) {
		IPath path= ICONS_PATH.append(prefix).append(name);
		return createImageDescriptor(FrameworkUtil.getBundle(JavaPluginImages.class), path, useMissingImageDescriptor);
	}
	
	/*
	 * Creates an image descriptor for the given path in a bundle. The path can contain variables
	 * like $NL$.
	 * If no image could be found, <code>useMissingImageDescriptor</code> decides if either
	 * the 'missing image descriptor' is returned or <code>null</code>.
	 * Added for 3.1.1.
	 */
	public static ImageDescriptor createImageDescriptor(Bundle bundle, IPath path, boolean useMissingImageDescriptor) {
		URL url= FileLocator.find(bundle, path, null);
		if (url != null) {
			return ImageDescriptor.createFromURL(url);
		}
		if (useMissingImageDescriptor) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
		return null;
	}
	
	/*
	 * Creates an image descriptor for the given prefix and name in the JDT UI bundle and let type descriptor cache the image data.
	 * If no image could be found, the 'missing image descriptor' is returned.
	 */
	private static ImageDescriptor createUnManagedCached(String prefix, String name) {
		try {
			return new CachedImageDescriptor(create(prefix, name, true));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ImageDescriptor.getMissingImageDescriptor();
	}

}
