/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.e4.demo.simpleide.editor.model.simpleide.impl;

import org.eclipse.e4.demo.simpleide.editor.model.simpleide.EditorPartDescriptor;
import org.eclipse.e4.demo.simpleide.editor.model.simpleide.SimpleIDEApplication;
import org.eclipse.e4.demo.simpleide.editor.model.simpleide.SimpleideFactory;
import org.eclipse.e4.demo.simpleide.editor.model.simpleide.SimpleidePackage;

import org.eclipse.e4.ui.model.application.commands.impl.CommandsPackageImpl;

import org.eclipse.e4.ui.model.application.impl.ApplicationPackageImpl;

import org.eclipse.e4.ui.model.application.ui.impl.UiPackageImpl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SimpleidePackageImpl extends EPackageImpl implements SimpleidePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass simpleIDEApplicationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass editorPartDescriptorEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.e4.demo.simpleide.editor.model.simpleide.SimpleidePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private SimpleidePackageImpl() {
		super(eNS_URI, SimpleideFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link SimpleidePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static SimpleidePackage init() {
		if (isInited) return (SimpleidePackage)EPackage.Registry.INSTANCE.getEPackage(SimpleidePackage.eNS_URI);

		// Obtain or create and register package
		SimpleidePackageImpl theSimpleidePackage = (SimpleidePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof SimpleidePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new SimpleidePackageImpl());

		isInited = true;

		// Initialize simple dependencies
		ApplicationPackageImpl.eINSTANCE.eClass();

		// Create package meta-data objects
		theSimpleidePackage.createPackageContents();

		// Initialize created meta-data
		theSimpleidePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theSimpleidePackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(SimpleidePackage.eNS_URI, theSimpleidePackage);
		return theSimpleidePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSimpleIDEApplication() {
		return simpleIDEApplicationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSimpleIDEApplication_EditorPartDescriptors() {
		return (EReference)simpleIDEApplicationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEditorPartDescriptor() {
		return editorPartDescriptorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEditorPartDescriptor_ContributionURI() {
		return (EAttribute)editorPartDescriptorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEditorPartDescriptor_Fileextensions() {
		return (EAttribute)editorPartDescriptorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SimpleideFactory getSimpleideFactory() {
		return (SimpleideFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		simpleIDEApplicationEClass = createEClass(SIMPLE_IDE_APPLICATION);
		createEReference(simpleIDEApplicationEClass, SIMPLE_IDE_APPLICATION__EDITOR_PART_DESCRIPTORS);

		editorPartDescriptorEClass = createEClass(EDITOR_PART_DESCRIPTOR);
		createEAttribute(editorPartDescriptorEClass, EDITOR_PART_DESCRIPTOR__CONTRIBUTION_URI);
		createEAttribute(editorPartDescriptorEClass, EDITOR_PART_DESCRIPTOR__FILEEXTENSIONS);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		ApplicationPackageImpl theApplicationPackage = (ApplicationPackageImpl)EPackage.Registry.INSTANCE.getEPackage(ApplicationPackageImpl.eNS_URI);
		UiPackageImpl theUiPackage = (UiPackageImpl)EPackage.Registry.INSTANCE.getEPackage(UiPackageImpl.eNS_URI);
		CommandsPackageImpl theCommandsPackage = (CommandsPackageImpl)EPackage.Registry.INSTANCE.getEPackage(CommandsPackageImpl.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		simpleIDEApplicationEClass.getESuperTypes().add(theApplicationPackage.getApplication());
		editorPartDescriptorEClass.getESuperTypes().add(theUiPackage.getUILabel());
		editorPartDescriptorEClass.getESuperTypes().add(theCommandsPackage.getHandlerContainer());
		editorPartDescriptorEClass.getESuperTypes().add(theCommandsPackage.getBindings());
		editorPartDescriptorEClass.getESuperTypes().add(theApplicationPackage.getApplicationElement());

		// Initialize classes and features; add operations and parameters
		initEClass(simpleIDEApplicationEClass, SimpleIDEApplication.class, "SimpleIDEApplication", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSimpleIDEApplication_EditorPartDescriptors(), this.getEditorPartDescriptor(), null, "editorPartDescriptors", null, 0, -1, SimpleIDEApplication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(editorPartDescriptorEClass, EditorPartDescriptor.class, "EditorPartDescriptor", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEditorPartDescriptor_ContributionURI(), ecorePackage.getEString(), "contributionURI", null, 0, 1, EditorPartDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEditorPartDescriptor_Fileextensions(), ecorePackage.getEString(), "fileextensions", null, 0, -1, EditorPartDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //SimpleidePackageImpl
