package model.transfer.importer;

import java.util.Collection;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLFactory;

import mode.transfer.export.DataTypeSheetCreator;
import mode.transfer.util.CellUtil;
import mode.transfer.util.ModelObjectUtil;

public class DataTypeCreator {

	private Package modelPackage;

	private Workbook workbook;

	private TransactionalEditingDomain editingDomain;

	private static final String EXCEL_DATA_TYPES = "ImportaltTipusok";

	public DataTypeCreator(Workbook workbook, Package modelPackage, TransactionalEditingDomain editingDomain) {
		this.workbook = workbook;
		this.modelPackage = modelPackage;
		this.editingDomain = editingDomain;
	}

	public void createTypes() {
		Sheet dataTypeSheet = workbook.getSheet(DataTypeSheetCreator.DATA_TYPE_SHEET_NAME);

		Package targetPackage = createDataTypePackage(modelPackage);
		Collection<DataType> existingTypes = ModelObjectUtil.getDataTypes(modelPackage.allOwnedElements());
		createDataTypes(existingTypes, dataTypeSheet, targetPackage);
	}

	private void createDataTypes(Collection<DataType> existingTypes, Sheet dataTypeSheet, Package targetPackage) {
		for (int i = 1; i <= dataTypeSheet.getLastRowNum(); i++) {
			String xmiId = CellUtil.getStringCellValue(dataTypeSheet.getRow(i).getCell(0));
			String typeName = CellUtil.getStringCellValue(dataTypeSheet.getRow(i).getCell(1));
			if (xmiId.isEmpty()) {
				createDataTypeByName(existingTypes, typeName, targetPackage);
			} else {
				createDataTypeByXmiId(existingTypes, xmiId, typeName, targetPackage);
			}
		}
	}

	private DataType createDataTypeByXmiId(Collection<DataType> existingTypes, String xmiId, String typeName,
			Package targetPackage) {
		DataType dataType = getDataTypeByXmiId(existingTypes, xmiId);
		if (dataType == null) {
			return createNewDataType(typeName, targetPackage);
		}
		if (dataType != null && !editingDomain.isReadOnly(dataType.eResource())) {
			dataType.setName(typeName);
			return dataType;
		}
		return dataType;
	}

	private DataType createDataTypeByName(Collection<DataType> existingTypes, String typeName,
			Package targetPackage) {
		DataType dataType = getDataTypeByName(existingTypes, typeName);
		if (dataType != null) {
			return dataType;
		}
		return createNewDataType(typeName, targetPackage);
	}

	private DataType createNewDataType(String typeName, Package targetPackage) {
		DataType newDataType = UMLFactory.eINSTANCE.createDataType();
		newDataType.setName(typeName);
		newDataType.setPackage(targetPackage);
		return newDataType;
	}

	private DataType getDataTypeByName(Collection<DataType> existingTypes, String typeName) {
		for (DataType dataType : existingTypes) {
			if (typeName.equals(dataType.getName())) {
				return dataType;
			}
		}
		return null;
	}

	private DataType getDataTypeByXmiId(Collection<DataType> existingTypes, String xmiId) {
		for (DataType dataType : existingTypes) {
			String fragment = EcoreUtil.getURI(dataType).fragment();
			if (fragment.equals(xmiId)) {
				return dataType;
			}
		}
		return null;
	}

	private Package createDataTypePackage(Package modelPackage) {
		Model model = modelPackage.getModel();
		Package nestedPackage = model.getNestedPackage(EXCEL_DATA_TYPES);
		if (nestedPackage != null) {
			return nestedPackage;
		}
		return model.createNestedPackage(EXCEL_DATA_TYPES);
	}

}
