package model.transfer.importer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;

import database.modeling.util.uml.ModelObjectUtil;
import model.transfer.util.CellUtil;
import model.transfer.util.ExcelReaderUtil;

/**
 * Attribútumok és metódusok létrehozását kezeli.
 * 
 * @author Horváth Tibor
 *
 */
public class PropertyAndMethodCreator extends ObjectImporter {

	private final static String METHODS = "metódusok";

	private Set<DataType> dataTypes = new HashSet<>();

	public PropertyAndMethodCreator(Workbook workbook, Package modelPackage) {
		super(workbook, modelPackage);

		dataTypes = ModelObjectUtil.getDataTypesFromModel(modelPackage);
	}

	/**
	 * Metódusok és attribútumok létrehozása.
	 */
	public void createMethodsAndProperties() {
		createClassMethodsAndProperties();
		createInterfaceMethodsAndProperties();
	}

	/**
	 * Interfész metódusok és attribútumok létrehozása.
	 */
	private void createInterfaceMethodsAndProperties() {
		Collection<Interface> interfaces = ModelObjectUtil.getInterfaces(modelPackage.allOwnedElements());
		for (Interface interfac : interfaces) {
			Sheet interfacSheet = workbook.getSheet(interfac.getName());
			int lastRowNum = interfacSheet.getLastRowNum();

			int mehtodHeaderRowNumber = getMethodHeaderRowNumber(interfacSheet);
			for (int i = 1; i < mehtodHeaderRowNumber; i++) {
				Row row = interfacSheet.getRow(i);
				if (row == null) {
					continue;
				}
				createInterfaceProperty(interfac, row);
			}

			createInterfaceMethods(interfac, interfacSheet, lastRowNum, mehtodHeaderRowNumber);

		}
	}

	/**
	 * Osztály metódusok és attribútumok létrehozása.
	 */
	private void createClassMethodsAndProperties() {
		Collection<Class> classes = ModelObjectUtil.getClasses(modelPackage.allOwnedElements());
		for (Class clazz : classes) {
			Sheet classSheet = workbook.getSheet(clazz.getName());
			int lastRowNum = classSheet.getLastRowNum();
			int mehtodHeaderRowNumber = getMethodHeaderRowNumber(classSheet);
			for (int i = 1; i < mehtodHeaderRowNumber; i++) {
				Row row = classSheet.getRow(i);
				if (row == null) {
					continue;
				}
				createClassProperty(clazz, row);
			}

			createClassMethod(clazz, classSheet, lastRowNum, mehtodHeaderRowNumber);

		}
	}

	/**
	 * Interfész metódusok létrehozása.
	 * 
	 * @param interfac              interfész aminek a metódusait létrehozzuk
	 * @param interfacSheet         munkalap amiről a metódus adatok olvassuk
	 * @param lastRowNum            sorszám amitől kezve haladunk
	 * @param mehtodHeaderRowNumber utolsó sor száma
	 */
	private void createInterfaceMethods(Interface interfac, Sheet interfacSheet, int lastRowNum,
			int mehtodHeaderRowNumber) {
		Operation operation = null;
		for (int i = ++mehtodHeaderRowNumber; i <= lastRowNum; i++) {
			Row row = interfacSheet.getRow(i);
			String xmiId = CellUtil.getStringCellValue(row.getCell(1));
			String name = CellUtil.getStringCellValue(row.getCell(2));
			String visibility = CellUtil.getStringCellValue(row.getCell(3));
			String isAbstract = CellUtil.getStringCellValue(row.getCell(4));
			String returnTypeName = CellUtil.getStringCellValue(row.getCell(5));
			String parameterTypeName = CellUtil.getStringCellValue(row.getCell(6));
			String parameterName = CellUtil.getStringCellValue(row.getCell(7));
			String comment = CellUtil.getStringCellValue(row.getCell(8));

			if (name.isEmpty()) {
				addOperationParameter(operation, parameterTypeName, parameterName);
			} else {
				operation = getOrCreateOperation(interfac, xmiId);
				operation.getOwnedParameters().clear();
				operation.setName(name);
				operation.setVisibility(ExcelReaderUtil.stringToVisibilityKind(visibility));
				operation.setIsAbstract(ExcelReaderUtil.stringToBoolean(isAbstract));
				DataType returnType = getdataTypeByName(returnTypeName);
				if (returnType != null) {
					Parameter parameter = UMLFactory.eINSTANCE.createParameter();
					parameter.setType(returnType);
					parameter.setDirection(ParameterDirectionKind.RETURN_LITERAL);
					operation.getOwnedParameters().add(parameter);
				}
				addOperationParameter(operation, parameterTypeName, parameterName);
				ModelObjectUtil.addComment(operation, comment);
			}
			if (operation != null) {
				operation.setInterface(interfac);
			}

		}
	}

