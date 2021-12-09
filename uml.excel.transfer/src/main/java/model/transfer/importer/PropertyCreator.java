package model.transfer.importer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;

import mode.transfer.util.CellUtil;
import mode.transfer.util.ExcelReaderUtil;
import mode.transfer.util.ModelObjectUtil;

public class PropertyCreator {

	private final String METHODS = "metódusok";
	private final String ATTRIBUTES = "attribútumok";

	private Package modelPackage;

	private Workbook workbook;

	private Collection<DataType> dataTypes;

	public PropertyCreator(Workbook workbook, Package modelPackage) {
		this.workbook = workbook;
		this.modelPackage = modelPackage;
	}

	public void createMethodsAndProperties() {
		dataTypes = ModelObjectUtil.getDataTypes(modelPackage.getModel().allOwnedElements());

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

			createInterfaceMethod(interfac, interfacSheet, lastRowNum, mehtodHeaderRowNumber);

		}
	}

	private void createInterfaceMethod(Interface interfac, Sheet interfacSheet, int lastRowNum,
			int mehtodHeaderRowNumber) {
		Operation operation = null;
		for (int i = ++mehtodHeaderRowNumber; i < lastRowNum; i++) {
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
				operation.setName(name);
				operation.setVisibility(ExcelReaderUtil.stringToVisibilityKind(visibility));
				operation.setIsAbstract(ExcelReaderUtil.stringToBoolean(isAbstract));
				DataType returnType = getdataTypeByName(returnTypeName);
				if (returnType != null) {
					Parameter parameter = UMLFactory.eINSTANCE.createParameter();
					parameter.setType(returnType);
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

	private void createClassMethod(Class clazz, Sheet classSheet, int lastRowNum, int mehtodHeaderRowNumber) {
		Operation operation = null;
		for (int i = ++mehtodHeaderRowNumber; i < lastRowNum; i++) {
			Row row = classSheet.getRow(i);
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
				operation = getOrCreateOperation(clazz, xmiId);
				operation.setName(name);
				operation.setVisibility(ExcelReaderUtil.stringToVisibilityKind(visibility));
				operation.setIsAbstract(ExcelReaderUtil.stringToBoolean(isAbstract));
				DataType returnType = getdataTypeByName(returnTypeName);
				if (returnType != null) {
					Parameter parameter = UMLFactory.eINSTANCE.createParameter();
					parameter.setType(returnType);
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

	private int getMethodHeaderRowNumber(Sheet interfacSheet) {
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

	private void createInterfaceProperty(Interface interfac, Row row) {
		String xmiId = CellUtil.getStringCellValue(row.getCell(1));
		String name = CellUtil.getStringCellValue(row.getCell(2));
		String dataTypeName = CellUtil.getStringCellValue(row.getCell(3));
		String comment = CellUtil.getStringCellValue(row.getCell(4));

		Property property = getOrCreateProperty(interfac, xmiId);

		property.setName(name);
		DataType dataType = getdataTypeByName(dataTypeName);
		if (dataType != null) {
			property.setDatatype(dataType);
		}
		ModelObjectUtil.addComment(property, comment);
		property.setInterface(interfac);
	}

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
			property.setDatatype(dataType);
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
