/**
 */
package DatabaseModeling.impl;

import DatabaseModeling.DatabaseModelingPackage;
import DatabaseModeling.FK;

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
 *   <li>{@link DatabaseModeling.impl.FKImpl#getBase_Property <em>Base Property</em>}</li>
 *   <li>{@link DatabaseModeling.impl.FKImpl#getForeignKeyConstraint <em>Foreign Key Constraint</em>}</li>
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
	 * The default value of the '{@link #getForeignKeyConstraint() <em>Foreign Key Constraint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getForeignKeyConstraint()
	 * @generated
	 * @ordered
	 */
	protected static final String FOREIGN_KEY_CONSTRAINT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getForeignKeyConstraint() <em>Foreign Key Constraint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getForeignKeyConstraint()
	 * @generated
	 * @ordered
	 */
	protected String foreignKeyConstraint = FOREIGN_KEY_CONSTRAINT_EDEFAULT;

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
		return DatabaseModelingPackage.Literals.FK;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DatabaseModelingPackage.FK__BASE_PROPERTY, oldBase_Property, base_Property));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DatabaseModelingPackage.FK__BASE_PROPERTY, oldBase_Property, base_Property));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getForeignKeyConstraint() {
		return foreignKeyConstraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setForeignKeyConstraint(String newForeignKeyConstraint) {
		String oldForeignKeyConstraint = foreignKeyConstraint;
		foreignKeyConstraint = newForeignKeyConstraint;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabaseModelingPackage.FK__FOREIGN_KEY_CONSTRAINT, oldForeignKeyConstraint, foreignKeyConstraint));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DatabaseModelingPackage.FK__BASE_PROPERTY:
				if (resolve) return getBase_Property();
				return basicGetBase_Property();
			case DatabaseModelingPackage.FK__FOREIGN_KEY_CONSTRAINT:
				return getForeignKeyConstraint();
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
			case DatabaseModelingPackage.FK__BASE_PROPERTY:
				setBase_Property((Property)newValue);
				return;
			case DatabaseModelingPackage.FK__FOREIGN_KEY_CONSTRAINT:
				setForeignKeyConstraint((String)newValue);
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
			case DatabaseModelingPackage.FK__BASE_PROPERTY:
				setBase_Property((Property)null);
				return;
			case DatabaseModelingPackage.FK__FOREIGN_KEY_CONSTRAINT:
				setForeignKeyConstraint(FOREIGN_KEY_CONSTRAINT_EDEFAULT);
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
			case DatabaseModelingPackage.FK__BASE_PROPERTY:
				return base_Property != null;
			case DatabaseModelingPackage.FK__FOREIGN_KEY_CONSTRAINT:
				return FOREIGN_KEY_CONSTRAINT_EDEFAULT == null ? foreignKeyConstraint != null : !FOREIGN_KEY_CONSTRAINT_EDEFAULT.equals(foreignKeyConstraint);
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
		result.append(" (foreignKeyConstraint: ");
		result.append(foreignKeyConstraint);
		result.append(')');
		return result.toString();
	}

} //FKImpl
