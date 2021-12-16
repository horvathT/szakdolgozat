package uml.papyrus.script.generator;

import java.util.Collection;
import java.util.HashSet;
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
import database.modeling.util.stereotype.StereotypeManagementUtil;
import database.modeling.util.uml.ModelObjectUtil;

public class ModelValidator {

	public static final Logger log = LoggerFactory.getLogger(ModelValidator.class);

	private Package modelPackage;

	private Set<Property> propertyMissingTypeErrorMessage;

	private Shell shell;

	public ModelValidator(Package modelPackage) {
		this.modelPackage = modelPackage;
		shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
	}

	public void validateModel() {
		propertyMissingTypeErrorMessage = new HashSet<>();
		everyPropertyHasType();
		if (!propertyMissingTypeErrorMessage.isEmpty()) {
			String errorMessage = compilePropertyMissingSqlTypeErrorMessage();
			MessageDialog.openError(shell, "Generálási hiba", errorMessage);
			System.exit(1);
		}

	}

	private String compilePropertyMissingSqlTypeErrorMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Generálás sikertelen! A következő attribútumok nem rendelekeznek SQL adattípussal: "
				+ System.lineSeparator());
		for (Property property : propertyMissingTypeErrorMessage) {
			Element owner = property.getOwner();
			if (owner instanceof Classifier) {
				Classifier classifier = (Classifier) owner;
				sb.append(classifier.getName() + "." + property.getName() + System.lineSeparator());
			}
		}
		return sb.toString();
	}

	private void everyPropertyHasType() {
		Collection<Property> properties = ModelObjectUtil.getProperties(modelPackage.allOwnedElements());
		for (Property property : properties) {
			if (property.getAssociation() != null) {
				continue;
			}

			boolean hasStereotype = StereotypeManagementUtil.hasStereotype(property,
					ColumnUtil.STEREOTYPE_QUALIFIED_NAME);
			if (!hasStereotype) {
				propertyMissingTypeErrorMessage.add(property);
			}
		}

	}

}
