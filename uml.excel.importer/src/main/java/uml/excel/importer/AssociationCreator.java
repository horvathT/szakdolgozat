package uml.excel.importer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Sheet;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.util.UMLUtil;
import tooling.stereotype.EntityReferenceUtil;
import tooling.stereotype.UniqueIdentifierUtil;
import util.CellReaderUtil;

public class AssociationCreator {

  private Package modelPackage;

  public AssociationCreator(Package modelPackage) {
    this.modelPackage = modelPackage;
  }

  public void associationFromSheet(Sheet associationSheet) {
    List<AssociationData> excelAssociations = getAssociationsFromExcel(associationSheet);
    
    List<String> excelAssociaitonModelIds = getExcelAssociaitonModelIds(excelAssociations);
    removeDeletedAssociations(excelAssociaitonModelIds);
    
    createAssociations(excelAssociations);
  }
  
  
  private List<String> getExcelAssociaitonModelIds(List<AssociationData> excelAssociations) {
    List<String> modelIds = new ArrayList<>();
    for (AssociationData associationData : excelAssociations) {
      String modelId = associationData.getModelId();
      if(!modelId.isEmpty()) {
        modelIds.add(modelId);
      }
    }
    return modelIds;
  }

  private void removeDeletedAssociations(List<String> excelAssociaitonModelIds) {
    Collection<Association> associations =
        EcoreUtil.getObjectsByType(getModelPackageElements(), UMLPackage.Literals.ASSOCIATION);
    
    for (Iterator<Association> iterator = associations.iterator(); iterator.hasNext();) {
      Association assoc = iterator.next();
      String modelId = EcoreUtil.getURI(assoc).fragment();
      if(!excelAssociaitonModelIds.contains(modelId)) {
        removeEntityReferenceInterface(assoc);
        assoc.getMemberEnds().get(0).destroy();
        assoc.destroy();
      }
    }
  }

  private void removeEntityReferenceInterface(Association assoc) {
    Property referenceProperty = assoc.getMemberEnds().get(0);
    Interface parentInterface = (Interface) assoc.getMemberEnds().get(1).getType();
    List<Interface> nestedEntityReferenceInterfaces = EntityReferenceUtil.getNestedEntityReferenceInterfaces(parentInterface);
    for (Interface nestedInterface : nestedEntityReferenceInterfaces) {
      Property interfacReferenceProperty = EntityReferenceUtil.getReferenceProperty(nestedInterface);
      if(referenceProperty.equals(interfacReferenceProperty)) {
        removeRedefinedProperties(nestedInterface);
        EcoreUtil.delete(nestedInterface, true);
      }
    }
  }

  private void removeRedefinedProperties(Interface interfac) {
    EList<Property> allAttributes = interfac.getAllAttributes();
    for (Property property : allAttributes) {
      if(property.getRedefinedProperties().size() > 0) {
        Property redefinedProp = property.getRedefinedProperties().get(0);
        EcoreUtil.delete(redefinedProp);
      }
    }
  }

  private void createAssociations(List<AssociationData> excelAssociations) {
    Collection<Interface> interfaces =
        EcoreUtil.getObjectsByType(getModelPackageElements(), UMLPackage.Literals.INTERFACE);

    for (AssociationData associationData : excelAssociations) {
      createAssociation(interfaces, associationData);
    }
  }

  public void createAssociation(Collection<Interface> interfaces,
      AssociationData associationData) {
    Association associationInModel = getAssociationFromModel(associationData.getModelId());
    if (associationInModel != null) {
      updateValues(associationInModel, associationData);
      createSubInterface(associationInModel, associationData);
    } else {
      Interface start = getInterfaceByName(interfaces, associationData.getEnd1InterfaceName());
      Interface end = getInterfaceByName(interfaces, associationData.getEnd2InterfaceName());
      if (start == null) {
        warningMessageDialog("Missing association endpoint " + associationData.getEnd1PropertyName() + " at: " + associationData.getEnd1InterfaceName());
      }else if(end == null){
        warningMessageDialog("Missing association endpoint " + associationData.getEnd2PropertyName() + " at: " + associationData.getEnd2InterfaceName());
      }else{
        Association newAssociation = start.createAssociation(associationData.isEnd2IsNavigable(),
            associationData.getEnd2Aggregation(), associationData.getEnd2PropertyName(), associationData.getEnd2Lower(),
            associationData.getEnd2Upper(), end, associationData.isEnd1IsNavigable(), associationData.getEnd1Aggregation(),
            associationData.getEnd1PropertyName(), associationData.getEnd1Lower(), associationData.getEnd1Upper());

        newAssociation.createOwnedComment().setBody(associationData.getDescription());
        createSubInterface(newAssociation, associationData);
      }
    }
  }

