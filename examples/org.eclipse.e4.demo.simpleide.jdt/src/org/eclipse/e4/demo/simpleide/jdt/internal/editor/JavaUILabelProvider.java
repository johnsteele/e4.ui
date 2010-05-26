/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.demo.simpleide.jdt.internal.editor;

import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.demo.simpleide.jdt.internal.JavaUIMessages;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.StyledString;

public class JavaUILabelProvider extends LabelProvider implements IStyledLabelProvider {
	private long fTextFlags;
	private Logger logger;
	private JavaUIMessages messages;

	/**
	 * Creates a new label provider with default flags.
	 */
	public JavaUILabelProvider(Logger logger, JavaUIMessages messages) {
		this(JavaElementLabels.ALL_DEFAULT,logger,messages);
	}
	
	public JavaUILabelProvider(long textFlags, Logger logger, JavaUIMessages messages) {
		fTextFlags= textFlags;
		this.logger = logger;
		this.messages = messages;
	}
	
	@Override
	public String getText(Object element) {
		String result = JavaElementLabels.getTextLabel(element, evaluateTextFlags(element),logger,messages);
//		return decorateText(result, element);
		return result;
	}
	
	/**
	 * Evaluates the text flags for a element. Can be overwritten by super classes.
	 * @param element the element to compute the text flags for
	 * @return Returns a int
	 */
	protected long evaluateTextFlags(Object element) {
		return getTextFlags();
	}
	
	/**
	 * Gets the text flags.
	 * @return Returns a int
	 */
	public final long getTextFlags() {
		return fTextFlags;
	}

	public StyledString getStyledText(Object element) {
		StyledString string= JavaElementLabels.getStyledTextLabel(element, (evaluateTextFlags(element) | JavaElementLabels.COLORIZE), logger, messages);
		System.err.println("Styled string: " + string);
		System.err.println(string.getStyleRanges().length);
		return string;
	}
}
