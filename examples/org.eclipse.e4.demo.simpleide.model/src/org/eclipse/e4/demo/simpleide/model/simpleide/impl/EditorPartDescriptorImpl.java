/**
 * <copyright>
 * </copyright>
 *
 * $Id: EditorPartDescriptorImpl.java,v 1.4 2010/12/17 20:40:50 tschindl Exp $
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
import org.eclipse.e4.ui.model.application.impl.ApplicationElementImpl;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.model.application.ui.impl.UiPackageImpl;
import org.eclipse.e4.ui.model.application.impl.ApplicationPackageImpl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
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
 *   <li>{@link org.eclipse.e4.demo.simpleide.model.simpleide.impl.EditorPartDescriptorImpl#getLabel <em>Label</em>}</li>
 *   <li>{@link org.eclipse.e4.demo.simpleide.model.simpleide.impl.EditorPartDescriptorImpl#getIconURI <em>Icon URI</em>}</li>
 *   <li>{@link org.eclipse.e4.demo.simpleide.model.simpleide.impl.EditorPartDescriptorImpl#getTooltip <em>Tooltip</em>}</li>
 *   <li>{@link org.eclipse.e4.demo.simpleide.model.simpleide.impl.EditorPartDescriptorImpl#getHandlers <em>Handlers</em>}</li>
 *   <li>{@link org.eclipse.e4.demo.simpleide.model.simpleide.impl.EditorPartDescriptorImpl#getBindingContexts <em>Binding Contexts</em>}</li>
 *   <li>{@link org.eclipse.e4.demo.simpleide.model.simpleide.impl.EditorPartDescriptorImpl#getContributionURI <em>Contribution URI</em>}</li>
 *   <li>{@link org.eclipse.e4.demo.simpleide.model.simpleide.impl.EditorPartDescriptorImpl#getFileextensions <em>Fileextensions</em>}</li>
 *   <li>{@link org.eclipse.e4.demo.simpleide.model.simpleide.impl.EditorPartDescriptorImpl#getContenttypes <em>Contenttypes</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EditorPartDescriptorImpl extends ApplicationElementImpl implements MEditorPartDescriptor {
	/**
	 * The default value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected static final String LABEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected String label = LABEL_EDEFAULT;

	/**
	 * The default value of the '{@link #getIconURI() <em>Icon URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIconURI()
	 * @generated
	 * @ordered
	 */
	protected static final String ICON_URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIconURI() <em>Icon URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIconURI()
	 * @generated
	 * @ordered
	 */
	protected String iconURI = ICON_URI_EDEFAULT;

	/**
	 * The default value of the '{@link #getTooltip() <em>Tooltip</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTooltip()
	 * @generated
	 * @ordered
	 */
	protected static final String TOOLTIP_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTooltip() <em>Tooltip</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTooltip()
	 * @generated
	 * @ordered
	 */
	protected String tooltip = TOOLTIP_EDEFAULT;

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
	public String getLabel() {
		return label;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLabel(String newLabel) {
		String oldLabel = label;
		label = newLabel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__LABEL, oldLabel, label));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getIconURI() {
		return iconURI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIconURI(String newIconURI) {
		String oldIconURI = iconURI;
		iconURI = newIconURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__ICON_URI, oldIconURI, iconURI));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTooltip(String newTooltip) {
		String oldTooltip = tooltip;
		tooltip = newTooltip;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__TOOLTIP, oldTooltip, tooltip));
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
	public String getLocalizedLabel() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLocalizedTooltip() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
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
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__LABEL:
				return getLabel();
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__ICON_URI:
				return getIconURI();
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__TOOLTIP:
				return getTooltip();
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__HANDLERS:
				return getHandlers();
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__BINDING_CONTEXTS:
				return getBindingContexts();
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
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__LABEL:
				setLabel((String)newValue);
				return;
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__ICON_URI:
				setIconURI((String)newValue);
				return;
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__TOOLTIP:
				setTooltip((String)newValue);
				return;
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__HANDLERS:
				getHandlers().clear();
				getHandlers().addAll((Collection<? extends MHandler>)newValue);
				return;
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__BINDING_CONTEXTS:
				getBindingContexts().clear();
				getBindingContexts().addAll((Collection<? extends String>)newValue);
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
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__LABEL:
				setLabel(LABEL_EDEFAULT);
				return;
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__ICON_URI:
				setIconURI(ICON_URI_EDEFAULT);
				return;
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__TOOLTIP:
				setTooltip(TOOLTIP_EDEFAULT);
				return;
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__HANDLERS:
				getHandlers().clear();
				return;
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__BINDING_CONTEXTS:
				getBindingContexts().clear();
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
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__LABEL:
				return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals(label);
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__ICON_URI:
				return ICON_URI_EDEFAULT == null ? iconURI != null : !ICON_URI_EDEFAULT.equals(iconURI);
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__TOOLTIP:
				return TOOLTIP_EDEFAULT == null ? tooltip != null : !TOOLTIP_EDEFAULT.equals(tooltip);
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__HANDLERS:
				return handlers != null && !handlers.isEmpty();
			case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__BINDING_CONTEXTS:
				return bindingContexts != null && !bindingContexts.isEmpty();
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
		if (baseClass == MUILabel.class) {
			switch (derivedFeatureID) {
				case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__LABEL: return UiPackageImpl.UI_LABEL__LABEL;
				case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__ICON_URI: return UiPackageImpl.UI_LABEL__ICON_URI;
				case SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__TOOLTIP: return UiPackageImpl.UI_LABEL__TOOLTIP;
				default: return -1;
			}
		}
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
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == MUILabel.class) {
			switch (baseFeatureID) {
				case UiPackageImpl.UI_LABEL__LABEL: return SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__LABEL;
				case UiPackageImpl.UI_LABEL__ICON_URI: return SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__ICON_URI;
				case UiPackageImpl.UI_LABEL__TOOLTIP: return SimpleidePackageImpl.EDITOR_PART_DESCRIPTOR__TOOLTIP;
				default: return -1;
			}
		}
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
		result.append(" (label: ");
		result.append(label);
		result.append(", iconURI: ");
		result.append(iconURI);
		result.append(", tooltip: ");
		result.append(tooltip);
		result.append(", bindingContexts: ");
		result.append(bindingContexts);
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
