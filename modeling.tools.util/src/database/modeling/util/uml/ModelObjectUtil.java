package database.modeling.util.uml;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLPackage;

import database.modeling.util.resource.EclipseResourceUtil;

/**
 * UML modellen gyakran alkalmazott függvények gyűjteménye.
 * 
 * @author Horváth Tibor
 *
 */
public class ModelObjectUtil {

	public static Collection<Property> getProperties(EList<Element> elementList) {
		return EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.PROPERTY);
	}

	public static Collection<Interface> getInterfaces(EList<Element> elementList) {
		return EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.INTERFACE);
	}

	public static Collection<Class> getClasses(EList<Element> elementList) {
		return EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.CLASS);
	}

	public static Collection<Classifier> getClassifiers(EList<Element> elementList) {
		Collection<Classifier> objectsByType = EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.INTERFACE);
		objectsByType.addAll(EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.CLASS));
		return objectsByType;
	}

	public static Collection<Enumeration> getEnumerations(EList<Element> elementList) {
		return EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.ENUMERATION);
	}

	public static Collection<Association> getAssociations(EList<Element> elementList) {
		return EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.ASSOCIATION);
	}

	public static Collection<DataType> getDataTypes(EList<Element> elementList) {
		Collection<DataType> dataTypes = EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.DATA_TYPE);
		dataTypes.removeIf(dt -> dt instanceof Enumeration);
		return dataTypes;
	}

	public static Set<DataType> getDataTypesFromModel(Package modelPackage) {
		Set<DataType> dataTypes = new HashSet<>();

		Model model = modelPackage.getModel();
		ResourceSet resourceSet = model.eResource().getResourceSet();
		for (Resource resource : resourceSet.getResources()) {
			Collection<DataType> dataTypesPreFilter = getObjectsByType(resource.getAllContents(),
					UMLPackage.Literals.DATA_TYPE);
			Set<DataType> filteredDataTypes = dataTypesPreFilter.stream().filter(d -> !(d instanceof Enumeration))
					.collect(Collectors.toSet());
			dataTypes.addAll(filteredDataTypes);
		}
		return dataTypes;
	}

	/**
	 * Új komment létrehozása a paraméterben akpott tartalommla a paraméterben
	 * kapott elemen.
	 * 
	 * @param element
	 * @param comment
	 */
	public static void addComment(NamedElement element, String comment) {
		if (!comment.isEmpty()) {
			element.createOwnedComment().setBody(comment);
		}
	}

	public static <T> Collection<T> getObjectsByType(TreeIterator<EObject> elements,
			EClassifier type) {
		Collection<T> collection = new HashSet<T>();
		while (elements.hasNext()) {
			EObject next = elements.next();
			if (type.isInstance(next)) {
				T element = (T) next;
				collection.add(element);
			}
		}
		return collection;
	}

	/**
	 * Egyedi azonosító alapján megkeresi a hozzá tartozó {@link Property}-t. Nem
	 * létező elem esetén nullal tér vissza.
	 * 
	 * @param xmiId
	 * @param model
	 * @return
	 */
	public static Property getPropertyByXmiId(String xmiId, Model model) {
		Collection<Property> propertiesFromModel = getPropertiesFromModel(model);
		for (Property property : propertiesFromModel) {
			if (EcoreUtil.getURI(property).fragment().equals(xmiId)) {
				return property;
			}
		}
		return null;
	}

	public static Collection<Property> getPropertiesFromModel(Model model) {
		return getProperties(model.allOwnedElements());
	}

	public static IPath getModelFilePath(Model model) {
		return EclipseResourceUtil.getResourceFileLocationPath(model.eResource());
	}
}
