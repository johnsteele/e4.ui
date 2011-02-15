/**
 * <copyright>
 * </copyright>
 *
 * $Id: SimpleidePackageImpl.java,v 1.6 2011/01/17 12:18:07 pwebster Exp $
 */
package org.eclipse.e4.demo.simpleide.model.simpleide.impl;

import org.eclipse.e4.demo.simpleide.model.simpleide.MEditorPartDescriptor;
import org.eclipse.e4.demo.simpleide.model.simpleide.MSimpleIDEApplication;
import org.eclipse.e4.demo.simpleide.model.simpleide.MSimpleideFactory;
import org.eclipse.e4.ui.model.application.commands.impl.CommandsPackageImpl;
import org.eclipse.e4.ui.model.application.impl.ApplicationPackageImpl;
import org.eclipse.e4.ui.model.application.ui.impl.UiPackageImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.e4.demo.simpleide.model.simpleide.MSimpleideFactory
 * @model kind="package"
 * @generated
 */
public class SimpleidePackageImpl extends EPackageImpl {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNAME = "simpleide";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNS_URI = "http://www.eclipse.org/e4/demo/simpleide";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNS_PREFIX = "simpleide";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final SimpleidePackageImpl eINSTANCE = org.eclipse.e4.demo.simpleide.model.simpleide.impl.SimpleidePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.e4.demo.simpleide.model.simpleide.impl.SimpleIDEApplicationImpl <em>Simple IDE Application</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.demo.simpleide.model.simpleide.impl.SimpleIDEApplicationImpl
	 * @see org.eclipse.e4.demo.simpleide.model.simpleide.impl.SimpleidePackageImpl#getSimpleIDEApplication()
	 * @generated
	 */
	public static final int SIMPLE_IDE_APPLICATION = 0;

