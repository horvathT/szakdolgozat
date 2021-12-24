package uml.papyrus.script.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import database.modeling.model.DataTypeDefinition;
import database.modeling.util.resource.EclipseResourceUtil;
import database.modeling.util.stereotype.ColumnUtil;
import database.modeling.util.stereotype.FKUtil;
import database.modeling.util.stereotype.PKUtil;
import database.modeling.util.stereotype.StereotypeManagementUtil;
import database.modeling.util.uml.ModelObjectUtil;
import uml.papyrus.script.validator.ModelValidator;

public class ScriptGenerator {

	protected static final Bundle BUNDLE = FrameworkUtil.getBundle(ModelValidator.class);
	protected static final ILog LOGGER = Platform.getLog(BUNDLE);

	protected Package modelPackage;

	protected String filePath;

	protected List<DataTypeDefinition> dataTypes;

	public ScriptGenerator(Package modelPackage, String filePath) {
		this.modelPackage = modelPackage;
		this.filePath = filePath;
	}

	protected String appendLineSeparator(int numOfEmptyLines) {
		StringBuilder strb = new StringBuilder();
		if (numOfEmptyLines > 0) {
			for (int i = 0; i < numOfEmptyLines; ++i) {
				strb.append(appendLineSeparator());
			}
		}
		return strb.toString();
	}

	protected String appendLineSeparator() {
		return System.lineSeparator();
	}

	protected String appendTab(int numOfTabs) {
		StringBuilder strb = new StringBuilder();
		if (numOfTabs > 0) {
			for (int i = 0; i < numOfTabs; ++i) {
				strb.append(appendTab());
			}
		}
		return strb.toString();
	}

	protected String appendTab() {
		return "	";
	}

	protected String nullable(boolean isNullable) {
		if (isNullable) {
			return " NULL";
		}
		return " NOT NULL";
	}

	protected String unique(boolean isUnique) {
		if (isUnique) {
			return " UNIQUE";
		}
		return "";
	}

	protected String defaultValue(String defaultValue) {
		if (defaultValue.isEmpty()) {
			return "";
		}

		return " DEFAULT \'" + defaultValue + "\'";
	}

	protected DataTypeDefinition getTypeDefinitionByName(String name) {
		for (DataTypeDefinition dataTypeDefinition : dataTypes) {
			if (dataTypeDefinition.getName().equals(name)) {
				return dataTypeDefinition;
			}
		}
		throw new IllegalArgumentException("Nem található típus " + name + " névvel");
	}

	protected List<Property> getOwnedAttributes(Classifier classifier) {
		EList<Property> attributes = classifier.getAttributes();
		return attributes.stream().filter(p -> p.getAssociation() == null).collect(Collectors.toList());
	}

	protected Map<String, List<Property>> getFKProperties(Classifier classifier) {
		Map<String, List<Property>> fkByReferencedentity = new HashMap<>();

		EList<Property> attributes = classifier.getAttributes();
		for (Property property : attributes) {
			if (StereotypeManagementUtil.hasStereotype(property, FKUtil.STEREOTYPE_QUALIFIED_NAME)) {
				String referencedEntity = FKUtil.getReferencedEntity(property);
				List<Property> list = fkByReferencedentity.get(referencedEntity);
				if (list == null) {
					List<Property> propList = new ArrayList<>();
					propList.add(property);
					fkByReferencedentity.put(referencedEntity, propList);
				} else {
					list.add(property);
				}
			}
		}
		return fkByReferencedentity;
	}

	protected List<Property> getPKProperties(Classifier classifier) {
		List<Property> properties = new ArrayList<>();
		EList<Property> attributes = classifier.getAttributes();
		for (Property property : attributes) {
			if (StereotypeManagementUtil.hasStereotype(property, PKUtil.STEREOTYPE_QUALIFIED_NAME)) {
				properties.add(property);
			}
		}
		return properties;
	}

