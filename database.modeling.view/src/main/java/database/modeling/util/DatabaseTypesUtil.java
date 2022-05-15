package database.modeling.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import database.modeling.viewmodel.DataTypeDefinition;
import database.modeling.viewmodel.DataTypeDefinition.InputType;

/**
 * Különböző SQL implementációk és adattípusaik inicializálását, tárolását, és
 * lekérését kezeli.
 * 
 * @author Horváth Tibor
 *
 */
public class DatabaseTypesUtil {

	private Map<String, List<DataTypeDefinition>> databaseTypeMap = new HashMap<>();

	public static final String DEFAULT_DB = "Oracle";

	public DatabaseTypesUtil() {

		// Oracle
		List<DataTypeDefinition> oracleDataTypes = new ArrayList<>();
		oracleDataTypes.add(DataTypeDefinition.of().name("").hasDefaulValue(false));
		oracleDataTypes
				.add(DataTypeDefinition.of().name("BINARY_DOUBLE").setType(InputType.NUMERIC).hasDefaulValue(false));
		oracleDataTypes
				.add(DataTypeDefinition.of().name("BINARY_FLOAT").setType(InputType.NUMERIC).hasDefaulValue(false));
		oracleDataTypes.add(DataTypeDefinition.of().name("BLOB").setType(InputType.NUMERIC).hasDefaulValue(false));
		oracleDataTypes.add(DataTypeDefinition.of().name("CHAR").setType(InputType.TEXT)
				.lengthLowerBound(1)
				.lengthUpperBound(2000));
		oracleDataTypes.add(DataTypeDefinition.of().name("CLOB").hasDefaulValue(false));
		oracleDataTypes.add(DataTypeDefinition.of().name("DATE").hasDefaulValue(false));
		oracleDataTypes.add(DataTypeDefinition.of().name("FLOAT").setType(InputType.NUMERIC)
				.precisionLowerBound(0)
				.precisionUpperBound(123));
		oracleDataTypes.add(DataTypeDefinition.of().name("LONG").setType(InputType.NUMERIC));
		oracleDataTypes.add(DataTypeDefinition.of().name("NCHAR").setType(InputType.TEXT)
				.lengthLowerBound(1)
				.lengthUpperBound(2000));
		oracleDataTypes.add(DataTypeDefinition.of().name("NCLOB").hasDefaulValue(false));
		oracleDataTypes.add(DataTypeDefinition.of().name("NUMBER").setType(InputType.NUMERIC)
				.precisionLowerBound(1)
				.precisionUpperBound(38)
				.scaleLowerBound(0)
				.scaleUpperBound(38));
		oracleDataTypes.add(DataTypeDefinition.of().name("NVARCHAR2").setType(InputType.TEXT)
				.lengthLowerBound(1)
				.lengthUpperBound(4000));
		oracleDataTypes.add(DataTypeDefinition.of().name("RAW").setType(InputType.TEXT)
				.lengthLowerBound(1)
				.lengthUpperBound(2000));
		oracleDataTypes.add(DataTypeDefinition.of().name("ROWID").setType(InputType.NUMERIC).hasDefaulValue(false));
		oracleDataTypes.add(DataTypeDefinition.of().name("TIMESTAMP").setType(InputType.TEXT).hasDefaulValue(false)
				.precisionLowerBound(0)
				.precisionUpperBound(9));
		oracleDataTypes.add(DataTypeDefinition.of().name("TIMESTAMP WITH LOCAL TIME ZONE").setType(InputType.TEXT)
				.hasDefaulValue(false)
				.precisionLowerBound(0)
				.precisionUpperBound(9));
		oracleDataTypes.add(
				DataTypeDefinition.of().name("TIMESTAMP WITH TIME ZONE").setType(InputType.TEXT).hasDefaulValue(false)
						.precisionLowerBound(0)
						.precisionUpperBound(9));
		oracleDataTypes.add(DataTypeDefinition.of().name("VARCHAR2").setType(InputType.TEXT).lengthLowerBound(1)
				.lengthUpperBound(4000));

		// MySQL
		List<DataTypeDefinition> mySqlDataTypes = new ArrayList<>();
		mySqlDataTypes.add(DataTypeDefinition.of().name("").hasDefaulValue(false));
		mySqlDataTypes.add(DataTypeDefinition.of().name("BIGINT").setType(InputType.NUMERIC)
				.numericDefaultLowerBound(-9223372036854775808L)
				.numericDefaultUpperBound(9223372036854775807L));
		mySqlDataTypes.add(DataTypeDefinition.of().name("BINARY").hasDefaulValue(false)
				.lengthLowerBound(0)
				.lengthUpperBound(255));
		mySqlDataTypes.add(DataTypeDefinition.of().name("BIT").hasDefaulValue(false)
				.lengthLowerBound(0)
				.lengthUpperBound(64));
		mySqlDataTypes.add(DataTypeDefinition.of().name("BLOB").hasDefaulValue(false));
		mySqlDataTypes.add(DataTypeDefinition.of().name("BOOLEAN").setType(InputType.TEXT));
		mySqlDataTypes.add(DataTypeDefinition.of().name("CHAR").setType(InputType.TEXT)
				.lengthLowerBound(0)
				.lengthUpperBound(255));
		mySqlDataTypes.add(DataTypeDefinition.of().name("DATE").hasDefaulValue(false));
		mySqlDataTypes.add(DataTypeDefinition.of().name("DATETIME").setType(InputType.TEXT)
				.precisionLowerBound(0)
				.precisionUpperBound(6));
		mySqlDataTypes.add(DataTypeDefinition.of().name("DECIMAL").setType(InputType.NUMERIC)
				.precisionLowerBound(0)
				.precisionUpperBound(1000)
				.scaleLowerBound(0)
				.scaleUpperBound(70198160));
		mySqlDataTypes.add(DataTypeDefinition.of().name("DOUBLE").setType(InputType.NUMERIC)
				.precisionLowerBound(0)
				.precisionUpperBound(53)
				.scaleLowerBound(0)
				.scaleUpperBound(70198160));
		mySqlDataTypes.add(DataTypeDefinition.of().name("FLOAT").setType(InputType.NUMERIC)
				.precisionLowerBound(0)
				.precisionUpperBound(70198160)
				.scaleLowerBound(0)
				.scaleUpperBound(70198160));
		mySqlDataTypes.add(DataTypeDefinition.of().name("INT").setType(InputType.NUMERIC)
				.numericDefaultLowerBound(-2147483648L)
				.numericDefaultUpperBound(2147483648L));
		mySqlDataTypes.add(DataTypeDefinition.of().name("LONGBLOB").hasDefaulValue(false));
		mySqlDataTypes.add(DataTypeDefinition.of().name("LONGTEXT").hasDefaulValue(false));
		mySqlDataTypes.add(DataTypeDefinition.of().name("MEDIUMBLOB").hasDefaulValue(false));
		mySqlDataTypes.add(DataTypeDefinition.of().name("MEDIUMINT").setType(InputType.NUMERIC)
				.numericDefaultLowerBound(-8388607L)
				.numericDefaultUpperBound(8388607L));
		mySqlDataTypes.add(DataTypeDefinition.of().name("MEDIUMTEXT").setType(InputType.TEXT).hasDefaulValue(false));
		mySqlDataTypes.add(DataTypeDefinition.of().name("SMALLINT").setType(InputType.NUMERIC)
				.numericDefaultLowerBound(-32767L)
				.numericDefaultUpperBound(32767L));
		mySqlDataTypes.add(DataTypeDefinition.of().name("TEXT").setType(InputType.TEXT).hasDefaulValue(false));
		mySqlDataTypes.add(DataTypeDefinition.of().name("TIME").setType(InputType.TEXT).hasDefaulValue(false)
				.precisionLowerBound(0)
				.precisionUpperBound(6));
		mySqlDataTypes.add(DataTypeDefinition.of().name("TINYBLOB").hasDefaulValue(false));
		mySqlDataTypes.add(DataTypeDefinition.of().name("TINYINT").setType(InputType.NUMERIC)
				.numericDefaultLowerBound(0L)
				.numericDefaultUpperBound(255L));
		mySqlDataTypes.add(DataTypeDefinition.of().name("TINYTEXT").hasDefaulValue(false));
		mySqlDataTypes.add(DataTypeDefinition.of().name("VARCHAR").setType(InputType.TEXT)
				.lengthLowerBound(0)
				.lengthUpperBound(65535));

		// PostgreSQL
		List<DataTypeDefinition> postgreSqlDataTypes = new ArrayList<>();
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("").hasDefaulValue(false));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("BIGINT").setType(InputType.NUMERIC)
				.numericDefaultLowerBound(-9223372036854775808L)
				.numericDefaultUpperBound(+9223372036854775807L));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("BIGSERIAL").hasDefaulValue(false));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("BIT").setType(InputType.TEXT)
				.lengthLowerBound(0)
				.lengthUpperBound(4000));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("BOOLEAN").setType(InputType.TEXT));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("CHAR").setType(InputType.TEXT)
				.lengthLowerBound(0)
				.lengthUpperBound(10485760));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("CHARACTER").setType(InputType.TEXT));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("CHARACTER VARYING").setType(InputType.TEXT));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("DATE").setType(InputType.TEXT).hasDefaulValue(false));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("INTEGER").setType(InputType.NUMERIC)
				.numericDefaultLowerBound(-2147483648)
				.numericDefaultUpperBound(2147483648L));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("JSON").setType(InputType.TEXT).hasDefaulValue(false));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("JSONB").setType(InputType.TEXT).hasDefaulValue(false));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("MONEY").setType(InputType.NUMERIC));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("NUMERIC").setType(InputType.NUMERIC)
				.precisionLowerBound(0)
				.precisionUpperBound(1000)
				.scaleLowerBound(0)
				.scaleUpperBound(1000));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("SMALLINT").setType(InputType.NUMERIC)
				.numericDefaultLowerBound(-32768L)
				.numericDefaultUpperBound(32768L));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("TEXT").setType(InputType.TEXT));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("TIME").setType(InputType.TEXT).hasDefaulValue(false));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("TIME WITH TIME ZONE").hasDefaulValue(false));
		postgreSqlDataTypes.add(DataTypeDefinition.of().name("TIMESTAMP").hasDefaulValue(false));
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
