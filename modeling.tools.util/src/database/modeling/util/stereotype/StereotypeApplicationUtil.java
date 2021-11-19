package database.modeling.util.stereotype;

import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;

public class StereotypeApplicationUtil {

	public static boolean hasStereotype(NamedElement property, String stereotpyeQualifiedName) {
		if (property == null) {
			return false;
		}

		Stereotype applicableStereotype = property.getApplicableStereotype(stereotpyeQualifiedName);
		if (property.isStereotypeApplied(applicableStereotype)) {
			return true;
		}
		return false;
	}

	public static void applyStereotype(NamedElement pack, String stereotpyeQualifiedName) {
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

	static void setValue(String stereotpyeQualifiedName, String propertyName, NamedElement property, String data) {
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

	static String getStringAttributeValue(String stereotpyeQualifiedName, NamedElement element, String attributeName) {
		Stereotype applicableStereotype = element.getApplicableStereotype(stereotpyeQualifiedName);
		if (element.isStereotypeApplied(applicableStereotype)) {
			String value = (String) element.getValue(applicableStereotype, attributeName);
			if (value != null) {
				return value;
			}
		}
		return "";
	}

}
