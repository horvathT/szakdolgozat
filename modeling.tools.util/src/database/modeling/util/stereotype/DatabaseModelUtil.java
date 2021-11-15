package database.modeling.util.stereotype;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Package;

import DatabaseModeling.DatabaseModel;

public class DatabaseModelUtil {
	public final static String STEREOTYPE_QUALIFIED_NAME = "DatabaseModeling::DatabaseModel";
	private final static String DATABASE_TYPE = "databaseType";

	public static String getDatabaseType(Package pack) {
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

	public static void setDatabaseType(Package pack, String data) {
		StereotypeApplicationUtil.setValue(STEREOTYPE_QUALIFIED_NAME, DATABASE_TYPE, pack, data);
	}

}
