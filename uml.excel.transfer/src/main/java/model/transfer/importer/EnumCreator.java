package model.transfer.importer;

import java.util.Collection;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLFactory;

import database.modeling.util.uml.ModelObjectUtil;
import mode.transfer.export.EnumSheetCreator;
import mode.transfer.util.CellUtil;

public class EnumCreator extends ObjectImporter {

	public EnumCreator(Workbook workbook, Package modelPackage) {
		super(workbook, modelPackage);
	}

	public void createEnums() {
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

	public void removeDeletedEnumerations() {
		Sheet enumSheet = workbook.getSheet(EnumSheetCreator.SHEET_NAME);
		Collection<Enumeration> enumerations = ModelObjectUtil.getEnumerations(modelPackage.allOwnedElements());
		List<String> enumNamesInExcel = collectEntityNamesFromSheet(enumSheet);
		for (Enumeration enumeration : enumerations) {
			if (!enumNamesInExcel.contains(enumeration.getName())) {
				EcoreUtil.delete(enumeration);
			}
		}
	}

}
