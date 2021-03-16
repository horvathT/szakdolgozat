/**
 */
package database.impl;

import database.Column;
import database.DatabasePackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.uml2.uml.Property;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Column</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link database.impl.ColumnImpl#getBase_Property <em>Base Property</em>}</li>
 *   <li>{@link database.impl.ColumnImpl#getDataDefinitionLanguageName <em>Data Definition Language Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ColumnImpl extends MinimalEObjectImpl.Container implements Column {
	/**
	 * The cached value of the '{@link #getBase_Property() <em>Base Property</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBase_Property()
	 * @generated
	 * @ordered
	 */
	protected Property base_Property;

	/**
	 * The default value of the '{@link #getDataDefinitionLanguageName() <em>Data Definition Language Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataDefinitionLanguageName()
	 * @generated
	 * @ordered
	 */
	protected static final String DATA_DEFINITION_LANGUAGE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDataDefinitionLanguageName() <em>Data Definition Language Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataDefinitionLanguageName()
	 * @generated
	 * @ordered
	 */
	protected String dataDefinitionLanguageName = DATA_DEFINITION_LANGUAGE_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ColumnImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DatabasePackage.Literals.COLUMN;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Property getBase_Property() {
		if (base_Property != null && base_Property.eIsProxy()) {
			InternalEObject oldBase_Property = (InternalEObject)base_Property;
			base_Property = (Property)eResolveProxy(oldBase_Property);
			if (base_Property != oldBase_Property) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DatabasePackage.COLUMN__BASE_PROPERTY, oldBase_Property, base_Property));
			}
		}
		return base_Property;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Property basicGetBase_Property() {
		return base_Property;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBase_Property(Property newBase_Property) {
		Property oldBase_Property = base_Property;
		base_Property = newBase_Property;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabasePackage.COLUMN__BASE_PROPERTY, oldBase_Property, base_Property));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDataDefinitionLanguageName() {
		return dataDefinitionLanguageName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDataDefinitionLanguageName(String newDataDefinitionLanguageName) {
		String oldDataDefinitionLanguageName = dataDefinitionLanguageName;
		dataDefinitionLanguageName = newDataDefinitionLanguageName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabasePackage.COLUMN__DATA_DEFINITION_LANGUAGE_NAME, oldDataDefinitionLanguageName, dataDefinitionLanguageName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DatabasePackage.COLUMN__BASE_PROPERTY:
				if (resolve) return getBase_Property();
				return basicGetBase_Property();
			case DatabasePackage.COLUMN__DATA_DEFINITION_LANGUAGE_NAME:
				return getDataDefinitionLanguageName();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DatabasePackage.COLUMN__BASE_PROPERTY:
				setBase_Property((Property)newValue);
				return;
			case DatabasePackage.COLUMN__DATA_DEFINITION_LANGUAGE_NAME:
				setDataDefinitionLanguageName((String)newValue);
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
			case DatabasePackage.COLUMN__BASE_PROPERTY:
				setBase_Property((Property)null);
				return;
			case DatabasePackage.COLUMN__DATA_DEFINITION_LANGUAGE_NAME:
				setDataDefinitionLanguageName(DATA_DEFINITION_LANGUAGE_NAME_EDEFAULT);
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
			case DatabasePackage.COLUMN__BASE_PROPERTY:
				return base_Property != null;
			case DatabasePackage.COLUMN__DATA_DEFINITION_LANGUAGE_NAME:
				return DATA_DEFINITION_LANGUAGE_NAME_EDEFAULT == null ? dataDefinitionLanguageName != null : !DATA_DEFINITION_LANGUAGE_NAME_EDEFAULT.equals(dataDefinitionLanguageName);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (dataDefinitionLanguageName: ");
		result.append(dataDefinitionLanguageName);
		result.append(')');
		return result.toString();
	}

} //ColumnImpl
