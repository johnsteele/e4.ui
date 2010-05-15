/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.e4.demo.simpleide.model.simpleide;


/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @generated
 */
public interface MSimpleideFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MSimpleideFactory INSTANCE = org.eclipse.e4.demo.simpleide.model.simpleide.impl.SimpleideFactoryImpl.eINSTANCE;

	/**
	 * Returns a new object of class '<em>Simple IDE Application</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Simple IDE Application</em>'.
	 * @generated
	 */
	MSimpleIDEApplication createSimpleIDEApplication();

	/**
	 * Returns a new object of class '<em>Editor Part Descriptor</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Editor Part Descriptor</em>'.
	 * @generated
	 */
	MEditorPartDescriptor createEditorPartDescriptor();

} //MSimpleideFactory
