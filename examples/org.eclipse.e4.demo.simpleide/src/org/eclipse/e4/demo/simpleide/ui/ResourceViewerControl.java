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
package org.eclipse.e4.demo.simpleide.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class ResourceViewerControl extends Composite {
	private TreeViewer viewer;
	
	public ResourceViewerControl(Composite parent, int style, IWorkspace workspace, IResource resource) {
		super(parent, style);
		setLayout(new FillLayout());
				
		viewer = new TreeViewer(this);
		viewer.setContentProvider(new ResourceContentProviderImpl());
		viewer.setLabelProvider(new LabelProvider() {
			public String getText(Object element) {
				if (element instanceof IResource)
					return ((IResource) element).getName();
				return element == null ? "" : element.toString();
			}
		});
		viewer.setSorter(new ViewerSorter());
		viewer.setInput(workspace.getRoot());
		
		if( resource != null ) {
			IContainer resContainer;
			if( resource instanceof IContainer ) {
				resContainer = (IContainer) resource;
			} else {
				resContainer = resource.getParent();
			}
			
			if( resContainer != null ) {
				List<IContainer> path = new ArrayList<IContainer>();
				path.add(resContainer);
				while( (resContainer = resContainer.getParent()) != null ) {
					path.add(resContainer);
				}
				Collections.reverse(path);
				path.remove(0);
				viewer.setSelection(new TreeSelection(new TreePath(path.toArray())));
			}
		}
	}

	public IResource getResource() {
		if( viewer.getSelection().isEmpty() ) {
			return null;
		} else {
			IStructuredSelection s = (IStructuredSelection) viewer.getSelection();
			return (IResource) s.getFirstElement();
		}
	}
	
	private static class ResourceContentProviderImpl implements ITreeContentProvider {

		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub
			
		}

		public Object[] getChildren(Object parentElement) {
			final IContainer resource = (IContainer) parentElement;
			final List<IResource> children = new ArrayList<IResource>();
			try {
				for( IResource res : resource.members() ) {
					if( res instanceof IContainer ) {
						children.add(res);
					}
				}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return children.toArray();
		}

		public Object getParent(Object element) {
			IContainer resource = (IContainer) element;
			return resource.getParent();
		}

		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}
		
	}
}