	/**
	 * The feature id for the '<em><b>Element Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__ELEMENT_ID = ApplicationPackageImpl.APPLICATION__ELEMENT_ID;

	/**
	 * The feature id for the '<em><b>Tags</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__TAGS = ApplicationPackageImpl.APPLICATION__TAGS;

	/**
	 * The feature id for the '<em><b>Contributor URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__CONTRIBUTOR_URI = ApplicationPackageImpl.APPLICATION__CONTRIBUTOR_URI;

	/**
	 * The feature id for the '<em><b>Widget</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__WIDGET = ApplicationPackageImpl.APPLICATION__WIDGET;

	/**
	 * The feature id for the '<em><b>Renderer</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__RENDERER = ApplicationPackageImpl.APPLICATION__RENDERER;

	/**
	 * The feature id for the '<em><b>To Be Rendered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__TO_BE_RENDERED = ApplicationPackageImpl.APPLICATION__TO_BE_RENDERED;

	/**
	 * The feature id for the '<em><b>On Top</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__ON_TOP = ApplicationPackageImpl.APPLICATION__ON_TOP;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__VISIBLE = ApplicationPackageImpl.APPLICATION__VISIBLE;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__PARENT = ApplicationPackageImpl.APPLICATION__PARENT;

	/**
	 * The feature id for the '<em><b>Container Data</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__CONTAINER_DATA = ApplicationPackageImpl.APPLICATION__CONTAINER_DATA;

	/**
	 * The feature id for the '<em><b>Cur Shared Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__CUR_SHARED_REF = ApplicationPackageImpl.APPLICATION__CUR_SHARED_REF;

	/**
	 * The feature id for the '<em><b>Visible When</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__VISIBLE_WHEN = ApplicationPackageImpl.APPLICATION__VISIBLE_WHEN;

	/**
	 * The feature id for the '<em><b>Accessibility Phrase</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__ACCESSIBILITY_PHRASE = ApplicationPackageImpl.APPLICATION__ACCESSIBILITY_PHRASE;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__CHILDREN = ApplicationPackageImpl.APPLICATION__CHILDREN;

	/**
	 * The feature id for the '<em><b>Selected Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__SELECTED_ELEMENT = ApplicationPackageImpl.APPLICATION__SELECTED_ELEMENT;

	/**
	 * The feature id for the '<em><b>Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__CONTEXT = ApplicationPackageImpl.APPLICATION__CONTEXT;

	/**
	 * The feature id for the '<em><b>Variables</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__VARIABLES = ApplicationPackageImpl.APPLICATION__VARIABLES;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__PROPERTIES = ApplicationPackageImpl.APPLICATION__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Handlers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__HANDLERS = ApplicationPackageImpl.APPLICATION__HANDLERS;

	/**
	 * The feature id for the '<em><b>Binding Tables</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__BINDING_TABLES = ApplicationPackageImpl.APPLICATION__BINDING_TABLES;

	/**
	 * The feature id for the '<em><b>Root Context</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__ROOT_CONTEXT = ApplicationPackageImpl.APPLICATION__ROOT_CONTEXT;

	/**
	 * The feature id for the '<em><b>Descriptors</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__DESCRIPTORS = ApplicationPackageImpl.APPLICATION__DESCRIPTORS;

	/**
	 * The feature id for the '<em><b>Binding Contexts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__BINDING_CONTEXTS = ApplicationPackageImpl.APPLICATION__BINDING_CONTEXTS;

	/**
	 * The feature id for the '<em><b>Menu Contributions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__MENU_CONTRIBUTIONS = ApplicationPackageImpl.APPLICATION__MENU_CONTRIBUTIONS;

	/**
	 * The feature id for the '<em><b>Tool Bar Contributions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__TOOL_BAR_CONTRIBUTIONS = ApplicationPackageImpl.APPLICATION__TOOL_BAR_CONTRIBUTIONS;

	/**
	 * The feature id for the '<em><b>Trim Contributions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__TRIM_CONTRIBUTIONS = ApplicationPackageImpl.APPLICATION__TRIM_CONTRIBUTIONS;

	/**
	 * The feature id for the '<em><b>Commands</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__COMMANDS = ApplicationPackageImpl.APPLICATION__COMMANDS;

	/**
	 * The feature id for the '<em><b>Addons</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__ADDONS = ApplicationPackageImpl.APPLICATION__ADDONS;

	/**
	 * The feature id for the '<em><b>Categories</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__CATEGORIES = ApplicationPackageImpl.APPLICATION__CATEGORIES;

	/**
	 * The feature id for the '<em><b>Editor Part Descriptors</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION__EDITOR_PART_DESCRIPTORS = ApplicationPackageImpl.APPLICATION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Simple IDE Application</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SIMPLE_IDE_APPLICATION_FEATURE_COUNT = ApplicationPackageImpl.APPLICATION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.demo.simpleide.model.simpleide.impl.EditorPartDescriptorImpl <em>Editor Part Descriptor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.demo.simpleide.model.simpleide.impl.EditorPartDescriptorImpl
	 * @see org.eclipse.e4.demo.simpleide.model.simpleide.impl.SimpleidePackageImpl#getEditorPartDescriptor()
	 * @generated
	 */
	public static final int EDITOR_PART_DESCRIPTOR = 1;

