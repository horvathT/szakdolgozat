/**
 */
package DatabaseModeling.impl;

import DatabaseModeling.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DatabaseModelingFactoryImpl extends EFactoryImpl implements DatabaseModelingFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static DatabaseModelingFactory init() {
		try {
			DatabaseModelingFactory theDatabaseModelingFactory = (DatabaseModelingFactory)EPackage.Registry.INSTANCE.getEFactory(DatabaseModelingPackage.eNS_URI);
			if (theDatabaseModelingFactory != null) {
				return theDatabaseModelingFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DatabaseModelingFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DatabaseModelingFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case DatabaseModelingPackage.COLUMN: return createColumn();
			case DatabaseModelingPackage.PK: return createPK();
			case DatabaseModelingPackage.FK: return createFK();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Column createColumn() {
		ColumnImpl column = new ColumnImpl();
		return column;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PK createPK() {
		PKImpl pk = new PKImpl();
		return pk;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FK createFK() {
		FKImpl fk = new FKImpl();
		return fk;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DatabaseModelingPackage getDatabaseModelingPackage() {
		return (DatabaseModelingPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static DatabaseModelingPackage getPackage() {
		return DatabaseModelingPackage.eINSTANCE;
	}

} //DatabaseModelingFactoryImpl
