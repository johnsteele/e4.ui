package org.eclipse.e4.demo.simpleide.jdt.internal.editor;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

public class JavaTextTools {
//	/**
//	 * Array with legal content types.
//	 * @since 3.0
//	 */
//	private final static String[] LEGAL_CONTENT_TYPES= new String[] {
//		IJavaPartitions.JAVA_DOC,
//		IJavaPartitions.JAVA_MULTI_LINE_COMMENT,
//		IJavaPartitions.JAVA_SINGLE_LINE_COMMENT,
//		IJavaPartitions.JAVA_STRING,
//		IJavaPartitions.JAVA_CHARACTER
//	};

	/**
	 * This tools' preference listener.
	 */
	private class PreferenceListener implements IPropertyChangeListener {
		public void propertyChange(PropertyChangeEvent event) {
			adaptToPreferenceChange(event);
		}
	}
	
	/** The color manager. */
	private JavaColorManager fColorManager;
	/** The Java source code scanner. */
//	private JavaCodeScanner fCodeScanner;
	/** The Java multi-line comment scanner. */
	private JavaCommentScanner fMultilineCommentScanner;
	/** The Java single-line comment scanner. */
	private JavaCommentScanner fSinglelineCommentScanner;
	/** The Java string scanner. */
	private SingleTokenJavaScanner fStringScanner;
	/** The JavaDoc scanner. */
	private JavaDocScanner fJavaDocScanner;
	/** The preference store. */
	private IPreferenceStore fPreferenceStore;
	
	/**
	 * The core preference store.
	 * @since 2.1
	 */
	/** The preference change listener */
	private PreferenceListener fPreferenceListener= new PreferenceListener();

	public JavaTextTools(IPreferenceStore store, boolean autoDisposeOnDisplayDispose) {
		fPreferenceStore = store;
		fPreferenceStore.addPropertyChangeListener(fPreferenceListener);
		
		fColorManager= new JavaColorManager(autoDisposeOnDisplayDispose);
		fMultilineCommentScanner= new JavaCommentScanner(fColorManager, store, IJavaColorConstants.JAVA_MULTI_LINE_COMMENT);
		fSinglelineCommentScanner= new JavaCommentScanner(fColorManager, store, IJavaColorConstants.JAVA_SINGLE_LINE_COMMENT);
		fStringScanner= new SingleTokenJavaScanner(fColorManager, store, IJavaColorConstants.JAVA_STRING);
		fJavaDocScanner= new JavaDocScanner(fColorManager, store);
	}
	
	public JavaCommentScanner getMultilineCommentScanner() {
		return fMultilineCommentScanner;
	}
	
	public JavaCommentScanner getSinglelineCommentScanner() {
		return fSinglelineCommentScanner;
	}
	
	public SingleTokenJavaScanner getStringScanner() {
		return fStringScanner;
	}
	
	public ITokenScanner getJavaDocScanner() {
		return fJavaDocScanner;
	}
	
	/**
	 * Adapts the behavior of the contained components to the change
	 * encoded in the given event.
	 *
	 * @param event the event to which to adapt
	 * @since 2.0
	 */
	private void adaptToPreferenceChange(PropertyChangeEvent event) {
//		if (fCodeScanner.affectsBehavior(event))
//			fCodeScanner.adaptToPreferenceChange(event);
//		if (fMultilineCommentScanner.affectsBehavior(event))
//			fMultilineCommentScanner.adaptToPreferenceChange(event);
//		if (fSinglelineCommentScanner.affectsBehavior(event))
//			fSinglelineCommentScanner.adaptToPreferenceChange(event);
//		if (fStringScanner.affectsBehavior(event))
//			fStringScanner.adaptToPreferenceChange(event);
//		if (fJavaDocScanner.affectsBehavior(event))
//			fJavaDocScanner.adaptToPreferenceChange(event);
	}

}
