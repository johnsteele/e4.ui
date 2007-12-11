/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.tweaklets.dependencyinjection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.internal.misc.StatusUtil;
import org.eclipse.ui.internal.tweaklets.dependencyinjection.ClassIdentifier;
import org.eclipse.ui.services.IServiceLocator;

/**
 * Factory that uses reflection to create instances by constructor injection.
 * The factory attempts to create the target class by invoking the greediest
 * satisfiable constructor. That is, the constructor with the greatest number of
 * arguments where all the arguments can be found as services.
 * 
 * <p>
 * This factory should only be used in situations where a component only has one
 * constructor, or where it is completely obvious which constructor is the
 * greediest. If a component has many different constructors or needs to take
 * arguments to its constructor which can't simply be located by using the
 * argument types as service keys, clients should implement a custom factory
 * rather than trying to reuse this class.
 * </p>
 * 
 * <p>
 * This class is normally referred to in a plugin.xml file as part of some
 * extension markup. This will work for for any extension point that looks for
 * objects of type DIFactory. (such as the org.eclipse.ui.views extension
 * point).
 * </p>
 * 
 * <p>
 * For example, the following XML markup would create the
 * org.eclipse.ui.examples.components.views.context.RedirectAllView view by
 * passing any dependencies it needs into its constructor. Notice that CIFactory
 * needs to be given the name of the class to create by separating it with a
 * colon.
 * </p>
 * <code>
 * <view
 *    class="org.eclipse.ui.services.CIFactory:org.eclipse.ui.examples.components.views.context.RedirectAllView"
 *    category="org.eclipse.ui.examples.components.context"
 *    name="RedirectAllView"
 *    id="org.eclipse.ui.examples.components.views.context.RedirectAllView"/>          
 *    
 * </code>
 * 
 * <p>
 * Not intended to be subclassed or instantiated by clients.
 * </p>
 * 
 * @since 3.4
 */
public class CIFactory extends DIFactory implements IExecutableExtension {

	private ClassIdentifier classId = null;
	private Class targetClass = null;

	/**
	 * Should only be called when this factory is created through an extension
	 * point. If this constructor is used, the caller MUST call
	 * setInitializationData before attempting to use the factory. Call one of
	 * the other constructors if instantiating this object programmatically.
	 */
	public CIFactory() {
	}

	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {

		if (!(data instanceof String)) {
			throw new CoreException(StatusUtil.newStatus(IStatus.ERROR,
					"initialization data must be a string", null)); //$NON-NLS-1$
		}

		classId = new ClassIdentifier(config.getContributor().getName(),
				(String) data);

	}

	public Object createObject(IServiceLocator serviceLocator) {

		Class targetClass = this.targetClass;

		// Find the greediest satisfiable constructor
		if (targetClass == null) {
			try {
				targetClass = classId.loadClass();
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(
						"could not find class " + classId.getTypeName() + " in " + classId.getNamespace(), e); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		Constructor targetConstructor = null;

		Constructor[] constructors = targetClass.getConstructors();

		// Optimization: if there's only one constructor, use it.
		if (constructors.length == 1) {
			targetConstructor = constructors[0];
		} else {
			ArrayList toSort = new ArrayList();

			for (int i = 0; i < constructors.length; i++) {
				Constructor constructor = constructors[i];

				// Filter out non-public constructors
				if ((constructor.getModifiers() & Modifier.PUBLIC) != 0) {
					toSort.add(constructor);
				}
			}

			// Sort the constructors by descending number of constructor
			// arguments
			Collections.sort(toSort, new Comparator() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see java.util.Comparator#compare(java.lang.Object,
				 *      java.lang.Object)
				 */
				public int compare(Object arg0, Object arg1) {
					Constructor c1 = (Constructor) arg0;
					Constructor c2 = (Constructor) arg1;

					int l1 = c1.getParameterTypes().length;
					int l2 = c2.getParameterTypes().length;

					return l1 - l2;
				}
			});

			// Find the first satisfiable constructor
			for (Iterator iter = toSort.iterator(); iter.hasNext()
					&& targetConstructor == null;) {
				Constructor next = (Constructor) iter.next();

				boolean satisfiable = true;

				Class[] params = next.getParameterTypes();
				for (int i = 0; i < params.length && satisfiable; i++) {
					Class clazz = params[i];

					if (!serviceLocator.hasService(clazz)) {
						satisfiable = false;
					}
				}

				if (satisfiable) {
					targetConstructor = next;
				}
			}
		}

		if (targetConstructor == null) {
			throw new RuntimeException(
					"could not find satisfiable constructor in class " + classId.getTypeName() + " in " + classId.getNamespace()); //$NON-NLS-1$//$NON-NLS-2$
		}

		Class[] paramKeys = targetConstructor.getParameterTypes();

		try {
			Object[] params = new Object[paramKeys.length];
			for (int i = 0; i < params.length; i++) {
				params[i] = serviceLocator.getService(paramKeys[i]);
			}

			return targetConstructor.newInstance(params);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
