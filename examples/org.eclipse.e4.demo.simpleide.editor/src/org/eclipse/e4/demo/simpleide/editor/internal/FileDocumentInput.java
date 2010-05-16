package org.eclipse.e4.demo.simpleide.editor.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.demo.simpleide.editor.IDocumentInput;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;

public class FileDocumentInput extends AbstractInput implements IDocumentInput {
	private IFile file;
	private IDocument document;

	private static final int DEFAULT_FILE_SIZE = 15 * 1024;

	public FileDocumentInput(IFile file) {
		this.file = file;
	}

	public IStatus save() {

		// TODO Auto-generated method stub
		return null;
	}

	public IDocument getDocument() {
		if (document == null) {
			Document document = new Document();
			Reader in = null;

			InputStream contentStream = null;
			try {
				contentStream = file.getContents();
			} catch (CoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (contentStream == null) {
				return null;
			}

			try {
				String encoding = null;
				try {
					encoding = file.getCharset();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				if (encoding == null)
					encoding = "UTF-8";

				in = new BufferedReader(new InputStreamReader(contentStream,
						encoding), DEFAULT_FILE_SIZE);
				StringBuffer buffer = new StringBuffer(DEFAULT_FILE_SIZE);
				char[] readBuffer = new char[2048];
				int n = in.read(readBuffer);
				while (n > 0) {
					buffer.append(readBuffer, 0, n);
					n = in.read(readBuffer);
				}

				document.set(buffer.toString());
				this.document = document;
			} catch (IOException x) {
				x.printStackTrace();
			} finally {
				try {
					if (in != null)
						in.close();
					else
						contentStream.close();
				} catch (IOException x) {
				}
			}
		}

		return document;
	}
}
