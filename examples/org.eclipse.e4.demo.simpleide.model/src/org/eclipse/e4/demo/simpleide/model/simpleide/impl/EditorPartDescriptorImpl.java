/**
 * <copyright>
 * </copyright>
 *
 * $Id: EditorPartDescriptorImpl.java,v 1.1 2010/05/15 12:59:52 tschindl Exp $
 */
package org.eclipse.e4.demo.simpleide.model.simpleide.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.e4.demo.simpleide.model.simpleide.MEditorPartDescriptor;

import org.eclipse.e4.ui.model.application.MApplicationElement;

import org.eclipse.e4.ui.model.application.commands.MBindings;
import org.eclipse.e4.ui.model.application.commands.MHandler;
import org.eclipse.e4.ui.model.application.commands.MHandlerContainer;

import org.eclipse.e4.ui.model.application.commands.impl.CommandsPackageImpl;

import org.eclipse.e4.ui.model.application.impl.ApplicationPackageImpl;

import org.eclipse.e4.ui.model.application.ui.impl.UILabelImpl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Editor Part Descriptor</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.e4.demo.simpleide.model.simpleide.impl.EditorPartDescriptorImpl#getHandlers <em>Handlers</em>}</li>
 *   <li>{@link org.eclipse.e4.demo.simpleide.model.simpleide.impl.EditorPartDescriptorImpl#getBindingContexts <em>Binding Contexts</em>}</li>
 *   <li>{@link org.eclipse.e4.demo.simpleide.model.simpleide.impl.EditorPartDescriptorImpl#getElementId <em>Element Id</em>}</li>
 *   <li>{@link org.eclipse.e4.demo.simpleide.model.simpleide.impl.EditorPartDescriptorImpl#getTags <em>Tags</em>}</li>
 *   <li>{@link org.eclipse.e4.demo.simpleide.model.simpleide.impl.EditorPartDescriptorImpl#getContributionURI <em>Contribution URI</em>}</li>
 *   <li>{@link org.eclipse.e4.demo.simpleide.model.simpleide.impl.EditorPartDescriptorImpl#getFileextensions <em>Fileextensions</em>}</li>
 *   <li>{@link org.eclipse.e4.demo.simpleide.model.simpleide.impl.EditorPartDescriptorImpl#getContenttypes <em>Contenttypes</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EditorPartDescriptorImpl extends UILabelImpl implements MEditorPartDescriptor {
	/**
	 * The cached value of the '{@link #getHandlers() <em>Handlers</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHandlers()
	 * @generated
	 * @ordered
	 */
	protected EList<MHandler> handlers;

	/**
	 * The cached value of the '{@link #getBindingContexts() <em>Binding Contexts</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBindingContexts()
	 * @generated
	 * @ordered
	 */
	protected EList<String> bindingContexts;

	/**
	 * The default value of the '{@link #getElementId() <em>Element Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElementId()
	 * @generated
	 * @ordered
	 */
	protected static final String ELEMENT_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getElementId() <em>Element Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElementId()
	 * @generated
	 * @ordered
	 */
	protected String elementId = ELEMENT_ID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getTags() <em>Tags</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTags()
	 * @generated
	 * @ordered
	 */
	protected EList<String> tags;

	/**
	 * The default value of the '{@link #getContributionURI() <em>Contribution URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContributionURI()
	 * @generated
	 * @ordered
	 */
	protected static final String CONTRIBUTION_URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getContributionURI() <em>Contribution URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContributionURI()
	 * @generated
	 * @ordered
	 */
	protected String contributionURI = CONTRIBUTION_URI_EDEFAULT;

	/**
	 * The cached value of the '{@link #getFileextensions() <em>Fileextensions</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFileextensions()
	 * @generated
	 * @ordered
	 */
	protected EList<String> fileextensions;

	/**
	 * The cached value of the '{@link #getContenttypes() <em>Contenttypes</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContenttypes()
	 * @generated
	 * @ordered
	 */
	protected EList<String> contenttypes;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EditorPartDescriptorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SimpleidePackageImpl.Literals.EDITOR_PART_DESCRIPTOR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<MHandler> getHandlers() {
		if (handlers == null) {
			handlers = new EObjectContainmentEList<MHandler>(MHandler.class, this, SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__HANDLERS);
		}
		return handlers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<String> getBindingContexts() {
		if (bindingContexts == null) {
			bindingContexts = new EDataTypeUniqueEList<String>(String.class, this, SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__BINDING_CONTEXTS);
		}
		return bindingContexts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getElementId() {
		return elementId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setElementId(String newElementId) {
		String oldElementId = elementId;
		elementId = newElementId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__ELEMENT_ID, oldElementId, elementId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<String> getTags() {
		if (tags == null) {
			tags = new EDataTypeUniqueEList<String>(String.class, this, SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__TAGS);
		}
		return tags;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getContributionURI() {
		return contributionURI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContributionURI(String newContributionURI) {
		String oldContributionURI = contributionURI;
		contributionURI = newContributionURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__CONTRIBUTION_URI, oldContributionURI, contributionURI));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<String> getFileextensions() {
		if (fileextensions == null) {
			fileextensions = new EDataTypeUniqueEList<String>(String.class, this, SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__FILEEXTENSIONS);
		}
		return fileextensions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<String> getContenttypes() {
		if (contenttypes == null) {
			contenttypes = new EDataTypeUniqueEList<String>(String.class, this, SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__CONTENTTYPES);
		}
		return contenttypes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__HANDLERS:
				return ((InternalEList<?>)getHandlers()).basicRemove(otherEnd, msgs);
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
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__HANDLERS:
				return getHandlers();
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__BINDING_CONTEXTS:
				return getBindingContexts();
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__ELEMENT_ID:
				return getElementId();
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__TAGS:
				return getTags();
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__CONTRIBUTION_URI:
				return getContributionURI();
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__FILEEXTENSIONS:
				return getFileextensions();
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__CONTENTTYPES:
				return getContenttypes();
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
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__HANDLERS:
				getHandlers().clear();
				getHandlers().addAll((Collection<? extends MHandler>)newValue);
				return;
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__BINDING_CONTEXTS:
				getBindingContexts().clear();
				getBindingContexts().addAll((Collection<? extends String>)newValue);
				return;
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__ELEMENT_ID:
				setElementId((String)newValue);
				return;
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__TAGS:
				getTags().clear();
				getTags().addAll((Collection<? extends String>)newValue);
				return;
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__CONTRIBUTION_URI:
				setContributionURI((String)newValue);
				return;
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__FILEEXTENSIONS:
				getFileextensions().clear();
				getFileextensions().addAll((Collection<? extends String>)newValue);
				return;
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__CONTENTTYPES:
				getContenttypes().clear();
				getContenttypes().addAll((Collection<? extends String>)newValue);
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
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__HANDLERS:
				getHandlers().clear();
				return;
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__BINDING_CONTEXTS:
				getBindingContexts().clear();
				return;
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__ELEMENT_ID:
				setElementId(ELEMENT_ID_EDEFAULT);
				return;
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__TAGS:
				getTags().clear();
				return;
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__CONTRIBUTION_URI:
				setContributionURI(CONTRIBUTION_URI_EDEFAULT);
				return;
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__FILEEXTENSIONS:
				getFileextensions().clear();
				return;
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__CONTENTTYPES:
				getContenttypes().clear();
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
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__HANDLERS:
				return handlers != null && !handlers.isEmpty();
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__BINDING_CONTEXTS:
				return bindingContexts != null && !bindingContexts.isEmpty();
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__ELEMENT_ID:
				return ELEMENT_ID_EDEFAULT == null ? elementId != null : !ELEMENT_ID_EDEFAULT.equals(elementId);
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__TAGS:
				return tags != null && !tags.isEmpty();
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__CONTRIBUTION_URI:
				return CONTRIBUTION_URI_EDEFAULT == null ? contributionURI != null : !CONTRIBUTION_URI_EDEFAULT.equals(contributionURI);
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__FILEEXTENSIONS:
				return fileextensions != null && !fileextensions.isEmpty();
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__CONTENTTYPES:
				return contenttypes != null && !contenttypes.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == MHandlerContainer.class) {
			switch (derivedFeatureID) {
				case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__HANDLERS: return CommandsPackageImpl.HANDLER_CONTAINER__HANDLERS;
				default: return -1;
			}
		}
		if (baseClass == MBindings.class) {
			switch (derivedFeatureID) {
				case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__BINDING_CONTEXTS: return CommandsPackageImpl.BINDINGS__BINDING_CONTEXTS;
				default: return -1;
			}
		}
		if (baseClass == MApplicationElement.class) {
			switch (derivedFeatureID) {
				case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__ELEMENT_ID: return ApplicationPackageImpl.APPLICATION_ELEMENT__ELEMENT_ID;
				case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__TAGS: return ApplicationPackageImpl.APPLICATION_ELEMENT__TAGS;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == MHandlerContainer.class) {
			switch (baseFeatureID) {
				case CommandsPackageImpl.HANDLER_CONTAINER__HANDLERS: return SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__HANDLERS;
				default: return -1;
			}
		}
		if (baseClass == MBindings.class) {
			switch (baseFeatureID) {
				case CommandsPackageImpl.BINDINGS__BINDING_CONTEXTS: return SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__BINDING_CONTEXTS;
				default: return -1;
			}
		}
		if (baseClass == MApplicationElement.class) {
			switch (baseFeatureID) {
				case ApplicationPackageImpl.APPLICATION_ELEMENT__ELEMENT_ID: return SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__ELEMENT_ID;
				case ApplicationPackageImpl.APPLICATION_ELEMENT__TAGS: return SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__TAGS;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (bindingContexts: ");
		result.append(bindingContexts);
		result.append(", elementId: ");
		result.append(elementId);
		result.append(", tags: ");
		result.append(tags);
		result.append(", contributionURI: ");
		result.append(contributionURI);
		result.append(", fileextensions: ");
		result.append(fileextensions);
		result.append(", contenttypes: ");
		result.append(contenttypes);
		result.append(')');
		return result.toString();
	}

} //EditorPartDescriptorImpl
