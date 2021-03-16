/**
 */
package database;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Association</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link database.Association#getForeignConstraintName <em>Foreign Constraint Name</em>}</li>
 *   <li>{@link database.Association#getBase_Association <em>Base Association</em>}</li>
 * </ul>
 *
 * @see database.DatabasePackage#getAssociation()
 * @model
 * @generated
 */
public interface Association extends EObject {
	/**
	 * Returns the value of the '<em><b>Foreign Constraint Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Foreign Constraint Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Foreign Constraint Name</em>' attribute.
	 * @see #setForeignConstraintName(String)
	 * @see database.DatabasePackage#getAssociation_ForeignConstraintName()
	 * @model dataType="org.eclipse.uml2.types.String" ordered="false"
	 * @generated
	 */
	String getForeignConstraintName();

	/**
	 * Sets the value of the '{@link database.Association#getForeignConstraintName <em>Foreign Constraint Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Foreign Constraint Name</em>' attribute.
	 * @see #getForeignConstraintName()
	 * @generated
	 */
	void setForeignConstraintName(String value);

	/**
	 * Returns the value of the '<em><b>Base Association</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Association</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Association</em>' reference.
	 * @see #setBase_Association(org.eclipse.uml2.uml.Association)
	 * @see database.DatabasePackage#getAssociation_Base_Association()
	 * @model ordered="false"
	 * @generated
	 */
	org.eclipse.uml2.uml.Association getBase_Association();

	/**
	 * Sets the value of the '{@link database.Association#getBase_Association <em>Base Association</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Association</em>' reference.
	 * @see #getBase_Association()
	 * @generated
	 */
	void setBase_Association(org.eclipse.uml2.uml.Association value);

} // Association
