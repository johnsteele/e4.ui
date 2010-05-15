/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.e4.demo.simpleide.editor.model.simpleide;

import org.eclipse.e4.ui.model.application.MApplication;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Simple IDE Application</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.demo.simpleide.editor.model.simpleide.SimpleIDEApplication#getEditorPartDescriptors <em>Editor Part Descriptors</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.demo.simpleide.editor.model.simpleide.SimpleidePackage#getSimpleIDEApplication()
 * @model
 * @generated
 */
public interface SimpleIDEApplication extends EObject, MApplication {
	/**
	 * Returns the value of the '<em><b>Editor Part Descriptors</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.e4.demo.simpleide.editor.model.simpleide.EditorPartDescriptor}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Editor Part Descriptors</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Editor Part Descriptors</em>' containment reference list.
	 * @see org.eclipse.e4.demo.simpleide.editor.model.simpleide.SimpleidePackage#getSimpleIDEApplication_EditorPartDescriptors()
	 * @model containment="true"
	 * @generated
	 */
	EList<EditorPartDescriptor> getEditorPartDescriptors();

} // SimpleIDEApplication
