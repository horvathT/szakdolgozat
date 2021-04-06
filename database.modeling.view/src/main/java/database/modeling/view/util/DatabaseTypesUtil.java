package database.modeling.view.util;

import java.util.HashMap;
import java.util.Map;

public class DatabaseTypesUtil {
	
	private Map<String, String[]> databaseMap = new HashMap<>();
	
	public Map<String, String[]> getDatabaseMap() {
		return databaseMap;
	}

	public DatabaseTypesUtil(){
		databaseMap.put("Oracle", oracleTypes);
		databaseMap.put("MySQL", mySqlTypes);
	}
	
	private final String[] oracleTypes = new String[] {
			"NUMBER(precision, scale)", //precision from 1 to 38 & scale from -84 to 127
			"BINARY_FLOAT",
			"BINARY_DOUBLE",
			"INTEGER",
			"FLOAT",
			"DECIMAL",
			"NUMERIC",
			"INT",
			"SMALLINT",
			"REAL",
			
			"CHAR(length)",
			"NCHAR(length)",
			"VARCHAR2(length)",
			"NVARCHAR(length)",
			"RAW(length)",
			"BLOB",
			"CLOB",
			"NCLOB",
			
			"DATE",
			"TIMESTAMP",
			"TIMESTAMP WITH TIME ZONE",
			"TIMESTAMP WITH LOCAL TIME ZONE",
			
			"BFILE",
			"ROWID",
			"ROWID"
			};
	
	private final String[] mySqlTypes = new String[] {
			"INT",
			"TINYINT",
			"SMALLINT",
			"MEDIUMINT",
			"BIGINT",
			"FLOAT(precision, scale)",
			"DOUBLE(precision, scale)",
			"DECIMAL(precision, scale)",
			
			"DATE",
			"DATETIME",
			"TIMESTAMP",
			"TIME",
			"YEAR",
			
			"CHAR(length)", // 1-255
			"VARCHAR(length)", // 1-255
			"BLOB",
			"TEXT",
			"TINYBLOB", //1-255
			"TINITEXT", //1-255
			"MEDIUMBLOB", //1-16777215
			"MEDIUMTEXT", //1-16777215
			"LONGBLOB", //1-4294967295 
			"LONGTEXT", //1-4294967295 
			"ENUM"
			};
	
}
