package org.eclipse.e4.demo.simpleide.editor;

import org.eclipse.jface.text.IDocument;

public interface IDocumentInput extends IInput {
	public IDocument getDocument();
}
