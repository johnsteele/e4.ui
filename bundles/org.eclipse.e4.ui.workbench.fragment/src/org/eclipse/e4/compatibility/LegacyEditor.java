/*******************************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.compatibility;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.core.services.context.spi.IContextConstants;
import org.eclipse.e4.extensions.ExtensionUtils;
import org.eclipse.e4.extensions.ModelEditorReference;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.widgets.CTabFolder;
import org.eclipse.e4.ui.widgets.CTabItem;
import org.eclipse.e4.ui.workbench.swt.internal.AbstractPartRenderer;
import org.eclipse.e4.workbench.ui.internal.Activator;
import org.eclipse.e4.workbench.ui.internal.Policy;
import org.eclipse.e4.workbench.ui.internal.Trackable;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.SubActionBars;
import org.eclipse.ui.internal.EditorActionBars;
import org.eclipse.ui.internal.EditorActionBuilder;
import org.eclipse.ui.internal.EditorSite;
import org.eclipse.ui.internal.ViewSite;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.internal.registry.EditorDescriptor;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;

/**
 * This class is an implementation of an MPart that can be used to host a 3.x
 * EditorPart into an Eclipse 4.0 application.
 * 
 * @since 4.0
 * 
 */
public class LegacyEditor {
	public final static String LEGACY_VIEW_URI = "platform:/plugin/org.eclipse.ui.workbench/org.eclipse.e4.compatibility.LegacyEditor"; //$NON-NLS-1$

	private static final String IS_DIRTY = "isDirty"; //$NON-NLS-1$
	private static final String EDITOR_DISPOSED = "editorDisposed"; //$NON-NLS-1$

	private MPart editorPart;
	private IEditorPart editorWBPart;

	private Map<IEditorPart, Trackable> trackables = new HashMap<IEditorPart, Trackable>();

