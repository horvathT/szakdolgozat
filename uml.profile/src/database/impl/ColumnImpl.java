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
 *   <li>{@link database.impl.ColumnImpl#getOracleDataType <em>Oracle Data Type</em>}</li>
 *   <li>{@link database.impl.ColumnImpl#getOracleDefaultValue <em>Oracle Default Value</em>}</li>
 *   <li>{@link database.impl.ColumnImpl#getOracleLength <em>Oracle Length</em>}</li>
 *   <li>{@link database.impl.ColumnImpl#getOraclePrecision <em>Oracle Precision</em>}</li>
 *   <li>{@link database.impl.ColumnImpl#getOracleScale <em>Oracle Scale</em>}</li>
 *   <li>{@link database.impl.ColumnImpl#getMySqlDataType <em>My Sql Data Type</em>}</li>
 *   <li>{@link database.impl.ColumnImpl#getMySqlDefaultValue <em>My Sql Default Value</em>}</li>
 *   <li>{@link database.impl.ColumnImpl#getMySqlLength <em>My Sql Length</em>}</li>
 *   <li>{@link database.impl.ColumnImpl#getMySqlPrecision <em>My Sql Precision</em>}</li>
 *   <li>{@link database.impl.ColumnImpl#getMySqlScale <em>My Sql Scale</em>}</li>
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
	 * The default value of the '{@link #getOracleDataType() <em>Oracle Data Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOracleDataType()
	 * @generated
	 * @ordered
	 */
	protected static final String ORACLE_DATA_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOracleDataType() <em>Oracle Data Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOracleDataType()
	 * @generated
	 * @ordered
	 */
	protected String oracleDataType = ORACLE_DATA_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getOracleDefaultValue() <em>Oracle Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOracleDefaultValue()
	 * @generated
	 * @ordered
	 */
	protected static final String ORACLE_DEFAULT_VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOracleDefaultValue() <em>Oracle Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOracleDefaultValue()
	 * @generated
	 * @ordered
	 */
	protected String oracleDefaultValue = ORACLE_DEFAULT_VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getOracleLength() <em>Oracle Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOracleLength()
	 * @generated
	 * @ordered
	 */
	protected static final int ORACLE_LENGTH_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getOracleLength() <em>Oracle Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOracleLength()
	 * @generated
	 * @ordered
	 */
	protected int oracleLength = ORACLE_LENGTH_EDEFAULT;

	/**
	 * The default value of the '{@link #getOraclePrecision() <em>Oracle Precision</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOraclePrecision()
	 * @generated
	 * @ordered
	 */
	protected static final int ORACLE_PRECISION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getOraclePrecision() <em>Oracle Precision</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOraclePrecision()
	 * @generated
	 * @ordered
	 */
	protected int oraclePrecision = ORACLE_PRECISION_EDEFAULT;

	/**
	 * The default value of the '{@link #getOracleScale() <em>Oracle Scale</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOracleScale()
	 * @generated
	 * @ordered
	 */
	protected static final int ORACLE_SCALE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getOracleScale() <em>Oracle Scale</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOracleScale()
	 * @generated
	 * @ordered
	 */
	protected int oracleScale = ORACLE_SCALE_EDEFAULT;

	/**
	 * The default value of the '{@link #getMySqlDataType() <em>My Sql Data Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMySqlDataType()
	 * @generated
	 * @ordered
	 */
	protected static final String MY_SQL_DATA_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMySqlDataType() <em>My Sql Data Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMySqlDataType()
	 * @generated
	 * @ordered
	 */
	protected String mySqlDataType = MY_SQL_DATA_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getMySqlDefaultValue() <em>My Sql Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMySqlDefaultValue()
	 * @generated
	 * @ordered
	 */
	protected static final String MY_SQL_DEFAULT_VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMySqlDefaultValue() <em>My Sql Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMySqlDefaultValue()
	 * @generated
	 * @ordered
	 */
	protected String mySqlDefaultValue = MY_SQL_DEFAULT_VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getMySqlLength() <em>My Sql Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMySqlLength()
	 * @generated
	 * @ordered
	 */
	protected static final int MY_SQL_LENGTH_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMySqlLength() <em>My Sql Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMySqlLength()
	 * @generated
	 * @ordered
	 */
	protected int mySqlLength = MY_SQL_LENGTH_EDEFAULT;

	/**
	 * The default value of the '{@link #getMySqlPrecision() <em>My Sql Precision</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMySqlPrecision()
	 * @generated
	 * @ordered
	 */
	protected static final int MY_SQL_PRECISION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMySqlPrecision() <em>My Sql Precision</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMySqlPrecision()
	 * @generated
	 * @ordered
	 */
	protected int mySqlPrecision = MY_SQL_PRECISION_EDEFAULT;

	/**
	 * The default value of the '{@link #getMySqlScale() <em>My Sql Scale</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMySqlScale()
	 * @generated
	 * @ordered
	 */
	protected static final int MY_SQL_SCALE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMySqlScale() <em>My Sql Scale</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMySqlScale()
	 * @generated
	 * @ordered
	 */
	protected int mySqlScale = MY_SQL_SCALE_EDEFAULT;

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
	public String getOracleDataType() {
		return oracleDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOracleDataType(String newOracleDataType) {
		String oldOracleDataType = oracleDataType;
		oracleDataType = newOracleDataType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabasePackage.COLUMN__ORACLE_DATA_TYPE, oldOracleDataType, oracleDataType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOracleDefaultValue() {
		return oracleDefaultValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOracleDefaultValue(String newOracleDefaultValue) {
		String oldOracleDefaultValue = oracleDefaultValue;
		oracleDefaultValue = newOracleDefaultValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabasePackage.COLUMN__ORACLE_DEFAULT_VALUE, oldOracleDefaultValue, oracleDefaultValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getOracleLength() {
		return oracleLength;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOracleLength(int newOracleLength) {
		int oldOracleLength = oracleLength;
		oracleLength = newOracleLength;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabasePackage.COLUMN__ORACLE_LENGTH, oldOracleLength, oracleLength));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getOraclePrecision() {
		return oraclePrecision;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOraclePrecision(int newOraclePrecision) {
		int oldOraclePrecision = oraclePrecision;
		oraclePrecision = newOraclePrecision;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabasePackage.COLUMN__ORACLE_PRECISION, oldOraclePrecision, oraclePrecision));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getOracleScale() {
		return oracleScale;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOracleScale(int newOracleScale) {
		int oldOracleScale = oracleScale;
		oracleScale = newOracleScale;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabasePackage.COLUMN__ORACLE_SCALE, oldOracleScale, oracleScale));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getMySqlDataType() {
		return mySqlDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMySqlDataType(String newMySqlDataType) {
		String oldMySqlDataType = mySqlDataType;
		mySqlDataType = newMySqlDataType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabasePackage.COLUMN__MY_SQL_DATA_TYPE, oldMySqlDataType, mySqlDataType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getMySqlDefaultValue() {
		return mySqlDefaultValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMySqlDefaultValue(String newMySqlDefaultValue) {
		String oldMySqlDefaultValue = mySqlDefaultValue;
		mySqlDefaultValue = newMySqlDefaultValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabasePackage.COLUMN__MY_SQL_DEFAULT_VALUE, oldMySqlDefaultValue, mySqlDefaultValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMySqlLength() {
		return mySqlLength;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMySqlLength(int newMySqlLength) {
		int oldMySqlLength = mySqlLength;
		mySqlLength = newMySqlLength;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabasePackage.COLUMN__MY_SQL_LENGTH, oldMySqlLength, mySqlLength));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMySqlPrecision() {
		return mySqlPrecision;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMySqlPrecision(int newMySqlPrecision) {
		int oldMySqlPrecision = mySqlPrecision;
		mySqlPrecision = newMySqlPrecision;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabasePackage.COLUMN__MY_SQL_PRECISION, oldMySqlPrecision, mySqlPrecision));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMySqlScale() {
		return mySqlScale;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMySqlScale(int newMySqlScale) {
		int oldMySqlScale = mySqlScale;
		mySqlScale = newMySqlScale;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabasePackage.COLUMN__MY_SQL_SCALE, oldMySqlScale, mySqlScale));
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
			case DatabasePackage.COLUMN__ORACLE_DATA_TYPE:
				return getOracleDataType();
			case DatabasePackage.COLUMN__ORACLE_DEFAULT_VALUE:
				return getOracleDefaultValue();
			case DatabasePackage.COLUMN__ORACLE_LENGTH:
				return getOracleLength();
			case DatabasePackage.COLUMN__ORACLE_PRECISION:
				return getOraclePrecision();
			case DatabasePackage.COLUMN__ORACLE_SCALE:
				return getOracleScale();
			case DatabasePackage.COLUMN__MY_SQL_DATA_TYPE:
				return getMySqlDataType();
			case DatabasePackage.COLUMN__MY_SQL_DEFAULT_VALUE:
				return getMySqlDefaultValue();
			case DatabasePackage.COLUMN__MY_SQL_LENGTH:
				return getMySqlLength();
			case DatabasePackage.COLUMN__MY_SQL_PRECISION:
				return getMySqlPrecision();
			case DatabasePackage.COLUMN__MY_SQL_SCALE:
				return getMySqlScale();
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
			case DatabasePackage.COLUMN__ORACLE_DATA_TYPE:
				setOracleDataType((String)newValue);
				return;
			case DatabasePackage.COLUMN__ORACLE_DEFAULT_VALUE:
				setOracleDefaultValue((String)newValue);
				return;
			case DatabasePackage.COLUMN__ORACLE_LENGTH:
				setOracleLength((Integer)newValue);
				return;
			case DatabasePackage.COLUMN__ORACLE_PRECISION:
				setOraclePrecision((Integer)newValue);
				return;
			case DatabasePackage.COLUMN__ORACLE_SCALE:
				setOracleScale((Integer)newValue);
				return;
			case DatabasePackage.COLUMN__MY_SQL_DATA_TYPE:
				setMySqlDataType((String)newValue);
				return;
			case DatabasePackage.COLUMN__MY_SQL_DEFAULT_VALUE:
				setMySqlDefaultValue((String)newValue);
				return;
			case DatabasePackage.COLUMN__MY_SQL_LENGTH:
				setMySqlLength((Integer)newValue);
				return;
			case DatabasePackage.COLUMN__MY_SQL_PRECISION:
				setMySqlPrecision((Integer)newValue);
				return;
			case DatabasePackage.COLUMN__MY_SQL_SCALE:
				setMySqlScale((Integer)newValue);
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
			case DatabasePackage.COLUMN__ORACLE_DATA_TYPE:
				setOracleDataType(ORACLE_DATA_TYPE_EDEFAULT);
				return;
			case DatabasePackage.COLUMN__ORACLE_DEFAULT_VALUE:
				setOracleDefaultValue(ORACLE_DEFAULT_VALUE_EDEFAULT);
				return;
			case DatabasePackage.COLUMN__ORACLE_LENGTH:
				setOracleLength(ORACLE_LENGTH_EDEFAULT);
				return;
			case DatabasePackage.COLUMN__ORACLE_PRECISION:
				setOraclePrecision(ORACLE_PRECISION_EDEFAULT);
				return;
			case DatabasePackage.COLUMN__ORACLE_SCALE:
				setOracleScale(ORACLE_SCALE_EDEFAULT);
				return;
			case DatabasePackage.COLUMN__MY_SQL_DATA_TYPE:
				setMySqlDataType(MY_SQL_DATA_TYPE_EDEFAULT);
				return;
			case DatabasePackage.COLUMN__MY_SQL_DEFAULT_VALUE:
				setMySqlDefaultValue(MY_SQL_DEFAULT_VALUE_EDEFAULT);
				return;
			case DatabasePackage.COLUMN__MY_SQL_LENGTH:
				setMySqlLength(MY_SQL_LENGTH_EDEFAULT);
				return;
			case DatabasePackage.COLUMN__MY_SQL_PRECISION:
				setMySqlPrecision(MY_SQL_PRECISION_EDEFAULT);
				return;
			case DatabasePackage.COLUMN__MY_SQL_SCALE:
				setMySqlScale(MY_SQL_SCALE_EDEFAULT);
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
			case DatabasePackage.COLUMN__ORACLE_DATA_TYPE:
				return ORACLE_DATA_TYPE_EDEFAULT == null ? oracleDataType != null : !ORACLE_DATA_TYPE_EDEFAULT.equals(oracleDataType);
			case DatabasePackage.COLUMN__ORACLE_DEFAULT_VALUE:
				return ORACLE_DEFAULT_VALUE_EDEFAULT == null ? oracleDefaultValue != null : !ORACLE_DEFAULT_VALUE_EDEFAULT.equals(oracleDefaultValue);
			case DatabasePackage.COLUMN__ORACLE_LENGTH:
				return oracleLength != ORACLE_LENGTH_EDEFAULT;
			case DatabasePackage.COLUMN__ORACLE_PRECISION:
				return oraclePrecision != ORACLE_PRECISION_EDEFAULT;
			case DatabasePackage.COLUMN__ORACLE_SCALE:
				return oracleScale != ORACLE_SCALE_EDEFAULT;
			case DatabasePackage.COLUMN__MY_SQL_DATA_TYPE:
				return MY_SQL_DATA_TYPE_EDEFAULT == null ? mySqlDataType != null : !MY_SQL_DATA_TYPE_EDEFAULT.equals(mySqlDataType);
			case DatabasePackage.COLUMN__MY_SQL_DEFAULT_VALUE:
				return MY_SQL_DEFAULT_VALUE_EDEFAULT == null ? mySqlDefaultValue != null : !MY_SQL_DEFAULT_VALUE_EDEFAULT.equals(mySqlDefaultValue);
			case DatabasePackage.COLUMN__MY_SQL_LENGTH:
				return mySqlLength != MY_SQL_LENGTH_EDEFAULT;
			case DatabasePackage.COLUMN__MY_SQL_PRECISION:
				return mySqlPrecision != MY_SQL_PRECISION_EDEFAULT;
			case DatabasePackage.COLUMN__MY_SQL_SCALE:
				return mySqlScale != MY_SQL_SCALE_EDEFAULT;
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
		result.append(" (oracleDataType: ");
		result.append(oracleDataType);
		result.append(", oracleDefaultValue: ");
		result.append(oracleDefaultValue);
		result.append(", oracleLength: ");
		result.append(oracleLength);
		result.append(", oraclePrecision: ");
		result.append(oraclePrecision);
		result.append(", oracleScale: ");
		result.append(oracleScale);
		result.append(", mySqlDataType: ");
		result.append(mySqlDataType);
		result.append(", mySqlDefaultValue: ");
		result.append(mySqlDefaultValue);
		result.append(", mySqlLength: ");
		result.append(mySqlLength);
		result.append(", mySqlPrecision: ");
		result.append(mySqlPrecision);
		result.append(", mySqlScale: ");
		result.append(mySqlScale);
		result.append(')');
		return result.toString();
	}

} //ColumnImpl
