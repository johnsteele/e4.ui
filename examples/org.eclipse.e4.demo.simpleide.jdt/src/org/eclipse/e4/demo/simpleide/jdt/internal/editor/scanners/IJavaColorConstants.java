/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.demo.simpleide.jdt.internal.editor.scanners;

/**
 * Color keys used for syntax highlighting Java
 * code and Javadoc compliant comments.
 * A <code>IColorManager</code> is responsible for mapping
 * concrete colors to these keys.
 * <p>
 * This interface declares static final fields only; it is not intended to be
 * implemented.
 * </p>
 *
 * @see org.eclipse.e4.demo.simpleide.jdt.internal.editor.scanners.ui.text.IColorManager
 * @see org.eclipse.e4.demo.simpleide.jdt.internal.editor.scanners.ui.text.IColorManagerExtension
 *
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface IJavaColorConstants {
	/**
	 * The color key for task tags in java comments
	 * (value <code>"java_comment_task_tag"</code>).
	 *
	 * @since 2.1
	 */
	String TASK_TAG= "java_comment_task_tag"; //$NON-NLS-1$

	/** The color key for multi-line comments in Java code
	 * (value <code>"java_multi_line_comment"</code>).
	 */
	String JAVA_MULTI_LINE_COMMENT= "java_multi_line_comment"; //$NON-NLS-1$

	/** The color key for single-line comments in Java code
	 * (value <code>"java_single_line_comment"</code>).
	 */
	String JAVA_SINGLE_LINE_COMMENT= "java_single_line_comment"; //$NON-NLS-1$
	
	/** The color key for string and character literals in Java code
	 * (value <code>"java_string"</code>).
	 */
	String JAVA_STRING= "java_string"; //$NON-NLS-1$

	/**
	 * The color key for JavaDoc keywords (<code>@foo</code>) in JavaDoc comments
	 * (value <code>"java_doc_keyword"</code>).
	 */
	String JAVADOC_KEYWORD= "java_doc_keyword"; //$NON-NLS-1$

	/**
	 * The color key for HTML tags (<code>&lt;foo&gt;</code>) in JavaDoc comments
	 * (value <code>"java_doc_tag"</code>).
	 */
	String JAVADOC_TAG= "java_doc_tag"; //$NON-NLS-1$

	/**
	 * The color key for JavaDoc links (<code>{foo}</code>) in JavaDoc comments
	 * (value <code>"java_doc_link"</code>).
	 */
	String JAVADOC_LINK= "java_doc_link"; //$NON-NLS-1$

	/**
	 * The color key for everything in JavaDoc comments for which no other color is specified
	 * (value <code>"java_doc_default"</code>).
	 */
	String JAVADOC_DEFAULT= "java_doc_default"; //$NON-NLS-1$
}
