package database.modeling.util;

import org.eclipse.uml2.uml.Property;

public class ColumnUtil {
	private static final String SCALE = "scale";
	private static final String PRECISION = "precision";
	private static final String LENGTH = "length";
	private static final String DATA_TYPE = "dataType";
	private static final String DEFAULT_VALUE = "defaultValue";

	public final static String STEREOTYPE_QUALIFIED_NAME = "DatabaseModeling::Column";

	public static String getDataType(Property property) {
		return StereotypeApplicationUtil.getStringAttributeValue(STEREOTYPE_QUALIFIED_NAME, property, DATA_TYPE);
	}

	public static void setDataType(Property property, String data) {
		StereotypeApplicationUtil.setValue(STEREOTYPE_QUALIFIED_NAME, DATA_TYPE, property, data);
	}

	public static String getDefaultValue(Property property) {
		return StereotypeApplicationUtil.getStringAttributeValue(STEREOTYPE_QUALIFIED_NAME, property, DEFAULT_VALUE);
	}

	public static void setDefaultValue(Property property, String data) {
		StereotypeApplicationUtil.setValue(STEREOTYPE_QUALIFIED_NAME, DEFAULT_VALUE, property, data);
	}

	public static String getLength(Property property) {
		return StereotypeApplicationUtil.getStringAttributeValue(STEREOTYPE_QUALIFIED_NAME, property, LENGTH);
	}

	public static void setLength(Property property, String data) {
		StereotypeApplicationUtil.setValue(STEREOTYPE_QUALIFIED_NAME, LENGTH, property, data);
	}

	public static String getScale(Property property) {
		return StereotypeApplicationUtil.getStringAttributeValue(STEREOTYPE_QUALIFIED_NAME, property, SCALE);
	}

	public static void setScale(Property property, String data) {
		StereotypeApplicationUtil.setValue(STEREOTYPE_QUALIFIED_NAME, SCALE, property, data);
	}

	public static String getPrecision(Property property) {
		return StereotypeApplicationUtil.getStringAttributeValue(STEREOTYPE_QUALIFIED_NAME, property, PRECISION);
	}

	public static void setPrecision(Property property, String data) {
		StereotypeApplicationUtil.setValue(STEREOTYPE_QUALIFIED_NAME, PRECISION, property, data);
	}

}
