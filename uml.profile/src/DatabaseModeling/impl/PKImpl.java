/**
 */
package DatabaseModeling.impl;

import DatabaseModeling.DatabaseModelingPackage;
import DatabaseModeling.PK;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.uml2.uml.Property;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>PK</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link DatabaseModeling.impl.PKImpl#getBase_Property <em>Base Property</em>}</li>
 *   <li>{@link DatabaseModeling.impl.PKImpl#getOraclePrimaryKeyConstraint <em>Oracle Primary Key Constraint</em>}</li>
 *   <li>{@link DatabaseModeling.impl.PKImpl#getMySqlPrimaryKeyConstraint <em>My Sql Primary Key Constraint</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PKImpl extends MinimalEObjectImpl.Container implements PK {
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
	 * The default value of the '{@link #getOraclePrimaryKeyConstraint() <em>Oracle Primary Key Constraint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOraclePrimaryKeyConstraint()
	 * @generated
	 * @ordered
	 */
	protected static final String ORACLE_PRIMARY_KEY_CONSTRAINT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOraclePrimaryKeyConstraint() <em>Oracle Primary Key Constraint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOraclePrimaryKeyConstraint()
	 * @generated
	 * @ordered
	 */
	protected String oraclePrimaryKeyConstraint = ORACLE_PRIMARY_KEY_CONSTRAINT_EDEFAULT;

	/**
	 * The default value of the '{@link #getMySqlPrimaryKeyConstraint() <em>My Sql Primary Key Constraint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMySqlPrimaryKeyConstraint()
	 * @generated
	 * @ordered
	 */
	protected static final String MY_SQL_PRIMARY_KEY_CONSTRAINT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMySqlPrimaryKeyConstraint() <em>My Sql Primary Key Constraint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMySqlPrimaryKeyConstraint()
	 * @generated
	 * @ordered
	 */
	protected String mySqlPrimaryKeyConstraint = MY_SQL_PRIMARY_KEY_CONSTRAINT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PKImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DatabaseModelingPackage.Literals.PK;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DatabaseModelingPackage.PK__BASE_PROPERTY, oldBase_Property, base_Property));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DatabaseModelingPackage.PK__BASE_PROPERTY, oldBase_Property, base_Property));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOraclePrimaryKeyConstraint() {
		return oraclePrimaryKeyConstraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOraclePrimaryKeyConstraint(String newOraclePrimaryKeyConstraint) {
		String oldOraclePrimaryKeyConstraint = oraclePrimaryKeyConstraint;
		oraclePrimaryKeyConstraint = newOraclePrimaryKeyConstraint;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabaseModelingPackage.PK__ORACLE_PRIMARY_KEY_CONSTRAINT, oldOraclePrimaryKeyConstraint, oraclePrimaryKeyConstraint));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getMySqlPrimaryKeyConstraint() {
		return mySqlPrimaryKeyConstraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMySqlPrimaryKeyConstraint(String newMySqlPrimaryKeyConstraint) {
		String oldMySqlPrimaryKeyConstraint = mySqlPrimaryKeyConstraint;
		mySqlPrimaryKeyConstraint = newMySqlPrimaryKeyConstraint;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabaseModelingPackage.PK__MY_SQL_PRIMARY_KEY_CONSTRAINT, oldMySqlPrimaryKeyConstraint, mySqlPrimaryKeyConstraint));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DatabaseModelingPackage.PK__BASE_PROPERTY:
				if (resolve) return getBase_Property();
				return basicGetBase_Property();
			case DatabaseModelingPackage.PK__ORACLE_PRIMARY_KEY_CONSTRAINT:
				return getOraclePrimaryKeyConstraint();
			case DatabaseModelingPackage.PK__MY_SQL_PRIMARY_KEY_CONSTRAINT:
				return getMySqlPrimaryKeyConstraint();
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
			case DatabaseModelingPackage.PK__BASE_PROPERTY:
				setBase_Property((Property)newValue);
				return;
			case DatabaseModelingPackage.PK__ORACLE_PRIMARY_KEY_CONSTRAINT:
				setOraclePrimaryKeyConstraint((String)newValue);
				return;
			case DatabaseModelingPackage.PK__MY_SQL_PRIMARY_KEY_CONSTRAINT:
				setMySqlPrimaryKeyConstraint((String)newValue);
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
			case DatabaseModelingPackage.PK__BASE_PROPERTY:
				setBase_Property((Property)null);
				return;
			case DatabaseModelingPackage.PK__ORACLE_PRIMARY_KEY_CONSTRAINT:
				setOraclePrimaryKeyConstraint(ORACLE_PRIMARY_KEY_CONSTRAINT_EDEFAULT);
				return;
			case DatabaseModelingPackage.PK__MY_SQL_PRIMARY_KEY_CONSTRAINT:
				setMySqlPrimaryKeyConstraint(MY_SQL_PRIMARY_KEY_CONSTRAINT_EDEFAULT);
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
			case DatabaseModelingPackage.PK__BASE_PROPERTY:
				return base_Property != null;
			case DatabaseModelingPackage.PK__ORACLE_PRIMARY_KEY_CONSTRAINT:
				return ORACLE_PRIMARY_KEY_CONSTRAINT_EDEFAULT == null ? oraclePrimaryKeyConstraint != null : !ORACLE_PRIMARY_KEY_CONSTRAINT_EDEFAULT.equals(oraclePrimaryKeyConstraint);
			case DatabaseModelingPackage.PK__MY_SQL_PRIMARY_KEY_CONSTRAINT:
				return MY_SQL_PRIMARY_KEY_CONSTRAINT_EDEFAULT == null ? mySqlPrimaryKeyConstraint != null : !MY_SQL_PRIMARY_KEY_CONSTRAINT_EDEFAULT.equals(mySqlPrimaryKeyConstraint);
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
		result.append(" (oraclePrimaryKeyConstraint: ");
		result.append(oraclePrimaryKeyConstraint);
		result.append(", mySqlPrimaryKeyConstraint: ");
		result.append(mySqlPrimaryKeyConstraint);
		result.append(')');
		return result.toString();
	}

} //PKImpl
