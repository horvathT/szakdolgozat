package uml.excel.exporter.excelutils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Property;
import tooling.stereotype.EntityDataTypeUtil;
import tooling.stereotype.EntityReferenceUtil;
import tooling.stereotype.TableUtil;
import tooling.stereotype.UniqueIdentifierUtil;
import uml.excel.exporter.CellAppender;
import uml.excel.exporter.PropertyRow;

public class SheetFactory {

  private final String DATA_TYPE_SHEET_NAME = "DataTypes";
  private final String ASSOCIATION_SHEET_NAME = "Associations";
  private final String ENTITIES_SHEET_NAME = "Entities";

  public List<Sheet> createEntityPropertySheets(Workbook workbook,
      Collection<Interface> interfaces) {
    List<Sheet> sheets = new ArrayList<>();
    for (Interface interfac : interfaces) {
      if (!nestedInterface(interfac)) {
        sheets.add(createEntityPropertySheet(interfac, workbook));
      }
    }
    return sheets;
  }

  public Sheet createEntityPropertySheet(Interface interfac, Workbook wb) {
    Sheet sheet = wb.createSheet(interfac.getName());
    createPropertyHeaderRow(sheet);
    fillPropertyRows(sheet, interfac);
    return sheet;
  }

  private void fillPropertyRows(Sheet sheet, Interface interfac) {
    EList<Property> allAttributes = interfac.allAttributes();
    List<Property> allProperties = new ArrayList<Property>();
    allProperties.addAll(allAttributes);

    int rowNumber = 1;
    for (Property property : allProperties) {
      if (property.getAssociation() != null) {
        // TO-DO save associations
      } else {
        Row row = sheet.createRow(rowNumber);
        PropertyRow propertyRow = new PropertyRow(property);
        propertyRow.createRow(row);
        ++rowNumber;
      }
    }
  }

  public Sheet createDataTypeSheet(Workbook workbook, Collection<DataType> dataTypes) {
    Sheet sheet = workbook.createSheet(DATA_TYPE_SHEET_NAME);
    createDataTypeHeaderRow(sheet);
    fillDataTypeRows(sheet, dataTypes);
    return sheet;
  }

  private void fillDataTypeRows(Sheet sheet, Collection<DataType> dataTypes) {
    int i = 1;
    for (DataType dataType : dataTypes) {
      Row dataRow = sheet.createRow(i);
      fillRow(dataRow, dataType);
      ++i;
    }
  }

  private void fillRow(Row dataRow, DataType dataType) {
    CellAppender row = new CellAppender(dataRow);
    row.appendCellWithValue(dataType.getName())
        .appendCellWithValue(EntityDataTypeUtil.getSqlType(dataType))
        .appendCellWithValue(EntityDataTypeUtil.getJavaType(dataType))
        .appendCellWithValue(EntityDataTypeUtil.getJavaImport(dataType))
        .appendCellWithValue(EntityDataTypeUtil.getYamlType(dataType))
        .appendCellWithValue(EntityDataTypeUtil.getYamlFormat(dataType));
  }

  public Sheet createAssociationSheet(Workbook workbook, Collection<Association> associations) {
    Sheet associationSheet = workbook.createSheet(ASSOCIATION_SHEET_NAME);
    createAssociationHeaderRow(associationSheet);
    fillAssociationRows(associationSheet, associations);
    return associationSheet;
  }

  private void fillAssociationRows(Sheet sheet, Collection<Association> associations) {
    int rowNumber = 1;
    for (Association association : associations) {
      Row row = sheet.createRow(rowNumber);
      fillAssociationRow(row, association);
      ++rowNumber;
    }
  }

  private void fillAssociationRow(Row row, Association association) {
    EList<Property> memberEnds = association.getMemberEnds();
    Property otherEnd = memberEnds.get(0);
    Property ownEnd = memberEnds.get(1);
    Interface interfac = (Interface) ownEnd.getType();

    CellAppender assocRow = new CellAppender(row);
    assocRow.appendCellWithValue(EcoreUtil.getURI(association).fragment())

        .appendCellWithValue(ownEnd.getType().getName()).appendCellWithValue(ownEnd.getName())
        .appendCellWithValue(ownEnd.isNavigable() ? "yes" : "no")
        .appendCellWithValue(ownEnd.getAggregation().toString())
        .appendCellWithValue(ownEnd.getLower()).appendCellWithValue(ownEnd.getUpper())

        .appendCellWithValue(otherEnd.getType().getName()).appendCellWithValue(otherEnd.getName())
        .appendCellWithValue(otherEnd.isNavigable() ? "yes" : "no")
        .appendCellWithValue(otherEnd.getAggregation().toString())
        .appendCellWithValue(otherEnd.getLower()).appendCellWithValue(otherEnd.getUpper())

        .appendCellWithValue(EntityReferenceUtil
            .getNestedEntityReferenceForeignKeyConstraint(interfac, otherEnd.getName()))
        .appendCellWithValue(getComment(association));


    List<Interface> subInterfaces = getNestedInterfaces(interfac);

    for (Interface ifac : subInterfaces) {
      if (EntityReferenceUtil.hasStereotype(ifac)
          && otherEnd.equals(EntityReferenceUtil.getReferenceProperty(ifac))) {
        EList<Property> allOwnedElements = ifac.allAttributes();
        for (Property prop : allOwnedElements) {
          EList<Property> redefinedProperties = prop.getRedefinedProperties();
          EList<Property> subsettedProperties = prop.getSubsettedProperties();
          if (redefinedProperties.size() == subsettedProperties.size()) {
            for (int i = 0; i < redefinedProperties.size(); i++) {
              assocRow.appendCellWithValue(redefinedProperties.get(i).getName())
                  .appendCellWithValue(subsettedProperties.get(i).getName());
            }
          } else {
            warningMessageDialog(
                "Entity reference " + ifac.getName() + "." + prop.getName() + " incorrectly set.");
          }
        }
      }
    }
  }

