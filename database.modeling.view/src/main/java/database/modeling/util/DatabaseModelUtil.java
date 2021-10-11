package database.modeling.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Stereotype;

import DatabaseModeling.DatabaseModel;


public class DatabaseModelUtil {
	private final static String STEREOTYPE_QUALIFIED_NAME = "DatabaseModeling::DatabaseModel";
	private final static String DATABASE_TYPE = "databaseType";

	public static boolean hasStereotype(Package pack) {
		if (pack == null) {
			return false;
		}

		Stereotype applicableStereotype = pack.getApplicableStereotype(STEREOTYPE_QUALIFIED_NAME);
		if (pack.isStereotypeApplied(applicableStereotype)) {
			return true;
		}
		return false;
	}

	public static void applyStereotype(Package pack) {
		if (pack == null) {
			return;
		}
		Stereotype applicableStereotype = pack.getApplicableStereotype(STEREOTYPE_QUALIFIED_NAME);
		if (pack.isStereotypeApplied(applicableStereotype)) {
		} else {
			if (applicableStereotype != null) {
				pack.applyStereotype(applicableStereotype);
			}
		}
	}

	public static String getConstraintName(Package pack) {
		for (EObject object : pack.getStereotypeApplications()) {
			if (object instanceof DatabaseModel) {
				DatabaseModel prop = (DatabaseModel) object;
				String value = prop.getDatabaseType();
				if (value != null && !value.isEmpty()) {
					return value;
				}
			}
		}
		return null;
	}

	public static void setPrecision(Package pack, String data) {
		setValue(STEREOTYPE_QUALIFIED_NAME, DATABASE_TYPE, pack, data);
	}

	private static void setValue(String stereotypeName, String propertyName, Package pack, String data) {
		if (data != null && pack != null) {
			Stereotype applicableStereotype = pack.getApplicableStereotype(stereotypeName);
			if (hasStereotype(pack)) {
				pack.setValue(applicableStereotype, propertyName, data);
			} else {
				if (applicableStereotype != null) {
					applyStereotype(pack);
					pack.setValue(applicableStereotype, propertyName, data);
				}
			}
		}
	}
}
