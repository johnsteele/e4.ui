/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.demo.simpleide.jdt.internal.editor;

import org.eclipse.jface.viewers.StyledString;
import org.eclipse.osgi.util.TextProcessor;

/**
 * Helper class to provide String manipulation functions not available in standard JDK.
 */
public class Strings {
	/**
	 * Tells whether we have to use the {@link TextProcessor}
	 * <p>
	 * This is used for performance optimization.
	 * </p>
	 * @since 3.4
	 */
	public static final boolean USE_TEXT_PROCESSOR;
	static {
		String testString= "args : String[]"; //$NON-NLS-1$
		USE_TEXT_PROCESSOR= testString != TextProcessor.process(testString);
	}
	
	private static final String JAVA_ELEMENT_DELIMITERS= TextProcessor.getDefaultDelimiters() + "<>(),?{} "; //$NON-NLS-1$
	
	/**
	 * Adds special marks so that that the given Java element label is readable in a BiDi
	 * environment.
	 * 
	 * @param string the string
	 * @return the processed styled string
	 * @since 3.6
	 */
	public static String markJavaElementLabelLTR(String string) {
		if (!USE_TEXT_PROCESSOR)
			return string;

		return TextProcessor.process(string, JAVA_ELEMENT_DELIMITERS);
	}
	
	/**
	 * Adds special marks so that that the given styled Java element label is readable in a BiDi
	 * environment.
	 * 
	 * @param styledString the styled string
	 * @return the processed styled string
	 * @since 3.6
	 */
	public static StyledString markJavaElementLabelLTR(StyledString styledString) {
		if (!USE_TEXT_PROCESSOR)
			return styledString;

		String inputString= styledString.getString();
		String string= TextProcessor.process(inputString, JAVA_ELEMENT_DELIMITERS);
		if (string != inputString)
			insertMarks(styledString, inputString, string);
		return styledString;
	}

	/**
	 * Inserts the marks into the given styled string.
	 * 
	 * @param styledString the styled string
	 * @param originalString the original string
	 * @param processedString the processed string
	 * @since 3.5
	 */
	private static void insertMarks(StyledString styledString, String originalString, String processedString) {
		int originalLength= originalString.length();
		int processedStringLength= processedString.length();
		char orig= originalLength > 0 ? originalString.charAt(0) : '\0';
		for (int o= 0, p= 0; p < processedStringLength; p++) {
			char processed= processedString.charAt(p);
			if (o < originalLength) {
				if (orig == processed) {
					o++;
					if (o < originalLength)
						orig= originalString.charAt(o);
					continue;
				}
			}
			styledString.insert(processed, p);
		}
	}
}
