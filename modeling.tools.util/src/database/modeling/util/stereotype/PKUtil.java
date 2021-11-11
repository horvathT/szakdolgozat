package database.modeling.util.stereotype;

import org.eclipse.uml2.uml.Property;

public class PKUtil {
	private final static String PRIMARY_KEY_CONSTRAINT = "primaryKeyConstraint";

	public final static String STEREOTYPE_QUALIFIED_NAME = "DatabaseModeling::PK";

	public static String getConstraintName(Property property) {
		return StereotypeApplicationUtil.getStringAttributeValue(STEREOTYPE_QUALIFIED_NAME, property,
				PRIMARY_KEY_CONSTRAINT);
	}

	public static void setConstraintName(Property property, String data) {
		StereotypeApplicationUtil.setValue(STEREOTYPE_QUALIFIED_NAME, PRIMARY_KEY_CONSTRAINT, property, data);
	}

}
