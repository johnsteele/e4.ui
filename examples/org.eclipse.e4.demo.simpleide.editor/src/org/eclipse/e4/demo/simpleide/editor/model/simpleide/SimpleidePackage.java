/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.e4.demo.simpleide.editor.model.simpleide;

import org.eclipse.e4.ui.model.application.impl.ApplicationPackageImpl;

import org.eclipse.e4.ui.model.application.ui.impl.UiPackageImpl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

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
 * @see org.eclipse.e4.demo.simpleide.editor.model.simpleide.SimpleideFactory
 * @model kind="package"
 * @generated
 */
public interface SimpleidePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "simpleide";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/e4/demo/simpleide";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "simpleide";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SimpleidePackage eINSTANCE = org.eclipse.e4.demo.simpleide.editor.model.simpleide.impl.SimpleidePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.e4.demo.simpleide.editor.model.simpleide.impl.SimpleIDEApplicationImpl <em>Simple IDE Application</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.demo.simpleide.editor.model.simpleide.impl.SimpleIDEApplicationImpl
	 * @see org.eclipse.e4.demo.simpleide.editor.model.simpleide.impl.SimpleidePackageImpl#getSimpleIDEApplication()
	 * @generated
	 */
	int SIMPLE_IDE_APPLICATION = 0;

	/**
	 * The feature id for the '<em><b>Element Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION__ELEMENT_ID = ApplicationPackageImpl.APPLICATION__ELEMENT_ID;

	/**
	 * The feature id for the '<em><b>Tags</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION__TAGS = ApplicationPackageImpl.APPLICATION__TAGS;

	/**
	 * The feature id for the '<em><b>Widget</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION__WIDGET = ApplicationPackageImpl.APPLICATION__WIDGET;

	/**
	 * The feature id for the '<em><b>Renderer</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION__RENDERER = ApplicationPackageImpl.APPLICATION__RENDERER;

	/**
	 * The feature id for the '<em><b>To Be Rendered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION__TO_BE_RENDERED = ApplicationPackageImpl.APPLICATION__TO_BE_RENDERED;

	/**
	 * The feature id for the '<em><b>On Top</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION__ON_TOP = ApplicationPackageImpl.APPLICATION__ON_TOP;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION__VISIBLE = ApplicationPackageImpl.APPLICATION__VISIBLE;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION__PARENT = ApplicationPackageImpl.APPLICATION__PARENT;

	/**
	 * The feature id for the '<em><b>Container Data</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION__CONTAINER_DATA = ApplicationPackageImpl.APPLICATION__CONTAINER_DATA;

	/**
	 * The feature id for the '<em><b>Cur Shared Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION__CUR_SHARED_REF = ApplicationPackageImpl.APPLICATION__CUR_SHARED_REF;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION__CHILDREN = ApplicationPackageImpl.APPLICATION__CHILDREN;

	/**
	 * The feature id for the '<em><b>Selected Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION__SELECTED_ELEMENT = ApplicationPackageImpl.APPLICATION__SELECTED_ELEMENT;

	/**
	 * The feature id for the '<em><b>Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION__CONTEXT = ApplicationPackageImpl.APPLICATION__CONTEXT;

	/**
	 * The feature id for the '<em><b>Variables</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION__VARIABLES = ApplicationPackageImpl.APPLICATION__VARIABLES;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION__PROPERTIES = ApplicationPackageImpl.APPLICATION__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Handlers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION__HANDLERS = ApplicationPackageImpl.APPLICATION__HANDLERS;

	/**
	 * The feature id for the '<em><b>Binding Tables</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION__BINDING_TABLES = ApplicationPackageImpl.APPLICATION__BINDING_TABLES;

	/**
	 * The feature id for the '<em><b>Root Context</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION__ROOT_CONTEXT = ApplicationPackageImpl.APPLICATION__ROOT_CONTEXT;

	/**
	 * The feature id for the '<em><b>Descriptors</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION__DESCRIPTORS = ApplicationPackageImpl.APPLICATION__DESCRIPTORS;

	/**
	 * The feature id for the '<em><b>Binding Contexts</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION__BINDING_CONTEXTS = ApplicationPackageImpl.APPLICATION__BINDING_CONTEXTS;

	/**
	 * The feature id for the '<em><b>Commands</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION__COMMANDS = ApplicationPackageImpl.APPLICATION__COMMANDS;

	/**
	 * The feature id for the '<em><b>Addons</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION__ADDONS = ApplicationPackageImpl.APPLICATION__ADDONS;

	/**
	 * The feature id for the '<em><b>Editor Part Descriptors</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION__EDITOR_PART_DESCRIPTORS = ApplicationPackageImpl.APPLICATION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Simple IDE Application</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_IDE_APPLICATION_FEATURE_COUNT = ApplicationPackageImpl.APPLICATION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.demo.simpleide.editor.model.simpleide.impl.EditorPartDescriptorImpl <em>Editor Part Descriptor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.demo.simpleide.editor.model.simpleide.impl.EditorPartDescriptorImpl
	 * @see org.eclipse.e4.demo.simpleide.editor.model.simpleide.impl.SimpleidePackageImpl#getEditorPartDescriptor()
	 * @generated
	 */
	int EDITOR_PART_DESCRIPTOR = 1;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDITOR_PART_DESCRIPTOR__LABEL = UiPackageImpl.UI_LABEL__LABEL;

	/**
	 * The feature id for the '<em><b>Icon URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDITOR_PART_DESCRIPTOR__ICON_URI = UiPackageImpl.UI_LABEL__ICON_URI;

	/**
	 * The feature id for the '<em><b>Tooltip</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDITOR_PART_DESCRIPTOR__TOOLTIP = UiPackageImpl.UI_LABEL__TOOLTIP;

	/**
	 * The feature id for the '<em><b>Handlers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDITOR_PART_DESCRIPTOR__HANDLERS = UiPackageImpl.UI_LABEL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Binding Contexts</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDITOR_PART_DESCRIPTOR__BINDING_CONTEXTS = UiPackageImpl.UI_LABEL_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Element Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDITOR_PART_DESCRIPTOR__ELEMENT_ID = UiPackageImpl.UI_LABEL_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Tags</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDITOR_PART_DESCRIPTOR__TAGS = UiPackageImpl.UI_LABEL_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Contribution URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDITOR_PART_DESCRIPTOR__CONTRIBUTION_URI = UiPackageImpl.UI_LABEL_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Fileextensions</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDITOR_PART_DESCRIPTOR__FILEEXTENSIONS = UiPackageImpl.UI_LABEL_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Editor Part Descriptor</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDITOR_PART_DESCRIPTOR_FEATURE_COUNT = UiPackageImpl.UI_LABEL_FEATURE_COUNT + 6;


	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.demo.simpleide.editor.model.simpleide.SimpleIDEApplication <em>Simple IDE Application</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Simple IDE Application</em>'.
	 * @see org.eclipse.e4.demo.simpleide.editor.model.simpleide.SimpleIDEApplication
	 * @generated
	 */
	EClass getSimpleIDEApplication();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.e4.demo.simpleide.editor.model.simpleide.SimpleIDEApplication#getEditorPartDescriptors <em>Editor Part Descriptors</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Editor Part Descriptors</em>'.
	 * @see org.eclipse.e4.demo.simpleide.editor.model.simpleide.SimpleIDEApplication#getEditorPartDescriptors()
	 * @see #getSimpleIDEApplication()
	 * @generated
	 */
	EReference getSimpleIDEApplication_EditorPartDescriptors();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.demo.simpleide.editor.model.simpleide.EditorPartDescriptor <em>Editor Part Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Editor Part Descriptor</em>'.
	 * @see org.eclipse.e4.demo.simpleide.editor.model.simpleide.EditorPartDescriptor
	 * @generated
	 */
	EClass getEditorPartDescriptor();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.demo.simpleide.editor.model.simpleide.EditorPartDescriptor#getContributionURI <em>Contribution URI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Contribution URI</em>'.
	 * @see org.eclipse.e4.demo.simpleide.editor.model.simpleide.EditorPartDescriptor#getContributionURI()
	 * @see #getEditorPartDescriptor()
	 * @generated
	 */
	EAttribute getEditorPartDescriptor_ContributionURI();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.e4.demo.simpleide.editor.model.simpleide.EditorPartDescriptor#getFileextensions <em>Fileextensions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Fileextensions</em>'.
	 * @see org.eclipse.e4.demo.simpleide.editor.model.simpleide.EditorPartDescriptor#getFileextensions()
	 * @see #getEditorPartDescriptor()
	 * @generated
	 */
	EAttribute getEditorPartDescriptor_Fileextensions();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	SimpleideFactory getSimpleideFactory();

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
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.e4.demo.simpleide.editor.model.simpleide.impl.SimpleIDEApplicationImpl <em>Simple IDE Application</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.demo.simpleide.editor.model.simpleide.impl.SimpleIDEApplicationImpl
		 * @see org.eclipse.e4.demo.simpleide.editor.model.simpleide.impl.SimpleidePackageImpl#getSimpleIDEApplication()
		 * @generated
		 */
		EClass SIMPLE_IDE_APPLICATION = eINSTANCE.getSimpleIDEApplication();

		/**
		 * The meta object literal for the '<em><b>Editor Part Descriptors</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SIMPLE_IDE_APPLICATION__EDITOR_PART_DESCRIPTORS = eINSTANCE.getSimpleIDEApplication_EditorPartDescriptors();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.demo.simpleide.editor.model.simpleide.impl.EditorPartDescriptorImpl <em>Editor Part Descriptor</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.demo.simpleide.editor.model.simpleide.impl.EditorPartDescriptorImpl
		 * @see org.eclipse.e4.demo.simpleide.editor.model.simpleide.impl.SimpleidePackageImpl#getEditorPartDescriptor()
		 * @generated
		 */
		EClass EDITOR_PART_DESCRIPTOR = eINSTANCE.getEditorPartDescriptor();

		/**
		 * The meta object literal for the '<em><b>Contribution URI</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EDITOR_PART_DESCRIPTOR__CONTRIBUTION_URI = eINSTANCE.getEditorPartDescriptor_ContributionURI();

		/**
		 * The meta object literal for the '<em><b>Fileextensions</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EDITOR_PART_DESCRIPTOR__FILEEXTENSIONS = eINSTANCE.getEditorPartDescriptor_Fileextensions();

	}

} //SimpleidePackage
