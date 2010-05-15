/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.e4.demo.simpleide.editor.model.simpleide.impl;

import java.util.Collection;

import org.eclipse.e4.demo.simpleide.editor.model.simpleide.EditorPartDescriptor;
import org.eclipse.e4.demo.simpleide.editor.model.simpleide.SimpleIDEApplication;
import org.eclipse.e4.demo.simpleide.editor.model.simpleide.SimpleidePackage;

import org.eclipse.e4.ui.model.application.impl.ApplicationImpl;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Simple IDE Application</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.e4.demo.simpleide.editor.model.simpleide.impl.SimpleIDEApplicationImpl#getEditorPartDescriptors <em>Editor Part Descriptors</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SimpleIDEApplicationImpl extends ApplicationImpl implements SimpleIDEApplication {
	/**
	 * The cached value of the '{@link #getEditorPartDescriptors() <em>Editor Part Descriptors</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEditorPartDescriptors()
	 * @generated
	 * @ordered
	 */
	protected EList<EditorPartDescriptor> editorPartDescriptors;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SimpleIDEApplicationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SimpleidePackage.Literals.SIMPLE_IDE_APPLICATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EditorPartDescriptor> getEditorPartDescriptors() {
		if (editorPartDescriptors == null) {
			editorPartDescriptors = new EObjectContainmentEList<EditorPartDescriptor>(EditorPartDescriptor.class, this, SimpleidePackage.SIMPLE_IDE_APPLICATION__EDITOR_PART_DESCRIPTORS);
		}
		return editorPartDescriptors;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SimpleidePackage.SIMPLE_IDE_APPLICATION__EDITOR_PART_DESCRIPTORS:
				return ((InternalEList<?>)getEditorPartDescriptors()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SimpleidePackage.SIMPLE_IDE_APPLICATION__EDITOR_PART_DESCRIPTORS:
				return getEditorPartDescriptors();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case SimpleidePackage.SIMPLE_IDE_APPLICATION__EDITOR_PART_DESCRIPTORS:
				getEditorPartDescriptors().clear();
				getEditorPartDescriptors().addAll((Collection<? extends EditorPartDescriptor>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case SimpleidePackage.SIMPLE_IDE_APPLICATION__EDITOR_PART_DESCRIPTORS:
				getEditorPartDescriptors().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case SimpleidePackage.SIMPLE_IDE_APPLICATION__EDITOR_PART_DESCRIPTORS:
				return editorPartDescriptors != null && !editorPartDescriptors.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //SimpleIDEApplicationImpl
