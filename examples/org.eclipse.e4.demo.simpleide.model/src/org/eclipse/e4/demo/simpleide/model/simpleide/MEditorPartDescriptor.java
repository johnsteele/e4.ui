/**
 * <copyright>
 * </copyright>
 *
 * $Id: MEditorPartDescriptor.java,v 1.3 2010/06/04 20:22:14 johna Exp $
 */
package org.eclipse.e4.demo.simpleide.model.simpleide;

import java.util.List;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.commands.MBindings;
import org.eclipse.e4.ui.model.application.commands.MHandlerContainer;
import org.eclipse.e4.ui.model.application.ui.MUILabel;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Editor Part Descriptor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.demo.simpleide.model.simpleide.MEditorPartDescriptor#getContributionURI <em>Contribution URI</em>}</li>
 *   <li>{@link org.eclipse.e4.demo.simpleide.model.simpleide.MEditorPartDescriptor#getFileextensions <em>Fileextensions</em>}</li>
 *   <li>{@link org.eclipse.e4.demo.simpleide.model.simpleide.MEditorPartDescriptor#getContenttypes <em>Contenttypes</em>}</li>
 * </ul>
 * </p>
 *
 * @model
 * @generated
 */
public interface MEditorPartDescriptor extends MApplicationElement, MUILabel, MHandlerContainer, MBindings {
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
	 * @model
	 * @generated
	 */
	String getContributionURI();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.demo.simpleide.model.simpleide.MEditorPartDescriptor#getContributionURI <em>Contribution URI</em>}' attribute.
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
	 * @model
	 * @generated
	 */
	List<String> getFileextensions();

	/**
	 * Returns the value of the '<em><b>Contenttypes</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contenttypes</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contenttypes</em>' attribute list.
	 * @model
	 * @generated
	 */
	List<String> getContenttypes();

} // MEditorPartDescriptor
