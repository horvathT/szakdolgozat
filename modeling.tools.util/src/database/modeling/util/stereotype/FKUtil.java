package database.modeling.util.stereotype;

import org.eclipse.uml2.uml.Property;

public class FKUtil {
	private static final String REFERENCED_PROPERTY = "referencedProperty";
	private static final String REFERENCED_ENTITY = "referencedEntity";
	private final static String FOREIGN_KEY_CONSTRAINT = "foreignKeyConstraint";

	public final static String STEREOTYPE_QUALIFIED_NAME = "DatabaseModeling::FK";

	public static String getConstraintName(Property property) {
		return StereotypeManagementUtil.getStringAttributeValue(STEREOTYPE_QUALIFIED_NAME, property,
				FOREIGN_KEY_CONSTRAINT);
	}

	public static void setConstraintName(Property property, String data) {
		StereotypeManagementUtil.setStringValue(STEREOTYPE_QUALIFIED_NAME, FOREIGN_KEY_CONSTRAINT, property, data);
	}

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