  private void createSubInterface(Association association, AssociationData assocData) {
    if (assocData.getReferenceMap().isEmpty()) {
      return;
    }
    
    String referenceEntityName = assocData.getEnd2PropertyName();
    // Reference property
    Property referenceProperty = association.getMemberEnds().get(0);
    Interface parentInterface = (Interface) association.getMemberEnds().get(1).getType();
    
    // Look for nested interface with matching reference property
    Interface nestedInterface = null;
    List<Interface> nestedEntityReferenceInterfaces = EntityReferenceUtil.getNestedEntityReferenceInterfaces(parentInterface);
    for (Interface interfac : nestedEntityReferenceInterfaces) {
      Property interfacReferenceProperty = EntityReferenceUtil.getReferenceProperty(interfac);
      if(referenceProperty.equals(interfacReferenceProperty)) {
        nestedInterface = interfac;
      }
    }
    
    if(nestedInterface == null) {
      nestedInterface = UMLFactory.eINSTANCE.createInterface();
      nestedInterface.setName(referenceEntityName);
      parentInterface.getNestedClassifiers().add(nestedInterface);
      EntityReferenceUtil.setReferenceProperty(nestedInterface, referenceProperty);
      EntityReferenceUtil.setForeignKeyConstraintName(nestedInterface, assocData.getfKName());
    }


    for (Map.Entry<String, String> entry : assocData.getReferenceMap().entrySet()) {
      String redefinedName = entry.getKey();
      String subsettedName = entry.getValue();
      Property property = getOrCreatePropertyByName(nestedInterface, redefinedName);

      Property redefinedProperty = getPropertyByName(parentInterface, redefinedName);
      Interface endpointInterface = (Interface) association.getMemberEnds().get(0).getType();
      Property subsettedProperty = getSubsettedProperty(endpointInterface, subsettedName);

      if (subsettedProperty == null) {
        warningMessageDialog("Association missing property: " + subsettedName);
      } else if (redefinedProperty == null) {
        warningMessageDialog("Association missing property: " + redefinedName);
      } else {
        EList<Property> redefinedProperties = property.getRedefinedProperties();
        EList<Property> subsettedProperties = property.getSubsettedProperties();
        redefinedProperties.add(redefinedProperty);
        subsettedProperties.add(subsettedProperty);
      }
    }
  }

  private Property getSubsettedProperty(Interface endpointInterface, String subsettedName) {
    if(getPropertyByName(endpointInterface, subsettedName) != null) {
      return getPropertyByName(endpointInterface, subsettedName);
    }
    Interface nestedPkInterfac = UniqueIdentifierUtil.getNestedInterfaceWithStereotype(endpointInterface);
    return getPropertyByName(nestedPkInterfac, subsettedName);
  }

  private Property getOrCreatePropertyByName(Interface interfac, String name) {
    Property property = getPropertyByName(interfac, name);
    if (property != null) {
      return property;
    }
    property = UMLFactory.eINSTANCE.createProperty();
    property.setName(name);
    interfac.getOwnedAttributes().add(property);
    return property;
  }

  private Property getPropertyByName(Interface start, String end2Name) {
    EList<Property> allAttributes = start.getAllAttributes();
    for (Property property : allAttributes) {
      if (end2Name.equals(property.getName())) {
        return property;
      }
    }
    return null;
  }

  private void updateValues(Association associationInModel, AssociationData assoc) {
    Collection<Interface> interfaces =
        EcoreUtil.getObjectsByType(getModelPackageElements(), UMLPackage.Literals.INTERFACE);
    EList<Property> memberEnds = associationInModel.getMemberEnds();
    Property end2 = memberEnds.get(0);
    Property end1 = memberEnds.get(1);

    Interface end1Type = getInterfaceByName(interfaces, assoc.getEnd1InterfaceName());
    Interface end2Type = getInterfaceByName(interfaces, assoc.getEnd2InterfaceName());

    end1.setType(end1Type);
    end1.setName(assoc.getEnd1PropertyName());
    end1.setIsNavigable(assoc.isEnd1IsNavigable());
    end1.setAggregation(assoc.getEnd1Aggregation());
    end1.setLower(assoc.getEnd1Lower());
    end1.setUpper(assoc.getEnd1Upper());

    end2.setType(end2Type);
    end2.setName(assoc.getEnd2PropertyName());
    end2.setIsNavigable(assoc.isEnd2IsNavigable());
    end2.setAggregation(assoc.getEnd2Aggregation());
    end2.setLower(assoc.getEnd2Lower());
    end2.setUpper(assoc.getEnd2Upper());

    associationInModel.createOwnedComment().setBody(assoc.getDescription());
  }

