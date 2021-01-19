package uml.papyrus.scriptgen;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.smartbit4all.tooling.emf.EclipseResourceUtil;

import SB4UMLProfile.reference;
import tooling.stereotype.ColumnUtil;
import tooling.stereotype.EntityDataTypeUtil;
import tooling.stereotype.IndexUtil;
import tooling.stereotype.TableUtil;
import tooling.stereotype.UniqueIdentifierUtil;


public class SqlScriptFactory {
  @Inject
  Logger logger;

  List<String> longKeywords = new ArrayList<>();
  int sqlLengthLimit;

  JavaCodeStringFormatter jcsf = new JavaCodeStringFormatter();

  public static final String SQL_ID_TYPE = "NUMBER";
  public static final String SQL_ID_TYPE_LENGTH = "38";
  public static final String SQL_VALUE_FOR_ID = SQL_ID_TYPE + "(" + SQL_ID_TYPE_LENGTH + ")";

  public static final String SQL_ENUM_TYPE = "VARCHAR2";
  public static final String SQL_ENUM_TYPE_LENGTH = "50";
  public static final String SQL_VALUE_FOR_ENUM = SQL_ENUM_TYPE + "(" + SQL_ENUM_TYPE_LENGTH + ")";

  public SqlScriptFactory(String sqlLengthLimit) {
    this.sqlLengthLimit = Integer.parseInt(sqlLengthLimit);
  }

