package database.modeling.util.stereotype;

import org.eclipse.uml2.uml.Property;

public class FKUtil {
	private static final String REFERENCED_PROPERTY = "referencedProperty";
	private static final String REFERENCED_ENTITY = "referencedEntity";

	public final static String STEREOTYPE_QUALIFIED_NAME = "DatabaseModeling::FK";

	public static String getReferencedEntity(Property property) {
		return StereotypeManagementUtil.getStringAttributeValue(STEREOTYPE_QUALIFIED_NAME, property,
				REFERENCED_ENTITY);
	}

	public static void setReferencedEntity(Property property, String data) {
		StereotypeManagementUtil.setStringValue(STEREOTYPE_QUALIFIED_NAME, REFERENCED_ENTITY, property, data);
	}

	public static String getReferencedProperty(Property property) {
		return StereotypeManagementUtil.getStringAttributeValue(STEREOTYPE_QUALIFIED_NAME, property,
				REFERENCED_PROPERTY);
	}

	public static void setReferencedProperty(Property property, String data) {
		StereotypeManagementUtil.setStringValue(STEREOTYPE_QUALIFIED_NAME, REFERENCED_PROPERTY, property, data);
	}

}
