package org.eclipse.e4.demo.simpleide.editor.text;

import javax.inject.Inject;

import org.eclipse.e4.demo.simpleide.editor.IDocumentInput;
import org.eclipse.e4.demo.simpleide.editor.text.xml.ColorManager;
import org.eclipse.e4.demo.simpleide.editor.text.xml.XMLConfiguration;
import org.eclipse.e4.demo.simpleide.editor.text.xml.XMLPartitionScanner;
import org.eclipse.e4.workbench.ui.Persist;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class XMLTextEditor {
	private static final int VERTICAL_RULER_WIDTH = 12;
	
	private ColorManager colorManager;
	
	private IDocumentInput input;

	@Inject
	public XMLTextEditor(Composite parent, IDocumentInput input) {
		this.input = input;
		parent.setLayout(new FillLayout());
		
		colorManager = new ColorManager();
		VerticalRuler verticalRuler = new VerticalRuler(VERTICAL_RULER_WIDTH);
		
		int styles= SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION;
		SourceViewer viewer = new SourceViewer(parent, verticalRuler, styles);
		viewer.configure(new XMLConfiguration(colorManager));
		IDocument document = input.getDocument();
		IDocumentPartitioner partitioner =
			new FastPartitioner(
				new XMLPartitionScanner(),
				new String[] {
					XMLPartitionScanner.XML_TAG,
					XMLPartitionScanner.XML_COMMENT });
		partitioner.connect(document);
		document.setDocumentPartitioner(partitioner);
		viewer.setDocument(document);
	}
	
	@Persist
	public void save() {
		System.err.println("Saving ...");
		input.save();
	}
}
