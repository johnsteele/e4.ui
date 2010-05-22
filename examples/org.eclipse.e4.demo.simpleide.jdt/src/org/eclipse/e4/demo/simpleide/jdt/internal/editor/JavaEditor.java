package org.eclipse.e4.demo.simpleide.jdt.internal.editor;

import javax.inject.Inject;

import org.eclipse.e4.demo.simpleide.editor.IDocumentInput;
import org.eclipse.e4.demo.simpleide.jdt.internal.editor.scanners.IJavaColorConstants;
import org.eclipse.e4.demo.simpleide.jdt.internal.editor.scanners.IJavaPartitions;
import org.eclipse.e4.demo.simpleide.jdt.internal.editor.scanners.JavaSourceViewerConfiguration;
import org.eclipse.e4.demo.simpleide.jdt.internal.editor.scanners.JavaTextTools;
import org.eclipse.e4.demo.simpleide.jdt.internal.editor.scanners.PreferenceConstants;
import org.eclipse.e4.workbench.ui.Persist;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.IDocument;
//import org.eclipse.jface.text.IDocumentPartitioner;
//import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;

public class JavaEditor {
	private static final int VERTICAL_RULER_WIDTH = 12;

	private IDocumentInput input;
	
//	private static JavaColorManager colorManager = new JavaColorManager(true);
	private static PreferenceStore store = new PreferenceStore();
	
	static {
		store.setDefault(IJavaColorConstants.JAVA_MULTI_LINE_COMMENT, "102,153,102"); // 669966

		store.setDefault(IJavaColorConstants.JAVA_SINGLE_LINE_COMMENT, "102,153,102"); // 669966
		
		store.setDefault(IJavaColorConstants.JAVA_STRING, "51,0,255"); // 3300FF
		
		store.setDefault(IJavaColorConstants.JAVADOC_DEFAULT, "51,102,204"); // 3366CC
		
		store.setDefault(IJavaColorConstants.JAVADOC_KEYWORD, "51,102,204");
		store.setDefault(IJavaColorConstants.JAVADOC_KEYWORD+PreferenceConstants.EDITOR_BOLD_SUFFIX, true);
		
		store.setDefault(IJavaColorConstants.JAVADOC_LINK, "255,0,255");
		
		store.setDefault(IJavaColorConstants.JAVADOC_TAG, "255,255,0");
		
		store.setDefault(IJavaColorConstants.TASK_TAG, "255,100,0");
		
		store.setDefault(IJavaColorConstants.JAVA_KEYWORD, "153,51,102"); // 993366
		store.setDefault(IJavaColorConstants.JAVA_KEYWORD+PreferenceConstants.EDITOR_BOLD_SUFFIX, true);
		
		store.setDefault(IJavaColorConstants.JAVA_DEFAULT, "0,0,0");
		
		store.setDefault(IJavaColorConstants.JAVA_KEYWORD_RETURN, "153,51,102"); // 993366
		store.setDefault(IJavaColorConstants.JAVA_KEYWORD_RETURN+PreferenceConstants.EDITOR_BOLD_SUFFIX, true);
		
		store.setDefault(IJavaColorConstants.JAVA_OPERATOR, "0,0,0");
		store.setDefault(IJavaColorConstants.JAVA_BRACKET, "0,0,0");
	}
	
	private static final JavaTextTools textTools = new JavaTextTools(store, true);

	@Inject
	public JavaEditor(Composite parent, IDocumentInput input) {
		this.input = input;
		VerticalRuler verticalRuler = new VerticalRuler(VERTICAL_RULER_WIDTH);
		
		int styles= SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION;
		SourceViewer viewer = new SourceViewer(parent, verticalRuler, styles);
		
		Font f = null;
		if( ! JFaceResources.getFontRegistry().hasValueFor("JavaEditorFont") ) {
			if( SWT.getPlatform().equals("carbon") || SWT.getPlatform().equals("cocoa") ) {
				JFaceResources.getFontRegistry().put("JavaEditorFont", new FontData[] {new FontData("Monaco",11,SWT.NONE)});
			}
		}
		
		f = JFaceResources.getFontRegistry().get("JavaEditorFont");
		viewer.getTextWidget().setFont(f);
		
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
