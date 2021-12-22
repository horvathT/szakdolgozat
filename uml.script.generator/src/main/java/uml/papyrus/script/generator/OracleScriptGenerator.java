package uml.papyrus.script.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;

import database.modeling.model.DataTypeDefinition;
import database.modeling.model.DatabaseTypesUtil;
import database.modeling.util.stereotype.ColumnUtil;
import database.modeling.util.stereotype.StereotypeManagementUtil;
import database.modeling.util.uml.ModelObjectUtil;

public class OracleScriptGenerator extends ScriptGenerator {

	public static final String IDENTIFIER = "Oracle";

	public OracleScriptGenerator(Package modelPackage, String filePath) {
		super(modelPackage, filePath);

		DatabaseTypesUtil dbTypes = new DatabaseTypesUtil();
		Map<String, List<DataTypeDefinition>> databaseTypeMap = dbTypes.getDatabaseTypeMap();
		dataTypes = databaseTypeMap.get(IDENTIFIER);
	}

	public void generateDdlScript() {
		Collection<Classifier> classifiers = ModelObjectUtil.getClassifiers(modelPackage.allOwnedElements());
		StringBuilder sb = new StringBuilder();
		for (Classifier classifier : classifiers) {
			sb.append(createTableScript(classifier));
			sb.append(appendLineSeparator());
		}

		for (Classifier classifier : classifiers) {
			sb.append(primaryKeyConstraint(classifier));
			sb.append(appendLineSeparator());
		}

		for (Classifier classifier : classifiers) {
			sb.append(foreignKeyConstraints(classifier));
			sb.append(appendLineSeparator());
		}
	}

	private Object foreignKeyConstraints(Classifier classifier) {
		// TODO Auto-generated method stub
		return null;
	}

	private String createTableScript(Classifier classifier) {
		String createTable = "CREATE TABLE \"" + classifier.getName() + "\" ("
				+ appendLineSeparator()
				+ defineProperties(classifier) + ");"
				+ appendLineSeparator();
		return createTable;
	}

	private String primaryKeyConstraint(Classifier classifier) {
		List<Property> pkProp = getPripmaryKeyProperties(classifier);
		if (pkProp.isEmpty()) {
			return "";
		}

		String statement = "ALTER TABLE \"" + classifier.getName() + "\" ADD CONSTRAINT PK_" + classifier.getName();
		return statement;
	}

	private List<Property> getPripmaryKeyProperties(Classifier classifier) {
		List<Property> properties = new ArrayList<>();
		EList<Property> attributes = classifier.getAttributes();
		for (Property property : attributes) {
			if (StereotypeManagementUtil.hasStereotype(property, ColumnUtil.STEREOTYPE_QUALIFIED_NAME)) {
				properties.add(property);
			}
		}
		return properties;
	}

	private String defineProperties(Classifier classifier) {
		StringBuilder sb = new StringBuilder();
		EList<Property> attributes = classifier.getAttributes();
		for (int i = 0; i < attributes.size(); i++) {
			Property property = attributes.get(i);
			sb.append(appendTab() + defineColumn(property));
			if (i != attributes.size() - 1) {
				sb.append(",");
			}
			sb.append(System.lineSeparator());
		}

		return sb.toString();
	}

	private String defineColumn(Property property) {

		String tableColumn = property.getName() + " " + compileDataType(property)
				+ defaultValue(ColumnUtil.getDefaultValue(property)) +
				nullable(ColumnUtil.getNullable(property)) + unique(ColumnUtil.getUnique(property));
		return tableColumn;
	}

	private String compileDataType(Property property) {
		String dataTypeName = ColumnUtil.getDataType(property);
		DataTypeDefinition typeDefinition = getTypeDefinitionByName(dataTypeName);

		if (typeDefinition.hasLength()) {
			String length = ColumnUtil.getLength(property);
			if (length.isEmpty()) {
				return " " + dataTypeName;
			}
			return " " + dataTypeName + "(" + length + ")";
		}

		if (typeDefinition.hasPrecision()) {
			String precision = ColumnUtil.getPrecision(property);
			if (precision.isEmpty()) {
				return " " + dataTypeName;
			}

			String scale = ColumnUtil.getScale(property);
			if (scale.isEmpty()) {
				scale = "0";
			}
			return " " + dataTypeName + "(" + precision + "," + scale + ")";
		}

		return " " + dataTypeName;
	}

}
