package model.transfer.importer;

import java.util.Collection;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLFactory;

import database.modeling.util.uml.ModelObjectUtil;
import model.transfer.export.ClassSummarySheetCreator;
import model.transfer.export.InterfaceSummarySheetCreator;
import model.transfer.util.CellUtil;
import model.transfer.util.ExcelReaderUtil;

public class EntityCreator extends ObjectImporter {

	public EntityCreator(Workbook workbook, Package modelPackage) {
		super(workbook, modelPackage);
	}

	public void createEntities() {
		createInterfaces();
		createClasses();
	}

	public void removeDeletedEntities() {
		removeDeletedInterfaces();
		removeDeletedClasses();
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

}
