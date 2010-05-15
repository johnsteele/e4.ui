/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.e4.demo.simpleide.editor.model.simpleide;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.e4.demo.simpleide.editor.model.simpleide.SimpleidePackage
 * @generated
 */
public interface SimpleideFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SimpleideFactory eINSTANCE = org.eclipse.e4.demo.simpleide.editor.model.simpleide.impl.SimpleideFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Simple IDE Application</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Simple IDE Application</em>'.
	 * @generated
	 */
	SimpleIDEApplication createSimpleIDEApplication();

	/**
	 * Returns a new object of class '<em>Editor Part Descriptor</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Editor Part Descriptor</em>'.
	 * @generated
	 */
	EditorPartDescriptor createEditorPartDescriptor();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	SimpleidePackage getSimpleidePackage();

} //SimpleideFactory
