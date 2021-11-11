package database.modeling.util.stereotype;

import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;

public class StereotypeApplicationUtil {

	public static boolean hasStereotype(Property property, String stereotpyeQualifiedName) {
		if (property == null) {
			return false;
		}

		Stereotype applicableStereotype = property.getApplicableStereotype(stereotpyeQualifiedName);
		if (property.isStereotypeApplied(applicableStereotype)) {
			return true;
		}
		return false;
	}

	public static void applyStereotype(Property property, String stereotpyeQualifiedName) {
		if (property == null) {
			return;
		}
		Stereotype applicableStereotype = property.getApplicableStereotype(stereotpyeQualifiedName);
		if (property.isStereotypeApplied(applicableStereotype)) {
		} else {
			if (applicableStereotype != null) {
				property.applyStereotype(applicableStereotype);
			}
		}
	}

	public static boolean hasStereotype(Package pack, String stereotpyeQualifiedName) {
		if (pack == null) {
			return false;
		}

		Stereotype applicableStereotype = pack.getApplicableStereotype(stereotpyeQualifiedName);
		if (pack.isStereotypeApplied(applicableStereotype)) {
			return true;
		}
		return false;
	}

	public static void applyStereotype(Package pack, String stereotpyeQualifiedName) {
		if (pack == null) {
			return;
		}
		Stereotype applicableStereotype = pack.getApplicableStereotype(stereotpyeQualifiedName);
		if (pack.isStereotypeApplied(applicableStereotype)) {
		} else {
			if (applicableStereotype != null) {
				pack.applyStereotype(applicableStereotype);
			}
		}
	}

	public static void removeStereotype(Property property, String stereotpyeQualifiedName) {
		if (hasStereotype(property, stereotpyeQualifiedName)) {
			Stereotype applicableStereotype = property.getApplicableStereotype(stereotpyeQualifiedName);
			property.unapplyStereotype(applicableStereotype);
		}
	}

	static void setValue(String stereotpyeQualifiedName, String propertyName, Property property, String data) {
		if (data != null && property != null) {
			Stereotype applicableStereotype = property.getApplicableStereotype(stereotpyeQualifiedName);
			if (StereotypeApplicationUtil.hasStereotype(property, stereotpyeQualifiedName)) {
				property.setValue(applicableStereotype, propertyName, data);
			} else {
				if (applicableStereotype != null) {
					StereotypeApplicationUtil.applyStereotype(property, stereotpyeQualifiedName);
					property.setValue(applicableStereotype, propertyName, data);
				}
			}
		}
	}

	static String getStringAttributeValue(String stereotpyeQualifiedName, Property property, String attributeName) {
		Stereotype applicableStereotype = property.getApplicableStereotype(stereotpyeQualifiedName);
		if (property.isStereotypeApplied(applicableStereotype)) {
			String value = (String) property.getValue(applicableStereotype, attributeName);
			if (value != null) {
				return value;
			}
		}
		return "";
	}

}
