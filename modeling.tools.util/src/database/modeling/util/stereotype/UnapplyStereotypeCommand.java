package database.modeling.util.stereotype;

import java.util.Collection;

import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Stereotype;

public class UnapplyStereotypeCommand extends RecordingCommand {

	private Element element;

	private String stereotypeQualifiedName;

	private Collection<Stereotype> stereotypes;

	public UnapplyStereotypeCommand(Element element, String stereotypeQualifiedName,
			TransactionalEditingDomain domain) {
		super(domain);
		this.element = element;
		this.stereotypeQualifiedName = stereotypeQualifiedName;
	}

	@Override
	protected void doExecute() {
		Stereotype appliedStereotype = element.getAppliedStereotype(stereotypeQualifiedName);
		if (appliedStereotype != null) {
			element.unapplyStereotype(appliedStereotype);
		}
	}
}
