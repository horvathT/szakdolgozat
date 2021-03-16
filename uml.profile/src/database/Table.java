/**
 */
package database;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.uml2.uml.Interface;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Table</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link database.Table#getBase_Interface <em>Base Interface</em>}</li>
 *   <li>{@link database.Table#getBase_Class <em>Base Class</em>}</li>
 *   <li>{@link database.Table#getPrimaryKeyConstraintName <em>Primary Key Constraint Name</em>}</li>
 *   <li>{@link database.Table#getDataDefinitionLanguageName <em>Data Definition Language Name</em>}</li>
 * </ul>
 *
 * @see database.DatabasePackage#getTable()
 * @model
 * @generated
 */
public interface Table extends EObject {
	/**
	 * Returns the value of the '<em><b>Base Interface</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Interface</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Interface</em>' reference.
	 * @see #setBase_Interface(Interface)
	 * @see database.DatabasePackage#getTable_Base_Interface()
	 * @model ordered="false"
	 * @generated
	 */
	Interface getBase_Interface();

	/**
	 * Sets the value of the '{@link database.Table#getBase_Interface <em>Base Interface</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Interface</em>' reference.
	 * @see #getBase_Interface()
	 * @generated
	 */
	void setBase_Interface(Interface value);

	/**
	 * Returns the value of the '<em><b>Base Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Class</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Class</em>' reference.
	 * @see #setBase_Class(org.eclipse.uml2.uml.Class)
	 * @see database.DatabasePackage#getTable_Base_Class()
	 * @model ordered="false"
	 * @generated
	 */
	org.eclipse.uml2.uml.Class getBase_Class();

	/**
	 * Sets the value of the '{@link database.Table#getBase_Class <em>Base Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Class</em>' reference.
	 * @see #getBase_Class()
	 * @generated
	 */
	void setBase_Class(org.eclipse.uml2.uml.Class value);

	/**
	 * Returns the value of the '<em><b>Primary Key Constraint Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Primary Key Constraint Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Primary Key Constraint Name</em>' attribute.
	 * @see #setPrimaryKeyConstraintName(String)
	 * @see database.DatabasePackage#getTable_PrimaryKeyConstraintName()
	 * @model dataType="org.eclipse.uml2.types.String" required="true" ordered="false"
	 * @generated
	 */
	String getPrimaryKeyConstraintName();

	/**
	 * Sets the value of the '{@link database.Table#getPrimaryKeyConstraintName <em>Primary Key Constraint Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Primary Key Constraint Name</em>' attribute.
	 * @see #getPrimaryKeyConstraintName()
	 * @generated
	 */
	void setPrimaryKeyConstraintName(String value);

	/**
	 * Returns the value of the '<em><b>Data Definition Language Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data Definition Language Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data Definition Language Name</em>' attribute.
	 * @see #setDataDefinitionLanguageName(String)
	 * @see database.DatabasePackage#getTable_DataDefinitionLanguageName()
	 * @model dataType="org.eclipse.uml2.types.String" required="true" ordered="false"
	 * @generated
	 */
	String getDataDefinitionLanguageName();

	/**
	 * Sets the value of the '{@link database.Table#getDataDefinitionLanguageName <em>Data Definition Language Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Data Definition Language Name</em>' attribute.
	 * @see #getDataDefinitionLanguageName()
	 * @generated
	 */
	void setDataDefinitionLanguageName(String value);

} // Table
