/**
 */
package database;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.uml2.uml.Property;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>PK</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link database.PK#getBase_Property <em>Base Property</em>}</li>
 *   <li>{@link database.PK#getOraclePrimaryKeyConstraint <em>Oracle Primary Key Constraint</em>}</li>
 *   <li>{@link database.PK#getMySqlPrimaryKeyConstraint <em>My Sql Primary Key Constraint</em>}</li>
 * </ul>
 *
 * @see database.DatabasePackage#getPK()
 * @model
 * @generated
 */
public interface PK extends EObject {
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
	 * @see database.DatabasePackage#getPK_Base_Property()
	 * @model ordered="false"
	 * @generated
	 */
	Property getBase_Property();

	/**
	 * Sets the value of the '{@link database.PK#getBase_Property <em>Base Property</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Property</em>' reference.
	 * @see #getBase_Property()
	 * @generated
	 */
	void setBase_Property(Property value);

	/**
	 * Returns the value of the '<em><b>Oracle Primary Key Constraint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Oracle Primary Key Constraint</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Oracle Primary Key Constraint</em>' attribute.
	 * @see #setOraclePrimaryKeyConstraint(String)
	 * @see database.DatabasePackage#getPK_OraclePrimaryKeyConstraint()
	 * @model dataType="org.eclipse.uml2.types.String" ordered="false"
	 * @generated
	 */
	String getOraclePrimaryKeyConstraint();

	/**
	 * Sets the value of the '{@link database.PK#getOraclePrimaryKeyConstraint <em>Oracle Primary Key Constraint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Oracle Primary Key Constraint</em>' attribute.
	 * @see #getOraclePrimaryKeyConstraint()
	 * @generated
	 */
	void setOraclePrimaryKeyConstraint(String value);

	/**
	 * Returns the value of the '<em><b>My Sql Primary Key Constraint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>My Sql Primary Key Constraint</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>My Sql Primary Key Constraint</em>' attribute.
	 * @see #setMySqlPrimaryKeyConstraint(String)
	 * @see database.DatabasePackage#getPK_MySqlPrimaryKeyConstraint()
	 * @model dataType="org.eclipse.uml2.types.String" required="true" ordered="false"
	 * @generated
	 */
	String getMySqlPrimaryKeyConstraint();

	/**
	 * Sets the value of the '{@link database.PK#getMySqlPrimaryKeyConstraint <em>My Sql Primary Key Constraint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>My Sql Primary Key Constraint</em>' attribute.
	 * @see #getMySqlPrimaryKeyConstraint()
	 * @generated
	 */
	void setMySqlPrimaryKeyConstraint(String value);

} // PK
