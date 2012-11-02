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

import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.services.IServiceLocatorCreator;
import org.eclipse.ui.internal.util.Util;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IDisposable;
import org.eclipse.ui.services.IServiceLocator;
import org.eclipse.ui.tweaklets.dependencyinjection.DIFactory;
import org.eclipse.ui.tweaklets.dependencyinjection.IFocusable;
import org.eclipse.ui.tweaklets.dependencyinjection.IPartDescription;
import org.eclipse.ui.tweaklets.dependencyinjection.ISaveable;

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
public class DIEditorPart extends EditorPart implements IAdaptable {

	private Object implementation;
	private Composite parentComposite;
	private DIFactory factory;
	private LocalResourceManager resourceManager;
	private IPartDescription partDescription;

	public DIEditorPart(DIFactory factory) {
		this.factory = factory;
		this.resourceManager = new LocalResourceManager(JFaceResources
				.getResources());
	}

	public void createPartControl(Composite parent) {
		this.parentComposite = parent;
		this.partDescription = new IPartDescription() {

			public void setContentDescription(String contentDescription) {
				DIEditorPart.this.setContentDescription(contentDescription);
			}

			public void setImage(ImageDescriptor theImage) {
				DIEditorPart.this.setTitleImage(resourceManager
						.createImage(theImage));
			}

			public void setName(String newName) {
				DIEditorPart.this.setPartName(newName);
			}

			public void setTooltip(String toolTip) {
				DIEditorPart.this.setTitleToolTip(toolTip);
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
				if (IEditorInput.class.equals(serviceInterface)) {
					return getEditorInput();
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
				delegatingFactory, new IDisposable(){
			public void dispose() {
				getSite().getPage().closeEditor(DIEditorPart.this, true);
			}
		});

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
		

		ISaveable saveable = (ISaveable) Util.getAdapter(implementation,
				ISaveable.class);
		if (saveable != null) {
			IObservableValue dirty = saveable.getDirty();
			if (((Boolean)dirty.getValue()).booleanValue()) {
				firePropertyChange(ISaveablePart.PROP_DIRTY);
			}
			dirty.addChangeListener(new IChangeListener(){
				public void handleChange(ChangeEvent event) {
					firePropertyChange(ISaveablePart.PROP_DIRTY);
				}});
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

	public void doSave(IProgressMonitor monitor) {
		ISaveable saveable = (ISaveable) Util.getAdapter(implementation,
				ISaveable.class);
		if (saveable != null) {
			try {
				saveable.doSave(monitor, getSite());
			} catch (CoreException e) {
				monitor.setCanceled(true);
				throw new RuntimeException(e);
			}
		}
	}

	public void doSaveAs() {
		ISaveable saveable = (ISaveable) Util.getAdapter(implementation,
				ISaveable.class);
		if (saveable != null) {
			try {
				saveable.doSaveAs(getSite());
			} catch (CoreException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
	}

	public boolean isDirty() {
		ISaveable saveable = (ISaveable) Util.getAdapter(implementation,
				ISaveable.class);
		if (saveable != null) {
			return ((Boolean) saveable.getDirty().getValue()).booleanValue();
		}
		return false;
	}

	public boolean isSaveAsAllowed() {
		ISaveable saveable = (ISaveable) Util.getAdapter(implementation,
				ISaveable.class);
		if (saveable != null) {
			return saveable.isSaveAsAllowed();
		}
		return false;
	}

}
