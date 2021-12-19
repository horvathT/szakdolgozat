package uml.papyrus.script.generator;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import database.modeling.util.stereotype.ColumnUtil;
import database.modeling.util.stereotype.FKUtil;
import database.modeling.util.stereotype.StereotypeManagementUtil;
import database.modeling.util.uml.ModelObjectUtil;

public class ModelValidator {

	public static final Logger log = LoggerFactory.getLogger(ModelValidator.class);

	private Package modelPackage;

	private Shell shell;

	private Collection<Property> properties;

	public ModelValidator(Package modelPackage) {
		this.modelPackage = modelPackage;
		shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		properties = ModelObjectUtil.getProperties(modelPackage.allOwnedElements());
	}

	public void validateModel() {
		Set<Property> propertyMissingType = checkPropertiesForSqlType();

		if (!propertyMissingType.isEmpty()) {
			String errorMessage = compilePropertyMissingSqlTypeErrorMessage(propertyMissingType);
			MessageDialog.openError(shell, "Generálási hiba", errorMessage);
			System.exit(1);
		}
		Set<Property> fkMissingReference = checkFkMissingReference();

		Map<Property, Property> fkTypeConsistency = checkFkTypeConsitency();

	}

	/**
	 * Ellenőrzés, hogy az idegenkulcshoz be van-e állítva hivatkozott attribútum. A
	 * hibás elemek egy Set-be kerülnek összegyűjtésre.
	 * 
	 * @return Set<Property>
	 */
	private Set<Property> checkFkMissingReference() {
		Set<Property> fkMissingReference = new HashSet<>();
		for (Property property : properties) {
			boolean hasStereotype = StereotypeManagementUtil.hasStereotype(property,
					FKUtil.STEREOTYPE_QUALIFIED_NAME);
			if (hasStereotype) {
				String referencedEntityName = FKUtil.getReferencedEntity(property);
				String referencedPropertyName = FKUtil.getReferencedProperty(property);
			}
		}

		return fkMissingReference;
	}

	/**
	 * Összegyűjti azon idegenkulcs - hivatkozott attribútum párokat amelyek typusa
	 * nem egyezik, vagy a hivatkozott attribútum nullozható.
	 * 
	 * @return Map<Property, Property>
	 */
	private Map<Property, Property> checkFkTypeConsitency() {
		Map<Property, Property> fkTypeConsistency = new HashMap<>();

		return fkTypeConsistency;
	}

	private String compilePropertyMissingSqlTypeErrorMessage(Set<Property> propertyMissingType) {
		StringBuilder sb = new StringBuilder();
		sb.append("Generálás sikertelen! A következő attribútumok nem rendelekeznek SQL adattípussal: "
				+ System.lineSeparator());
		for (Property property : propertyMissingType) {
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
