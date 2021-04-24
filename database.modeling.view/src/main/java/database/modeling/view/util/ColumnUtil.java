package database.modeling.view.util;

import DatabaseModeling.Column;
import org.eclipse.emf.ecore.EObject;
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
		for (EObject object : property.getStereotypeApplications()) {
			if (object instanceof Column) {
				Column prop = (Column) object;
				String value = prop.getDataType();
				if (value != null && !value.isEmpty()) {
					return value;
				}
			}
		}
		return null;
	}

	public static void setDataType(Property property, String data) {
		setValue(STEREOTYPE_QUALIFIED_NAME, DATA_TYPE, property, data);
	}

	public static String getDefaultValue(Property property) {
		for (EObject object : property.getStereotypeApplications()) {
			if (object instanceof Column) {
				Column prop = (Column) object;
				String value = prop.getDefaultValue();
				if (value != null && !value.isEmpty()) {
					return value;
				}
			}
		}
		return null;
	}

	public static void setDefaultValue(Property property, String data) {
		setValue(STEREOTYPE_QUALIFIED_NAME, DEFAULT_VALUE, property, data);
	}

	public static String getLength(Property property) {
		for (EObject object : property.getStereotypeApplications()) {
			if (object instanceof Column) {
				Column prop = (Column) object;
				String value = prop.getLength();
				if (value != null && !value.isEmpty()) {
					return value;
				}
			}
		}
		return null;
	}

	public static void setLength(Property property, String data) {
		setValue(STEREOTYPE_QUALIFIED_NAME, LENGTH, property, data);
	}

	public static String getScale(Property property) {
		for (EObject object : property.getStereotypeApplications()) {
			if (object instanceof Column) {
				Column prop = (Column) object;
				String value = prop.getScale();
				if (value != null && !value.isEmpty()) {
					return value;
				}
			}
		}
		return null;
	}

	public static void setScale(Property property, String data) {
		setValue(STEREOTYPE_QUALIFIED_NAME, SCALE, property, data);
	}

	public static String getPrecision(Property property) {
		for (EObject object : property.getStereotypeApplications()) {
			if (object instanceof Column) {
				Column prop = (Column) object;
				String value = prop.getPrecision();
				if (value != null && !value.isEmpty()) {
					return value;
				}
			}
		}
		return null;
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
}
