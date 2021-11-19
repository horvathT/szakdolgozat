package database.modeling.util.resource;

import java.util.Collection;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLPackage;

public class EclipseModelUtil {

	public static Property getPropertyByXmiId(String xmiId, Model model) {
		Collection<Property> propertiesFromModel = getPropertiesFromModel(model);
		for (Property property : propertiesFromModel) {
			if (EcoreUtil.getURI(property).fragment().equals(xmiId)) {
				return property;
			}
		}
		return null;
	}

	public static Collection<Property> getProperties(EList<Element> elementList) {
		return EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.PROPERTY);
	}

	public static Collection<Property> getPropertiesFromModel(Model model) {
		return getProperties(model.allOwnedElements());
	}

	public static IPath getModelFilePath(Model model) {
		return EclipseResourceUtil.getResourceFileLocationPath(model.eResource());
	}
}
