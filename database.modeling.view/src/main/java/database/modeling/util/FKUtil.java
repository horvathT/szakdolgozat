package database.modeling.util;

import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;

public class FKUtil {
	private static final String REFERENCED_PROPERTY = "referencedProperty";
	private static final String REFERENCED_ENTITY = "referencedEntity";
	private final static String STEREOTYPE_QUALIFIED_NAME = "DatabaseModeling::FK";
	private final static String FOREIGN_KEY_CONSTRAINT = "foreignKeyConstraint";

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
		return getStringAttributeValue(property, FOREIGN_KEY_CONSTRAINT);
	}

	public static void setConstraintName(Property property, String data) {
		setValue(STEREOTYPE_QUALIFIED_NAME, FOREIGN_KEY_CONSTRAINT, property, data);
	}
	
	public static String getReferencedEntity(Property property) {
		return getStringAttributeValue(property, REFERENCED_ENTITY);
	}

	public static void setReferencedEntity(Property property, String data) {
		setValue(STEREOTYPE_QUALIFIED_NAME, REFERENCED_ENTITY, property, data);
	}
	
	public static String getReferencedProperty(Property property) {
		return getStringAttributeValue(property, REFERENCED_PROPERTY);
	}

	public static void setReferencedProperty(Property property, String data) {
		setValue(STEREOTYPE_QUALIFIED_NAME, REFERENCED_PROPERTY, property, data);
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
