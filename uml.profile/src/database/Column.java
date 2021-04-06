/**
 */
package database;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.uml2.uml.Property;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Column</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link database.Column#getBase_Property <em>Base Property</em>}</li>
 *   <li>{@link database.Column#getOracleDataType <em>Oracle Data Type</em>}</li>
 *   <li>{@link database.Column#getOracleDefaultValue <em>Oracle Default Value</em>}</li>
 * </ul>
 *
 * @see database.DatabasePackage#getColumn()
 * @model
 * @generated
 */
public interface Column extends EObject {
	/**
	 * Returns the value of the '<em><b>Base Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Property</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Property</em>' reference.
	 * @see #setBase_Property(Property)
	 * @see database.DatabasePackage#getColumn_Base_Property()
	 * @model ordered="false"
	 * @generated
	 */
	Property getBase_Property();

	/**
	 * Sets the value of the '{@link database.Column#getBase_Property <em>Base Property</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Property</em>' reference.
	 * @see #getBase_Property()
	 * @generated
	 */
	void setBase_Property(Property value);

	/**
	 * Returns the value of the '<em><b>Oracle Data Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Oracle Data Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Oracle Data Type</em>' attribute.
	 * @see #setOracleDataType(String)
	 * @see database.DatabasePackage#getColumn_OracleDataType()
	 * @model dataType="org.eclipse.uml2.types.String" ordered="false"
	 * @generated
	 */
	String getOracleDataType();

	/**
	 * Sets the value of the '{@link database.Column#getOracleDataType <em>Oracle Data Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Oracle Data Type</em>' attribute.
	 * @see #getOracleDataType()
	 * @generated
	 */
	void setOracleDataType(String value);

	/**
	 * Returns the value of the '<em><b>Oracle Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Oracle Default Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Oracle Default Value</em>' attribute.
	 * @see #setOracleDefaultValue(String)
	 * @see database.DatabasePackage#getColumn_OracleDefaultValue()
	 * @model dataType="org.eclipse.uml2.types.String" ordered="false"
	 * @generated
	 */
	String getOracleDefaultValue();

	/**
	 * Sets the value of the '{@link database.Column#getOracleDefaultValue <em>Oracle Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Oracle Default Value</em>' attribute.
	 * @see #getOracleDefaultValue()
	 * @generated
	 */
	void setOracleDefaultValue(String value);

} // Column
