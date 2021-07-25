package uml.excel.exporter;

import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Property;
import tooling.stereotype.ColumnUtil;
import tooling.stereotype.EntityDataTypeUtil;
import tooling.stereotype.UniqueIdentifierUtil;

public class PropertyRow{
  private String modelId;
  private String name;
  private String label;
  private String dDLName;
  private String typeName;
  private String sqlType;
  private String ownedComment;
  private String nullable;
  private boolean isPrimaryKey;
  
  
  public PropertyRow(Property property) {
    this.modelId = EcoreUtil.getURI(property).fragment();
    this.name = property.getName();
    this.label = property.getLabel();
    this.dDLName = ColumnUtil.getPropertyDDLName(property);
    if(property.getType() != null) {
    	this.typeName = property.getType().getName();
    	this.sqlType = EntityDataTypeUtil.sqlByCustomType(property);
    }else {
    	this.typeName = "";
    	this.sqlType = "";
    }
    this.ownedComment = getFirstComment(property);
    this.nullable = property.getLower() == 0 ? "yes" : "no";
    this.isPrimaryKey = isPrimaryKey(property); 
  }
  
  public boolean isPrimaryKey(Property property) {
    Interface interfac = property.getInterface();
    List<Property> redefinedPkProps = UniqueIdentifierUtil.getRedefinedPrimaryKeyComponents(interfac);
    if(redefinedPkProps.contains(property)) {
      return true;
    }
    return false;
  }

  public void createRow(Row row) {
    row.createCell(0).setCellValue(modelId);
    row.createCell(1).setCellValue(name);
    row.createCell(2).setCellValue(label);
    row.createCell(3).setCellValue(dDLName);
    row.createCell(4).setCellValue(typeName);
    Cell cell = row.createCell(5);
    
    setVlookupOnType(cell);
    if(sqlType != null && !sqlType.isEmpty()) {
      cell.setCellValue(sqlType);
    } else {
      cell.setCellValue("");
    }
    row.createCell(6).setCellValue(ownedComment);
    row.createCell(7).setCellValue(nullable);
    if(isPrimaryKey) {
      row.createCell(8).setCellValue("yes");
    }else {
      row.createCell(8).setCellValue("no");
    }
  }

  private String getFirstComment(Property property) {
    if (property.getOwnedComments().size() > 0) {
      return property.getOwnedComments().get(0).getBody();
    }
    return"";
  }
  
  private void setVlookupOnType(Cell cell) {
    Workbook workbook = cell.getRow().getSheet().getWorkbook();
    String colString = CellReference.convertNumToColString(cell.getColumnIndex()-1);
    int rowNum = cell.getRowIndex()+1;
    
    cell.setCellFormula("VLOOKUP("+ colString + rowNum + ",DataTypes!$A$2:$B$51,2,false)");

    XSSFFormulaEvaluator formulaEvaluator = (XSSFFormulaEvaluator) workbook.getCreationHelper().createFormulaEvaluator();
    formulaEvaluator.evaluateFormulaCell(cell);
  }
}
