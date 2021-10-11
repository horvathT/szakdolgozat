package database.modeling.util;

import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;

public class PKUtil {
	private final static String STEREOTYPE_QUALIFIED_NAME = "DatabaseModeling::PK";
	private final static String PRIMARY_KEY_CONSTRAINT = "primaryKeyConstraint";

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

	public static String getConstraintName(Property property) {
		return getStringAttributeValue(property, PRIMARY_KEY_CONSTRAINT);
	}

	public static void setConstraintName(Property property, String data) {
		setValue(STEREOTYPE_QUALIFIED_NAME, PRIMARY_KEY_CONSTRAINT, property, data);
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
