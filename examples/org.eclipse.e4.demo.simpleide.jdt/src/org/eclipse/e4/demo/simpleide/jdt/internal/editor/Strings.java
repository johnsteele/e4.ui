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
}
