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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.services.IDisposable;
import org.eclipse.ui.services.IServiceLocator;

/**
 * A <code>DIFactory</code> is used to construct objects using dependency
 * injection. Most factories will construct a new object each time they are
 * asked for a handle, however it is also possible for factories to return
 * objects that are implemented as singletons, or shared by multiple owners.
 * 
 * @since 3.4
 */
public abstract class DIFactory {

	/**
	 * Returns an object, given an <code>IServiceProvider</code> that will
	 * contain all of the object's dependencies. If a required dependency is
	 * missing, a CoreException will be thrown. If the returned object
	 * implements or adapts to {@link IDisposable}, the caller is responsible
	 * for calling {@link IDisposable#dispose()} on the returned object when
	 * they are done with it.
	 * 
	 * @param services
	 *            the service locator that holds all services available to the
	 *            object to be returned
	 * @return the created object
	 * @throws CoreException
	 *             if unable to instantiate the object
	 */
	public abstract Object createObject(IServiceLocator services)
			throws CoreException;

}