	/**
	 * The feature id for the '<em><b>Element Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EDITOR_PART_DESCRIPTOR__ELEMENT_ID = ApplicationPackageImpl.APPLICATION_ELEMENT__ELEMENT_ID;

	/**
	 * The feature id for the '<em><b>Tags</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EDITOR_PART_DESCRIPTOR__TAGS = ApplicationPackageImpl.APPLICATION_ELEMENT__TAGS;

	/**
	 * The feature id for the '<em><b>Contributor URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EDITOR_PART_DESCRIPTOR__CONTRIBUTOR_URI = ApplicationPackageImpl.APPLICATION_ELEMENT__CONTRIBUTOR_URI;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EDITOR_PART_DESCRIPTOR__LABEL = ApplicationPackageImpl.APPLICATION_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Icon URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EDITOR_PART_DESCRIPTOR__ICON_URI = ApplicationPackageImpl.APPLICATION_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Tooltip</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EDITOR_PART_DESCRIPTOR__TOOLTIP = ApplicationPackageImpl.APPLICATION_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Handlers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EDITOR_PART_DESCRIPTOR__HANDLERS = ApplicationPackageImpl.APPLICATION_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Binding Contexts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EDITOR_PART_DESCRIPTOR__BINDING_CONTEXTS = ApplicationPackageImpl.APPLICATION_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Contribution URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EDITOR_PART_DESCRIPTOR__CONTRIBUTION_URI = ApplicationPackageImpl.APPLICATION_ELEMENT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Fileextensions</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EDITOR_PART_DESCRIPTOR__FILEEXTENSIONS = ApplicationPackageImpl.APPLICATION_ELEMENT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Contenttypes</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EDITOR_PART_DESCRIPTOR__CONTENTTYPES = ApplicationPackageImpl.APPLICATION_ELEMENT_FEATURE_COUNT + 7;

	/**
	 * The number of structural features of the '<em>Editor Part Descriptor</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int EDITOR_PART_DESCRIPTOR_FEATURE_COUNT = ApplicationPackageImpl.APPLICATION_ELEMENT_FEATURE_COUNT + 8;

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
	 * @see org.eclipse.e4.demo.simpleide.model.simpleide.impl.SimpleidePackageImpl#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private SimpleidePackageImpl() {
		super(eNS_URI, ((EFactory)MSimpleideFactory.INSTANCE));
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
	 * <p>This method is used to initialize {@link SimpleidePackageImpl#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static SimpleidePackageImpl init() {
		if (isInited) return (SimpleidePackageImpl)EPackage.Registry.INSTANCE.getEPackage(SimpleidePackageImpl.eNS_URI);

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
		EPackage.Registry.INSTANCE.put(SimpleidePackageImpl.eNS_URI, theSimpleidePackage);
		return theSimpleidePackage;
	}


	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.demo.simpleide.model.simpleide.MSimpleIDEApplication <em>Simple IDE Application</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Simple IDE Application</em>'.
	 * @see org.eclipse.e4.demo.simpleide.model.simpleide.MSimpleIDEApplication
	 * @generated
	 */
	public EClass getSimpleIDEApplication() {
		return simpleIDEApplicationEClass;
	}

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.e4.demo.simpleide.model.simpleide.MSimpleIDEApplication#getEditorPartDescriptors <em>Editor Part Descriptors</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Editor Part Descriptors</em>'.
	 * @see org.eclipse.e4.demo.simpleide.model.simpleide.MSimpleIDEApplication#getEditorPartDescriptors()
	 * @see #getSimpleIDEApplication()
	 * @generated
	 */
	public EReference getSimpleIDEApplication_EditorPartDescriptors() {
		return (EReference)simpleIDEApplicationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.demo.simpleide.model.simpleide.MEditorPartDescriptor <em>Editor Part Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Editor Part Descriptor</em>'.
	 * @see org.eclipse.e4.demo.simpleide.model.simpleide.MEditorPartDescriptor
	 * @generated
	 */
	public EClass getEditorPartDescriptor() {
		return editorPartDescriptorEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.demo.simpleide.model.simpleide.MEditorPartDescriptor#getContributionURI <em>Contribution URI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Contribution URI</em>'.
	 * @see org.eclipse.e4.demo.simpleide.model.simpleide.MEditorPartDescriptor#getContributionURI()
	 * @see #getEditorPartDescriptor()
	 * @generated
	 */
	public EAttribute getEditorPartDescriptor_ContributionURI() {
		return (EAttribute)editorPartDescriptorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.e4.demo.simpleide.model.simpleide.MEditorPartDescriptor#getFileextensions <em>Fileextensions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Fileextensions</em>'.
	 * @see org.eclipse.e4.demo.simpleide.model.simpleide.MEditorPartDescriptor#getFileextensions()
	 * @see #getEditorPartDescriptor()
	 * @generated
	 */
	public EAttribute getEditorPartDescriptor_Fileextensions() {
		return (EAttribute)editorPartDescriptorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.e4.demo.simpleide.model.simpleide.MEditorPartDescriptor#getContenttypes <em>Contenttypes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Contenttypes</em>'.
	 * @see org.eclipse.e4.demo.simpleide.model.simpleide.MEditorPartDescriptor#getContenttypes()
	 * @see #getEditorPartDescriptor()
	 * @generated
	 */
	public EAttribute getEditorPartDescriptor_Contenttypes() {
		return (EAttribute)editorPartDescriptorEClass.getEStructuralFeatures().get(2);
	}


	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	public MSimpleideFactory getSimpleideFactory() {
		return (MSimpleideFactory)getEFactoryInstance();
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
		createEAttribute(editorPartDescriptorEClass, EDITOR_PART_DESCRIPTOR__CONTENTTYPES);
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
		editorPartDescriptorEClass.getESuperTypes().add(theApplicationPackage.getApplicationElement());
		editorPartDescriptorEClass.getESuperTypes().add(theUiPackage.getUILabel());
		editorPartDescriptorEClass.getESuperTypes().add(theCommandsPackage.getHandlerContainer());
		editorPartDescriptorEClass.getESuperTypes().add(theCommandsPackage.getBindings());

		// Initialize classes and features; add operations and parameters
		initEClass(simpleIDEApplicationEClass, MSimpleIDEApplication.class, "SimpleIDEApplication", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSimpleIDEApplication_EditorPartDescriptors(), this.getEditorPartDescriptor(), null, "editorPartDescriptors", null, 0, -1, MSimpleIDEApplication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(editorPartDescriptorEClass, MEditorPartDescriptor.class, "EditorPartDescriptor", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEditorPartDescriptor_ContributionURI(), ecorePackage.getEString(), "contributionURI", null, 0, 1, MEditorPartDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEditorPartDescriptor_Fileextensions(), ecorePackage.getEString(), "fileextensions", null, 0, -1, MEditorPartDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEditorPartDescriptor_Contenttypes(), ecorePackage.getEString(), "contenttypes", null, 0, -1, MEditorPartDescriptor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.e4.demo.simpleide.model.simpleide.impl.SimpleIDEApplicationImpl <em>Simple IDE Application</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.demo.simpleide.model.simpleide.impl.SimpleIDEApplicationImpl
		 * @see org.eclipse.e4.demo.simpleide.model.simpleide.impl.SimpleidePackageImpl#getSimpleIDEApplication()
		 * @generated
		 */
		public static final EClass SIMPLE_IDE_APPLICATION = eINSTANCE.getSimpleIDEApplication();

		/**
		 * The meta object literal for the '<em><b>Editor Part Descriptors</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference SIMPLE_IDE_APPLICATION__EDITOR_PART_DESCRIPTORS = eINSTANCE.getSimpleIDEApplication_EditorPartDescriptors();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.demo.simpleide.model.simpleide.impl.EditorPartDescriptorImpl <em>Editor Part Descriptor</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.demo.simpleide.model.simpleide.impl.EditorPartDescriptorImpl
		 * @see org.eclipse.e4.demo.simpleide.model.simpleide.impl.SimpleidePackageImpl#getEditorPartDescriptor()
		 * @generated
		 */
		public static final EClass EDITOR_PART_DESCRIPTOR = eINSTANCE.getEditorPartDescriptor();

		/**
		 * The meta object literal for the '<em><b>Contribution URI</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute EDITOR_PART_DESCRIPTOR__CONTRIBUTION_URI = eINSTANCE.getEditorPartDescriptor_ContributionURI();

		/**
		 * The meta object literal for the '<em><b>Fileextensions</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute EDITOR_PART_DESCRIPTOR__FILEEXTENSIONS = eINSTANCE.getEditorPartDescriptor_Fileextensions();

		/**
		 * The meta object literal for the '<em><b>Contenttypes</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute EDITOR_PART_DESCRIPTOR__CONTENTTYPES = eINSTANCE.getEditorPartDescriptor_Contenttypes();

	}

} //SimpleidePackageImpl
