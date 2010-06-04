/**
 * <copyright>
 * </copyright>
 *
 * $Id: MSimpleIDEApplication.java,v 1.1 2010/05/15 12:59:52 tschindl Exp $
 */
package org.eclipse.e4.demo.simpleide.model.simpleide;

import java.util.List;
import org.eclipse.e4.ui.model.application.MApplication;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Simple IDE Application</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.demo.simpleide.model.simpleide.MSimpleIDEApplication#getEditorPartDescriptors <em>Editor Part Descriptors</em>}</li>
 * </ul>
 * </p>
 *
 * @model
 * @generated
 */
public interface MSimpleIDEApplication extends MApplication {
	/**
	 * Returns the value of the '<em><b>Editor Part Descriptors</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.e4.demo.simpleide.model.simpleide.MEditorPartDescriptor}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Editor Part Descriptors</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Editor Part Descriptors</em>' containment reference list.
	 * @model containment="true"
	 * @generated
	 */
	List<MEditorPartDescriptor> getEditorPartDescriptors();

} // MSimpleIDEApplication
