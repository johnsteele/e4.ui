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
package org.eclipse.e4.demo.simpleide.internal;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.e4.demo.simpleide.services.INLSLookupFactoryService;

public class NLSLookupFactoryServiceImpl implements INLSLookupFactoryService {
	static class Entry {
		private Class<?> clazz;
		private Locale locale;

		Entry(Class<?> clazz, Locale locale) {
			this.clazz = clazz;
			this.locale = locale;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
			result = prime * result
					+ ((locale == null) ? 0 : locale.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Entry other = (Entry) obj;
			if (clazz == null) {
				if (other.clazz != null)
					return false;
			} else if (!clazz.equals(other.clazz))
				return false;
			if (locale == null) {
				if (other.locale != null)
					return false;
			} else if (!locale.equals(other.locale))
				return false;
			return true;
		}
	}

	private Map<Entry, Object> CACHE = new ConcurrentHashMap<Entry, Object>();
	private Map<String,List<ITranslationService>> translationServices = new ConcurrentHashMap<String, List<ITranslationService>>();

	public void addTranslationService(ITranslationService translationService, Map<String, Object> properties) {
		System.err.println("Added Service: " + translationService);
		System.err.println("Properties: " + properties);
	}
	
	public void removeTranslationService(ITranslationService translationService, Map<String, Object> properties) {
		System.err.println("Removed Service: " + translationService);
		System.err.println("Properties: " + properties);
	}
	
	public String translate(String category, String key, Object... args) {
		throw new UnsupportedOperationException("OSGi Service based translation is not implemented yet!");
	}
	
	public <L> L createDynamicNLSLookup(Class<L> clazz) {
		return createNLSLookup(clazz, null);
	}

	public <L> L createNLSLookup(Class<L> clazz) {
		return createNLSLookup(clazz, Locale.getDefault());
	}

	public <L> L createNLSLookup(Class<L> clazz, Locale locale) {
		if (clazz.getAnnotation(Cache.class) != null) {
			return createFromCache(clazz, locale);
		}
		return create(clazz, locale);
	}

	@SuppressWarnings("unchecked")
	private <L> L createFromCache(Class<L> clazz, Locale locale) {
		Entry entry = new Entry(clazz, locale);
		Object rv = CACHE.get(entry);
		if (rv != null) {
			L l = create(clazz, locale);
			CACHE.put(entry, l);
			return l;
		} else {
			return (L) rv;
		}
	}

	@SuppressWarnings("unchecked")
	private <L> L create(Class<L> clazz, Locale locale) {
		OSGiService osgiService = clazz.getAnnotation(OSGiService.class);
		if (osgiService != null) {
			throw new UnsupportedOperationException("OSGi Service based translation is not implemented yet!");
		} else {
			return (L) Proxy.newProxyInstance(clazz.getClassLoader(),
					new Class<?>[] { clazz }, new NLSFileInvocationHandler(
							clazz, locale));
		}
	}
	
//	private static class NLSServiceInvocationHandler implements InvocationHandler {
//
//		public Object invoke(Object proxy, Method method, Object[] args)
//				throws Throwable {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		
//	}

	private static class NLSFileInvocationHandler implements InvocationHandler {
		private static final String EXTENSION = ".properties"; //$NON-NLS-1$

		private Properties properties;

		public NLSFileInvocationHandler(Class<?> clazz, Locale locale) {
			properties = initProperties(clazz, locale);
		}

		private static Properties initProperties(Class<?> clazz, Locale locale) {
			String plain = clazz.getName();
			String language = plain + "_" + locale.getLanguage();
			String country = language + "_" + locale.getCountry();
			String variant = country + "_" + locale.getVariant();

			ClassLoader loader = clazz.getClassLoader();
			Properties properties = new Properties();

			for (String aVariant : new String[] { plain, language, country,
					variant }) {
				String file = aVariant.replace('.', '/') + EXTENSION;
				final InputStream input = loader == null ? ClassLoader
						.getSystemResourceAsStream(file) : loader
						.getResourceAsStream(file);
				if (input == null) {
					continue;
				}

				try {
					properties.load(input);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return properties;
		}

		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			if (properties == null) {
				return "Lookup is disposed";
			} else {
				Key tmp = method.getAnnotation(Key.class);
				String key;
				String value;
				if (tmp == null) {
					key = method.getName();
				} else {
					key = tmp.key();
				}

				value = properties.getProperty(key);
				if (value != null) {
					if (args.length > 0) {
						return MessageFormat.format(value, args);
					} else {
						return value;
					}
				} else {
					return "***NLS '" + key + "' UNKNOWN***";
				}
			}
		}
	}
}