	protected String compileDataType(Property property) {
		String dataTypeName = ColumnUtil.getDataType(property);
		DataTypeDefinition typeDefinition = getTypeDefinitionByName(dataTypeName);

		if (typeDefinition.hasLength()) {
			String length = ColumnUtil.getLength(property);
			if (length.isEmpty() || Integer.parseInt(length) == 0) {
				return " " + dataTypeName;
			}
			return " " + dataTypeName + "(" + length + ")";
		}

		if (typeDefinition.hasPrecision()) {
			String precision = ColumnUtil.getPrecision(property);
			if (precision.isEmpty() || Integer.parseInt(precision) == 0) {
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

		EclipseResourceUtil.writeFile(filePath, sb.toString());
	}

	protected String foreignKeyConstraints(Classifier classifier) {
		StringBuilder fkConstraints = new StringBuilder();

		Map<String, List<Property>> fkMap = getFKProperties(classifier);
		if (fkMap.isEmpty()) {
			return "";
		}
		for (Entry<String, List<Property>> entry : fkMap.entrySet()) {
			fkConstraints.append(foreignKeyConstraint(classifier.getName(), fkMap, entry));
		}

		return fkConstraints.toString();
	}

	protected String foreignKeyConstraint(String classifierName, Map<String, List<Property>> fkMap,
			Entry<String, List<Property>> entry) {
		String referredEntityName = entry.getKey();
		List<Property> fkPropList = entry.getValue();

		StringBuilder localProps = new StringBuilder();
		StringBuilder refProps = new StringBuilder();

		for (int i = 0; i < fkPropList.size(); i++) {
			Property property = fkPropList.get(i);
			localProps.append("" + property.getName() + "");

			String referencedProperty = FKUtil.getReferencedProperty(property);
			refProps.append("" + referencedProperty + "");

			if (i != fkPropList.size() - 1) {
				localProps.append(", ");
				refProps.append(", ");
			}

		}

		return "ALTER TABLE " + classifierName + " ADD CONSTRAINT FK_" + classifierName
				+ " FOREIGN KEY(" + localProps.toString() + ") REFERENCES " + referredEntityName + " ("
				+ refProps.toString() + ");";
	}

	/**
	 * A paraméterben kapott Classifier-nek megfeleltethető tábla létrehozására
	 * alkalmas DDL script összeállítását végzi.
	 * 
	 * @param classifier
	 * @return
	 */
	protected String createTableScript(Classifier classifier) {
		String createTable = "CREATE TABLE " + classifier.getName() + " ("
				+ appendLineSeparator()
				+ defineProperties(classifier) + ");"
				+ appendLineSeparator();
		return createTable;
	}

	protected String primaryKeyConstraint(Classifier classifier) {
		List<Property> pkProp = getPKProperties(classifier);
		if (pkProp.isEmpty()) {
			return "";
		}

		StringBuilder pk = new StringBuilder();
		for (int i = 0; i < pkProp.size(); i++) {
			Property property = pkProp.get(i);
			pk.append("\"" + property.getName() + "\"");
			if (i != pkProp.size() - 1) {
				pk.append(", ");
			}
		}
		String classifierName = classifier.getName();
		String statement = "ALTER TABLE " + classifierName + " ADD CONSTRAINT PK_" + classifierName
				+ " PRIMARY KEY(" + pk.toString() + ");";
		return statement;
	}

	protected String defineProperties(Classifier classifier) {
		StringBuilder sb = new StringBuilder();
		List<Property> attributes = getOwnedAttributes(classifier);
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

	protected String defineColumn(Property property) {

		String tableColumn = property.getName() + " " + compileDataType(property)
				+ defaultValue(ColumnUtil.getDefaultValue(property)) +
				nullable(ColumnUtil.getNullable(property)) + unique(ColumnUtil.getUnique(property));
		return tableColumn;
	}

}