  public void generateScripts(Collection<Interface> interfaces, String scriptFilesLoaction,
      String projectName, String license) {

    try {
      String tableInput = generateTableScriptFile(interfaces, scriptFilesLoaction, projectName, license);
      String indexInput = generateIndexScriptFile(interfaces, scriptFilesLoaction, projectName, license);
      String referenceInput =
          generateReferenceScriptFile(interfaces, scriptFilesLoaction, projectName, license);
      String installString = tableInput + indexInput + referenceInput;
      generateInstallScript(installString, scriptFilesLoaction, projectName);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void generateInstallScript(String installString, String scriptFilesLoaction,
      String projectName) throws IOException {

    try (InputStream targetStream = new ByteArrayInputStream(installString.toString().getBytes())) {
      EclipseResourceUtil.writeFile(scriptFilesLoaction, projectName + "_install.sql",
          targetStream);
    } catch (CoreException e) {
      logger.error(e, "Failed to write install script");
    }
  }

  private String generateReferenceScriptFile(Collection<Interface> interfaces,
      String scriptFilesLoaction, String projectName, String license) throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append(license);
    
    for (Interface interfac : interfaces) {
      if (!TableUtil.hasTableStereotype(interfac)) {
        continue;
      }
      EList<Property> allAttributes = interfac.allAttributes();
      if (!allAttributes.isEmpty()) {
        for (Property property : allAttributes) {
          if (isAssociation(property) && !isEnumeration(property)) {
            String constraint = getForeignKeyConstraintName(interfac, property);
            if (isTooLong(constraint)) {
              constraint = shorten(constraint);
              if (isTooLong(constraint)) {
                longKeywords.add(constraint);
              }
            }

            String assocPropertyInterfaceDDlName = getAssocPropertyInterfaceDDlName(property);
            String foreignKey = foreignKey(property).toUpperCase();
            sb.append("ALTER TABLE " + TableUtil.getInterfaceDDLName(interfac) + " ADD CONSTRAINT "
                + constraint + " FOREIGN KEY ("
                + NamingUtil.getGeneratedId(property.getName().toUpperCase()) + ")" + " REFERENCES "
                + assocPropertyInterfaceDDlName + " (" + foreignKey + ");"
                + jcsf.appendLineSeparator());
          }
        }
        sb.append(jcsf.appendLineSeparator());
      }

    }

    try (InputStream targetStream = new ByteArrayInputStream(sb.toString().getBytes())) {
      EclipseResourceUtil.writeFile(scriptFilesLoaction, projectName + "_ref.sql", targetStream);
    } catch (CoreException e) {
      logger.error(e, "Failed to write file");
    }
    return sb.toString();
  }

  private String generateIndexScriptFile(Collection<Interface> interfaces,
      String scriptFilesLoaction, 
      String projectName,
      String license) throws IOException {
    
    StringBuilder sb = new StringBuilder();
    sb.append(license);
    for (Interface interfac : interfaces) {
      if (!TableUtil.hasTableStereotype(interfac)) {
        continue;
      }

      EList<Property> allAttributes = interfac.allAttributes();
      if (!allAttributes.isEmpty()) {
        if(IndexUtil.hasPropertyWithStereotype(interfac)) {
          List<Interface> nestedInterfaces = IndexUtil.getNestedInterfaces(interfac);
          sb.append(generateIndexFromStereotype(interfac, false));
          for(Interface iface : nestedInterfaces) {
            sb.append(generateIndexFromStereotype(iface, true));
          }
          sb.append(jcsf.appendLineSeparator());
        }else {
          sb.append(generateIndex(interfac, allAttributes));
        }
      }
    }

    try (InputStream targetStream = new ByteArrayInputStream(sb.toString().getBytes())) {
      EclipseResourceUtil.writeFile(scriptFilesLoaction, projectName + "_idx.sql", targetStream);
    } catch (CoreException e) {
      logger.error(e, "Failed to write file");
    }
    return sb.toString();
  }

  private String generateIndexFromStereotype(Interface interfac, boolean nested) {
    StringBuilder sb = new StringBuilder();
    EList<Property> allAttributes = interfac.allAttributes();
    
    for (Property property : allAttributes) {
      if(IndexUtil.hasStereotype(property)) {
        String indexName = IndexUtil.getPropertyDDLName(property);
        String indexColumn = property.getName().toUpperCase();
        String interfaceName;
        if(nested) {
          Interface parentInterface = (Interface) interfac.eContainer();
          interfaceName = TableUtil.getInterfaceDDLName(parentInterface);
        }else {
          interfaceName = TableUtil.getInterfaceDDLName(interfac);
        }
        boolean isUnique = IndexUtil.getIsUnique(property);
        String unique = isUnique ? "UNIQUE " : "";
        
        sb.append("CREATE " + unique + "INDEX " + indexName + " ON " + interfaceName + 
            " (" + indexColumn + ");" + jcsf.appendLineSeparator());
      }
    }
    return sb.toString();
  }

  private String generateIndex(Interface interfac,
      EList<Property> allAttributes) {
    StringBuilder sb = new StringBuilder();
    EList<Constraint> ownedRules = interfac.getOwnedRules();
    
    for (Property property : allAttributes) {
      if (isAssociation(property) && !isEnumeration(property)) {
        String index = limitIndexLength(interfac, property);

        sb.append("CREATE INDEX " + index + " ON " + TableUtil.getInterfaceDDLName(interfac) + " ("
            + NamingUtil.getGeneratedId(property.getName().toUpperCase()) + ");"
            + jcsf.appendLineSeparator());
      }
    }

    for (Constraint cons : ownedRules) {
      if (cons.getSpecification().stringValue().startsWith("isUnique(")) {
        String propertyName = cons.getSpecification().stringValue().substring(9,
            cons.getSpecification().stringValue().length() - 1);
        sb.append(uniqueIndex(interfac.getName(), cons.getName().toUpperCase(), propertyName));
      }
    }
    sb.append(jcsf.appendLineSeparator());
    return sb.toString();
  }

  private String limitIndexLength(Interface interfac, Property property) {
    String index = TableUtil.getInterfaceDDLName(interfac) + "_"
        + NamingUtil.getGeneratedId(property.getName().toUpperCase()) + "_IDX";

    if (isTooLong(index)) {
      index = shorten(interfac.getName()) + "_"
          + shorten(NamingUtil.getGeneratedId(property.getName())).toUpperCase() + "_IDX";
      if (isTooLong(index)) {
        longKeywords.add(index);
      }
    }
    return index;
  }

  private String uniqueIndex(String tableName, String consName, String propertyName) {
    String uIndexName = tableName + "_" + consName + "_IDX";
    if (isTooLong(uIndexName)) {
      uIndexName = shorten(tableName) + "_" + shorten(consName) + "_IDX";
      if (isTooLong(uIndexName)) {
        longKeywords.add(uIndexName);
      }
    }
    String uniqueIndex = "CREATE UNIQUE INDEX " + uIndexName + " ON " + tableName + " ("
        + propertyName + ");" + jcsf.appendLineSeparator();
    return uniqueIndex;
  }

  private String generateTableScriptFile(Collection<Interface> interfaces,
      String scriptFilesLoaction, String projectName, String license) throws IOException {

    StringBuilder sb = new StringBuilder();
    sb.append(license);
    for (Interface interfac : interfaces) {
      if (TableUtil.hasTableStereotype(interfac)) {
        sb.append(createTableScript(interfac));
      }
    }
    try (InputStream targetStream = new ByteArrayInputStream(sb.toString().getBytes())) {
      EclipseResourceUtil.writeFile(scriptFilesLoaction, projectName + "_table.sql", targetStream);
    } catch (CoreException e) {
      logger.error(e, "Failed to write file");
    }
    return sb.toString();
  }

  private String createTableScript(Interface interfac) {
    String createTable = "CREATE TABLE " + TableUtil.getInterfaceDDLName(interfac) + " ("
        + jcsf.appendLineSeparator() + defineProperties(interfac) + ");"
        + jcsf.appendLineSeparator();
    String alterTable = alterTableAddPrimaryKey(interfac) + jcsf.appendLineSeparator(2);

    String completeFile = createTable + alterTable;
    return completeFile;
  }



  private String defineProperties(Interface interfac) {
    StringBuilder sb = new StringBuilder();
    EList<Property> allAttributes = interfac.allAttributes();
    for (Property property : allAttributes) {
      if (!isAssociation(property)) {
          String name = ColumnUtil.getPropertyDDLName(property);
          if(name == null) {
            property.getName();
          }
        sb.append(jcsf.appendTab()
            + defineTableColumn(name,
                EntityDataTypeUtil.sqlByCustomType(property),
                ColumnUtil.getPropertyDDLDefaultValue(property), nullable(property))
            + jcsf.appendLineSeparator());

      } else if (isEnumeration(property)) {

        sb.append(jcsf.appendTab()
            + defineTableColumn(property.getName(), SQL_VALUE_FOR_ENUM,
                ColumnUtil.getPropertyDDLDefaultValue(property), nullable(property))
            + jcsf.appendLineSeparator());

      } else if (isAssociation(property)) {
        String idDDLType = geReferencePropertyType(property);
        sb.append(jcsf.appendTab()
            + defineTableColumn(NamingUtil.getGeneratedId(property.getName()), idDDLType,
                ColumnUtil.getPropertyDDLDefaultValue(property), nullable(property))
            + jcsf.appendLineSeparator());
      }
    }
    if (!UniqueIdentifierUtil.hasExplicitPrimaryKey(interfac)
        && !UniqueIdentifierUtil.hasNestedInterfaceWithStereotype(interfac)) {
      sb.append(jcsf.appendTab() + "\"" + NamingUtil.getGeneratedId(interfac).toUpperCase() + "\" "
          + SQL_VALUE_FOR_ID + " NOT NULL");
    }
    return sb.toString();
  }

  private String geReferencePropertyType(Property property) {
    Type type = property.getType();
    if (type instanceof Interface) {
      Interface interfac = (Interface) type;
      if (UniqueIdentifierUtil.hasExplicitPrimaryKey(interfac)) {
        Property primaryKey = UniqueIdentifierUtil.getExplicitPrimaryKey(interfac);
        return EntityDataTypeUtil.sqlByCustomType(primaryKey);
      }
    }
    return SQL_VALUE_FOR_ID;
  }

  private String alterTableAddPrimaryKey(Interface interfac) {
    List<String> pkComponentList = UniqueIdentifierUtil.getPrimaryKeyColumnNames(interfac);
    if(pkComponentList.isEmpty()) {
      pkComponentList.add(interfac.getName());
    }
    String pkComponents = UniqueIdentifierUtil.listToString(pkComponentList).toUpperCase();
    String primaryKeyConstraintName = UniqueIdentifierUtil.getPrimaryKeyConstraintName(interfac);

    String alterTableCommand = "ALTER TABLE " + TableUtil.getInterfaceDDLName(interfac)
        + " ADD CONSTRAINT " + primaryKeyConstraintName + " PRIMARY KEY (" + pkComponents + ");";
    return alterTableCommand;
  }

  private String defineTableColumn(String name, String type, String defaultValue, String nullable) {
    if (isTooLong(name)) {
      name = shorten(name);
      if (isTooLong(name)) {
        longKeywords.add(name);
      }
    }
    String tableColumn = "\"" + name.toUpperCase() + "\" " + type + defaultValue + nullable;
    return tableColumn;
  }

  private boolean isTooLong(String name) {
    if (name.length() <= sqlLengthLimit) {
      return false;
    }
    return true;
  }

  private String nullable(Property property) {
    if (isMandatory(property)) {
      return " NOT NULL,";
    } else {
      return " NULL,";
    }
  }

  public static boolean isMandatory(Property property) {
    return property.getLower() == 1;
  }

  public static boolean isEnumeration(Property property) {
    return property.getType() instanceof Enumeration;
  }

  public List<String> getLongKeywords() {
    return longKeywords;
  }

  public static boolean isAssociation(Property property) {
    return property.getAssociation() != null;
  }

  private String shorten(String name) {
    char[] charArray = name.toCharArray();
    charArray[0] = Character.toUpperCase(charArray[0]);
    int lowerCase = 0;
    for (int i = 0; i < charArray.length; i++) {
      if (Character.valueOf(charArray[i]).equals('_')) {
        lowerCase = 0;

        continue;
      }
      if (Character.isUpperCase(charArray[i])) {
        lowerCase = 0;
      }
      if (Character.isLowerCase(charArray[i])) {
        lowerCase++;
        if (lowerCase > 2) {
          charArray[i] = '#';
        }
      }
    }
    String newName = new String(charArray);
    return newName.replaceAll("#", "");
  }

  private String getForeignKeyConstraintName(Interface interfac, Property property) {
    Association association = property.getAssociation();
    for (EObject object : association.getStereotypeApplications()) {
      if (object instanceof reference) {
        reference assoc = (reference) object;
        String foreignKeyConstraintName = assoc.getForeignKeyConstraintName();
        if (foreignKeyConstraintName != null && !foreignKeyConstraintName.isEmpty()) {
          return foreignKeyConstraintName;
        }
      }
    }
    return "REF_" + interfac.getName() + "_" + property.getName();
  }

  public String getAssocPropertyInterfaceDDlName(Property property) {
    Type type = property.getType();
    if (type instanceof Interface) {
      Interface interf = (Interface) type;
      return TableUtil.getInterfaceDDLName(interf);
    }
    return property.getType().getName();
  }


  private String foreignKey(Property property) {
    Type type = property.getType();
    if (type instanceof Interface) {
      Interface interfac = (Interface) type;
      if (UniqueIdentifierUtil.hasExplicitPrimaryKey(interfac)) {
        return UniqueIdentifierUtil.getExplicitPrimaryKey(interfac).getName();
      }
    }
    return NamingUtil.getGeneratedId(property.getType().getName()).toUpperCase();
  }

}
