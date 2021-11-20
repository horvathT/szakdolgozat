/**
 */
package DatabaseModeling;

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
 * @see DatabaseModeling.DatabaseModelingFactory
 * @model kind="package"
 * @generated
 */
public interface DatabaseModelingPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "DatabaseModeling";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///DatabaseModeling.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "DatabaseModeling";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DatabaseModelingPackage eINSTANCE = DatabaseModeling.impl.DatabaseModelingPackageImpl.init();

	/**
	 * The meta object id for the '{@link DatabaseModeling.impl.ColumnImpl <em>Column</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see DatabaseModeling.impl.ColumnImpl
	 * @see DatabaseModeling.impl.DatabaseModelingPackageImpl#getColumn()
	 * @generated
	 */
	int COLUMN = 0;

	/**
	 * The feature id for the '<em><b>Base Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN__BASE_PROPERTY = 0;

	/**
	 * The feature id for the '<em><b>Data Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN__DATA_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN__DEFAULT_VALUE = 2;

	/**
	 * The feature id for the '<em><b>Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN__LENGTH = 3;

	/**
	 * The feature id for the '<em><b>Precision</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN__PRECISION = 4;

	/**
	 * The feature id for the '<em><b>Scale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN__SCALE = 5;

	/**
	 * The feature id for the '<em><b>Unique</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN__UNIQUE = 6;

	/**
	 * The feature id for the '<em><b>Nullable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN__NULLABLE = 7;

	/**
	 * The number of structural features of the '<em>Column</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN_FEATURE_COUNT = 8;

	/**
	 * The number of operations of the '<em>Column</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLUMN_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link DatabaseModeling.impl.PKImpl <em>PK</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see DatabaseModeling.impl.PKImpl
	 * @see DatabaseModeling.impl.DatabaseModelingPackageImpl#getPK()
	 * @generated
	 */
	int PK = 1;

	/**
	 * The feature id for the '<em><b>Base Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PK__BASE_PROPERTY = 0;

	/**
	 * The feature id for the '<em><b>Primary Key Constraint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PK__PRIMARY_KEY_CONSTRAINT = 1;

	/**
	 * The number of structural features of the '<em>PK</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PK_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>PK</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PK_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link DatabaseModeling.impl.FKImpl <em>FK</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see DatabaseModeling.impl.FKImpl
	 * @see DatabaseModeling.impl.DatabaseModelingPackageImpl#getFK()
	 * @generated
	 */
	int FK = 2;

	/**
	 * The feature id for the '<em><b>Base Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FK__BASE_PROPERTY = 0;

	/**
	 * The feature id for the '<em><b>Foreign Key Constraint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FK__FOREIGN_KEY_CONSTRAINT = 1;

	/**
	 * The feature id for the '<em><b>Referenced Entity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FK__REFERENCED_ENTITY = 2;

	/**
	 * The feature id for the '<em><b>Referenced Property</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FK__REFERENCED_PROPERTY = 3;

	/**
	 * The number of structural features of the '<em>FK</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FK_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>FK</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FK_OPERATION_COUNT = 0;


	/**
	 * The meta object id for the '{@link DatabaseModeling.impl.DatabaseModelImpl <em>Database Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see DatabaseModeling.impl.DatabaseModelImpl
	 * @see DatabaseModeling.impl.DatabaseModelingPackageImpl#getDatabaseModel()
	 * @generated
	 */
	int DATABASE_MODEL = 3;

	/**
	 * The feature id for the '<em><b>Base Package</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATABASE_MODEL__BASE_PACKAGE = 0;

	/**
	 * The feature id for the '<em><b>Database Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATABASE_MODEL__DATABASE_TYPE = 1;

	/**
	 * The number of structural features of the '<em>Database Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATABASE_MODEL_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Database Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATABASE_MODEL_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link DatabaseModeling.Column <em>Column</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Column</em>'.
	 * @see DatabaseModeling.Column
	 * @generated
	 */
	EClass getColumn();

	/**
	 * Returns the meta object for the reference '{@link DatabaseModeling.Column#getBase_Property <em>Base Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Base Property</em>'.
	 * @see DatabaseModeling.Column#getBase_Property()
	 * @see #getColumn()
	 * @generated
	 */
	EReference getColumn_Base_Property();

	/**
	 * Returns the meta object for the attribute '{@link DatabaseModeling.Column#getDataType <em>Data Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Data Type</em>'.
	 * @see DatabaseModeling.Column#getDataType()
	 * @see #getColumn()
	 * @generated
	 */
	EAttribute getColumn_DataType();

	/**
	 * Returns the meta object for the attribute '{@link DatabaseModeling.Column#getDefaultValue <em>Default Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Value</em>'.
	 * @see DatabaseModeling.Column#getDefaultValue()
	 * @see #getColumn()
	 * @generated
	 */
	EAttribute getColumn_DefaultValue();

	/**
	 * Returns the meta object for the attribute '{@link DatabaseModeling.Column#getLength <em>Length</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Length</em>'.
	 * @see DatabaseModeling.Column#getLength()
	 * @see #getColumn()
	 * @generated
	 */
	EAttribute getColumn_Length();

	/**
	 * Returns the meta object for the attribute '{@link DatabaseModeling.Column#getPrecision <em>Precision</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Precision</em>'.
	 * @see DatabaseModeling.Column#getPrecision()
	 * @see #getColumn()
	 * @generated
	 */
	EAttribute getColumn_Precision();

	/**
	 * Returns the meta object for the attribute '{@link DatabaseModeling.Column#getScale <em>Scale</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Scale</em>'.
	 * @see DatabaseModeling.Column#getScale()
	 * @see #getColumn()
	 * @generated
	 */
	EAttribute getColumn_Scale();

	/**
	 * Returns the meta object for the attribute '{@link DatabaseModeling.Column#isUnique <em>Unique</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Unique</em>'.
	 * @see DatabaseModeling.Column#isUnique()
	 * @see #getColumn()
	 * @generated
	 */
	EAttribute getColumn_Unique();

	/**
	 * Returns the meta object for the attribute '{@link DatabaseModeling.Column#isNullable <em>Nullable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Nullable</em>'.
	 * @see DatabaseModeling.Column#isNullable()
	 * @see #getColumn()
	 * @generated
	 */
	EAttribute getColumn_Nullable();

	/**
	 * Returns the meta object for class '{@link DatabaseModeling.PK <em>PK</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>PK</em>'.
	 * @see DatabaseModeling.PK
	 * @generated
	 */
	EClass getPK();

	/**
	 * Returns the meta object for the reference '{@link DatabaseModeling.PK#getBase_Property <em>Base Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Base Property</em>'.
	 * @see DatabaseModeling.PK#getBase_Property()
	 * @see #getPK()
	 * @generated
	 */
	EReference getPK_Base_Property();

	/**
	 * Returns the meta object for the attribute '{@link DatabaseModeling.PK#getPrimaryKeyConstraint <em>Primary Key Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Primary Key Constraint</em>'.
	 * @see DatabaseModeling.PK#getPrimaryKeyConstraint()
	 * @see #getPK()
	 * @generated
	 */
	EAttribute getPK_PrimaryKeyConstraint();

	/**
	 * Returns the meta object for class '{@link DatabaseModeling.FK <em>FK</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>FK</em>'.
	 * @see DatabaseModeling.FK
	 * @generated
	 */
	EClass getFK();

	/**
	 * Returns the meta object for the reference '{@link DatabaseModeling.FK#getBase_Property <em>Base Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Base Property</em>'.
	 * @see DatabaseModeling.FK#getBase_Property()
	 * @see #getFK()
	 * @generated
	 */
	EReference getFK_Base_Property();

	/**
	 * Returns the meta object for the attribute '{@link DatabaseModeling.FK#getForeignKeyConstraint <em>Foreign Key Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Foreign Key Constraint</em>'.
	 * @see DatabaseModeling.FK#getForeignKeyConstraint()
	 * @see #getFK()
	 * @generated
	 */
	EAttribute getFK_ForeignKeyConstraint();

	/**
	 * Returns the meta object for the attribute '{@link DatabaseModeling.FK#getReferencedEntity <em>Referenced Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Referenced Entity</em>'.
	 * @see DatabaseModeling.FK#getReferencedEntity()
	 * @see #getFK()
	 * @generated
	 */
	EAttribute getFK_ReferencedEntity();

	/**
	 * Returns the meta object for the attribute '{@link DatabaseModeling.FK#getReferencedProperty <em>Referenced Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Referenced Property</em>'.
	 * @see DatabaseModeling.FK#getReferencedProperty()
	 * @see #getFK()
	 * @generated
	 */
	EAttribute getFK_ReferencedProperty();

	/**
	 * Returns the meta object for class '{@link DatabaseModeling.DatabaseModel <em>Database Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Database Model</em>'.
	 * @see DatabaseModeling.DatabaseModel
	 * @generated
	 */
	EClass getDatabaseModel();

	/**
	 * Returns the meta object for the reference '{@link DatabaseModeling.DatabaseModel#getBase_Package <em>Base Package</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Base Package</em>'.
	 * @see DatabaseModeling.DatabaseModel#getBase_Package()
	 * @see #getDatabaseModel()
	 * @generated
	 */
	EReference getDatabaseModel_Base_Package();

	/**
	 * Returns the meta object for the attribute '{@link DatabaseModeling.DatabaseModel#getDatabaseType <em>Database Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Database Type</em>'.
	 * @see DatabaseModeling.DatabaseModel#getDatabaseType()
	 * @see #getDatabaseModel()
	 * @generated
	 */
	EAttribute getDatabaseModel_DatabaseType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DatabaseModelingFactory getDatabaseModelingFactory();

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
		 * The meta object literal for the '{@link DatabaseModeling.impl.ColumnImpl <em>Column</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see DatabaseModeling.impl.ColumnImpl
		 * @see DatabaseModeling.impl.DatabaseModelingPackageImpl#getColumn()
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
		 * The meta object literal for the '<em><b>Data Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COLUMN__DATA_TYPE = eINSTANCE.getColumn_DataType();

		/**
		 * The meta object literal for the '<em><b>Default Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COLUMN__DEFAULT_VALUE = eINSTANCE.getColumn_DefaultValue();

		/**
		 * The meta object literal for the '<em><b>Length</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COLUMN__LENGTH = eINSTANCE.getColumn_Length();

		/**
		 * The meta object literal for the '<em><b>Precision</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COLUMN__PRECISION = eINSTANCE.getColumn_Precision();

		/**
		 * The meta object literal for the '<em><b>Scale</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COLUMN__SCALE = eINSTANCE.getColumn_Scale();

		/**
		 * The meta object literal for the '<em><b>Unique</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COLUMN__UNIQUE = eINSTANCE.getColumn_Unique();

		/**
		 * The meta object literal for the '<em><b>Nullable</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COLUMN__NULLABLE = eINSTANCE.getColumn_Nullable();

		/**
		 * The meta object literal for the '{@link DatabaseModeling.impl.PKImpl <em>PK</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see DatabaseModeling.impl.PKImpl
		 * @see DatabaseModeling.impl.DatabaseModelingPackageImpl#getPK()
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
		 * The meta object literal for the '<em><b>Primary Key Constraint</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PK__PRIMARY_KEY_CONSTRAINT = eINSTANCE.getPK_PrimaryKeyConstraint();

		/**
		 * The meta object literal for the '{@link DatabaseModeling.impl.FKImpl <em>FK</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see DatabaseModeling.impl.FKImpl
		 * @see DatabaseModeling.impl.DatabaseModelingPackageImpl#getFK()
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

		/**
		 * The meta object literal for the '<em><b>Foreign Key Constraint</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FK__FOREIGN_KEY_CONSTRAINT = eINSTANCE.getFK_ForeignKeyConstraint();

		/**
		 * The meta object literal for the '<em><b>Referenced Entity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FK__REFERENCED_ENTITY = eINSTANCE.getFK_ReferencedEntity();

		/**
		 * The meta object literal for the '<em><b>Referenced Property</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FK__REFERENCED_PROPERTY = eINSTANCE.getFK_ReferencedProperty();

		/**
		 * The meta object literal for the '{@link DatabaseModeling.impl.DatabaseModelImpl <em>Database Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see DatabaseModeling.impl.DatabaseModelImpl
		 * @see DatabaseModeling.impl.DatabaseModelingPackageImpl#getDatabaseModel()
		 * @generated
		 */
		EClass DATABASE_MODEL = eINSTANCE.getDatabaseModel();

		/**
		 * The meta object literal for the '<em><b>Base Package</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATABASE_MODEL__BASE_PACKAGE = eINSTANCE.getDatabaseModel_Base_Package();

		/**
		 * The meta object literal for the '<em><b>Database Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATABASE_MODEL__DATABASE_TYPE = eINSTANCE.getDatabaseModel_DatabaseType();

	}

} //DatabaseModelingPackage
