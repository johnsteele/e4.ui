/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.e4.demo.simpleide.editor.model.simpleide.impl;

import org.eclipse.e4.demo.simpleide.editor.model.simpleide.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SimpleideFactoryImpl extends EFactoryImpl implements SimpleideFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SimpleideFactory init() {
		try {
			SimpleideFactory theSimpleideFactory = (SimpleideFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/e4/demo/simpleide"); 
			if (theSimpleideFactory != null) {
				return theSimpleideFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new SimpleideFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SimpleideFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case SimpleidePackage.SIMPLE_IDE_APPLICATION: return createSimpleIDEApplication();
			case SimpleidePackage.EDITOR_PART_DESCRIPTOR: return createEditorPartDescriptor();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SimpleIDEApplication createSimpleIDEApplication() {
		SimpleIDEApplicationImpl simpleIDEApplication = new SimpleIDEApplicationImpl();
		return simpleIDEApplication;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EditorPartDescriptor createEditorPartDescriptor() {
		EditorPartDescriptorImpl editorPartDescriptor = new EditorPartDescriptorImpl();
		return editorPartDescriptor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SimpleidePackage getSimpleidePackage() {
		return (SimpleidePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static SimpleidePackage getPackage() {
		return SimpleidePackage.eINSTANCE;
	}

} //SimpleideFactoryImpl
