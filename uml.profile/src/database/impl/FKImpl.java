/**
 */
package database.impl;

import database.DatabasePackage;
import database.FK;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.uml2.uml.Property;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>FK</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link database.impl.FKImpl#getBase_Property <em>Base Property</em>}</li>
 *   <li>{@link database.impl.FKImpl#getOracleForeingKeyConstraintName <em>Oracle Foreing Key Constraint Name</em>}</li>
 *   <li>{@link database.impl.FKImpl#getMySqlForeignKeyConstraint <em>My Sql Foreign Key Constraint</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FKImpl extends MinimalEObjectImpl.Container implements FK {
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
	 * The default value of the '{@link #getOracleForeingKeyConstraintName() <em>Oracle Foreing Key Constraint Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOracleForeingKeyConstraintName()
	 * @generated
	 * @ordered
	 */
	protected static final String ORACLE_FOREING_KEY_CONSTRAINT_NAME_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getOracleForeingKeyConstraintName() <em>Oracle Foreing Key Constraint Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOracleForeingKeyConstraintName()
	 * @generated
	 * @ordered
	 */
	protected String oracleForeingKeyConstraintName = ORACLE_FOREING_KEY_CONSTRAINT_NAME_EDEFAULT;
	/**
	 * The default value of the '{@link #getMySqlForeignKeyConstraint() <em>My Sql Foreign Key Constraint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMySqlForeignKeyConstraint()
	 * @generated
	 * @ordered
	 */
	protected static final String MY_SQL_FOREIGN_KEY_CONSTRAINT_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getMySqlForeignKeyConstraint() <em>My Sql Foreign Key Constraint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMySqlForeignKeyConstraint()
	 * @generated
	 * @ordered
	 */
	protected String mySqlForeignKeyConstraint = MY_SQL_FOREIGN_KEY_CONSTRAINT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FKImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DatabasePackage.Literals.FK;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DatabasePackage.FK__BASE_PROPERTY, oldBase_Property, base_Property));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DatabasePackage.FK__BASE_PROPERTY, oldBase_Property, base_Property));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOracleForeingKeyConstraintName() {
		return oracleForeingKeyConstraintName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOracleForeingKeyConstraintName(String newOracleForeingKeyConstraintName) {
		String oldOracleForeingKeyConstraintName = oracleForeingKeyConstraintName;
		oracleForeingKeyConstraintName = newOracleForeingKeyConstraintName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabasePackage.FK__ORACLE_FOREING_KEY_CONSTRAINT_NAME, oldOracleForeingKeyConstraintName, oracleForeingKeyConstraintName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getMySqlForeignKeyConstraint() {
		return mySqlForeignKeyConstraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMySqlForeignKeyConstraint(String newMySqlForeignKeyConstraint) {
		String oldMySqlForeignKeyConstraint = mySqlForeignKeyConstraint;
		mySqlForeignKeyConstraint = newMySqlForeignKeyConstraint;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabasePackage.FK__MY_SQL_FOREIGN_KEY_CONSTRAINT, oldMySqlForeignKeyConstraint, mySqlForeignKeyConstraint));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DatabasePackage.FK__BASE_PROPERTY:
				if (resolve) return getBase_Property();
				return basicGetBase_Property();
			case DatabasePackage.FK__ORACLE_FOREING_KEY_CONSTRAINT_NAME:
				return getOracleForeingKeyConstraintName();
			case DatabasePackage.FK__MY_SQL_FOREIGN_KEY_CONSTRAINT:
				return getMySqlForeignKeyConstraint();
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
			case DatabasePackage.FK__BASE_PROPERTY:
				setBase_Property((Property)newValue);
				return;
			case DatabasePackage.FK__ORACLE_FOREING_KEY_CONSTRAINT_NAME:
				setOracleForeingKeyConstraintName((String)newValue);
				return;
			case DatabasePackage.FK__MY_SQL_FOREIGN_KEY_CONSTRAINT:
				setMySqlForeignKeyConstraint((String)newValue);
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
			case DatabasePackage.FK__BASE_PROPERTY:
				setBase_Property((Property)null);
				return;
			case DatabasePackage.FK__ORACLE_FOREING_KEY_CONSTRAINT_NAME:
				setOracleForeingKeyConstraintName(ORACLE_FOREING_KEY_CONSTRAINT_NAME_EDEFAULT);
				return;
			case DatabasePackage.FK__MY_SQL_FOREIGN_KEY_CONSTRAINT:
				setMySqlForeignKeyConstraint(MY_SQL_FOREIGN_KEY_CONSTRAINT_EDEFAULT);
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
			case DatabasePackage.FK__BASE_PROPERTY:
				return base_Property != null;
			case DatabasePackage.FK__ORACLE_FOREING_KEY_CONSTRAINT_NAME:
				return ORACLE_FOREING_KEY_CONSTRAINT_NAME_EDEFAULT == null ? oracleForeingKeyConstraintName != null : !ORACLE_FOREING_KEY_CONSTRAINT_NAME_EDEFAULT.equals(oracleForeingKeyConstraintName);
			case DatabasePackage.FK__MY_SQL_FOREIGN_KEY_CONSTRAINT:
				return MY_SQL_FOREIGN_KEY_CONSTRAINT_EDEFAULT == null ? mySqlForeignKeyConstraint != null : !MY_SQL_FOREIGN_KEY_CONSTRAINT_EDEFAULT.equals(mySqlForeignKeyConstraint);
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
		result.append(" (oracleForeingKeyConstraintName: ");
		result.append(oracleForeingKeyConstraintName);
		result.append(", mySqlForeignKeyConstraint: ");
		result.append(mySqlForeignKeyConstraint);
		result.append(')');
		return result.toString();
	}

} //FKImpl
