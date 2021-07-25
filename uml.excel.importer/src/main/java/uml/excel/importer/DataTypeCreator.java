package uml.excel.importer;

import java.util.Collection;
import org.apache.poi.ss.usermodel.Sheet;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLFactory;
import tooling.stereotype.EntityDataTypeUtil;
import util.CellReaderUtil;

public class DataTypeCreator {

  private static final String PLATFORM = "platform";

  private static final String EXCEL_DATA_TYPES = "ExcelDataTypes";

  Package modelPackage;

  public DataTypeCreator(Package modelPackage) {
    this.modelPackage = modelPackage;
  }

  public void createNewTypes(Sheet dataTypeSheet) {
    Package targetPackage = createPackage(modelPackage);
    Collection<DataType> existingTypes = getDataTypes(modelPackage);
    createDataTypes(existingTypes, dataTypeSheet, targetPackage);
  }

  private Package createPackage(Package modelPackage) {
    Model model = modelPackage.getModel();
    Package nestedPackage = model.getNestedPackage(EXCEL_DATA_TYPES);
    if (nestedPackage != null) {
      return nestedPackage;
    }
    return model.createNestedPackage(EXCEL_DATA_TYPES);
  }

  private void createDataTypes(Collection<DataType> existingTypes, Sheet dataTypeSheet,
      Package targetPackage) {
    for (int i = 1; i <= dataTypeSheet.getLastRowNum(); i++) {
      String typeName = dataTypeSheet.getRow(i).getCell(0).getStringCellValue();
      String sqlType = CellReaderUtil.getStringValue(dataTypeSheet.getRow(i).getCell(1));
      String javaType = CellReaderUtil.getStringValue(dataTypeSheet.getRow(i).getCell(2));
      String javaImport = CellReaderUtil.getStringValue(dataTypeSheet.getRow(i).getCell(3));
      String yamlType = CellReaderUtil.getStringValue(dataTypeSheet.getRow(i).getCell(4));
      String yamlFormat = CellReaderUtil.getStringValue(dataTypeSheet.getRow(i).getCell(5));

      DataType dataType = getOrCreateDataTypeByName(existingTypes, typeName, targetPackage);
      if (!isPlatformDataType(dataType)) {
        EntityDataTypeUtil.setSqlType(dataType, sqlType);
        EntityDataTypeUtil.setJavaType(dataType, javaType);
        EntityDataTypeUtil.setJavaImport(dataType, javaImport);
        EntityDataTypeUtil.setYamlType(dataType, yamlType);
        EntityDataTypeUtil.setYamlFormat(dataType, yamlFormat);
      }
    }
  }

  private boolean isPlatformDataType(DataType dataType) {
    String name = dataType.getModel().getName();
    return PLATFORM.equals(name);
  }

  private DataType getOrCreateDataTypeByName(Collection<DataType> existingTypes, String typeName,
      Package targetPackage) {
    if (contains(existingTypes, typeName)) {
      for (DataType dataType : existingTypes) {
        if (typeName.equals(dataType.getName())) {
          return dataType;
        }
      }
    }
    DataType newDataType = UMLFactory.eINSTANCE.createDataType();
    newDataType.setName(typeName);
    newDataType.setPackage(targetPackage);
    return newDataType;
  }

  private boolean contains(Collection<DataType> existingTypes, String typeName) {
    for (DataType dataType : existingTypes) {
      if (typeName.equals(dataType.getName())) {
        return true;
      }
    }
    return false;
  }

  private Collection<DataType> getDataTypes(Package modelPackage) {
    Model model = modelPackage.getModel();
    ResourceSet resourceSet = model.eResource().getResourceSet();
    return EntityDataTypeUtil.getEntityDataTypes(resourceSet);
  }
}
