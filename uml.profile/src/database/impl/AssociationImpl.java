/**
 */
package database.impl;

import database.Association;
import database.DatabasePackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Association</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link database.impl.AssociationImpl#getForeignConstraintName <em>Foreign Constraint Name</em>}</li>
 *   <li>{@link database.impl.AssociationImpl#getBase_Association <em>Base Association</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssociationImpl extends MinimalEObjectImpl.Container implements Association {
	/**
	 * The default value of the '{@link #getForeignConstraintName() <em>Foreign Constraint Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getForeignConstraintName()
	 * @generated
	 * @ordered
	 */
	protected static final String FOREIGN_CONSTRAINT_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getForeignConstraintName() <em>Foreign Constraint Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getForeignConstraintName()
	 * @generated
	 * @ordered
	 */
	protected String foreignConstraintName = FOREIGN_CONSTRAINT_NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getBase_Association() <em>Base Association</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBase_Association()
	 * @generated
	 * @ordered
	 */
	protected org.eclipse.uml2.uml.Association base_Association;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AssociationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DatabasePackage.Literals.ASSOCIATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getForeignConstraintName() {
		return foreignConstraintName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setForeignConstraintName(String newForeignConstraintName) {
		String oldForeignConstraintName = foreignConstraintName;
		foreignConstraintName = newForeignConstraintName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabasePackage.ASSOCIATION__FOREIGN_CONSTRAINT_NAME, oldForeignConstraintName, foreignConstraintName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public org.eclipse.uml2.uml.Association getBase_Association() {
		if (base_Association != null && base_Association.eIsProxy()) {
			InternalEObject oldBase_Association = (InternalEObject)base_Association;
			base_Association = (org.eclipse.uml2.uml.Association)eResolveProxy(oldBase_Association);
			if (base_Association != oldBase_Association) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DatabasePackage.ASSOCIATION__BASE_ASSOCIATION, oldBase_Association, base_Association));
			}
		}
		return base_Association;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public org.eclipse.uml2.uml.Association basicGetBase_Association() {
		return base_Association;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBase_Association(org.eclipse.uml2.uml.Association newBase_Association) {
		org.eclipse.uml2.uml.Association oldBase_Association = base_Association;
		base_Association = newBase_Association;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabasePackage.ASSOCIATION__BASE_ASSOCIATION, oldBase_Association, base_Association));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DatabasePackage.ASSOCIATION__FOREIGN_CONSTRAINT_NAME:
				return getForeignConstraintName();
			case DatabasePackage.ASSOCIATION__BASE_ASSOCIATION:
				if (resolve) return getBase_Association();
				return basicGetBase_Association();
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
			case DatabasePackage.ASSOCIATION__FOREIGN_CONSTRAINT_NAME:
				setForeignConstraintName((String)newValue);
				return;
			case DatabasePackage.ASSOCIATION__BASE_ASSOCIATION:
				setBase_Association((org.eclipse.uml2.uml.Association)newValue);
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
			case DatabasePackage.ASSOCIATION__FOREIGN_CONSTRAINT_NAME:
				setForeignConstraintName(FOREIGN_CONSTRAINT_NAME_EDEFAULT);
				return;
			case DatabasePackage.ASSOCIATION__BASE_ASSOCIATION:
				setBase_Association((org.eclipse.uml2.uml.Association)null);
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
			case DatabasePackage.ASSOCIATION__FOREIGN_CONSTRAINT_NAME:
				return FOREIGN_CONSTRAINT_NAME_EDEFAULT == null ? foreignConstraintName != null : !FOREIGN_CONSTRAINT_NAME_EDEFAULT.equals(foreignConstraintName);
			case DatabasePackage.ASSOCIATION__BASE_ASSOCIATION:
				return base_Association != null;
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
		result.append(" (foreignConstraintName: ");
		result.append(foreignConstraintName);
		result.append(')');
		return result.toString();
	}

} //AssociationImpl
