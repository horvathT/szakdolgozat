package model.transfer.importer;

import java.util.Collection;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.VisibilityKind;

import mode.transfer.export.ClassSummarySheetCreator;
import mode.transfer.export.EnumSheetCreator;
import mode.transfer.export.InterfaceSummarySheetCreator;
import mode.transfer.util.CellUtil;
import mode.transfer.util.ModelObjectUtil;

public class EntityCreator {

	private Package modelPackage;

	private Workbook workbook;

	public EntityCreator(Workbook workbook, Package modelPackage) {
		this.workbook = workbook;
		this.modelPackage = modelPackage;
	}

	public void createEntities() {
		createInterfaces();
		createClasses();
		createEnums();
	}

	private void createEnums() {
		Sheet enumSheet = workbook.getSheet(EnumSheetCreator.SHEET_NAME);
		Collection<Enumeration> enumerations = ModelObjectUtil.getEnumerations(modelPackage.allOwnedElements());
		Enumeration currentEnum = null;
		int rowNum = enumSheet.getLastRowNum();
		for (int i = 1; i <= rowNum; i++) {
			Row row = enumSheet.getRow(i);

			String xmiId = CellUtil.getStringCellValue(row.getCell(0));
			String enumName = CellUtil.getStringCellValue(row.getCell(1));
			if (!enumName.isEmpty()) {
				Enumeration enumeration = (Enumeration) getByXmiId(enumerations, xmiId);
				if (enumeration != null) {
					currentEnum = enumeration;
				} else {
					currentEnum = UMLFactory.eINSTANCE.createEnumeration();
					modelPackage.getPackagedElements().add(currentEnum);
					currentEnum.setName(enumName);
				}
			}
			String literalName = CellUtil.getStringCellValue(row.getCell(0));
			if (!literalName.isEmpty() && currentEnum != null) {
				EnumerationLiteral literal = (EnumerationLiteral) getByXmiId(currentEnum.getOwnedLiterals(), xmiId);
				if (literal != null) {
					literal.setName(literalName);
				} else {
					EnumerationLiteral newLiteral = UMLFactory.eINSTANCE.createEnumerationLiteral();
					newLiteral.setName(literalName);
					currentEnum.getOwnedLiterals().add(newLiteral);
				}

			}
		}
	}

	private void createClasses() {
		Sheet classSheet = workbook.getSheet(ClassSummarySheetCreator.SHEET_NAME);
		Collection<org.eclipse.uml2.uml.Class> classes = ModelObjectUtil.getClasses(modelPackage.allOwnedElements());
		int rowNum = classSheet.getLastRowNum();
		for (int i = 1; i <= rowNum; i++) {
			createClassFromRow(classSheet.getRow(i), classes);
		}
	}

	private void createClassFromRow(Row row, Collection<Class> classes) {
		String xmiId = CellUtil.getStringCellValue(row.getCell(0));
		String name = CellUtil.getStringCellValue(row.getCell(1));
		String visibility = CellUtil.getStringCellValue(row.getCell(2));
		String isAbstract = CellUtil.getStringCellValue(row.getCell(3));
		String comment = CellUtil.getStringCellValue(row.getCell(6));

		if (name.isEmpty()) {
			return;
		}

		Class clazz = (Class) getByXmiId(classes, xmiId);
		if (clazz != null) {
			setClassParameters(name, visibility, isAbstract, comment, clazz);
		} else {
			Class newClazz = UMLFactory.eINSTANCE.createClass();
			modelPackage.getPackagedElements().add(newClazz);
			setClassParameters(name, visibility, isAbstract, comment, newClazz);
		}
	}

	private Class setClassParameters(String name, String visibility, String isAbstract, String comment, Class clazz) {
		clazz.setName(name);
		clazz.setVisibility(stringToVisibilityKind(visibility));
		clazz.setIsAbstract(stringToBoolean(isAbstract));
		if (!comment.isEmpty()) {
			clazz.createOwnedComment().setBody(comment);
		}
		return null;
	}

	private void createInterfaces() {
		Sheet interfaceSheet = workbook.getSheet(InterfaceSummarySheetCreator.SHEET_NAME);
		Collection<Interface> interfaces = ModelObjectUtil.getInterfaces(modelPackage.allOwnedElements());
		int rowNum = interfaceSheet.getLastRowNum();
		for (int i = 1; i <= rowNum; i++) {
			createInterfaceFromRow(interfaceSheet.getRow(i), interfaces);
		}
	}

	private Interface createInterfaceFromRow(Row row, Collection<Interface> interfaces) {
		String xmiId = CellUtil.getStringCellValue(row.getCell(0));
		String name = CellUtil.getStringCellValue(row.getCell(1));
		String visibility = CellUtil.getStringCellValue(row.getCell(2));
		String comment = CellUtil.getStringCellValue(row.getCell(4));

		Interface interfac = (Interface) getByXmiId(interfaces, xmiId);
		if (interfac != null) {
			setInterfaceParameters(name, visibility, comment, interfac);
		} else {
			Interface newInterfac = UMLFactory.eINSTANCE.createInterface();
			modelPackage.getPackagedElements().add(newInterfac);
			setInterfaceParameters(name, visibility, comment, newInterfac);
		}
		return interfac;
	}

	private void setInterfaceParameters(String interfaceName, String visibility, String comment, Interface interfac) {
		interfac.setName(interfaceName);
		interfac.setVisibility(stringToVisibilityKind(visibility));
		if (!comment.isEmpty()) {
			interfac.createOwnedComment().setBody(comment);
		}
	}

	private VisibilityKind stringToVisibilityKind(String visibility) {
		if (visibility.equals("public")) {
			return VisibilityKind.PUBLIC_LITERAL;
		} else if (visibility.equals("private")) {
			return VisibilityKind.PRIVATE_LITERAL;
		} else if (visibility.equals("protected")) {
			return VisibilityKind.PROTECTED_LITERAL;
		} else if (visibility.equals("package")) {
			return VisibilityKind.PACKAGE_LITERAL;
		}
		throw new IllegalArgumentException("Hibás láthatósági paraméter: " + visibility);
	}

	private boolean stringToBoolean(String boolString) {
		if ("igen".equals(boolString)) {
			return true;
		} else if ("nem".equals(boolString)) {
			return false;
		}
		throw new IllegalArgumentException("Hibás igen/nem paraméter: " + boolString);
	}

	private EObject getByXmiId(Collection<? extends EObject> classifiers, String xmiId) {
		for (EObject classifier : classifiers) {
			String fragment = EcoreUtil.getURI(classifier).fragment();
			if (fragment.equals(xmiId)) {
				return classifier;
			}
		}
		return null;
	}

}
