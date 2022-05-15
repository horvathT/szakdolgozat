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

import database.modeling.util.resource.EclipseResourceUtil;
import database.modeling.util.stereotype.ColumnUtil;
import database.modeling.util.stereotype.FKUtil;
import database.modeling.util.stereotype.PKUtil;
import database.modeling.util.stereotype.StereotypeManagementUtil;
import database.modeling.util.uml.ModelObjectUtil;
import database.modeling.viewmodel.DataTypeDefinition;
import uml.papyrus.script.validator.ModelValidator;

/**
 * Script generáláshoz létrehozott ősosztály, tartalmazza a script generáláshoz
 * legszükségesebb metódusokat.
 * 
 * @author Horváth Tibor
 *
 */
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

	/**
	 * Paraméterben meghatározott mennyiségű új sor String reprezentációjával tér
	 * vissza.
	 * 
	 * @param numOfEmptyLines
	 * @return
	 */
	protected String appendLineSeparator(int numOfEmptyLines) {
		StringBuilder strb = new StringBuilder();
		if (numOfEmptyLines > 0) {
			for (int i = 0; i < numOfEmptyLines; ++i) {
				strb.append(appendLineSeparator());
			}
		}
		return strb.toString();
	}

	/**
	 * Új sor String reprezentációjával tér vissza.
	 * 
	 * @return
	 */
	protected String appendLineSeparator() {
		return System.lineSeparator();
	}

	/**
	 * Paraméterben meghatározott mennyiségű tab String reprezentációjával tér
	 * vissza.
	 * 
	 * @param numOfTabs
	 * @return
	 */
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

	/**
	 * Igaz érték esetén "NULL", hamis esetén "NOT NULL" értéket ad vissza.
	 * 
	 * @param isNullable
	 * @return
	 */
	protected String nullable(boolean isNullable) {
		if (isNullable) {
			return " NULL";
		}
		return " NOT NULL";
	}

	/**
	 * Igaz érték esetén "UNIQUE", hamis esetén üres értéket ad vissza.
	 * 
	 * @param isNullable
	 * @return
	 */
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

	/**
	 * Név alapján visszatér a hozzá tartozó {@link DataTypeDefinition}-el
	 * 
	 * @param name
	 * @return
	 * @throws IllegalArgumentException ha nincs a névhez tartózó
	 *                                  {@link DataTypeDefinition}
	 */
	protected DataTypeDefinition getTypeDefinitionByName(String name) {
		for (DataTypeDefinition dataTypeDefinition : dataTypes) {
			if (dataTypeDefinition.getName().equals(name)) {
				return dataTypeDefinition;
			}
		}
		throw new IllegalArgumentException("Nem található típus " + name + " névvel");
	}

	/**
	 * Visszatér a Classifier saját attribútumaival amelyek nem tartoznak
	 * asszociációhoz.
	 * 
	 * @param classifier
	 * @return
	 */
	protected List<Property> getOwnedAttributes(Classifier classifier) {
		EList<Property> attributes = classifier.getAttributes();
		return attributes.stream().filter(p -> p.getAssociation() == null).collect(Collectors.toList());
	}

	/**
	 * Összegyűjti azokat az attribútumokat amelyeken van FK sztereotípus.
	 * 
	 * @param classifier
	 * @return
	 */
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

	/**
	 * Összegyűjti azokat az attribútumokat amelyeken van PK sztereotípus.
	 * 
	 * @param classifier
	 * @return
	 */
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

	/**
	 * A sztereotípusokban szereplő adatok alapján összeállítja a típus DDL
	 * megfelelőjét.
	 * 
	 * @param property
	 * @return
	 */
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

	/**
	 * Generálja a DDL scriptet.
	 */
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

	/**
	 * Összeállítja a classifier idegenkulcs megszorításait
	 * 
	 * @param classifier
	 * @return
	 */
	protected String foreignKeyConstraints(Classifier classifier) {
		StringBuilder fkConstraints = new StringBuilder();

		Map<String, List<Property>> fkMap = getFKProperties(classifier);
		if (fkMap.isEmpty()) {
			return "";
		}
		for (Entry<String, List<Property>> entry : fkMap.entrySet()) {
			fkConstraints.append(foreignKeyConstraint(classifier.getName(), entry));
		}

		return fkConstraints.toString();
	}

	/**
	 * Összeállítja a classifier idegenkulcs megszorítását
	 * 
	 * @param classifier
	 * @return
	 */
	protected String foreignKeyConstraint(String classifierName,
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

	/**
	 * Összeállítja a classifier elsődleges kulcs megszorításait
	 * 
	 * @param classifier
	 * @return
	 */
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

	/**
	 * Tábla oszlopainak definiálása.
	 * 
	 * @param classifier
	 * @return
	 */
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

	/**
	 * Attribútumhoz tartozó oszlop definiálása.
	 * 
	 * @param property
	 * @return
	 */
	protected String defineColumn(Property property) {

		String tableColumn = property.getName() + " " + compileDataType(property)
				+ defaultValue(ColumnUtil.getDefaultValue(property)) +
				nullable(ColumnUtil.getNullable(property)) + unique(ColumnUtil.getUnique(property));
		return tableColumn;
	}

}
