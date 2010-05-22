package org.eclipse.e4.demo.simpleide.jdt.internal.editor;

import javax.inject.Inject;

import org.eclipse.e4.demo.simpleide.editor.IDocumentInput;
import org.eclipse.e4.demo.simpleide.jdt.internal.editor.scanners.IJavaColorConstants;
import org.eclipse.e4.demo.simpleide.jdt.internal.editor.scanners.IJavaPartitions;
import org.eclipse.e4.demo.simpleide.jdt.internal.editor.scanners.JavaSourceViewerConfiguration;
import org.eclipse.e4.demo.simpleide.jdt.internal.editor.scanners.JavaTextTools;
import org.eclipse.e4.workbench.ui.Persist;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.text.IDocument;
//import org.eclipse.jface.text.IDocumentPartitioner;
//import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class JavaEditor {
	private static final int VERTICAL_RULER_WIDTH = 12;

	private IDocumentInput input;
	
//	private static JavaColorManager colorManager = new JavaColorManager(true);
	private static PreferenceStore store = new PreferenceStore();
	
	static {
		store.setDefault(IJavaColorConstants.JAVA_MULTI_LINE_COMMENT, "255,0,0");
		store.setDefault(IJavaColorConstants.JAVA_SINGLE_LINE_COMMENT, "255,0,0");
		store.setDefault(IJavaColorConstants.JAVA_STRING, "0,255,0");
		store.setDefault(IJavaColorConstants.JAVADOC_DEFAULT, "0,0,255");
		store.setDefault(IJavaColorConstants.JAVADOC_KEYWORD, "0,255,255");
		store.setDefault(IJavaColorConstants.JAVADOC_LINK, "255,0,255");
		store.setDefault(IJavaColorConstants.JAVADOC_TAG, "255,255,0");
		store.setDefault(IJavaColorConstants.TASK_TAG, "255,100,0");		
	}
	
	private static final JavaTextTools textTools = new JavaTextTools(store, true);

	@Inject
	public JavaEditor(Composite parent, IDocumentInput input) {
		VerticalRuler verticalRuler = new VerticalRuler(VERTICAL_RULER_WIDTH);
		
		int styles= SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION;
		SourceViewer viewer = new SourceViewer(parent, verticalRuler, styles);
		
		viewer.configure(new JavaSourceViewerConfiguration(textTools));
		IDocument document = input.getDocument();
		textTools.setupJavaDocumentPartitioner(document, IJavaPartitions.JAVA_PARTITIONING);
		viewer.setDocument(document);
	}
	
	@Persist
	public void save() {
		System.err.println("Saving ...");
		input.save();
	}
}
