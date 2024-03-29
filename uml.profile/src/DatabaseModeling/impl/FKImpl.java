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
 *   <li>{@link DatabaseModeling.impl.FKImpl#getReferencedEntity <em>Referenced Entity</em>}</li>
 *   <li>{@link DatabaseModeling.impl.FKImpl#getReferencedProperty <em>Referenced Property</em>}</li>
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
	 * The default value of the '{@link #getReferencedEntity() <em>Referenced Entity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferencedEntity()
	 * @generated
	 * @ordered
	 */
	protected static final String REFERENCED_ENTITY_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getReferencedEntity() <em>Referenced Entity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferencedEntity()
	 * @generated
	 * @ordered
	 */
	protected String referencedEntity = REFERENCED_ENTITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getReferencedProperty() <em>Referenced Property</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferencedProperty()
	 * @generated
	 * @ordered
	 */
	protected static final String REFERENCED_PROPERTY_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getReferencedProperty() <em>Referenced Property</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferencedProperty()
	 * @generated
	 * @ordered
	 */
	protected String referencedProperty = REFERENCED_PROPERTY_EDEFAULT;

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
	public String getReferencedEntity() {
		return referencedEntity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReferencedEntity(String newReferencedEntity) {
		String oldReferencedEntity = referencedEntity;
		referencedEntity = newReferencedEntity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabaseModelingPackage.FK__REFERENCED_ENTITY, oldReferencedEntity, referencedEntity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getReferencedProperty() {
		return referencedProperty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReferencedProperty(String newReferencedProperty) {
		String oldReferencedProperty = referencedProperty;
		referencedProperty = newReferencedProperty;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatabaseModelingPackage.FK__REFERENCED_PROPERTY, oldReferencedProperty, referencedProperty));
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
			case DatabaseModelingPackage.FK__REFERENCED_ENTITY:
				return getReferencedEntity();
			case DatabaseModelingPackage.FK__REFERENCED_PROPERTY:
				return getReferencedProperty();
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
			case DatabaseModelingPackage.FK__REFERENCED_ENTITY:
				setReferencedEntity((String)newValue);
				return;
			case DatabaseModelingPackage.FK__REFERENCED_PROPERTY:
				setReferencedProperty((String)newValue);
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
			case DatabaseModelingPackage.FK__REFERENCED_ENTITY:
				setReferencedEntity(REFERENCED_ENTITY_EDEFAULT);
				return;
			case DatabaseModelingPackage.FK__REFERENCED_PROPERTY:
				setReferencedProperty(REFERENCED_PROPERTY_EDEFAULT);
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
			case DatabaseModelingPackage.FK__REFERENCED_ENTITY:
				return REFERENCED_ENTITY_EDEFAULT == null ? referencedEntity != null : !REFERENCED_ENTITY_EDEFAULT.equals(referencedEntity);
			case DatabaseModelingPackage.FK__REFERENCED_PROPERTY:
				return REFERENCED_PROPERTY_EDEFAULT == null ? referencedProperty != null : !REFERENCED_PROPERTY_EDEFAULT.equals(referencedProperty);
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
		result.append(" (referencedEntity: ");
		result.append(referencedEntity);
		result.append(", referencedProperty: ");
		result.append(referencedProperty);
		result.append(')');
		return result.toString();
	}

} //FKImpl
