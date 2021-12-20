package uml.papyrus.script.generator;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;

import database.modeling.util.stereotype.ColumnUtil;
import database.modeling.util.uml.ModelObjectUtil;

public class OracleScriptGenerator extends ScriptGenerator {

	public static final String IDENTIFIER = "Oracle";

	public OracleScriptGenerator(Package modelPackage, String filePath) {
		super(modelPackage, filePath);
	}

	public void generateDdlScript() {
		Collection<Classifier> classifiers = ModelObjectUtil.getClassifiers(modelPackage.allOwnedElements());
		StringBuilder sb = new StringBuilder();
		for (Classifier classifier : classifiers) {
			sb.append(createTableScript(classifier));
			sb.append(appendLineSeparator());
		}
	}

	private String createTableScript(Classifier classifier) {
		String createTable = "CREATE TABLE " + classifier.getName() + " ("
				+ appendLineSeparator()
				+ defineProperties(classifier) + ");"
				+ appendLineSeparator();
		String alterTable = alterTableAddPrimaryKey(classifier) + appendLineSeparator(2);

		String completeSegment = createTable + alterTable;
		return completeSegment;
	}

	private String alterTableAddPrimaryKey(Classifier classifier) {
		// TODO Auto-generated method stub
		return null;
	}

	private String defineProperties(Classifier classifier) {
		StringBuilder sb = new StringBuilder();
		EList<Property> attributes = classifier.getAttributes();
		for (Property property : attributes) {
			sb.append(appendTab() + defineColumn(property) + appendLineSeparator());
		}

		return sb.toString();
	}

	private String defineColumn(Property property) {

		String tableColumn = property.getName() + " " + compileDataType(property)
				+ nullable(ColumnUtil.getNullable(property));
		return tableColumn;
	}

	private String compileDataType(Property property) {
		String dataType = ColumnUtil.getDataType(property);

		return null;
	}

}
