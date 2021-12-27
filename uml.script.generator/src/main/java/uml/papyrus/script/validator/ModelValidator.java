package uml.papyrus.script.validator;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import database.modeling.util.stereotype.ColumnUtil;
import database.modeling.util.stereotype.FKUtil;
import database.modeling.util.stereotype.StereotypeManagementUtil;
import database.modeling.util.uml.ModelObjectUtil;

public class ModelValidator {

	private static final Bundle BUNDLE = FrameworkUtil.getBundle(ModelValidator.class);
	private static final ILog LOGGER = Platform.getLog(BUNDLE);

	private Package modelPackage;

	private Shell shell;

	private Collection<Property> properties;

	private Map<Property, Property> fkTypeConsistency = new HashMap<>();
	private Map<Property, Property> fkReferencedAttrNullable = new HashMap<>();
	private Set<Property> fkMissingReference = new HashSet<>();

	public ModelValidator(Package modelPackage) {
		this.modelPackage = modelPackage;
		shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		properties = ModelObjectUtil.getProperties(modelPackage.allOwnedElements());
	}

	public void validateModel() {
		Set<Property> propertyMissingType = checkPropertiesForSqlType();

		if (!propertyMissingType.isEmpty()) {
			String errorMessage = compilePropertyMissingSqlTypeErrorMessage(propertyMissingType);
			validationErrorMessage(errorMessage);
		}

		checkFkReferenceConsistency();

		if (!fkMissingReference.isEmpty()) {
			String errorMessage = compileFkMissingReferenceErrorMessage();
			validationErrorMessage(errorMessage);
		}

		if (!fkTypeConsistency.isEmpty()) {
			String errorMessage = compileFkTypeConsistencyErrorMessage();
			validationErrorMessage(errorMessage);
		}

		if (!fkReferencedAttrNullable.isEmpty()) {
			String errorMessage = compileFkReferencedAttrNullableErrorMessage();
			validationErrorMessage(errorMessage);
		}

	}

	private String compileFkReferencedAttrNullableErrorMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append(
				"Generation failed! The following foreign keys are referring a nullable attribute: "
						+ System.lineSeparator());

		for (Entry<Property, Property> entry : fkReferencedAttrNullable.entrySet()) {

			Property key = entry.getKey();
			Property value = entry.getValue();

			LOGGER.error(
					"The following foreign key is referring a nullable attribute: "
							+ key.getName() + " -> " + value.getName());

			sb.append(key.getName() + " -> " + value.getName() + System.lineSeparator());
		}
		return sb.toString();
	}

	private String compileFkTypeConsistencyErrorMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append(
				"Generation failed! The following foreing keys and their referred attributes have different type: "
						+ System.lineSeparator());

		for (Entry<Property, Property> entry : fkTypeConsistency.entrySet()) {

			Property key = entry.getKey();
			Property value = entry.getValue();

			LOGGER.error(
					" The following foreing key and it's referred attribute have different type: "
							+ key.getName() + " -> " + value.getName());

			sb.append(key.getName() + " -> " + value.getName() + System.lineSeparator());
		}
		return sb.toString();
	}

	private String compileFkMissingReferenceErrorMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append(
				"Generation failed! The following attributes are marked as foreing key,"
						+ " but have no referred attribute set: "
						+ System.lineSeparator());

		for (Property property : fkMissingReference) {
			LOGGER.error(
					"The following attribute is marked as foreing key,"
							+ " but have no referred attribute set: "
							+ property.getName());

			Element owner = property.getOwner();
			if (owner instanceof Classifier) {
				Classifier classifier = (Classifier) owner;
				sb.append(classifier.getName() + "." + property.getName() + System.lineSeparator());
			}
		}
		return sb.toString();
	}

	private void validationErrorMessage(String errorMessage) {
		MessageDialog.openError(shell, "Generation failed", errorMessage);
		System.exit(1);
	}

	private void checkFkReferenceConsistency() {
		for (Property property : properties) {
			boolean hasStereotype = StereotypeManagementUtil.hasStereotype(property,
					FKUtil.STEREOTYPE_QUALIFIED_NAME);
			if (hasStereotype) {

				String referencedEntityName = FKUtil.getReferencedEntity(property);
				String referencedPropertyName = FKUtil.getReferencedProperty(property);
				if (referencedEntityName.isEmpty() || referencedPropertyName.isEmpty()) {
					fkMissingReference.add(property);
					continue;
				}

				Classifier classifier = getEntityByName(referencedEntityName);
				if (classifier == null) {
					fkMissingReference.add(property);
					continue;
				}
				Property propertyByName = getPropertyByName(referencedPropertyName, classifier.getAttributes());
				if (propertyByName == null) {
					fkMissingReference.add(property);
					continue;
				}

				String dataType = ColumnUtil.getDataType(property);
				String referredDataType = ColumnUtil.getDataType(propertyByName);
				if (!dataType.equals(referredDataType)) {
					fkTypeConsistency.put(property, propertyByName);
				}

				boolean nullable = ColumnUtil.getNullable(propertyByName);
				if (nullable) {
					fkReferencedAttrNullable.put(property, propertyByName);
				}

			}
		}
	}

	private Property getPropertyByName(String referencedPropertyName, List<Property> properties) {
		for (Property property : properties) {
			String name = property.getName();
			if (name.equals(referencedPropertyName)) {
				return property;
			}
		}
		return null;
	}

	private Classifier getEntityByName(String referencedEntityName) {
		Collection<Classifier> classifiers = ModelObjectUtil.getClassifiers(modelPackage.allOwnedElements());
		for (Classifier classifier : classifiers) {
			String name = classifier.getName();
			if (name.equals(referencedEntityName)) {
				return classifier;
			}
		}
		return null;
	}

	private String compilePropertyMissingSqlTypeErrorMessage(Set<Property> propertyMissingType) {
		StringBuilder sb = new StringBuilder();
		sb.append("Generation failed! The following attributes have no SQL data type set: "
				+ System.lineSeparator());
		for (Property property : propertyMissingType) {
			LOGGER.error(
					"The following attribute has not SQL data type set:  " + property.getName());
			Element owner = property.getOwner();
			if (owner instanceof Classifier) {
				Classifier classifier = (Classifier) owner;
				sb.append(classifier.getName() + "." + property.getName() + System.lineSeparator());
			}
		}
		return sb.toString();
	}

	private Set<Property> checkPropertiesForSqlType() {
		Set<Property> typelessProperties = new HashSet<>();
		for (Property property : properties) {
			if (property.getAssociation() != null) {
				continue;
			}

			boolean hasStereotype = StereotypeManagementUtil.hasStereotype(property,
					ColumnUtil.STEREOTYPE_QUALIFIED_NAME);
			if (!hasStereotype) {
				typelessProperties.add(property);
			}
		}
		return typelessProperties;
	}

}
