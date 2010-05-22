package org.eclipse.e4.demo.simpleide.jdt.internal.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class JavaSourceViewerConfiguration extends SourceViewerConfiguration {
	private JavaTextTools textTools;

	public JavaSourceViewerConfiguration(JavaTextTools textTools) {
		this.textTools = textTools;
	}
	
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
				IDocument.DEFAULT_CONTENT_TYPE, 
				IJavaPartitions.JAVA_DOC,
				IJavaPartitions.JAVA_MULTI_LINE_COMMENT,
				IJavaPartitions.JAVA_SINGLE_LINE_COMMENT,
				IJavaPartitions.JAVA_STRING,
				IJavaPartitions.JAVA_CHARACTER
		};
	}
	
	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler= new JavaPresentationReconciler();
		reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
		
		DefaultDamagerRepairer dr;
//		DefaultDamagerRepairer dr= new DefaultDamagerRepairer(null/*textTools.getCodeScanner()*/);
//		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
//		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		dr= new DefaultDamagerRepairer(textTools.getJavaDocScanner());
		reconciler.setDamager(dr, IJavaPartitions.JAVA_DOC);
		reconciler.setRepairer(dr, IJavaPartitions.JAVA_DOC);

		dr= new DefaultDamagerRepairer(textTools.getMultilineCommentScanner());
		reconciler.setDamager(dr, IJavaPartitions.JAVA_MULTI_LINE_COMMENT);
		reconciler.setRepairer(dr, IJavaPartitions.JAVA_MULTI_LINE_COMMENT);

		dr= new DefaultDamagerRepairer(textTools.getSinglelineCommentScanner());
		reconciler.setDamager(dr, IJavaPartitions.JAVA_SINGLE_LINE_COMMENT);
		reconciler.setRepairer(dr, IJavaPartitions.JAVA_SINGLE_LINE_COMMENT);

		dr= new DefaultDamagerRepairer(textTools.getStringScanner());
		reconciler.setDamager(dr, IJavaPartitions.JAVA_STRING);
		reconciler.setRepairer(dr, IJavaPartitions.JAVA_STRING);

		dr= new DefaultDamagerRepairer(textTools.getStringScanner());
		reconciler.setDamager(dr, IJavaPartitions.JAVA_CHARACTER);
		reconciler.setRepairer(dr, IJavaPartitions.JAVA_CHARACTER);


		return reconciler;
	}
}