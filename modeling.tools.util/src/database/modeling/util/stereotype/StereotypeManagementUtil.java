package database.modeling.util.stereotype;

import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;

/**
 * Sztereotípus attribútum getter és setter metódusok általánosítása.
 * Sztereotípus alkalmazása, és alkalmazottság vizsgáltat általános formában.
 * 
 * @author Horváth Tibor
 *
 */
public class StereotypeManagementUtil {

	/**
	 * Megnézi alkalmazva van-e a sztereotípus az elemen.
	 * 
	 * @param property
	 * @param stereotpyeQualifiedName
	 * @return
	 */
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

	/**
	 * Alkalmazza a sztereotípust az elemen.
	 * 
	 * @param property
	 * @param stereotpyeQualifiedName
	 * @return
	 */
	public static void applyStereotype(NamedElement element, String stereotpyeQualifiedName) {
		if (element == null) {
			return;
		}
		Stereotype applicableStereotype = element.getApplicableStereotype(stereotpyeQualifiedName);
		if (element.isStereotypeApplied(applicableStereotype)) {
		} else {
			if (applicableStereotype != null) {
				element.applyStereotype(applicableStereotype);
			}
		}
	}

	/**
	 * String típusú attribútum értékének beállítása.
	 * 
	 * @param stereotpyeQualifiedName
	 * @param propertyName
	 * @param property
	 * @param data
	 */
	static void setStringValue(String stereotpyeQualifiedName, String propertyName, NamedElement property,
			String data) {
		if (data != null && property != null) {
			Stereotype applicableStereotype = property.getApplicableStereotype(stereotpyeQualifiedName);
			if (StereotypeManagementUtil.hasStereotype(property, stereotpyeQualifiedName)) {
				property.setValue(applicableStereotype, propertyName, data);
			} else {
				if (applicableStereotype != null) {
					StereotypeManagementUtil.applyStereotype(property, stereotpyeQualifiedName);
					property.setValue(applicableStereotype, propertyName, data);
				}
			}
		}
	}

	/**
	 * String típusú attribútum értékének lekérdezése.
	 * 
	 * @param stereotpyeQualifiedName
	 * @param element
	 * @param attributeName
	 * @return
	 */
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

	/**
	 * boolean típusú attribútum értékének beállítása.
	 * 
	 * @param stereotpyeQualifiedName
	 * @param propertyName
	 * @param property
	 * @param data
	 */
	static void setBooleanValue(String stereotpyeQualifiedName, String propertyName, NamedElement property,
			boolean data) {

		if (property != null) {
			Stereotype applicableStereotype = property.getApplicableStereotype(stereotpyeQualifiedName);
			if (StereotypeManagementUtil.hasStereotype(property, stereotpyeQualifiedName)) {
				property.setValue(applicableStereotype, propertyName, Boolean.valueOf(data));
			} else {
				if (applicableStereotype != null) {
					StereotypeManagementUtil.applyStereotype(property, stereotpyeQualifiedName);
					property.setValue(applicableStereotype, propertyName, data);
				}
			}
		}
	}

	/**
	 * boolean típusú attribútum értékének lekérdezése.
	 * 
	 * @param stereotpyeQualifiedName
	 * @param element
	 * @param attributeName
	 * @return
	 */
	static boolean getBooleanAttributeValue(String stereotpyeQualifiedName, NamedElement element,
			String attributeName) {
		Stereotype applicableStereotype = element.getApplicableStereotype(stereotpyeQualifiedName);
		if (element.isStereotypeApplied(applicableStereotype)) {
			return (boolean) element.getValue(applicableStereotype, attributeName);
		}
		throw new IllegalArgumentException(
				"Stereotype: " + stereotpyeQualifiedName + " not found on element: "
						+ element.toString());
	}

	/**
	 * Az {@element} paraméterben megadott elemről eltávolítja a
	 * {@stereotpyeQualifiedName} megnevezésű sztereotípust ha az alkalmazva van
	 * rajta. Fontos hogy a metódust RecordingCommand belsejéből kell hívni,
	 * különben a változtatás nem hajtható végre.
	 * 
	 * @param element
	 * @param stereotpyeQualifiedName
	 */
	public static void unapplyStereotype(NamedElement element, String stereotpyeQualifiedName) {
		Stereotype appliedStereotype = element.getAppliedStereotype(stereotpyeQualifiedName);
		if (appliedStereotype != null) {
			element.unapplyStereotype(appliedStereotype);
		}
	}

}