	/**
	 * Osztály metódusok létrehozása.
	 * 
	 * @param clazz                 osztály metódusok létrehozása.
	 * @param classSheet            munkalap amiről a metódus adatok olvassuk
	 * @param lastRowNum            sorszám amitől kezve haladunk
	 * @param mehtodHeaderRowNumber utolsó sor száma
	 */
	private void createClassMethod(Class clazz, Sheet classSheet, int lastRowNum, int mehtodHeaderRowNumber) {
		Operation operation = null;
		for (int i = ++mehtodHeaderRowNumber; i <= lastRowNum; i++) {
			Row row = classSheet.getRow(i);
			String xmiId = CellUtil.getStringCellValue(row.getCell(1));
			String name = CellUtil.getStringCellValue(row.getCell(2));
			String visibility = CellUtil.getStringCellValue(row.getCell(3));
			String isStatic = CellUtil.getStringCellValue(row.getCell(4));
			String isAbstract = CellUtil.getStringCellValue(row.getCell(5));
			String returnTypeName = CellUtil.getStringCellValue(row.getCell(6));
			String parameterTypeName = CellUtil.getStringCellValue(row.getCell(7));
			String parameterName = CellUtil.getStringCellValue(row.getCell(8));
			String comment = CellUtil.getStringCellValue(row.getCell(9));

			if (name.isEmpty()) {
				addOperationParameter(operation, parameterTypeName, parameterName);
			} else {
				operation = getOrCreateOperation(clazz, xmiId);
				operation.getOwnedParameters().clear();
				operation.setName(name);
				operation.setVisibility(ExcelReaderUtil.stringToVisibilityKind(visibility));
				operation.setIsStatic(ExcelReaderUtil.stringToBoolean(isStatic));
				operation.setIsAbstract(ExcelReaderUtil.stringToBoolean(isAbstract));
				DataType returnType = getdataTypeByName(returnTypeName);
				if (returnType != null) {
					Parameter parameter = UMLFactory.eINSTANCE.createParameter();
					parameter.setType(returnType);
					parameter.setDirection(ParameterDirectionKind.RETURN_LITERAL);
					operation.getOwnedParameters().add(parameter);
				}
				addOperationParameter(operation, parameterTypeName, parameterName);
				ModelObjectUtil.addComment(operation, comment);
			}
			if (operation != null) {
				clazz.getOwnedOperations().add(operation);
			}
		}
	}

	/**
	 * Bemeneti paraméter létrehozása és metódushoz adás.
	 * 
	 * @param operation
	 * @param parameterTypeName
	 * @param parameterName
	 */
	private void addOperationParameter(Operation operation, String parameterTypeName,
			String parameterName) {
		DataType parameterType = getdataTypeByName(parameterTypeName);
		if (parameterType != null && !parameterName.isEmpty()) {
			Parameter parameter = UMLFactory.eINSTANCE.createParameter();
			parameter.setType(parameterType);
			parameter.setName(parameterName);
			operation.getOwnedParameters().add(parameter);
		}
	}

