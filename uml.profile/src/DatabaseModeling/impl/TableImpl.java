/**
 */
package DatabaseModeling.impl;

import DatabaseModeling.DatabaseModelingPackage;
import DatabaseModeling.Table;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.uml2.uml.Interface;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Table</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link DatabaseModeling.impl.TableImpl#getBase_Interface <em>Base Interface</em>}</li>
 *   <li>{@link DatabaseModeling.impl.TableImpl#getBase_Class <em>Base Class</em>}</li>
 *   <li>{@link DatabaseModeling.impl.TableImpl#getPrimaryKeyConstraintName <em>Primary Key Constraint Name</em>}</li>
 *   <li>{@link DatabaseModeling.impl.TableImpl#getDataDefinitionLanguageName <em>Data Definition Language Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TableImpl extends MinimalEObjectImpl.Container implements Table {
	/**
	 * The cached value of the '{@link #getBase_Interface() <em>Base Interface</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBase_Interface()
	 * @generated
	 * @ordered
	 */
	protected Interface base_Interface;

	/**
	 * The cached value of the '{@link #getBase_Class() <em>Base Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBase_Class()
	 * @generated
	 * @ordered
	 */
	protected org.eclipse.uml2.uml.Class base_Class;

	/**
	 * The default value of the '{@link #getPrimaryKeyConstraintName() <em>Primary Key Constraint Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrimaryKeyConstraintName()
	 * @generated
	 * @ordered
	 */
	protected static final String PRIMARY_KEY_CONSTRAINT_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPrimaryKeyConstraintName() <em>Primary Key Constraint Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrimaryKeyConstraintName()
	 * @generated
	 * @ordered
	 */
	protected String primaryKeyConstraintName = PRIMARY_KEY_CONSTRAINT_NAME_EDEFAULT;

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
	protected TableImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DatabaseModelingPackage.Literals.TABLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Interface getBase_Interface() {
		if (base_Interface != null && base_Interface.eIsProxy()) {
			InternalEObject oldBase_Interface = (InternalEObject)base_Interface;
			base_Interface = (Interface)eResolveProxy(oldBase_Interface);
			if (base_Interface != oldBase_Interface) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DatabaseModelingPackage.TABLE__BASE_INTERFACE, oldBase_Interface, base_Interface));
			}
		}
		return base_Interface;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Interface basicGetBase_Interface() {
		return base_Interface;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBase_Interface(Interface newBase_Interface) {
		Interface oldBase_Interface = base_Interface;
		base_Interface = newBase_Interface;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabaseModelingPackage.TABLE__BASE_INTERFACE, oldBase_Interface, base_Interface));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public org.eclipse.uml2.uml.Class getBase_Class() {
		if (base_Class != null && base_Class.eIsProxy()) {
			InternalEObject oldBase_Class = (InternalEObject)base_Class;
			base_Class = (org.eclipse.uml2.uml.Class)eResolveProxy(oldBase_Class);
			if (base_Class != oldBase_Class) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DatabaseModelingPackage.TABLE__BASE_CLASS, oldBase_Class, base_Class));
			}
		}
		return base_Class;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public org.eclipse.uml2.uml.Class basicGetBase_Class() {
		return base_Class;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBase_Class(org.eclipse.uml2.uml.Class newBase_Class) {
		org.eclipse.uml2.uml.Class oldBase_Class = base_Class;
		base_Class = newBase_Class;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabaseModelingPackage.TABLE__BASE_CLASS, oldBase_Class, base_Class));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPrimaryKeyConstraintName() {
		return primaryKeyConstraintName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPrimaryKeyConstraintName(String newPrimaryKeyConstraintName) {
		String oldPrimaryKeyConstraintName = primaryKeyConstraintName;
		primaryKeyConstraintName = newPrimaryKeyConstraintName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabaseModelingPackage.TABLE__PRIMARY_KEY_CONSTRAINT_NAME, oldPrimaryKeyConstraintName, primaryKeyConstraintName));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DatabaseModelingPackage.TABLE__DATA_DEFINITION_LANGUAGE_NAME, oldDataDefinitionLanguageName, dataDefinitionLanguageName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DatabaseModelingPackage.TABLE__BASE_INTERFACE:
				if (resolve) return getBase_Interface();
				return basicGetBase_Interface();
			case DatabaseModelingPackage.TABLE__BASE_CLASS:
				if (resolve) return getBase_Class();
				return basicGetBase_Class();
			case DatabaseModelingPackage.TABLE__PRIMARY_KEY_CONSTRAINT_NAME:
				return getPrimaryKeyConstraintName();
			case DatabaseModelingPackage.TABLE__DATA_DEFINITION_LANGUAGE_NAME:
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
			case DatabaseModelingPackage.TABLE__BASE_INTERFACE:
				setBase_Interface((Interface)newValue);
				return;
			case DatabaseModelingPackage.TABLE__BASE_CLASS:
				setBase_Class((org.eclipse.uml2.uml.Class)newValue);
				return;
			case DatabaseModelingPackage.TABLE__PRIMARY_KEY_CONSTRAINT_NAME:
				setPrimaryKeyConstraintName((String)newValue);
				return;
			case DatabaseModelingPackage.TABLE__DATA_DEFINITION_LANGUAGE_NAME:
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
			case DatabaseModelingPackage.TABLE__BASE_INTERFACE:
				setBase_Interface((Interface)null);
				return;
			case DatabaseModelingPackage.TABLE__BASE_CLASS:
				setBase_Class((org.eclipse.uml2.uml.Class)null);
				return;
			case DatabaseModelingPackage.TABLE__PRIMARY_KEY_CONSTRAINT_NAME:
				setPrimaryKeyConstraintName(PRIMARY_KEY_CONSTRAINT_NAME_EDEFAULT);
				return;
			case DatabaseModelingPackage.TABLE__DATA_DEFINITION_LANGUAGE_NAME:
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
			case DatabaseModelingPackage.TABLE__BASE_INTERFACE:
				return base_Interface != null;
			case DatabaseModelingPackage.TABLE__BASE_CLASS:
				return base_Class != null;
			case DatabaseModelingPackage.TABLE__PRIMARY_KEY_CONSTRAINT_NAME:
				return PRIMARY_KEY_CONSTRAINT_NAME_EDEFAULT == null ? primaryKeyConstraintName != null : !PRIMARY_KEY_CONSTRAINT_NAME_EDEFAULT.equals(primaryKeyConstraintName);
			case DatabaseModelingPackage.TABLE__DATA_DEFINITION_LANGUAGE_NAME:
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
		result.append(" (primaryKeyConstraintName: ");
		result.append(primaryKeyConstraintName);
		result.append(", dataDefinitionLanguageName: ");
		result.append(dataDefinitionLanguageName);
		result.append(')');
		return result.toString();
	}

} //TableImpl
