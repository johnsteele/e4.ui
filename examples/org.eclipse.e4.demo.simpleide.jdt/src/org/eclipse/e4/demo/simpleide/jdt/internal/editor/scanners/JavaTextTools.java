package org.eclipse.e4.demo.simpleide.jdt.internal.editor.scanners;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

public class JavaTextTools {
	/**
	 * Array with legal content types.
	 * @since 3.0
	 */
	private final static String[] LEGAL_CONTENT_TYPES= new String[] {
		IJavaPartitions.JAVA_DOC,
		IJavaPartitions.JAVA_MULTI_LINE_COMMENT,
		IJavaPartitions.JAVA_SINGLE_LINE_COMMENT,
		IJavaPartitions.JAVA_STRING,
		IJavaPartitions.JAVA_CHARACTER
	};

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
	private JavaCodeScanner fCodeScanner;
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
		fCodeScanner= new JavaCodeScanner(fColorManager, store);
		fMultilineCommentScanner= new JavaCommentScanner(fColorManager, store, IJavaColorConstants.JAVA_MULTI_LINE_COMMENT);
		fSinglelineCommentScanner= new JavaCommentScanner(fColorManager, store, IJavaColorConstants.JAVA_SINGLE_LINE_COMMENT);
		fStringScanner= new SingleTokenJavaScanner(fColorManager, store, IJavaColorConstants.JAVA_STRING);
		fJavaDocScanner= new JavaDocScanner(fColorManager, store);
	}
	
	public ITokenScanner getMultilineCommentScanner() {
		return fMultilineCommentScanner;
	}
	
	public ITokenScanner getSinglelineCommentScanner() {
		return fSinglelineCommentScanner;
	}
	
	public ITokenScanner getStringScanner() {
		return fStringScanner;
	}
	
	public ITokenScanner getJavaDocScanner() {
		return fJavaDocScanner;
	}
	
	public ITokenScanner getCodeScanner() {
		return fCodeScanner;
	}
	
	/**
	 * Sets up the Java document partitioner for the given document for the given partitioning.
	 *
	 * @param document the document to be set up
	 * @param partitioning the document partitioning
	 * @since 3.0
	 */
	public void setupJavaDocumentPartitioner(IDocument document, String partitioning) {
		IDocumentPartitioner partitioner= createDocumentPartitioner();
		if (document instanceof IDocumentExtension3) {
			IDocumentExtension3 extension3= (IDocumentExtension3) document;
			extension3.setDocumentPartitioner(partitioning, partitioner);
		} else {
			document.setDocumentPartitioner(partitioner);
		}
		partitioner.connect(document);
	}
	
	/**
	 * Returns a scanner which is configured to scan
	 * Java-specific partitions, which are multi-line comments,
	 * Javadoc comments, and regular Java source code.
	 *
	 * @return a Java partition scanner
	 */
	public IPartitionTokenScanner getPartitionScanner() {
		return new FastJavaPartitionScanner();
	}

	/**
	 * Factory method for creating a Java-specific document partitioner
	 * using this object's partitions scanner. This method is a
	 * convenience method.
	 *
	 * @return a newly created Java document partitioner
	 */
	public IDocumentPartitioner createDocumentPartitioner() {
		return new FastPartitioner(getPartitionScanner(), LEGAL_CONTENT_TYPES);
	}
	
	/**
	 * Adapts the behavior of the contained components to the change
	 * encoded in the given event.
	 *
	 * @param event the event to which to adapt
	 * @since 2.0
	 */
	private void adaptToPreferenceChange(PropertyChangeEvent event) {
		if (fCodeScanner.affectsBehavior(event))
			fCodeScanner.adaptToPreferenceChange(event);
		if (fMultilineCommentScanner.affectsBehavior(event))
			fMultilineCommentScanner.adaptToPreferenceChange(event);
		if (fSinglelineCommentScanner.affectsBehavior(event))
			fSinglelineCommentScanner.adaptToPreferenceChange(event);
		if (fStringScanner.affectsBehavior(event))
			fStringScanner.adaptToPreferenceChange(event);
		if (fJavaDocScanner.affectsBehavior(event))
			fJavaDocScanner.adaptToPreferenceChange(event);
	}
}
