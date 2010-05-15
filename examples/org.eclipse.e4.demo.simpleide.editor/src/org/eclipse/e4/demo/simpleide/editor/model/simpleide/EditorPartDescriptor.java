/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.e4.demo.simpleide.editor.model.simpleide;

import org.eclipse.e4.ui.model.application.MApplicationElement;

import org.eclipse.e4.ui.model.application.commands.MBindings;
import org.eclipse.e4.ui.model.application.commands.MHandlerContainer;

import org.eclipse.e4.ui.model.application.ui.MUILabel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Editor Part Descriptor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.demo.simpleide.editor.model.simpleide.EditorPartDescriptor#getContributionURI <em>Contribution URI</em>}</li>
 *   <li>{@link org.eclipse.e4.demo.simpleide.editor.model.simpleide.EditorPartDescriptor#getFileextensions <em>Fileextensions</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.demo.simpleide.editor.model.simpleide.SimpleidePackage#getEditorPartDescriptor()
 * @model
 * @generated
 */
public interface EditorPartDescriptor extends EObject, MUILabel, MHandlerContainer, MBindings, MApplicationElement {
	/**
	 * Returns the value of the '<em><b>Contribution URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contribution URI</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contribution URI</em>' attribute.
	 * @see #setContributionURI(String)
	 * @see org.eclipse.e4.demo.simpleide.editor.model.simpleide.SimpleidePackage#getEditorPartDescriptor_ContributionURI()
	 * @model
	 * @generated
	 */
	String getContributionURI();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.demo.simpleide.editor.model.simpleide.EditorPartDescriptor#getContributionURI <em>Contribution URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contribution URI</em>' attribute.
	 * @see #getContributionURI()
	 * @generated
	 */
	void setContributionURI(String value);

	/**
	 * Returns the value of the '<em><b>Fileextensions</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fileextensions</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fileextensions</em>' attribute list.
	 * @see org.eclipse.e4.demo.simpleide.editor.model.simpleide.SimpleidePackage#getEditorPartDescriptor_Fileextensions()
	 * @model
	 * @generated
	 */
	EList<String> getFileextensions();

} // EditorPartDescriptor
