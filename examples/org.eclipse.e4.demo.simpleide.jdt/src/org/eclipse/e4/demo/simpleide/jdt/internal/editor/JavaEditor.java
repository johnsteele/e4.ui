package org.eclipse.e4.demo.simpleide.jdt.internal.editor;

import javax.inject.Inject;

import org.eclipse.e4.demo.simpleide.editor.IDocumentInput;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class JavaEditor {
	private static final int VERTICAL_RULER_WIDTH = 12;

	private IDocumentInput input;
	
	private ColorManager colorManager;
	

	@Inject
	public JavaEditor(Composite parent, IDocumentInput input) {
		colorManager = new ColorManager();
		VerticalRuler verticalRuler = new VerticalRuler(VERTICAL_RULER_WIDTH);
		
		int styles= SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION;
		SourceViewer viewer = new SourceViewer(parent, verticalRuler, styles);
//		viewer.configure(new XMLConfiguration(colorManager));
		IDocument document = input.getDocument();
//		IDocumentPartitioner partitioner =
//			new FastPartitioner(
//				new XMLPartitionScanner(),
//				new String[] {
//					XMLPartitionScanner.XML_TAG,
//					XMLPartitionScanner.XML_COMMENT });
//		partitioner.connect(document);
//		document.setDocumentPartitioner(partitioner);
		viewer.setDocument(document);
	}
}
