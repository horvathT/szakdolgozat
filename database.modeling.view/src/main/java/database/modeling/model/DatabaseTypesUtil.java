package database.modeling.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import database.modeling.model.DataTypeDefinition.InputType;

public class DatabaseTypesUtil {

	private Map<String, List<DataTypeDefinition>> databaseTypeMap = new HashMap<>();

	public static final String DEFAULT_DB = "Oracle";

	public DatabaseTypesUtil() {

		List<DataTypeDefinition> oracleDataTypes = new ArrayList<>();
		oracleDataTypes.add(DataTypeDefinition.of().name("").hasDefaulValue(false));
		oracleDataTypes
				.add(DataTypeDefinition.of().name("BINARY_DOUBLE").setType(InputType.NUMERIC).hasDefaulValue(false));
		oracleDataTypes
				.add(DataTypeDefinition.of().name("BINARY_FLOAT").setType(InputType.NUMERIC).hasDefaulValue(false));
		oracleDataTypes.add(DataTypeDefinition.of().name("BLOB").setType(InputType.NUMERIC).hasDefaulValue(false));
		oracleDataTypes.add(DataTypeDefinition.of().name("CHAR").setType(InputType.TEXT).lengthLowerBound(1)
				.lengthUpperBound(2000));
		oracleDataTypes.add(DataTypeDefinition.of().name("CLOB").setType(InputType.TEXT));
		oracleDataTypes.add(DataTypeDefinition.of().name("DATE").setType(InputType.TEXT));
		oracleDataTypes.add(DataTypeDefinition.of().name("FLOAT").setType(InputType.NUMERIC));
		oracleDataTypes.add(DataTypeDefinition.of().name("LONG").setType(InputType.NUMERIC));
		oracleDataTypes.add(DataTypeDefinition.of().name("NCHAR").setType(InputType.TEXT).lengthLowerBound(1)
				.lengthUpperBound(2000));
		oracleDataTypes.add(DataTypeDefinition.of().name("NCLOB").setType(InputType.TEXT));
		oracleDataTypes.add(DataTypeDefinition.of().name("NUMBER").setType(InputType.NUMERIC).precisionLowerBound(1)
				.precisionUpperBound(38).scaleLowerBound(0).scaleUpperBound(38));
		oracleDataTypes.add(DataTypeDefinition.of().name("NVARCHAR2").setType(InputType.TEXT).lengthLowerBound(1)
				.lengthUpperBound(4000));
		oracleDataTypes.add(
				DataTypeDefinition.of().name("RAW").setType(InputType.TEXT).lengthLowerBound(1).lengthUpperBound(2000));
		oracleDataTypes.add(DataTypeDefinition.of().name("ROWID").setType(InputType.NUMERIC).hasDefaulValue(false));
		oracleDataTypes.add(DataTypeDefinition.of().name("TIMESTAMP").setType(InputType.TEXT).hasDefaulValue(false));
		oracleDataTypes.add(DataTypeDefinition.of().name("TIMESTAMP WITH LOCAL TIME ZONE").setType(InputType.TEXT)
				.hasDefaulValue(false));
		oracleDataTypes.add(
				DataTypeDefinition.of().name("TIMESTAMP WITH TIME ZONE").setType(InputType.TEXT).hasDefaulValue(false));
		oracleDataTypes.add(DataTypeDefinition.of().name("VARCHAR2").setType(InputType.TEXT).lengthLowerBound(1)
				.lengthUpperBound(4000));

		List<DataTypeDefinition> mySqlDataTypes = new ArrayList<>();
		mySqlDataTypes.add(DataTypeDefinition.of().name("").hasDefaulValue(false));
		mySqlDataTypes.add(DataTypeDefinition.of().name("BIGINT").setType(InputType.NUMERIC));
		mySqlDataTypes.add(DataTypeDefinition.of().name("BINARY").hasDefaulValue(false));
		mySqlDataTypes.add(DataTypeDefinition.of().name("BIT").hasDefaulValue(false));
		mySqlDataTypes.add(DataTypeDefinition.of().name("BLOB").hasDefaulValue(false));
		mySqlDataTypes.add(DataTypeDefinition.of().name("BOOLEAN").setType(InputType.TEXT));
		mySqlDataTypes.add(
				DataTypeDefinition.of().name("CHAR").setType(InputType.TEXT).lengthLowerBound(1).lengthUpperBound(255));
		mySqlDataTypes.add(DataTypeDefinition.of().name("DATE").setType(InputType.TEXT));
		mySqlDataTypes.add(DataTypeDefinition.of().name("DATETIME").setType(InputType.TEXT).hasDefaulValue(false));
		mySqlDataTypes.add(DataTypeDefinition.of().name("DECIMAL").setType(InputType.NUMERIC).precisionLowerBound(1)
				.precisionUpperBound(65).scaleLowerBound(0).scaleUpperBound(64));
		mySqlDataTypes.add(DataTypeDefinition.of().name("DOUBLE").setType(InputType.NUMERIC));
		mySqlDataTypes.add(DataTypeDefinition.of().name("FLOAT").setType(InputType.NUMERIC));
		mySqlDataTypes.add(DataTypeDefinition.of().name("INT").setType(InputType.NUMERIC));
		mySqlDataTypes.add(DataTypeDefinition.of().name("LONGBLOB").setType(InputType.TEXT));
		mySqlDataTypes.add(DataTypeDefinition.of().name("LONGTEXT").setType(InputType.TEXT));
		mySqlDataTypes.add(DataTypeDefinition.of().name("MEDIUMBLOB").setType(InputType.TEXT));
		mySqlDataTypes.add(DataTypeDefinition.of().name("MEDIUMINT").setType(InputType.NUMERIC));
		mySqlDataTypes.add(DataTypeDefinition.of().name("MEDIUMTEXT").setType(InputType.TEXT));
		mySqlDataTypes.add(DataTypeDefinition.of().name("SMALLINT").setType(InputType.NUMERIC));
		mySqlDataTypes.add(DataTypeDefinition.of().name("TEXT").setType(InputType.TEXT));
		mySqlDataTypes.add(DataTypeDefinition.of().name("TIME").setType(InputType.TEXT));
		mySqlDataTypes.add(DataTypeDefinition.of().name("TINYBLOB").setType(InputType.TEXT));
		mySqlDataTypes.add(DataTypeDefinition.of().name("TINYINT").setType(InputType.NUMERIC));
		mySqlDataTypes.add(DataTypeDefinition.of().name("TINYTEXT").setType(InputType.TEXT));
		mySqlDataTypes.add(DataTypeDefinition.of().name("VARCHAR").setType(InputType.TEXT).lengthLowerBound(1)
				.lengthUpperBound(255));

		List<DataTypeDefinition> postgreSqlDataTypes = new ArrayList<>();
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("").hasDefaulValue(false));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("BIGINT").setType(InputType.NUMERIC)
				.rangeLowerBound(-9223372036854775808L).rangeUpperBound(+9223372036854775807L));
		postgreSqlDataTypes
				.add(DataTypeDefinition.of().name("BIGSERIAL").setType(InputType.NUMERIC).hasDefaulValue(false));
		postgreSqlDataTypes.add(
				DataTypeDefinition.of().name("BIT").setType(InputType.TEXT).lengthLowerBound(0).lengthUpperBound(4000));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("BOOLEAN").setType(InputType.TEXT));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("CHAR").setType(InputType.TEXT).lengthLowerBound(0)
				.lengthUpperBound(10485760));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("CHARACTER").setType(InputType.TEXT));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("CHARACTER VARYING").setType(InputType.TEXT));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("DATE").setType(InputType.TEXT).hasDefaulValue(false));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("INTEGER").setType(InputType.NUMERIC));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("JSON").setType(InputType.TEXT).hasDefaulValue(false));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("JSONB").setType(InputType.TEXT).hasDefaulValue(false));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("MONEY").setType(InputType.NUMERIC));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("NUMERIC").setType(InputType.NUMERIC));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("SMALLINT").setType(InputType.NUMERIC));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("TEXT").setType(InputType.TEXT));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("TIME").setType(InputType.TEXT).hasDefaulValue(false));
		postgreSqlDataTypes
				.add(DataTypeDefinition.of().name("TIME WITH TIME ZONE").setType(InputType.TEXT).hasDefaulValue(false));
		postgreSqlDataTypes
				.add(DataTypeDefinition.of().name("TIMESTAMP").setType(InputType.TEXT).hasDefaulValue(false));
		postgreSqlDataTypes.add(
				DataTypeDefinition.of().name("TIMESTAMP WITH TIME ZONE").setType(InputType.TEXT).hasDefaulValue(false));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("UUID").setType(InputType.TEXT).hasDefaulValue(false));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("VARCHAR").setType(InputType.TEXT));

		databaseTypeMap.put("Oracle", oracleDataTypes);
		databaseTypeMap.put("MySQL", mySqlDataTypes);
		databaseTypeMap.put("PostgreSql", postgreSqlDataTypes);

	}

	public Map<String, List<DataTypeDefinition>> getDatabaseTypeMap() {
		return databaseTypeMap;
	}

	public Set<String> getDatabases() {
		return databaseTypeMap.keySet();
	}

}