  public Interface getInterfaceByName(Collection<Interface> interfaces, String interfaceName) {
    for (Interface interfac : interfaces) {
      if (interfac.getName().equals(interfaceName)) {
        return interfac;
      }
    }
    return null;
  }

  private List<AssociationData> getAssociationsFromExcel(Sheet associationSheet) {
    List<AssociationData> excelAssociations = new ArrayList<>();
    for (int i = 1; i <= associationSheet.getLastRowNum(); i++) {
      AssociationData associationData = new AssociationData();
      associationData
          .setModelId(CellReaderUtil.getStringValue(associationSheet.getRow(i).getCell(0)));
      // END1
      associationData
          .setEnd1InterfaceName(CellReaderUtil.getStringValue(associationSheet.getRow(i).getCell(1)));
      associationData
          .setEnd1PropertyName(CellReaderUtil.getStringValue(associationSheet.getRow(i).getCell(2)));

      boolean isNavigable1 =
          CellReaderUtil.getStringValue(associationSheet.getRow(i).getCell(3)).contains("yes")
              ? true
              : false;
      associationData.setEnd1IsNavigable(isNavigable1);

      AggregationKind aggregationKind1 =
          getAggregationKind(CellReaderUtil.getStringValue(associationSheet.getRow(i).getCell(4)));
      if (aggregationKind1 != null) {
        associationData.setEnd1Aggregation(aggregationKind1);
      }

      int lower1 = CellReaderUtil.getNumericValue(associationSheet.getRow(i).getCell(5));
      associationData.setEnd1Lower(lower1);
      int upper1 = CellReaderUtil.getNumericValue(associationSheet.getRow(i).getCell(6));
      associationData.setEnd1Upper(upper1);

      // END2
      associationData
          .setEnd2InterfaceName(CellReaderUtil.getStringValue(associationSheet.getRow(i).getCell(7)));
      associationData
          .setEnd2PropertyName(CellReaderUtil.getStringValue(associationSheet.getRow(i).getCell(8)));

      boolean isNavigable2 =
          CellReaderUtil.getStringValue(associationSheet.getRow(i).getCell(9)).contains("yes")
              ? true
              : false;
      associationData.setEnd2IsNavigable(isNavigable2);

      AggregationKind aggregationKind2 =
          getAggregationKind(CellReaderUtil.getStringValue(associationSheet.getRow(i).getCell(10)));
      if (aggregationKind2 != null) {
        associationData.setEnd2Aggregation(aggregationKind2);
      }

      int lower2 = CellReaderUtil.getNumericValue(associationSheet.getRow(i).getCell(11));
      associationData.setEnd2Lower(lower2);
      int upper2 = CellReaderUtil.getNumericValue(associationSheet.getRow(i).getCell(12));
      associationData.setEnd2Upper(upper2);

      associationData
          .setfKName(CellReaderUtil.getStringValue(associationSheet.getRow(i).getCell(13)));
      associationData
          .setDescription(CellReaderUtil.getStringValue(associationSheet.getRow(i).getCell(14)));

      Map<String, String> referenceMap = new LinkedHashMap<>();
      int maxColNum = associationSheet.getRow(i).getLastCellNum();
      for (int j = 15; j < maxColNum - 1; j += 2) {
        String redefined = CellReaderUtil.getStringValue(associationSheet.getRow(i).getCell(j));
        String subsetted = CellReaderUtil.getStringValue(associationSheet.getRow(i).getCell(j + 1));
        if (!redefined.isEmpty() && !subsetted.isEmpty()) {
          referenceMap.put(redefined, subsetted);
        }
      }
      associationData.setReferenceMap(referenceMap);

      excelAssociations.add(associationData);
    }
    return excelAssociations;
  }

  private AggregationKind getAggregationKind(String stringValue) {
    return AggregationKind.getByName(stringValue);
  }

  private Association getAssociationFromModel(String modelId) {
    if (modelId != null && !modelId.isEmpty()) {
      Collection<Association> associations = EcoreUtil
          .getObjectsByType(modelPackage.allOwnedElements(), UMLPackage.Literals.ASSOCIATION);
      for (Association assoc : associations) {
        if (modelId.equals(EcoreUtil.getURI(assoc).fragment())) {
          return assoc;
        }
      }
    }
    return null;
  }

  public EList<Element> getModelPackageElements() {
    return modelPackage.allOwnedElements();
  }
  
  public void warningMessageDialog(String message) {
    Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
    MessageDialog.openWarning(shell, "Warning", message);
  }

}
