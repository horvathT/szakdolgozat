package uml.papyrus.script.generator;

import java.util.List;
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

		return " DEFAULT " + defaultValue;
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
}
