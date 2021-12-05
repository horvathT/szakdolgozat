package mode.transfer.export;

import java.util.Collection;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.NamedElement;

import mode.transfer.export.util.CellAppender;

public class ClassSummarySheetCreator {

	private final String SHEET_NAME = "Osztályok";

	private Workbook workbook;

	private Collection<Class> classes;

	public ClassSummarySheetCreator(Workbook workbook, Collection<Class> classes) {
		this.workbook = workbook;
		this.classes = classes;
	}

	public Sheet createSheet() {
		Sheet classesSheet = workbook.createSheet(SHEET_NAME);
		createEntityHeaderRow(classesSheet);
		fillEntityRows(classes, classesSheet);
		return classesSheet;
	}

	private void fillEntityRows(Collection<Class> classes, Sheet classesSheet) {
		int rowNumber = 1;
		for (Class clazz : classes) {
			rowNumber = fillEntityRow(classesSheet, rowNumber, clazz);
		}
	}

	private int fillEntityRow(Sheet classesSheet, int rowNumber, Class clazz) {
		Row row = classesSheet.createRow(rowNumber);

		EList<Interface> implementedInterfaces = clazz.getImplementedInterfaces();

		CellAppender classifierRow = new CellAppender(row);
		classifierRow.appendCellWithValue(
				EcoreUtil.getURI(clazz).fragment())
				.appendCellWithValue(clazz.getName())
				.appendCellWithValue(clazz.getVisibility().toString())
				.appendCellWithValue(booleanTostring(clazz.isAbstract()))
				.appendCellWithValue(getParentClassName(clazz))
				.appendCellWithValue(getFirstImplementedInterfaceName(implementedInterfaces))
				.appendCellWithValue(getFirstComment(clazz));

		rowNumber = appendRemainingImplementedInterfacLines(++rowNumber, classesSheet, implementedInterfaces);
		return rowNumber;
	}

	private int appendRemainingImplementedInterfacLines(int rowNumber, Sheet classesSheet,
			EList<Interface> implementedInterfaces) {
		for (int i = 1; i < implementedInterfaces.size(); i++) {
			Row row = classesSheet.createRow(rowNumber);
			Cell cell = row.createCell(5);
			Interface interfac = implementedInterfaces.get(i);
			cell.setCellValue(interfac.getName());

			rowNumber++;
		}
		return rowNumber;
	}

	private String getFirstImplementedInterfaceName(EList<Interface> implementedInterfaces) {
		if (implementedInterfaces.isEmpty()) {
			return "";
		}
		return implementedInterfaces.get(0).getName();
	}

	private String getParentClassName(Class clazz) {
		EList<Generalization> generalizations = clazz.getGeneralizations();
		if (generalizations.isEmpty()) {
			return "";
		}
		Generalization generalization = generalizations.get(0);
		Classifier general = generalization.getGeneral();

		return general.getName();
	}

	private String booleanTostring(boolean bool) {
		if (bool) {
			return "y";
		}
		return "n";
	}

	private void createEntityHeaderRow(Sheet classSheet) {
		CellAppender row = new CellAppender(classSheet.createRow(0));
		row.appendCellWithValue("Xmi Id")
				.appendCellWithValue("Név")
				.appendCellWithValue("Láthatóság")
				.appendCellWithValue("Absztrakt")
				.appendCellWithValue("Leszármazza")
				.appendCellWithValue("Implementálja")
				.appendCellWithValue("Leírás");
	}

	private String getFirstComment(NamedElement element) {
		if (element.getOwnedComments().size() > 0) {
			return element.getOwnedComments().get(0).getBody();
		}
		return "";
	}

}
