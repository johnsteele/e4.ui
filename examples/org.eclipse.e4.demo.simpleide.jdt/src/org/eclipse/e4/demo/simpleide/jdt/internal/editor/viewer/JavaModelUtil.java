/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Matt Chapman, mpchapman@gmail.com - 89977 Make JDT .java agnostic
 *******************************************************************************/
package org.eclipse.e4.demo.simpleide.jdt.internal.editor.viewer;

import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class JavaModelUtil {
	/**
	 * @param type the type to test
	 * @return <code>true</code> iff the type is an interface or an annotation
	 * @throws JavaModelException thrown when the field can not be accessed
	 */
	public static boolean isInterfaceOrAnnotation(IType type) throws JavaModelException {
		return type.isInterface();
	}
	
	/**
	 * Returns the package fragment root of <code>IJavaElement</code>. If the given
	 * element is already a package fragment root, the element itself is returned.
	 * @param element the element
	 * @return the package fragment root of the element or <code>null</code>
	 */
	public static IPackageFragmentRoot getPackageFragmentRoot(IJavaElement element) {
		return (IPackageFragmentRoot) element.getAncestor(IJavaElement.PACKAGE_FRAGMENT_ROOT);
	}

	/**
	 * Returns the classpath entry of the given package fragment root. This is the raw entry, except
	 * if the root is a referenced library, in which case it's the resolved entry.
	 * 
	 * @param root a package fragment root
	 * @return the corresponding classpath entry
	 * @throws JavaModelException if accessing the entry failed
	 * @since 3.6
	 */
	public static IClasspathEntry getClasspathEntry(IPackageFragmentRoot root) throws JavaModelException {
		IClasspathEntry rawEntry= root.getRawClasspathEntry();
		int rawEntryKind= rawEntry.getEntryKind();
		switch (rawEntryKind) {
			case IClasspathEntry.CPE_LIBRARY:
			case IClasspathEntry.CPE_VARIABLE:
			case IClasspathEntry.CPE_CONTAINER: // should not happen, see https://bugs.eclipse.org/bugs/show_bug.cgi?id=305037
				if (root.isArchive() && root.getKind() == IPackageFragmentRoot.K_BINARY) {
					IClasspathEntry resolvedEntry= root.getResolvedClasspathEntry();
					if (resolvedEntry.getReferencingEntry() != null)
						return resolvedEntry;
					else
						return rawEntry;
				}
		}
		return rawEntry;
	}
	
	/**
	 * Checks whether the given type has a valid main method or not.
	 * @param type the type to test
	 * @return returns <code>true</code> if the type has a main method
	 * @throws JavaModelException thrown when the type can not be accessed
	 */
	public static boolean hasMainMethod(IType type) throws JavaModelException {
		IMethod[] methods= type.getMethods();
		for (int i= 0; i < methods.length; i++) {
			if (methods[i].isMainMethod()) {
				return true;
			}
		}
		return false;
	}

}
