/**
 */
package DatabaseModeling;

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
 *   <li>{@link DatabaseModeling.Column#getBase_Property <em>Base Property</em>}</li>
 *   <li>{@link DatabaseModeling.Column#getOracleDataType <em>Oracle Data Type</em>}</li>
 *   <li>{@link DatabaseModeling.Column#getOracleDefaultValue <em>Oracle Default Value</em>}</li>
 *   <li>{@link DatabaseModeling.Column#getOracleLength <em>Oracle Length</em>}</li>
 *   <li>{@link DatabaseModeling.Column#getOraclePrecision <em>Oracle Precision</em>}</li>
 *   <li>{@link DatabaseModeling.Column#getOracleScale <em>Oracle Scale</em>}</li>
 *   <li>{@link DatabaseModeling.Column#getMySqlDataType <em>My Sql Data Type</em>}</li>
 *   <li>{@link DatabaseModeling.Column#getMySqlDefaultValue <em>My Sql Default Value</em>}</li>
 *   <li>{@link DatabaseModeling.Column#getMySqlLength <em>My Sql Length</em>}</li>
 *   <li>{@link DatabaseModeling.Column#getMySqlPrecision <em>My Sql Precision</em>}</li>
 *   <li>{@link DatabaseModeling.Column#getMySqlScale <em>My Sql Scale</em>}</li>
 * </ul>
 *
 * @see DatabaseModeling.DatabaseModelingPackage#getColumn()
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
	 * @see DatabaseModeling.DatabaseModelingPackage#getColumn_Base_Property()
	 * @model ordered="false"
	 * @generated
	 */
	Property getBase_Property();

	/**
	 * Sets the value of the '{@link DatabaseModeling.Column#getBase_Property <em>Base Property</em>}' reference.
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
	 * @see DatabaseModeling.DatabaseModelingPackage#getColumn_OracleDataType()
	 * @model dataType="org.eclipse.uml2.types.String" ordered="false"
	 * @generated
	 */
	String getOracleDataType();

	/**
	 * Sets the value of the '{@link DatabaseModeling.Column#getOracleDataType <em>Oracle Data Type</em>}' attribute.
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
	 * @see DatabaseModeling.DatabaseModelingPackage#getColumn_OracleDefaultValue()
	 * @model dataType="org.eclipse.uml2.types.String" ordered="false"
	 * @generated
	 */
	String getOracleDefaultValue();

	/**
	 * Sets the value of the '{@link DatabaseModeling.Column#getOracleDefaultValue <em>Oracle Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Oracle Default Value</em>' attribute.
	 * @see #getOracleDefaultValue()
	 * @generated
	 */
	void setOracleDefaultValue(String value);

	/**
	 * Returns the value of the '<em><b>Oracle Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Oracle Length</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Oracle Length</em>' attribute.
	 * @see #setOracleLength(int)
	 * @see DatabaseModeling.DatabaseModelingPackage#getColumn_OracleLength()
	 * @model dataType="org.eclipse.uml2.types.Integer" ordered="false"
	 * @generated
	 */
	int getOracleLength();

	/**
	 * Sets the value of the '{@link DatabaseModeling.Column#getOracleLength <em>Oracle Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Oracle Length</em>' attribute.
	 * @see #getOracleLength()
	 * @generated
	 */
	void setOracleLength(int value);

	/**
	 * Returns the value of the '<em><b>Oracle Precision</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Oracle Precision</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Oracle Precision</em>' attribute.
	 * @see #setOraclePrecision(int)
	 * @see DatabaseModeling.DatabaseModelingPackage#getColumn_OraclePrecision()
	 * @model dataType="org.eclipse.uml2.types.Integer" ordered="false"
	 * @generated
	 */
	int getOraclePrecision();

	/**
	 * Sets the value of the '{@link DatabaseModeling.Column#getOraclePrecision <em>Oracle Precision</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Oracle Precision</em>' attribute.
	 * @see #getOraclePrecision()
	 * @generated
	 */
	void setOraclePrecision(int value);

	/**
	 * Returns the value of the '<em><b>Oracle Scale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Oracle Scale</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Oracle Scale</em>' attribute.
	 * @see #setOracleScale(int)
	 * @see DatabaseModeling.DatabaseModelingPackage#getColumn_OracleScale()
	 * @model dataType="org.eclipse.uml2.types.Integer" ordered="false"
	 * @generated
	 */
	int getOracleScale();

	/**
	 * Sets the value of the '{@link DatabaseModeling.Column#getOracleScale <em>Oracle Scale</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Oracle Scale</em>' attribute.
	 * @see #getOracleScale()
	 * @generated
	 */
	void setOracleScale(int value);

	/**
	 * Returns the value of the '<em><b>My Sql Data Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>My Sql Data Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>My Sql Data Type</em>' attribute.
	 * @see #setMySqlDataType(String)
	 * @see DatabaseModeling.DatabaseModelingPackage#getColumn_MySqlDataType()
	 * @model dataType="org.eclipse.uml2.types.String" ordered="false"
	 * @generated
	 */
	String getMySqlDataType();

	/**
	 * Sets the value of the '{@link DatabaseModeling.Column#getMySqlDataType <em>My Sql Data Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>My Sql Data Type</em>' attribute.
	 * @see #getMySqlDataType()
	 * @generated
	 */
	void setMySqlDataType(String value);

	/**
	 * Returns the value of the '<em><b>My Sql Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>My Sql Default Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>My Sql Default Value</em>' attribute.
	 * @see #setMySqlDefaultValue(String)
	 * @see DatabaseModeling.DatabaseModelingPackage#getColumn_MySqlDefaultValue()
	 * @model dataType="org.eclipse.uml2.types.String" ordered="false"
	 * @generated
	 */
	String getMySqlDefaultValue();

	/**
	 * Sets the value of the '{@link DatabaseModeling.Column#getMySqlDefaultValue <em>My Sql Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>My Sql Default Value</em>' attribute.
	 * @see #getMySqlDefaultValue()
	 * @generated
	 */
	void setMySqlDefaultValue(String value);

	/**
	 * Returns the value of the '<em><b>My Sql Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>My Sql Length</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>My Sql Length</em>' attribute.
	 * @see #setMySqlLength(int)
	 * @see DatabaseModeling.DatabaseModelingPackage#getColumn_MySqlLength()
	 * @model dataType="org.eclipse.uml2.types.Integer" ordered="false"
	 * @generated
	 */
	int getMySqlLength();

	/**
	 * Sets the value of the '{@link DatabaseModeling.Column#getMySqlLength <em>My Sql Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>My Sql Length</em>' attribute.
	 * @see #getMySqlLength()
	 * @generated
	 */
	void setMySqlLength(int value);

	/**
	 * Returns the value of the '<em><b>My Sql Precision</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>My Sql Precision</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>My Sql Precision</em>' attribute.
	 * @see #setMySqlPrecision(int)
	 * @see DatabaseModeling.DatabaseModelingPackage#getColumn_MySqlPrecision()
	 * @model dataType="org.eclipse.uml2.types.Integer" ordered="false"
	 * @generated
	 */
	int getMySqlPrecision();

	/**
	 * Sets the value of the '{@link DatabaseModeling.Column#getMySqlPrecision <em>My Sql Precision</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>My Sql Precision</em>' attribute.
	 * @see #getMySqlPrecision()
	 * @generated
	 */
	void setMySqlPrecision(int value);

	/**
	 * Returns the value of the '<em><b>My Sql Scale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>My Sql Scale</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>My Sql Scale</em>' attribute.
	 * @see #setMySqlScale(int)
	 * @see DatabaseModeling.DatabaseModelingPackage#getColumn_MySqlScale()
	 * @model dataType="org.eclipse.uml2.types.Integer" ordered="false"
	 * @generated
	 */
	int getMySqlScale();

	/**
	 * Sets the value of the '{@link DatabaseModeling.Column#getMySqlScale <em>My Sql Scale</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>My Sql Scale</em>' attribute.
	 * @see #getMySqlScale()
	 * @generated
	 */
	void setMySqlScale(int value);

} // Column
