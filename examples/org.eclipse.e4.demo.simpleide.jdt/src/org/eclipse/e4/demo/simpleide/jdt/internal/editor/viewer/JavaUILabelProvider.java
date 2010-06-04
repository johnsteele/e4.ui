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
package org.eclipse.e4.demo.simpleide.jdt.internal.editor.viewer;

import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.demo.simpleide.jdt.internal.JavaUIMessages;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

public class JavaUILabelProvider extends LabelProvider implements IStyledLabelProvider {
	private long fTextFlags;
	private int fImageFlags;
	
	private Logger logger;
	private JavaUIMessages messages;
	
	protected JavaElementImageProvider fImageLabelProvider;

	/**
	 * Creates a new label provider with default flags.
	 */
	public JavaUILabelProvider(Logger logger, JavaUIMessages messages) {
		this(JavaElementLabels.ALL_DEFAULT,JavaElementImageProvider.OVERLAY_ICONS, logger,messages);
	}
	
	public JavaUILabelProvider(long textFlags, int imageFlags, Logger logger, JavaUIMessages messages) {
		fImageLabelProvider= new JavaElementImageProvider(logger);
		
		fTextFlags= textFlags;
		fImageFlags = imageFlags;
		this.logger = logger;
		this.messages = messages;
	}
	
	@Override
	public String getText(Object element) {
		String result = JavaElementLabels.getTextLabel(element, evaluateTextFlags(element),logger,messages);
//		return decorateText(result, element);
		return result;
	}
	
	public Image getImage(Object element) {
		Image result= fImageLabelProvider.getImageLabel(element, evaluateImageFlags(element),logger);
//		return decorateImage(result, element);
		return result;
	}
	
	/**
	 * Gets the image flags.
	 * Can be overwritten by super classes.
	 * @return Returns a int
	 */
	public final int getImageFlags() {
		return fImageFlags;
	}
	
	/**
	 * Evaluates the image flags for a element.
	 * Can be overwritten by super classes.
	 * @param element the element to compute the image flags for
	 * @return Returns a int
	 */
	protected int evaluateImageFlags(Object element) {
		return getImageFlags();
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
//		System.err.println("Styled string: " + string);
//		System.err.println(string.getStyleRanges().length);
		return string;
	}
}
