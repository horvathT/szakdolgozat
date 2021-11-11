package database.modeling.util.stereotype;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Stereotype;

import DatabaseModeling.DatabaseModel;

public class DatabaseModelUtil {
	public final static String STEREOTYPE_QUALIFIED_NAME = "DatabaseModeling::DatabaseModel";
	private final static String DATABASE_TYPE = "databaseType";

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
			if (StereotypeApplicationUtil.hasStereotype(pack, STEREOTYPE_QUALIFIED_NAME)) {
				pack.setValue(applicableStereotype, propertyName, data);
			} else {
				if (applicableStereotype != null) {
					StereotypeApplicationUtil.applyStereotype(pack, STEREOTYPE_QUALIFIED_NAME);
					pack.setValue(applicableStereotype, propertyName, data);
				}
			}
		}
	}
}