	/**
	 * Metódus fejléc sorszámát keresi meg. Ha nincs akkor nullát ad vissza.
	 * 
	 * @param interfacSheet
	 * @return
	 */
	public static int getMethodHeaderRowNumber(Sheet interfacSheet) {
		int lastRowNum = interfacSheet.getLastRowNum();
		for (int i = 0; i <= lastRowNum; i++) {
			Row row = interfacSheet.getRow(i);
			if (row == null) {
				continue;
			}
			String typeValue = CellUtil.getStringCellValue(row.getCell(0));
			if (typeValue.toLowerCase().equals(METHODS)) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * Interfész attribútum létrehozása
	 * 
	 * @param interfac
	 * @param row
	 */
	private void createInterfaceProperty(Interface interfac, Row row) {
		String xmiId = CellUtil.getStringCellValue(row.getCell(1));
		String name = CellUtil.getStringCellValue(row.getCell(2));
		String dataTypeName = CellUtil.getStringCellValue(row.getCell(3));
		String comment = CellUtil.getStringCellValue(row.getCell(4));

		Property property = getOrCreateProperty(interfac, xmiId);

		property.setName(name);
		DataType dataType = getdataTypeByName(dataTypeName);
		if (dataType != null) {
			property.setType(dataType);
		}
		ModelObjectUtil.addComment(property, comment);
		property.setInterface(interfac);
	}

	/**
	 * Attribútum keresése egyedi azonosító alapján. Ha nem található akkor egy új
	 * attribútumot ad vissza.
	 * 
	 * @param classifier  classifier aminek az attribútumai között keresünk
	 * @param propModelID egyedi azonosító
	 * @return
	 */
	private Property getOrCreateProperty(Classifier classifier, String propModelID) {
		Property property = UMLFactory.eINSTANCE.createProperty();
		if (propModelID != null) {
			for (Property prop : classifier.allAttributes()) {
				if (EcoreUtil.getURI(prop).fragment().contentEquals(propModelID)) {
					property = prop;
				}
			}
		}
		return property;
	}

	/**
	 * Metódus keresése egyedi azonosító alapján. Ha nem található akkor egy új
	 * metódust ad vissza.
	 * 
	 * @param classifier  classifier aminek a metódusai között keresünk
	 * @param propModelID egyedi azonosító
	 * @return
	 */
	private Operation getOrCreateOperation(Classifier classifier, String propModelID) {
		Operation operation = UMLFactory.eINSTANCE.createOperation();
		if (propModelID != null) {
			for (Operation op : classifier.getAllOperations()) {
				if (EcoreUtil.getURI(op).fragment().contentEquals(propModelID)) {
					operation = op;
				}
			}
		}
		return operation;
	}

	private DataType getdataTypeByName(String dataType) {
		for (DataType dt : dataTypes) {
			if (dt.getName().equals(dataType)) {
				return dt;
			}
		}
		return null;
	}

	/**
	 * Osztály attribútumának létrehozása.
	 * 
	 * @param clazz
	 * @param row
	 */
	private void createClassProperty(Class clazz, Row row) {
		String xmiId = CellUtil.getStringCellValue(row.getCell(1));
		String name = CellUtil.getStringCellValue(row.getCell(2));
		String visibility = CellUtil.getStringCellValue(row.getCell(3));
		String dataTypeName = CellUtil.getStringCellValue(row.getCell(4));
		String isStatic = CellUtil.getStringCellValue(row.getCell(5));
		String comment = CellUtil.getStringCellValue(row.getCell(6));

		Property property = getOrCreateProperty(clazz, xmiId);

		property.setName(name);
		property.setVisibility(ExcelReaderUtil.stringToVisibilityKind(visibility));
		DataType dataType = getdataTypeByName(dataTypeName);
		if (dataType != null) {
			property.setType(dataType);
		}
		property.setIsStatic(ExcelReaderUtil.stringToBoolean(isStatic));
		ModelObjectUtil.addComment(property, comment);
		clazz.getOwnedAttributes().add(property);
	}

	public void removeDeletedMethodsAndProperties() {
		Collection<Interface> interfaces = ModelObjectUtil.getInterfaces(modelPackage.allOwnedElements());
		removeDeletedProperties(interfaces);
		Collection<Class> classes = ModelObjectUtil.getClasses(modelPackage.allOwnedElements());
		removeDeletedProperties(classes);

		removeDeletedMethods(interfaces);
		removeDeletedMethods(classes);

	}

	/**
	 * Modellben még igen de Excellben már nem szereplő metódusok törlése.
	 * 
	 * @param classifiers
	 */
	private void removeDeletedMethods(Collection<? extends Classifier> classifiers) {
		for (Classifier classifier : classifiers) {
			Sheet classSheet = workbook.getSheet(classifier.getName());
			int mehtodHeaderRowNumber = getMethodHeaderRowNumber(classSheet);
			int lastRowNum = classSheet.getLastRowNum();
			List<String> excelMethodNames = collectMethodOrPropertyNames(mehtodHeaderRowNumber, lastRowNum, classifier);
			EList<Operation> operations = classifier.getOperations();
			for (Operation operation : operations) {
				if (!excelMethodNames.contains(operation.getName())) {
					EcoreUtil.delete(operation);
				}
			}
		}
	}

	/**
	 * Modellben még igen de Excellben már nem szereplő attribútumok törlése.
	 * 
	 * @param classifiers
	 */
	private void removeDeletedProperties(Collection<? extends Classifier> classifiers) {
		for (Classifier classifier : classifiers) {
			Sheet classSheet = workbook.getSheet(classifier.getName());
			int mehtodHeaderRowNumber = getMethodHeaderRowNumber(classSheet);
			List<String> propertyNames = collectMethodOrPropertyNames(1, mehtodHeaderRowNumber, classifier);
			for (Property property : classifier.allAttributes()) {
				if (property.getAssociation() == null) {
					if (!propertyNames.contains(property.getName())) {
						EcoreUtil.delete(property);
					}
				}
			}
		}
	}

	/**
	 * A megadott sor számok álltal közrefogott részben összegyűjti a kettes indexű
	 * oszlop értékeit.
	 * 
	 * @param firstRowNum innen kezdődik a sorok iterálása
	 * @param lastRowNum  ideig tart a sorok iterálása
	 * @param clazz
	 * @return
	 */
	private List<String> collectMethodOrPropertyNames(int firstRowNum, int lastRowNum, Classifier clazz) {
		List<String> propertyNames = new ArrayList<>();
		Sheet sheet = workbook.getSheet(clazz.getName());

		for (int i = firstRowNum; i <= lastRowNum; i++) {
			if (i == lastRowNum) {
				continue;
			}

			Row row = sheet.getRow(i);
			if (row != null) {
				String name = CellUtil.getStringCellValue(row.getCell(2));
				if (!name.isEmpty()) {
					propertyNames.add(name);
				}
			}
		}
		return propertyNames;
	}

}
