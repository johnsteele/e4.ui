/*******************************************************************************
 * Copyright (c) 2010 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.demo.simpleide.services;

import java.util.Locale;

public interface NLSLookFactoryService {
	public <L> L createNLSLookup(Class<L> clazz);
	public <L> L createNLSLookup(Class<L> clazz, Locale locale);
	
	/**
	 * Configures the cacheing of the Lookup class
	 */
	public @interface Cache {

	}
	
	/**
	 * Marks the value to be looked up through the given service class
	 */
	public @interface OSGiService {
		String serviceId = null;
	}
}