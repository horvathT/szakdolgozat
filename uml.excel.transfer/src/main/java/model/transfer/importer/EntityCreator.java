package model.transfer.importer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

import mode.transfer.export.ClassSummarySheetCreator;
import mode.transfer.export.EnumSheetCreator;
import mode.transfer.export.InterfaceSummarySheetCreator;
import mode.transfer.util.CellUtil;
import mode.transfer.util.ExcelReaderUtil;
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

	public void removeDeletedEntities() {
		removeDeletedInterfaces();
		removeDeletedClasses();
		removeDeletedEnumerations();
	}

	private void removeDeletedEnumerations() {
		Sheet enumSheet = workbook.getSheet(EnumSheetCreator.SHEET_NAME);
		Collection<Enumeration> enumerations = ModelObjectUtil.getEnumerations(modelPackage.allOwnedElements());
		List<String> enumNamesInExcel = collectEntityNamesFromSheet(enumSheet);
		for (Enumeration enumeration : enumerations) {
			if (!enumNamesInExcel.contains(enumeration.getName())) {
				EcoreUtil.delete(enumeration);
			}
		}
	}

	private void removeDeletedClasses() {
		Sheet classSheet = workbook.getSheet(ClassSummarySheetCreator.SHEET_NAME);
		Collection<Class> classes = ModelObjectUtil.getClasses(modelPackage.allOwnedElements());
		List<String> classNamesInExcel = collectEntityNamesFromSheet(classSheet);
		for (Class clazz : classes) {
			if (!classNamesInExcel.contains(clazz.getName())) {
				EcoreUtil.delete(clazz);
			}
		}
	}

	private void removeDeletedInterfaces() {
		Sheet interfaceSheet = workbook.getSheet(InterfaceSummarySheetCreator.SHEET_NAME);
		Collection<Interface> interfaces = ModelObjectUtil.getInterfaces(modelPackage.allOwnedElements());
		List<String> interfaceNamesInExcel = collectEntityNamesFromSheet(interfaceSheet);
		for (Interface interfac : interfaces) {
			if (!interfaceNamesInExcel.contains(interfac.getName())) {
				EcoreUtil.delete(interfac);
			}
		}
	}

	private List<String> collectEntityNamesFromSheet(Sheet interfaceSheet) {
		List<String> interfacesInExcel = new ArrayList<String>();
		int rowNum = interfaceSheet.getLastRowNum();
		for (int i = 1; i <= rowNum; i++) {
			String interfaceName = CellUtil.getStringCellValue(interfaceSheet.getRow(i).getCell(1));
			if (!interfaceName.isEmpty()) {
				interfacesInExcel.add(interfaceName);
			}
		}
		return interfacesInExcel;
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
			String literalName = CellUtil.getStringCellValue(row.getCell(2));
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
		if (name.isEmpty()) {
			return;
		}
		String visibility = CellUtil.getStringCellValue(row.getCell(2));
		String isAbstract = CellUtil.getStringCellValue(row.getCell(3));
		String comment = CellUtil.getStringCellValue(row.getCell(6));

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
		clazz.setVisibility(ExcelReaderUtil.stringToVisibilityKind(visibility));
		clazz.setIsAbstract(ExcelReaderUtil.stringToBoolean(isAbstract));
		ModelObjectUtil.addComment(clazz, comment);
		return clazz;
	}

	private void createInterfaces() {
		Sheet interfaceSheet = workbook.getSheet(InterfaceSummarySheetCreator.SHEET_NAME);
		Collection<Interface> interfaces = ModelObjectUtil.getInterfaces(modelPackage.allOwnedElements());
		int rowNum = interfaceSheet.getLastRowNum();
		for (int i = 1; i <= rowNum; i++) {
			createInterfaceFromRow(interfaceSheet.getRow(i), interfaces);
		}
	}

	private void createInterfaceFromRow(Row row, Collection<Interface> interfaces) {
		String xmiId = CellUtil.getStringCellValue(row.getCell(0));
		String name = CellUtil.getStringCellValue(row.getCell(1));
		if (name.isEmpty()) {
			return;
		}
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
	}

	private void setInterfaceParameters(String interfaceName, String visibility, String comment, Interface interfac) {
		interfac.setName(interfaceName);
		interfac.setVisibility(ExcelReaderUtil.stringToVisibilityKind(visibility));
		ModelObjectUtil.addComment(interfac, comment);
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
