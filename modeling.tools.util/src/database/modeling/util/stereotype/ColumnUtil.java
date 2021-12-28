package database.modeling.util.stereotype;

import org.eclipse.uml2.uml.Property;

/**
 * {@link Colum} sztereotípus és elemeinek kezelését vézi.
 * 
 * @author Horváth Tibor
 *
 */
public class ColumnUtil {
	private static final String SCALE = "scale";
	private static final String PRECISION = "precision";
	private static final String LENGTH = "length";
	private static final String DATA_TYPE = "dataType";
	private static final String DEFAULT_VALUE = "defaultValue";
	private static final String NULLABLE = "nullable";
	private static final String UNIQUE = "unique";

	public final static String STEREOTYPE_QUALIFIED_NAME = "DatabaseModeling::Column";

	public static String getDataType(Property property) {
		return StereotypeManagementUtil.getStringAttributeValue(STEREOTYPE_QUALIFIED_NAME, property, DATA_TYPE);
	}

	public static void setDataType(Property property, String data) {
		StereotypeManagementUtil.setStringValue(STEREOTYPE_QUALIFIED_NAME, DATA_TYPE, property, data);
	}

	public static String getDefaultValue(Property property) {
		return StereotypeManagementUtil.getStringAttributeValue(STEREOTYPE_QUALIFIED_NAME, property, DEFAULT_VALUE);
	}

	public static void setDefaultValue(Property property, String data) {
		StereotypeManagementUtil.setStringValue(STEREOTYPE_QUALIFIED_NAME, DEFAULT_VALUE, property, data);
	}

	public static String getLength(Property property) {
		return StereotypeManagementUtil.getStringAttributeValue(STEREOTYPE_QUALIFIED_NAME, property, LENGTH);
	}

	public static void setLength(Property property, String data) {
		StereotypeManagementUtil.setStringValue(STEREOTYPE_QUALIFIED_NAME, LENGTH, property, data);
	}

	public static String getScale(Property property) {
		return StereotypeManagementUtil.getStringAttributeValue(STEREOTYPE_QUALIFIED_NAME, property, SCALE);
	}

	public static void setScale(Property property, String data) {
		StereotypeManagementUtil.setStringValue(STEREOTYPE_QUALIFIED_NAME, SCALE, property, data);
	}

	public static String getPrecision(Property property) {
		return StereotypeManagementUtil.getStringAttributeValue(STEREOTYPE_QUALIFIED_NAME, property, PRECISION);
	}

	public static void setPrecision(Property property, String data) {
		StereotypeManagementUtil.setStringValue(STEREOTYPE_QUALIFIED_NAME, PRECISION, property, data);
	}

	public static boolean getUnique(Property property) {
		return StereotypeManagementUtil.getBooleanAttributeValue(STEREOTYPE_QUALIFIED_NAME, property, UNIQUE);
	}

	public static void setUnique(Property property, boolean data) {
		StereotypeManagementUtil.setBooleanValue(STEREOTYPE_QUALIFIED_NAME, UNIQUE, property, data);
	}

	public static boolean getNullable(Property property) {
		return StereotypeManagementUtil.getBooleanAttributeValue(STEREOTYPE_QUALIFIED_NAME, property, NULLABLE);
	}

	public static void setNullable(Property property, boolean data) {
		StereotypeManagementUtil.setBooleanValue(STEREOTYPE_QUALIFIED_NAME, NULLABLE, property, data);
	}

}
