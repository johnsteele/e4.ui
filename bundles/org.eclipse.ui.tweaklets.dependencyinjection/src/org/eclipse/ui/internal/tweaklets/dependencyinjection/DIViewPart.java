/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.internal.tweaklets.dependencyinjection;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.internal.services.IServiceLocatorCreator;
import org.eclipse.ui.internal.util.Util;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IDisposable;
import org.eclipse.ui.services.IServiceLocator;
import org.eclipse.ui.tweaklets.dependencyinjection.DIFactory;
import org.eclipse.ui.tweaklets.dependencyinjection.IFocusable;
import org.eclipse.ui.tweaklets.dependencyinjection.IPartDescription;

/**
 * Generic view part implementation acting as a bridge to views contributed
 * using dependency injection. Provides services to the underlying
 * implementation class from the part site, and forwards custom behavior
 * implemented to the workbench. Assumes that all custom behavior (e.g.,
 * ISelectionProvider, ISaveable) is requested by the Workbench using the
 * adapter mechanism.
 * 
 * @since 3.4
 * 
 */
public class DIViewPart extends ViewPart implements IAdaptable {

	private Object implementation;
	private Composite parentComposite;
	private DIFactory factory;
	private LocalResourceManager resourceManager;
	private IPartDescription partDescription;

	public DIViewPart(DIFactory factory) {
		this.factory = factory;
		this.resourceManager = new LocalResourceManager(JFaceResources
				.getResources());
	}

	public void createPartControl(Composite parent) {
		this.parentComposite = parent;
		this.partDescription = new IPartDescription() {

			public void setContentDescription(String contentDescription) {
				DIViewPart.this.setContentDescription(contentDescription);
			}

			public void setImage(ImageDescriptor theImage) {
				DIViewPart.this.setTitleImage(resourceManager
						.createImage(theImage));
			}

			public void setName(String newName) {
				DIViewPart.this.setPartName(newName);
			}

			public void setTooltip(String toolTip) {
				DIViewPart.this.setTitleToolTip(toolTip);
			}
		};

		// the factory provides requested services related to what is being
		// created
		AbstractServiceFactory delegatingFactory = new AbstractServiceFactory() {
			public Object create(Class serviceInterface,
					IServiceLocator parentLocator, IServiceLocator locator) {
				if (Composite.class.equals(serviceInterface)) {
					return parentComposite;
				}
				if (IPartDescription.class.equals(serviceInterface)) {
					return partDescription;
				}

				return null;
			}
		};

		IServiceLocatorCreator slc = (IServiceLocatorCreator) getSite()
				.getService(IServiceLocatorCreator.class);
		IServiceLocator delegatingLocator = slc.createServiceLocator(getSite(),
				delegatingFactory);
		
		try {
			implementation = factory.createObject(delegatingLocator);
		} catch (final CoreException e) {
			throw new RuntimeException(e) {
				private static final long serialVersionUID = 1L;

				public String getLocalizedMessage() {
					return e.getStatus().getMessage();
				}
			};
		}
	}

	public void setFocus() {
		IFocusable focusable = (IFocusable) Util.getAdapter(implementation,
				IFocusable.class);
		if (focusable != null) {
			if (focusable.setFocus()) {
				return;
			}
		}
		parentComposite.setFocus();
	}

	public Object getAdapter(Class adapter) {
		Object result = Util.getAdapter(implementation, adapter);
		if (result != null) {
			return result;
		}
		return super.getAdapter(adapter);
	}

	public void dispose() {
		IDisposable disposable = (IDisposable) Util.getAdapter(implementation,
				IDisposable.class);
		if (disposable != null) {
			disposable.dispose();
		}
		implementation = null;
		resourceManager.dispose();
		resourceManager = null;
		super.dispose();
	}

}
