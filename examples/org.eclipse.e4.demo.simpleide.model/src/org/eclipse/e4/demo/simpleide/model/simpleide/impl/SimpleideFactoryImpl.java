/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.e4.demo.simpleide.model.simpleide.impl;

import org.eclipse.e4.demo.simpleide.model.simpleide.*;

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
public class SimpleideFactoryImpl extends EFactoryImpl implements MSimpleideFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final SimpleideFactoryImpl eINSTANCE = init();

	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SimpleideFactoryImpl init() {
		try {
			SimpleideFactoryImpl theSimpleideFactory = (SimpleideFactoryImpl)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/e4/demo/simpleide"); 
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
			case SimpleidePackageImpl.SIMPLE_IDE_APPLICATION: return (EObject)createSimpleIDEApplication();
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR: return (EObject)createEditorPartDescriptor();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MSimpleIDEApplication createSimpleIDEApplication() {
		SimpleIDEApplicationImpl simpleIDEApplication = new SimpleIDEApplicationImpl();
		return simpleIDEApplication;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MEditorPartDescriptor createEditorPartDescriptor() {
		EditorPartDescriptorImpl editorPartDescriptor = new EditorPartDescriptorImpl();
		return editorPartDescriptor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SimpleidePackageImpl getSimpleidePackage() {
		return (SimpleidePackageImpl)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static SimpleidePackageImpl getPackage() {
		return SimpleidePackageImpl.eINSTANCE;
	}

} //SimpleideFactoryImpl