	/**
	 * Create and initialize a 3.x editor from an e4 contribution
	 * 
	 * @param parent
	 *            The SWT parent for the editor
	 * @param context
	 *            The context under which this part is being created
	 * @param part
	 *            The MPart representing the editor
	 */
	public LegacyEditor(final Composite parent, IEclipseContext context,
			final MPart part) {
		editorPart = part;

		IConfigurationElement editorElement = findEditorConfig(part.getId());

		EditorDescriptor desc = new EditorDescriptor(part.getId(),
				editorElement);

		parent.setLayout(new FillLayout());

		// Convert the relative path into a bundle URI
		String imagePath = editorElement.getAttribute("icon"); //$NON-NLS-1$
		String imageURI = imagePath;
		if (!imagePath.startsWith("platform:")) { //$NON-NLS-1$
			imagePath = imagePath.replace("$nl$", ""); //$NON-NLS-1$//$NON-NLS-2$
			if (imagePath.charAt(0) != '/') {
				imagePath = '/' + imagePath;
			}
			String bundleId = editorElement.getContributor().getName();
			imageURI = "platform:/plugin/" + bundleId + imagePath; //$NON-NLS-1$
		}

		part.setIconURI(imageURI);

		try {
			editorWBPart = desc.createEditor();
		} catch (CoreException e) {
			e.printStackTrace();
		}

		if (editorWBPart == null)
			return;

		try {
			final IEclipseContext localContext = part.getContext();
			IEclipseContext parentContext = (IEclipseContext) localContext
					.get(IContextConstants.PARENT);

			localContext.set(IContextConstants.DEBUG_STRING, "Legacy Editor(" //$NON-NLS-1$
					+ desc.getLabel() + ")"); //$NON-NLS-1$
			parentContext.set(IContextConstants.ACTIVE_CHILD, localContext);

			part.setObject(editorWBPart);
			// Assign a 'site' for the newly instantiated part
			WorkbenchPage page = (WorkbenchPage) localContext
					.get(WorkbenchPage.class.getName());
			ModelEditorReference ref = new ModelEditorReference(part, page);
			EditorSite site = new EditorSite(ref, editorWBPart, page);
			EditorActionBars bars = getEditorActionBars(desc, page, page
					.getWorkbenchWindow(), part.getId());
			site.setActionBars(bars);
			site.setConfigurationElement(editorElement);
			editorWBPart.init(site, (IEditorInput) localContext
					.get(IEditorInput.class.getName()));

			editorWBPart.createPartControl(parent);
			localContext.set(MPart.class.getName(), part);

			// HACK!! presumes it's the -last- child of the parent
			if (parent.getChildren().length > 0) {
				Control newCtrl = parent.getChildren()[parent.getChildren().length - 1];
				newCtrl.addDisposeListener(new DisposeListener() {
					public void widgetDisposed(DisposeEvent e) {
						disposeEditor(editorPart);
					}
				});
			}

			// Manage the 'dirty' state
			final IEditorPart implementation = editorWBPart;
			localContext.set(IS_DIRTY, implementation.isDirty());
			localContext.set(EDITOR_DISPOSED, Boolean.FALSE);

			Trackable updateDirty = new Trackable(localContext) {
				private CTabItem findItemForPart(CTabFolder ctf) {
					CTabItem[] items = ctf.getItems();
					for (int i = 0; i < items.length; i++) {
						if (items[i].getData(AbstractPartRenderer.OWNING_ME) == part) {
							return items[i];
						}
					}

					return null;
				}

				public void run() {
					if (!participating) {
						return;
					}
					trackingContext.get(EDITOR_DISPOSED);
					boolean dirty = (Boolean) trackingContext.get(IS_DIRTY);
					if (parent instanceof CTabFolder) {
						CTabFolder ctf = (CTabFolder) parent;
						CTabItem partItem = findItemForPart(ctf);
						if (partItem == null)
							return;

						String itemText = partItem.getText();
						if (dirty && itemText.indexOf('*') != 0) {
							itemText = '*' + itemText;
						} else if (itemText.indexOf('*') == 0) {
							itemText = itemText.substring(1);
						}
						partItem.setText(itemText);
					}
				}
			};
			trackables.put(implementation, updateDirty);
			localContext.runAndTrack(updateDirty);
			editorWBPart.addPropertyListener(new IPropertyListener() {
				public void propertyChanged(Object source, int propId) {
					localContext.set(IS_DIRTY, implementation.isDirty());
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void disposeEditor(MPart part) {
		Object obj = part.getObject();
		if (!(obj instanceof IWorkbenchPart))
			return;

		if (obj instanceof IEditorPart) {
			Activator.trace(Policy.DEBUG_RENDERER,
					"Disposing tracker for " + obj, null); //$NON-NLS-1$
			Trackable t = trackables.remove(obj);
			t.participating = false;
			part.getContext().set(EDITOR_DISPOSED, Boolean.TRUE);
		}
		((IWorkbenchPart) obj).dispose();
		if (obj instanceof IEditorPart) {
			EditorSite site = (EditorSite) ((IEditorPart) obj).getEditorSite();
			disposeEditorActionBars((EditorActionBars) site.getActionBars());
			site.dispose();
		} else if (obj instanceof IViewPart) {
			ViewSite site = (ViewSite) ((IViewPart) obj).getViewSite();
			SubActionBars bars = (SubActionBars) site.getActionBars();
			bars.dispose();
			site.dispose();
		}
		part.setObject(null);
	}

	Map<String, EditorActionBars> actionCache = new HashMap<String, EditorActionBars>();

	private EditorActionBars getEditorActionBars(EditorDescriptor desc,
			WorkbenchPage page, IWorkbenchWindow workbenchWindow, String type) {
		// Get the editor type.

		// If an action bar already exists for this editor type return it.
		EditorActionBars actionBars = actionCache.get(type);
		if (actionBars != null) {
			actionBars.addRef();
			return actionBars;
		}

		// Create a new action bar set.
		actionBars = new EditorActionBars(page, workbenchWindow, type);
		actionBars.addRef();
		actionCache.put(type, actionBars);

		// Read base contributor.
		IEditorActionBarContributor contr = desc.createActionBarContributor();
		if (contr != null) {
			actionBars.setEditorContributor(contr);
			contr.init(actionBars, page);
		}

		// Read action extensions.
		EditorActionBuilder builder = new EditorActionBuilder();
		contr = builder.readActionExtensions(desc);
		if (contr != null) {
			actionBars.setExtensionContributor(contr);
			contr.init(actionBars, page);
		}

		// Return action bars.
		return actionBars;
	}

	private void disposeEditorActionBars(EditorActionBars actionBars) {
		actionBars.removeRef();
		if (actionBars.getRef() <= 0) {
			String type = actionBars.getEditorType();
			actionCache.remove(type);
			actionBars.dispose();
		}
	}

	private IConfigurationElement findEditorConfig(String id) {
		IConfigurationElement[] editors = ExtensionUtils
				.getExtensions(IWorkbenchRegistryConstants.PL_EDITOR);
		IConfigurationElement editorContribution = ExtensionUtils
				.findExtension(editors, id);
		return editorContribution;
	}

	/**
	 * @return The client implementation of their 3.x part
	 */
	public IEditorPart getEditorWBPart() {
		return editorWBPart;
	}
}
