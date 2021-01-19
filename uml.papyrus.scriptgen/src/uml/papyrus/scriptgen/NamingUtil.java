package uml.papyrus.scriptgen;

import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

public class NamingUtil {

  public static String getInterfaceDefinitionNameForProperty(Property property) {
    return getEntityDefinitionInterfaceName(property.getType());
  }

  public static String getInterfaceNameForProperty(Property property) {
    return getEntityInterfaceName(property.getType());
  }

  public static String getEntityDefinitionInterfaceName(Type interfaceType) {
    return interfaceType.getName() + "Def";
  }

  public static String getEntityDefinitionInterfaceName(String interfaceName) {
    return interfaceName + "Def";
  }

  public static String getGeneratedId(Type interfaceType) {
    return interfaceType.getName() + "_ID";
  }

  public static String getGeneratedId(String interfaceName) {
    return interfaceName + "_ID";
  }

  public static String getEntityInterfaceName(Type interfaceType) {
    return interfaceType.getName();
  }

  public static String append_Col(String data) {
    return data + "_COL";
  }

  public static String foreignKeyMethodName(String data) {
    return data + "Id";
  }

  public static String foreignKeyMethodName(Property property) {
    return property.getName() + "Id";
  }
  
  public static String toCamelCase(String text) {
    return text.substring(0, 1).toLowerCase() + text.substring(1);
  }

}
