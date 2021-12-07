package mode.transfer.util;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.UMLPackage;

public class ModelObjectUtil {

	public static Collection<Interface> getInterfaces(EList<Element> elementList) {
		return EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.INTERFACE);
	}

	public static Collection<Class> getClasses(EList<Element> elementList) {
		return EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.CLASS);
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

}
