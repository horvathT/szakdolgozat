package database.modeling.util;

import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;

public class ColumnUtil {
	private static final String SCALE = "scale";
	private static final String PRECISION = "precision";
	private static final String LENGTH = "length";
	private static final String DATA_TYPE = "dataType";
	private static final String DEFAULT_VALUE = "defaultValue";
	private final static String STEREOTYPE_QUALIFIED_NAME = "DatabaseModeling::Column";

	public static boolean hasStereotype(Property property) {
		if (property == null) {
			return false;
		}

		Stereotype applicableStereotype = property.getApplicableStereotype(STEREOTYPE_QUALIFIED_NAME);
		if (property.isStereotypeApplied(applicableStereotype)) {
			return true;
		}
		return false;
	}

	public static void applyStereotype(Property property) {
		if (property == null) {
			return;
		}
		Stereotype applicableStereotype = property.getApplicableStereotype(STEREOTYPE_QUALIFIED_NAME);
		if (property.isStereotypeApplied(applicableStereotype)) {
		} else {
			if (applicableStereotype != null) {
				property.applyStereotype(applicableStereotype);
			}
		}
	}

	public static String getDataType(Property property) {
		return getStringAttributeValue(property, DATA_TYPE);
	}

	public static void setDataType(Property property, String data) {
		setValue(STEREOTYPE_QUALIFIED_NAME, DATA_TYPE, property, data);
	}

	public static String getDefaultValue(Property property) {
		return getStringAttributeValue(property, DEFAULT_VALUE);
	}

	public static void setDefaultValue(Property property, String data) {
		setValue(STEREOTYPE_QUALIFIED_NAME, DEFAULT_VALUE, property, data);
	}

	public static String getLength(Property property) {
		return getStringAttributeValue(property, LENGTH);
	}

	public static void setLength(Property property, String data) {
		setValue(STEREOTYPE_QUALIFIED_NAME, LENGTH, property, data);
	}

	public static String getScale(Property property) {
		return getStringAttributeValue(property, SCALE);
	}

	public static void setScale(Property property, String data) {
		setValue(STEREOTYPE_QUALIFIED_NAME, SCALE, property, data);
	}

	public static String getPrecision(Property property) {
		return getStringAttributeValue(property, PRECISION);
	}

	public static void setPrecision(Property property, String data) {
		setValue(STEREOTYPE_QUALIFIED_NAME, PRECISION, property, data);
	}

	private static void setValue(String stereotypeName, String propertyName, Property property, String data) {
		if (data != null && property != null) {
			Stereotype applicableStereotype = property.getApplicableStereotype(stereotypeName);
			if (hasStereotype(property)) {
				property.setValue(applicableStereotype, propertyName, data);
			} else {
				if (applicableStereotype != null) {
					applyStereotype(property);
					property.setValue(applicableStereotype, propertyName, data);
				}
			}
		}
	}
	
	private static String getStringAttributeValue(Property property, String attributeName) {
		Stereotype applicableStereotype = property.getApplicableStereotype(STEREOTYPE_QUALIFIED_NAME);
		if (property.isStereotypeApplied(applicableStereotype)) {
			 String value = (String) property.getValue(applicableStereotype, attributeName);
			 if(value != null){
				 return value;
			 }
		}
		return "";
	}
}
