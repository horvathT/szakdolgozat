package uml.excel.importer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import org.apache.poi.ss.usermodel.Sheet;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.util.UMLUtil;
import tooling.stereotype.ColumnUtil;
import tooling.stereotype.EntityDataTypeUtil;
import tooling.stereotype.UniqueIdentifierUtil;
import util.CellReaderUtil;

public class PropertyCreator {

  private static final String YES = "yes";
  
  public void interfacePropertiesFromSheet(Package modelPackage, Sheet sheet) {

    Collection<DataType> dataTypes = getDataTypes(modelPackage);

    int lastRowNumber = sheet.getLastRowNum();
    String entityName = sheet.getSheetName();

    Interface interfac = getEntityByName(modelPackage, entityName);

    // DELETE properties by modelId
    removeDeletedProperties(interfac, sheet);
    
    for (int i = 1; i <= lastRowNumber; i++) {
      String propModelID = CellReaderUtil.getStringValue(sheet.getRow(i).getCell(0));
      String name = CellReaderUtil.getStringValue(sheet.getRow(i).getCell(1));
      //String label = CellReaderUtil.getStringValue(sheet.getRow(i).getCell(2));
      String dDLName = CellReaderUtil.getStringValue(sheet.getRow(i).getCell(3));
      String typeName = CellReaderUtil.getStringValue(sheet.getRow(i).getCell(4));
      String sqlType = CellReaderUtil.getStringValue(sheet.getRow(i).getCell(5));
      String comment = CellReaderUtil.getStringValue(sheet.getRow(i).getCell(6));
      String nullable = CellReaderUtil.getStringValue(sheet.getRow(i).getCell(7));
      String isPrimaryKey = CellReaderUtil.getStringValue(sheet.getRow(i).getCell(8));
      
      // CREATE or MODIFY
      Property property = getOrCreateProperty(interfac, propModelID);
      
      property.setName(name);
      property.setInterface(interfac);
      
      ColumnUtil.setPropertyDDLName(property, dDLName);
      
      DataType dataType = getDataTypeForString(typeName, dataTypes);
      setDataType(sqlType, property, dataType);
      
      setComment(comment, property);
      property.setLower(nullable.contains(YES) ? 0 : 1);
      if(isPrimaryKey.contains(YES)) {
        setPrimaryKey(property);
      }
    }
  }
  
  private Collection<DataType> getDataTypes(Package modelPackage) {
    Package nestingPackage = modelPackage.getNestingPackage();
    if (nestingPackage != null) {
      ResourceSet resourceSet = nestingPackage.eResource().getResourceSet();
      return EntityDataTypeUtil.getEntityDataTypes(resourceSet);
    }
    ResourceSet resourceSet = modelPackage.eResource().getResourceSet();
    return EntityDataTypeUtil.getEntityDataTypes(resourceSet);
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
  
  private void removeDeletedProperties(Interface interfac, Sheet sheet) {
    removeFromProperties(interfac, sheet);
    removeFromNestedProperties(interfac, sheet);
  }

  private void removeFromNestedProperties(Interface interfac, Sheet sheet) {
    Interface nestedPkInterface = UniqueIdentifierUtil.getNestedInterfaceWithStereotype(interfac);
    removeFromProperties(nestedPkInterface, sheet);
  }

  private void removeFromProperties(Interface interfac, Sheet sheet) {
    List<String> excelPropertyModelIds = getExcelPropertyModelIds(sheet);
    EList<Property> allAttributes = interfac.getAllAttributes();
    
    for (Iterator<Property> iterator = allAttributes.iterator(); iterator.hasNext();) {
      Property property = iterator.next();
      String modelId = EcoreUtil.getURI(property).fragment();
      if(property.getAssociation() == null && !excelPropertyModelIds.contains(modelId)) {
        property.destroy();
      }
    }
  }
  
  private List<String> getExcelPropertyModelIds(Sheet sheet) {
    List<String> excelPropertyNames = new ArrayList<String>();
    int lastRowNumber = sheet.getLastRowNum();
    for (int i = 1; i <= lastRowNumber; i++) {
      String modelId = CellReaderUtil.getStringValue(sheet.getRow(i).getCell(0));
      if(!modelId.isEmpty()) {
        excelPropertyNames.add(modelId);
      }
    }
    return excelPropertyNames;
  }
  
  private Property getOrCreateProperty(Interface interfac, String propModelID) {
    Property property = UMLFactory.eINSTANCE.createProperty();
    if (propModelID != null) {
      for (Property prop : interfac.allAttributes()) {
        if (EcoreUtil.getURI(prop).fragment().contentEquals(propModelID)) {
          property = prop;
        }
      }
    }
    return property;
  }
  
  private DataType getDataTypeForString(String typeName, Collection<DataType> dataTypes) {
    for (DataType dataType : dataTypes) {
      if (dataType.getName().equals(typeName)) {
        return dataType;
      }
    }
    throw new NullPointerException("There is no datatype for name: " + typeName);
  }
  
  private void setDataType(String sqlType, Property property, DataType dataType) {
    if (dataType != null) {
      property.setType(dataType);
      EntityDataTypeUtil.setSqlType(property, sqlType);
    }
  }
  
  private void setComment(String comment, Property property) {
    if(!isDuplicateComment(property, comment) && !comment.isEmpty()) {
      property.createOwnedComment().setBody(comment);
    }
  }
  
  private void setPrimaryKey(Property property) {
    Interface interfac = property.getInterface();
    addToNestedInterface(interfac, property);
  }
  
  private void addToNestedInterface(Interface interfac, Property property) {
    Interface uniqueIdInterfac = UniqueIdentifierUtil.getNestedInterfaceWithStereotype(interfac);
    if(uniqueIdInterfac != null) {
      interfac.getOwnedAttributes().add(property);
      
      Property pkRefProperty = UMLFactory.eINSTANCE.createProperty();
      uniqueIdInterfac.getOwnedAttributes().add(pkRefProperty);
      pkRefProperty.setName(property.getName());
      pkRefProperty.getRedefinedProperties().add(property);
    }
  }
  
  private boolean isDuplicateComment(Property property, String description) {
    EList<Comment> ownedComments = property.getOwnedComments();
    for (Comment comment : ownedComments) {
      String content = comment.getBody();
      if(content.equals(description)) {
        return true;
      }
    }
    return false;
  }
}
