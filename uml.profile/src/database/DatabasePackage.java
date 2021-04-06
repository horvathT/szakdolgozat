/**
 */
package database;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see database.DatabaseFactory
 * @model kind="package"
 * @generated
 */
public interface DatabasePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "database";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///database.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "database";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DatabasePackage eINSTANCE = database.impl.DatabasePackageImpl.init();

	/**
	 * The meta object id for the '{@link database.impl.AssociationImpl <em>Association</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see database.impl.AssociationImpl
	 * @see database.impl.DatabasePackageImpl#getAssociation()
	 * @generated
	 */
	int ASSOCIATION = 0;

	/**
	 * The feature id for the '<em><b>Foreign Constraint Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__FOREIGN_CONSTRAINT_NAME = 0;

	/**
	 * The feature id for the '<em><b>Base Association</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__BASE_ASSOCIATION = 1;

	/**
	 * The number of structural features of the '<em>Association</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Association</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link database.impl.ColumnImpl <em>Column</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see database.impl.ColumnImpl
	 * @see database.impl.DatabasePackageImpl#getColumn()
	 * @generated
	 */
	int COLUMN = 1;

	/**
	 * The feature id for the '<em><b>Base Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN__BASE_PROPERTY = 0;

	/**
	 * The feature id for the '<em><b>Oracle Data Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN__ORACLE_DATA_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Oracle Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN__ORACLE_DEFAULT_VALUE = 2;

	/**
	 * The number of structural features of the '<em>Column</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Column</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link database.impl.TableImpl <em>Table</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see database.impl.TableImpl
	 * @see database.impl.DatabasePackageImpl#getTable()
	 * @generated
	 */
	int TABLE = 2;

	/**
	 * The feature id for the '<em><b>Base Interface</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__BASE_INTERFACE = 0;

	/**
	 * The feature id for the '<em><b>Base Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__BASE_CLASS = 1;

	/**
	 * The feature id for the '<em><b>Primary Key Constraint Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__PRIMARY_KEY_CONSTRAINT_NAME = 2;

	/**
	 * The feature id for the '<em><b>Data Definition Language Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__DATA_DEFINITION_LANGUAGE_NAME = 3;

	/**
	 * The number of structural features of the '<em>Table</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Table</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE_OPERATION_COUNT = 0;


	/**
	 * The meta object id for the '{@link database.impl.PKImpl <em>PK</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see database.impl.PKImpl
	 * @see database.impl.DatabasePackageImpl#getPK()
	 * @generated
	 */
	int PK = 3;

	/**
	 * The feature id for the '<em><b>Base Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PK__BASE_PROPERTY = 0;

	/**
	 * The number of structural features of the '<em>PK</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PK_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>PK</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PK_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link database.impl.FKImpl <em>FK</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see database.impl.FKImpl
	 * @see database.impl.DatabasePackageImpl#getFK()
	 * @generated
	 */
	int FK = 4;

	/**
	 * The feature id for the '<em><b>Base Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FK__BASE_PROPERTY = 0;

	/**
	 * The number of structural features of the '<em>FK</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FK_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>FK</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FK_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link database.Association <em>Association</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Association</em>'.
	 * @see database.Association
	 * @generated
	 */
	EClass getAssociation();

	/**
	 * Returns the meta object for the attribute '{@link database.Association#getForeignConstraintName <em>Foreign Constraint Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Foreign Constraint Name</em>'.
	 * @see database.Association#getForeignConstraintName()
	 * @see #getAssociation()
	 * @generated
	 */
	EAttribute getAssociation_ForeignConstraintName();

	/**
	 * Returns the meta object for the reference '{@link database.Association#getBase_Association <em>Base Association</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Base Association</em>'.
	 * @see database.Association#getBase_Association()
	 * @see #getAssociation()
	 * @generated
	 */
	EReference getAssociation_Base_Association();

	/**
	 * Returns the meta object for class '{@link database.Column <em>Column</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Column</em>'.
	 * @see database.Column
	 * @generated
	 */
	EClass getColumn();

	/**
	 * Returns the meta object for the reference '{@link database.Column#getBase_Property <em>Base Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Base Property</em>'.
	 * @see database.Column#getBase_Property()
	 * @see #getColumn()
	 * @generated
	 */
	EReference getColumn_Base_Property();

	/**
	 * Returns the meta object for the attribute '{@link database.Column#getOracleDataType <em>Oracle Data Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Oracle Data Type</em>'.
	 * @see database.Column#getOracleDataType()
	 * @see #getColumn()
	 * @generated
	 */
	EAttribute getColumn_OracleDataType();

	/**
	 * Returns the meta object for the attribute '{@link database.Column#getOracleDefaultValue <em>Oracle Default Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Oracle Default Value</em>'.
	 * @see database.Column#getOracleDefaultValue()
	 * @see #getColumn()
	 * @generated
	 */
	EAttribute getColumn_OracleDefaultValue();

	/**
	 * Returns the meta object for class '{@link database.Table <em>Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Table</em>'.
	 * @see database.Table
	 * @generated
	 */
	EClass getTable();

	/**
	 * Returns the meta object for the reference '{@link database.Table#getBase_Interface <em>Base Interface</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Base Interface</em>'.
	 * @see database.Table#getBase_Interface()
	 * @see #getTable()
	 * @generated
	 */
	EReference getTable_Base_Interface();

	/**
	 * Returns the meta object for the reference '{@link database.Table#getBase_Class <em>Base Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Base Class</em>'.
	 * @see database.Table#getBase_Class()
	 * @see #getTable()
	 * @generated
	 */
	EReference getTable_Base_Class();

	/**
	 * Returns the meta object for the attribute '{@link database.Table#getPrimaryKeyConstraintName <em>Primary Key Constraint Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Primary Key Constraint Name</em>'.
	 * @see database.Table#getPrimaryKeyConstraintName()
	 * @see #getTable()
	 * @generated
	 */
	EAttribute getTable_PrimaryKeyConstraintName();

	/**
	 * Returns the meta object for the attribute '{@link database.Table#getDataDefinitionLanguageName <em>Data Definition Language Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Data Definition Language Name</em>'.
	 * @see database.Table#getDataDefinitionLanguageName()
	 * @see #getTable()
	 * @generated
	 */
	EAttribute getTable_DataDefinitionLanguageName();

	/**
	 * Returns the meta object for class '{@link database.PK <em>PK</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>PK</em>'.
	 * @see database.PK
	 * @generated
	 */
	EClass getPK();

	/**
	 * Returns the meta object for the reference '{@link database.PK#getBase_Property <em>Base Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Base Property</em>'.
	 * @see database.PK#getBase_Property()
	 * @see #getPK()
	 * @generated
	 */
	EReference getPK_Base_Property();

	/**
	 * Returns the meta object for class '{@link database.FK <em>FK</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>FK</em>'.
	 * @see database.FK
	 * @generated
	 */
	EClass getFK();

	/**
	 * Returns the meta object for the reference '{@link database.FK#getBase_Property <em>Base Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Base Property</em>'.
	 * @see database.FK#getBase_Property()
	 * @see #getFK()
	 * @generated
	 */
	EReference getFK_Base_Property();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DatabaseFactory getDatabaseFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link database.impl.AssociationImpl <em>Association</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see database.impl.AssociationImpl
		 * @see database.impl.DatabasePackageImpl#getAssociation()
		 * @generated
		 */
		EClass ASSOCIATION = eINSTANCE.getAssociation();

		/**
		 * The meta object literal for the '<em><b>Foreign Constraint Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ASSOCIATION__FOREIGN_CONSTRAINT_NAME = eINSTANCE.getAssociation_ForeignConstraintName();

		/**
		 * The meta object literal for the '<em><b>Base Association</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSOCIATION__BASE_ASSOCIATION = eINSTANCE.getAssociation_Base_Association();

		/**
		 * The meta object literal for the '{@link database.impl.ColumnImpl <em>Column</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see database.impl.ColumnImpl
		 * @see database.impl.DatabasePackageImpl#getColumn()
		 * @generated
		 */
		EClass COLUMN = eINSTANCE.getColumn();

		/**
		 * The meta object literal for the '<em><b>Base Property</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COLUMN__BASE_PROPERTY = eINSTANCE.getColumn_Base_Property();

		/**
		 * The meta object literal for the '<em><b>Oracle Data Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COLUMN__ORACLE_DATA_TYPE = eINSTANCE.getColumn_OracleDataType();

		/**
		 * The meta object literal for the '<em><b>Oracle Default Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COLUMN__ORACLE_DEFAULT_VALUE = eINSTANCE.getColumn_OracleDefaultValue();

		/**
		 * The meta object literal for the '{@link database.impl.TableImpl <em>Table</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see database.impl.TableImpl
		 * @see database.impl.DatabasePackageImpl#getTable()
		 * @generated
		 */
		EClass TABLE = eINSTANCE.getTable();

		/**
		 * The meta object literal for the '<em><b>Base Interface</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TABLE__BASE_INTERFACE = eINSTANCE.getTable_Base_Interface();

		/**
		 * The meta object literal for the '<em><b>Base Class</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TABLE__BASE_CLASS = eINSTANCE.getTable_Base_Class();

		/**
		 * The meta object literal for the '<em><b>Primary Key Constraint Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TABLE__PRIMARY_KEY_CONSTRAINT_NAME = eINSTANCE.getTable_PrimaryKeyConstraintName();

		/**
		 * The meta object literal for the '<em><b>Data Definition Language Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TABLE__DATA_DEFINITION_LANGUAGE_NAME = eINSTANCE.getTable_DataDefinitionLanguageName();

		/**
		 * The meta object literal for the '{@link database.impl.PKImpl <em>PK</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see database.impl.PKImpl
		 * @see database.impl.DatabasePackageImpl#getPK()
		 * @generated
		 */
		EClass PK = eINSTANCE.getPK();

		/**
		 * The meta object literal for the '<em><b>Base Property</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PK__BASE_PROPERTY = eINSTANCE.getPK_Base_Property();

		/**
		 * The meta object literal for the '{@link database.impl.FKImpl <em>FK</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see database.impl.FKImpl
		 * @see database.impl.DatabasePackageImpl#getFK()
		 * @generated
		 */
		EClass FK = eINSTANCE.getFK();

		/**
		 * The meta object literal for the '<em><b>Base Property</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FK__BASE_PROPERTY = eINSTANCE.getFK_Base_Property();

	}

} //DatabasePackage
