package database.modeling.util.stereotype;

import org.eclipse.uml2.uml.Package;

public class DatabaseModelUtil {
	public final static String STEREOTYPE_QUALIFIED_NAME = "DatabaseModeling::DatabaseModel";
	private final static String DATABASE_TYPE = "databaseType";

	public static String getDatabaseType(Package pack) {
		return StereotypeManagementUtil.getStringAttributeValue(STEREOTYPE_QUALIFIED_NAME, pack, DATABASE_TYPE);
	}

	public static void setDatabaseType(Package pack, String data) {
		StereotypeManagementUtil.setStringValue(STEREOTYPE_QUALIFIED_NAME, DATABASE_TYPE, pack, data);
	}

}
