/**
 */
package database;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.uml2.uml.Property;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>FK</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link database.FK#getBase_Property <em>Base Property</em>}</li>
 *   <li>{@link database.FK#getOracleForeingKeyConstraintName <em>Oracle Foreing Key Constraint Name</em>}</li>
 *   <li>{@link database.FK#getMySqlForeignKeyConstraint <em>My Sql Foreign Key Constraint</em>}</li>
 * </ul>
 *
 * @see database.DatabasePackage#getFK()
 * @model
 * @generated
 */
public interface FK extends EObject {
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
	 * @see database.DatabasePackage#getFK_Base_Property()
	 * @model ordered="false"
	 * @generated
	 */
	Property getBase_Property();

	/**
	 * Sets the value of the '{@link database.FK#getBase_Property <em>Base Property</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Property</em>' reference.
	 * @see #getBase_Property()
	 * @generated
	 */
	void setBase_Property(Property value);

	/**
	 * Returns the value of the '<em><b>Oracle Foreing Key Constraint Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Oracle Foreing Key Constraint Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Oracle Foreing Key Constraint Name</em>' attribute.
	 * @see #setOracleForeingKeyConstraintName(String)
	 * @see database.DatabasePackage#getFK_OracleForeingKeyConstraintName()
	 * @model dataType="org.eclipse.uml2.types.String" ordered="false"
	 * @generated
	 */
	String getOracleForeingKeyConstraintName();

	/**
	 * Sets the value of the '{@link database.FK#getOracleForeingKeyConstraintName <em>Oracle Foreing Key Constraint Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Oracle Foreing Key Constraint Name</em>' attribute.
	 * @see #getOracleForeingKeyConstraintName()
	 * @generated
	 */
	void setOracleForeingKeyConstraintName(String value);

	/**
	 * Returns the value of the '<em><b>My Sql Foreign Key Constraint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>My Sql Foreign Key Constraint</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>My Sql Foreign Key Constraint</em>' attribute.
	 * @see #setMySqlForeignKeyConstraint(String)
	 * @see database.DatabasePackage#getFK_MySqlForeignKeyConstraint()
	 * @model dataType="org.eclipse.uml2.types.String" ordered="false"
	 * @generated
	 */
	String getMySqlForeignKeyConstraint();

	/**
	 * Sets the value of the '{@link database.FK#getMySqlForeignKeyConstraint <em>My Sql Foreign Key Constraint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>My Sql Foreign Key Constraint</em>' attribute.
	 * @see #getMySqlForeignKeyConstraint()
	 * @generated
	 */
	void setMySqlForeignKeyConstraint(String value);

} // FK