  private List<Interface> getNestedInterfaces(Interface interface1) {
    EList<Classifier> nestedClassifiers = interface1.getNestedClassifiers();
    List<Interface> subInterfaces = new ArrayList<Interface>();
    for (Classifier classifier : nestedClassifiers) {
      if (classifier instanceof Interface) {
        Interface interfac = (Interface) classifier;
        subInterfaces.add(interfac);
      }
    }
    return subInterfaces;
  }

  private String getComment(Association association) {
    if (association.getOwnedComments().size() > 0) {
      return association.getOwnedComments().get(0).getBody();
    }
    return "";
  }

  private void createPropertyHeaderRow(Sheet sheet) {
    CellAppender row = new CellAppender(sheet.createRow(0));
    row.appendCellWithValue("Model ID").appendCellWithValue("Name").appendCellWithValue("Label")
        .appendCellWithValue("DDL Name").appendCellWithValue("Typename")
        .appendCellWithValue("Database Type").appendCellWithValue("Description")
        .appendCellWithValue("Nullable").appendCellWithValue("Primary Key");
  }

  private void createDataTypeHeaderRow(Sheet sheet) {
    CellAppender row = new CellAppender(sheet.createRow(0));
    row.appendCellWithValue("TypeName").appendCellWithValue("SQL Type")
        .appendCellWithValue("Java Type").appendCellWithValue("Java Import")
        .appendCellWithValue("Yaml Type").appendCellWithValue("Yaml Format");
  }

  private void createAssociationHeaderRow(Sheet sheet) {
    CellAppender row = new CellAppender(sheet.createRow(0));
    row.appendCellWithValue("Model ID")

        .appendCellWithValue("Interface").appendCellWithValue("Property")
        .appendCellWithValue("Navigable").appendCellWithValue("Aggregation Type")
        .appendCellWithValue("Lower").appendCellWithValue("Upper")

        .appendCellWithValue("Interface").appendCellWithValue("Property")
        .appendCellWithValue("Navigable").appendCellWithValue("Aggregation Type")
        .appendCellWithValue("Lower").appendCellWithValue("Upper").appendCellWithValue("FK Name")
        .appendCellWithValue("Description")

        .appendCellWithValue("Redefined1").appendCellWithValue("Subsetted1")
        .appendCellWithValue("Redefined2").appendCellWithValue("Subsetted2");


  }

  private void createEntityHeaderRow(Sheet sheet) {
    CellAppender row = new CellAppender(sheet.createRow(0));
    row.appendCellWithValue("Model ID").appendCellWithValue("Name").appendCellWithValue("Label")
        .appendCellWithValue("DDL Name").appendCellWithValue("Primary key constraint name")
        .appendCellWithValue("Description");
  }

  public void createEntitiyUtilSheet(Workbook workbook, Collection<Interface> interfaces) {
    Sheet entityUtil = workbook.createSheet(ENTITIES_SHEET_NAME);
    createEntityHeaderRow(entityUtil);

    int rowNumber = 1;
    for (Interface interfac : interfaces) {
      if (!nestedInterface(interfac)) {
        createEntityRow(interfac, rowNumber, entityUtil);
        ++rowNumber;
      }
    }
  }

  private boolean nestedInterface(Interface interfac) {
    Element owner = interfac.getOwner();
    if (owner instanceof Interface) {
      return true;
    }
    if (EntityReferenceUtil.hasStereotype(interfac)
        || UniqueIdentifierUtil.hasStereotype(interfac)) {
      return true;
    }
    return false;
  }

  private void createEntityRow(Interface interfac, int rowNumber, Sheet sheet) {

    CellAppender row = new CellAppender(sheet.createRow(rowNumber));
    row.appendCellWithValue(EcoreUtil.getURI(interfac).fragment())
        .appendCellWithValue(interfac.getName()).appendCellWithValue(interfac.getLabel())
        .appendCellWithValue(TableUtil.getInterfaceDDLName(interfac))
        .appendCellWithValue(UniqueIdentifierUtil.getPrimaryKeyConstraintName(interfac))
        .appendCellWithValue(getFirstComment(interfac));
  }

  private String getFirstComment(Interface interfac) {
    if (interfac.getOwnedComments().size() > 0) {
      return interfac.getOwnedComments().get(0).getBody();
    }
    return "";
  }

  public void warningMessageDialog(String message) {
    Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
    MessageDialog.openWarning(shell, "Warning", message);
  }
}
