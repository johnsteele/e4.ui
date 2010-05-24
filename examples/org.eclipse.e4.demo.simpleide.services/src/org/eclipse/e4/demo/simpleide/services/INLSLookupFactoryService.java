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

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Locale;

public interface INLSLookupFactoryService {
	/**
	 * Creates a lookup which adapts dynamically to the current language stored
	 * in {@link Locale#getDefault()}
	 * 
	 * @param <L>
	 *            the interface type
	 * @param clazz
	 *            the interface type
	 * @return the lookup instance
	 */
	public <L> L createDynamicNLSLookup(Class<L> clazz);

	/**
	 * Create a lookup which uses the give locale for lookups in dependently
	 * from the current default locale
	 * 
	 * @param <L>
	 *            the interface type
	 * @param clazz
	 *            the interface type
	 * @param locale
	 *            the locale
	 * @return the lookup instance
	 */
	public <L> L createNLSLookup(Class<L> clazz, Locale locale);

	/**
	 * Create a lookup which uses the give locale for lookups in dependently
	 * from the current default locale.
	 * <p>
	 * This method has the same results as
	 * <code>createNLSLookup(My.class, Locale.getDefault())</code>
	 * </p>
	 * 
	 * @param <L>
	 *            the interface type
	 * @param clazz
	 *            the interface type
	 * @return the lookup instance
	 */
	public <L> L createNLSLookup(Class<L> clazz);

	/**
	 * Translate a value using one of the registered translation services
	 * 
	 * @param category
	 *            the category to identify the service
	 * @param key
	 *            the key
	 * @param args
	 *            the arguments for substitution
	 * @return the string in the current default locale
	 */
	public String translate(String category, String key, Object... args);

	/**
	 * Translate a value using one of the registered translation services
	 * 
	 * @param category
	 *            the category to identify the service
	 * @param key
	 *            the key
	 * @param locale
	 *            the locale to use
	 * @param args
	 *            the arguments for substitution
	 * @return the translated string
	 */
	public String translate(String category, String key, Locale locale,
			Object... args);

	/**
	 * Configures the cacheing of the Lookup class
	 */
	@Target(ElementType.TYPE)
	public @interface Cache {

	}

	/**
	 * Marks the value to be looked up through the given service class
	 */
	@Target(ElementType.TYPE)
	public @interface OSGiService {
		String serviceId();
	}

	@Target(ElementType.METHOD)
	public @interface Key {
		String category() default "N/A";

		String key();
	}

	public interface ITranslationService {
		public String translate(String category, String key, Locale locale);
	}
}