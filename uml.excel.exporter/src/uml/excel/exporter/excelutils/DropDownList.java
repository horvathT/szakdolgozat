package uml.excel.exporter.excelutils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;

public class DropDownList {
  private Set<String> namedAreaNames = new HashSet<>();
  
  public DropDownList(Workbook workbook) {
    createNamedAreas(workbook);
  }

  private void createNamedAreas(Workbook workbook) {
    for (Sheet sheet : workbook) {
      String sheetName = sheet.getSheetName();
      String namedAreaName = createNamedArea(workbook, sheetName, sheetName + "!$B$2:$B$1000");
      namedAreaNames.add(namedAreaName);
    }
  }
  
  public String createNamedArea(Workbook workbook, String areaName, String referencedArea) {
    String reference = referencedArea;
    Name namedArea = workbook.createName();
    namedArea.setNameName(areaName);
    namedArea.setRefersToFormula(reference);
    return areaName;
  }
  
  public void createDropdownList(Sheet sheet, String namedArea, int rowNum, int colNum) {
    CellRangeAddressList addressList = new CellRangeAddressList(rowNum, rowNum, colNum, colNum);
    DataValidationHelper dvHelper = sheet.getDataValidationHelper();
    DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint(namedArea);
    DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
    sheet.addValidationData(validation);
  }
  
  public void createAssociationSheetDropDowns(Sheet associationSheet) {
    String entityNamedArea = createNamedArea(associationSheet.getWorkbook(), "entityNamedArea", "Entities!$B$2:$B$1000");
    for(int rowNumber=1; rowNumber<=associationSheet.getLastRowNum(); rowNumber++) {
      createDropdownList(associationSheet, entityNamedArea, rowNumber, 1);
      createDropdownList(associationSheet, entityNamedArea, rowNumber, 7);
      
      //linked DDLists
      String thisInterface = "INDIRECT($B" + rowNumber +")";
      createDropdownList(associationSheet, thisInterface, rowNumber-1, 15);
      createDropdownList(associationSheet, thisInterface, rowNumber-1, 2);
      
      String otherInterface = "INDIRECT($H" + rowNumber +")";
      createDropdownList(associationSheet, otherInterface, rowNumber-1, 16);
      createDropdownList(associationSheet, otherInterface, rowNumber-1, 8);
      
    }
  }
  
  public void createEntityPropertySheetDropDowns(List<Sheet> sheets) {
    if(sheets.isEmpty()) {
      return;
    }
    Workbook workbook = sheets.get(0).getWorkbook(); 
    String typeNamedArea = createNamedArea(workbook, "typeNamedArea", "DataTypes!$A$2:$A$1000");
    for (Sheet sheet : sheets) {
      for(int rowNumber=1; rowNumber<=sheet.getLastRowNum(); rowNumber++) {
        createDropdownList(sheet, typeNamedArea, rowNumber, 4);
      }
    }
  }
}
