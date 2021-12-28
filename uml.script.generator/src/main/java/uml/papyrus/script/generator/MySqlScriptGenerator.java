package uml.papyrus.script.generator;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;

import database.modeling.model.DataTypeDefinition;
import database.modeling.model.DatabaseTypesUtil;
import database.modeling.util.stereotype.ColumnUtil;
import database.modeling.util.stereotype.FKUtil;

public class MySqlScriptGenerator extends ScriptGenerator {

	public static final String IDENTIFIER = "MySQL";

	public MySqlScriptGenerator(Package modelPackage, String filePath) {
		super(modelPackage, filePath);

		DatabaseTypesUtil dbTypes = new DatabaseTypesUtil();
		Map<String, List<DataTypeDefinition>> databaseTypeMap = dbTypes.getDatabaseTypeMap();
		dataTypes = databaseTypeMap.get(IDENTIFIER);
	}

	@Override
	protected String foreignKeyConstraint(String classifierName, Entry<String, List<Property>> entry) {
		String referredEntityName = entry.getKey();
		List<Property> fkPropList = entry.getValue();

		StringBuilder localProps = new StringBuilder();
		StringBuilder refProps = new StringBuilder();
		StringBuilder uniqueConstraintName = new StringBuilder();

		for (int i = 0; i < fkPropList.size(); i++) {
			Property property = fkPropList.get(i);
			localProps.append("`" + property.getName() + "`");

			String referencedProperty = FKUtil.getReferencedProperty(property);
			refProps.append("`" + referencedProperty + "`");
			uniqueConstraintName.append(referencedProperty);

			if (i != fkPropList.size() - 1) {
				localProps.append(", ");
				refProps.append(", ");
				uniqueConstraintName.append("_");
			}

		}
		String uniqueConstraint = "ALTER TABLE `" + referredEntityName + "` ADD CONSTRAINT `"
				+ uniqueConstraintName.toString() + "` UNIQUE(" + refProps.toString() + ");";

		String fkConstraint = "ALTER TABLE `" + classifierName + "` ADD CONSTRAINT `FK_" + classifierName
				+ "` FOREIGN KEY(" + localProps.toString() + ") REFERENCES `" + referredEntityName + "` ("
				+ refProps.toString() + ");";

		return uniqueConstraint + System.lineSeparator() + fkConstraint;
	}

	/**
	 * A paraméterben kapott Classifier-nek megfeleltethető tábla létrehozására
	 * alkalmas DDL script összeállítását végzi.
	 * 
	 * @param classifier
	 * @return
	 */
	@Override
	protected String createTableScript(Classifier classifier) {
		String createTable = "CREATE TABLE `" + classifier.getName() + "` ("
				+ appendLineSeparator()
				+ defineProperties(classifier) + ");"
				+ appendLineSeparator();
		return createTable;
	}

	@Override
	protected String primaryKeyConstraint(Classifier classifier) {
		List<Property> pkProp = getPKProperties(classifier);
		if (pkProp.isEmpty()) {
			return "";
		}

		StringBuilder pk = new StringBuilder();
		for (int i = 0; i < pkProp.size(); i++) {
			Property property = pkProp.get(i);
			pk.append("`" + property.getName() + "`");
			if (i != pkProp.size() - 1) {
				pk.append(", ");
			}
		}
		String classifierName = classifier.getName();
		String statement = "ALTER TABLE `" + classifierName + "` ADD CONSTRAINT `PK_" + classifierName
				+ "` PRIMARY KEY(" + pk.toString() + ");";
		return statement;
	}

	@Override
	protected String defineColumn(Property property) {

		String tableColumn = "`" + property.getName() + "` " + compileDataType(property) +
				nullable(ColumnUtil.getNullable(property)) +
				defaultValue(ColumnUtil.getDefaultValue(property)) +
				unique(ColumnUtil.getUnique(property));
		return tableColumn;
	}
}
