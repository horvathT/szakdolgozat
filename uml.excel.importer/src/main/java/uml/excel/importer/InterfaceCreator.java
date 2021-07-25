package uml.excel.importer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;

import tooling.stereotype.TableUtil;
import tooling.stereotype.UniqueIdentifierUtil;
import util.CellReaderUtil;

public class InterfaceCreator {
  
  private static final Logger log = PlatformUI.getWorkbench().getService(org.eclipse.e4.core.services.log.Logger.class);
  
  public List<Interface> createInterfacesFromSheet(Package modelPackage, Sheet sheet) {
    List<Interface> interfaces = new ArrayList<Interface>();
    Workbook workbook = sheet.getWorkbook();
    
    int rowNum = sheet.getLastRowNum();
    for(int i=1; i<= rowNum; i++) {
      String entityName = CellReaderUtil.getStringValue(sheet.getRow(i).getCell(1)); 
      
      if( workbook.getSheet(entityName) != null) {
        Interface newEntity = createInterfaceFromRow(sheet.getRow(i), modelPackage, entityName);
        interfaces.add(newEntity);
      }else {
        log.warn("No sheet created for entity: " + entityName);
      }
    }
    return interfaces;
  }
  
  private Interface createInterfaceFromRow(Row row, Package modelPackage, String entityName) {
    Interface newEntity = getEntityByName(modelPackage, entityName);
    modelPackage.getPackagedElements().add(newEntity);
    
    newEntity.setName(entityName);
    String label = CellReaderUtil.getStringValue(row.getCell(2));
    String dDLName = CellReaderUtil.getStringValue(row.getCell(3));
    String primaryKeyConstaintName = CellReaderUtil.getStringValue(row.getCell(4));
    String description = CellReaderUtil.getStringValue(row.getCell(5));
    
    Interface nestedPkInterface = UniqueIdentifierUtil.getNestedInterfaceWithStereotype(newEntity);
    if(nestedPkInterface != null) {
      UniqueIdentifierUtil.setPrimaryKeyConstraint(nestedPkInterface, primaryKeyConstaintName);
    }else {
      nestedPkInterface = UMLFactory.eINSTANCE.createInterface();
      nestedPkInterface.setName(entityName + "PK");
      newEntity.getNestedClassifiers().add(nestedPkInterface);
      UniqueIdentifierUtil.setPrimaryKeyConstraint(nestedPkInterface, primaryKeyConstaintName);
    }
    TableUtil.setInterfaceDDLName(newEntity, dDLName);
    if(!isDuplicateComment(newEntity, description) && !description.isEmpty()) {
      newEntity.createOwnedComment().setBody(description);
    }
    return newEntity;
  }
  
  private Interface getEntityByName(Package modelPackage, String entityName) {
    Interface interfac = UMLFactory.eINSTANCE.createInterface();
    Collection<Interface> interfaces =
        EcoreUtil.getObjectsByType(modelPackage.allOwnedElements(), UMLPackage.Literals.INTERFACE);
    for (Interface ifac : interfaces) {
      if (entityName.equals(ifac.getName())) {
        interfac = ifac;
      }
    }
    return interfac;
  }
  
  private boolean isDuplicateComment(Interface interfac, String description) {
    EList<Comment> ownedComments = interfac.getOwnedComments();
    for (Comment comment : ownedComments) {
      String content = comment.getBody();
      if(content.equals(description)) {
        return true;
      }
    }
    return false;
  }

}
