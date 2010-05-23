package org.eclipse.e4.demo.simpleide.dialogs.internal;

import org.eclipse.osgi.util.NLS;

public class Messages {
	
	public static String SelectionDialog_selectLabel;
	public static String SelectionDialog_deselectLabel;
	
	public static String GoHome_text;
	public static String GoHome_toolTip;
	public static String GoBack_toolTip;
	public static String GoInto_text;
	public static String GoInto_toolTip;
	public static String GoBack_text;
	
	public static String ContainerGroup_message;
	public static String ContainerGroup_selectFolder;
	public static String ContainerSelectionDialog_title;
	public static String ContainerSelectionDialog_message;

	static {
		NLS.initializeMessages(Messages.class.getName(), Messages.class);
	}
}
