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
package org.eclipse.e4.demo.simpleide.jdt.internal.editor;

import java.util.Vector;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.demo.simpleide.jdt.internal.JavaUIMessages;
import org.eclipse.e4.demo.simpleide.services.INLSLookupFactoryService;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IParent;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class JDTOutlinePage {
//	// FIXME We should use image service and pooling
//	private static Image PACKAGE_DECL;
//	private static Image IMPORT_CONTAINER_DECL;
//	private static Image IMPORT_DECL;
//
//	private static Image PUBLIC_CLASS_DECL;
//	private static Image PACKAGE_CLASS_DECL;
//
//	private static Image PUBLIC_METHOD_DECL;
//	private static Image PACKAGE_METHOD_DECL;
//	private static Image PROTECTED_METHOD_DECL;
//	private static Image PRIVATE_METHOD_DECL;

	private class ContentProvider implements ITreeContentProvider {
		private final Object[] EMPTY = new Object[0];

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		protected boolean matches(IJavaElement element) {
			if (element.getElementType() == IJavaElement.METHOD) {
				String name = element.getElementName();
				return (name != null && name.indexOf('<') >= 0);
			}
			return false;
		}

		protected IJavaElement[] filter(IJavaElement[] children) {
			boolean initializers = false;
			for (int i = 0; i < children.length; i++) {
				if (matches(children[i])) {
					initializers = true;
					break;
				}
			}

			if (!initializers)
				return children;

			Vector<IJavaElement> v = new Vector<IJavaElement>();
			for (int i = 0; i < children.length; i++) {
				if (matches(children[i]))
					continue;
				v.addElement(children[i]);
			}

			IJavaElement[] result = new IJavaElement[v.size()];
			v.copyInto(result);
			return result;
		}

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof IParent) {
				IParent c = (IParent) parentElement;
				try {
					return filter(c.getChildren());
				} catch (JavaModelException x) {

				}
			}
			return EMPTY;
		}

		public Object getParent(Object element) {
			if (element instanceof IJavaElement) {
				IJavaElement e = (IJavaElement) element;
				return e.getParent();
			}
			return null;
		}

		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}
	}

//	private class LabelProviderImpl extends DelegatingStyledCellLabelProvider {
//		public LabelProviderImpl() {
//			super();
//			// TODO Auto-generated constructor stub
//		}
//
//		@Override
//		public void update(ViewerCell cell) {
//			IJavaElement obj = (IJavaElement) cell.getElement();
//			if (obj instanceof IAnnotation) {
//			} else if (obj instanceof IImportContainer) {
//				cell.setImage(IMPORT_CONTAINER_DECL);
//				cell.setText("import declarations");
//			} else if (obj instanceof IImportDeclaration) {
//				cell.setImage(IMPORT_DECL);
//				cell.setText(obj.getElementName());
//			} else if (obj instanceof IPackageDeclaration) {
//				cell.setImage(PACKAGE_DECL);
//				cell.setText(obj.getElementName());
//			} else if (obj instanceof IType) {
//				IType type = (IType) obj;
//				try {
//					if (type.isLocal()) {
//						cell.setText(type.toString());
//						System.err.println(type);
//					} else {
//						int flags = type.getFlags();
//						if (Flags.isInterface(flags)) {
//
//						} else {
//							if (Flags.isPublic(flags)) {
//								cell.setImage(PUBLIC_CLASS_DECL);
//							} else if (Flags.isPrivate(flags)) {
//
//							} else if (Flags.isPackageDefault(flags)) {
//								cell.setImage(PACKAGE_CLASS_DECL);
//							}
//						}
//
//						cell.setText(obj.getElementName());
//					}
//				} catch (JavaModelException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			} else if (obj instanceof IMethod) {
//				IMethod type = (IMethod) obj;
//				try {
//					int flags = type.getFlags();
//
//					if (Flags.isPrivate(flags)) {
//						cell.setImage(PRIVATE_METHOD_DECL);
//					} else if (Flags.isPackageDefault(flags)) {
//						cell.setImage(PACKAGE_CLASS_DECL);
//					} else if (Flags.isProtected(flags)) {
//						cell.setImage(PROTECTED_METHOD_DECL);
//					} else {
//						cell.setImage(PUBLIC_METHOD_DECL);
//					}
//				} catch (JavaModelException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				cell.setText(obj.getElementName());
//			} else if (obj instanceof IField) {
//				cell.setText(obj.getElementName());
//			} else {
//				cell.setText(obj.getClass().getName());
//			}
//
//			System.err.println(obj.getClass());
//
//			super.update(cell);
//		}
//	}

	@Inject
	public JDTOutlinePage(Composite parent,
			@Named(IServiceConstants.ACTIVE_PART) MPart part,
			IWorkspace workspace,
			Logger logger,
			INLSLookupFactoryService nlsFactory) {
//		if (PACKAGE_DECL == null) {
//			initImages(parent);
//		}
		parent.setLayout(new FillLayout());

		JavaEditor editor = (JavaEditor) part.getObject();

		//FIXME We need to cache this!
		JavaUIMessages messages = nlsFactory.createNLSLookup(JavaUIMessages.class);
		
		TreeViewer viewer = new TreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ContentProvider());
		viewer.setLabelProvider(new JavaUILabelProvider(logger, messages));
		viewer.setInput(editor.getCompilationUnit());
	}

//	private void initImages(Composite parent) {
//		PACKAGE_DECL = new Image(parent.getDisplay(), getClass()
//				.getClassLoader().getResourceAsStream(
//						"/icons/outline/packd_obj.gif"));
//		IMPORT_CONTAINER_DECL = new Image(parent.getDisplay(), getClass()
//				.getClassLoader().getResourceAsStream(
//						"/icons/outline/impc_obj.gif"));
//		IMPORT_DECL = new Image(parent.getDisplay(), getClass()
//				.getClassLoader().getResourceAsStream(
//						"/icons/outline/imp_obj.gif"));
//		PUBLIC_CLASS_DECL = new Image(parent.getDisplay(), getClass()
//				.getClassLoader().getResourceAsStream(
//						"/icons/outline/class_obj.gif"));
//		PACKAGE_CLASS_DECL = new Image(parent.getDisplay(), getClass()
//				.getClassLoader().getResourceAsStream(
//						"/icons/outline/class_default_obj.gif"));
//		PUBLIC_METHOD_DECL = new Image(parent.getDisplay(), getClass()
//				.getClassLoader().getResourceAsStream(
//						"/icons/outline/methpub_obj.gif"));
//		PACKAGE_METHOD_DECL = new Image(parent.getDisplay(), getClass()
//				.getClassLoader().getResourceAsStream(
//						"/icons/outline/methdef_obj.gif"));
//		PROTECTED_METHOD_DECL = new Image(parent.getDisplay(), getClass()
//				.getClassLoader().getResourceAsStream(
//						"/icons/outline/methpro_obj.gif"));
//		PRIVATE_METHOD_DECL = new Image(parent.getDisplay(), getClass()
//				.getClassLoader().getResourceAsStream(
//						"/icons/outline/methpri_obj.gif"));
//	}

	@Inject
	public void setSelection(
			@Optional @Named(IServiceConstants.SELECTION) IJDTSelection selection) {
		if (selection != null) {
			System.err.println("Updating to JavaSelection '" + selection + "'");
		}
	}

	@PreDestroy
	public void dispose() {
	}
}
