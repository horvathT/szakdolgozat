package model.transfer.export;

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

import model.transfer.util.CellAppender;

/**
 * Osztályok összegző munkalapja.
 * 
 * @author Horváth Tibor
 *
 */
public class ClassSummarySheetCreator extends SheetCreator {

	public final static String SHEET_NAME = "Classes";

	private Workbook workbook;

	private Collection<Class> classes;

	public ClassSummarySheetCreator(Workbook workbook, Collection<Class> classes) {
		this.workbook = workbook;
		this.classes = classes;
	}

	/**
	 * Munkalap létrehozása.
	 * 
	 * @return
	 */
	public Sheet createSheet() {
		Sheet classesSheet = workbook.createSheet(SHEET_NAME);
		createEntityHeaderRow(classesSheet);
		fillEntityRows(classes, classesSheet);
		return classesSheet;
	}

	/**
	 * Sorok feltöltése az osztályok adataival.
	 * 
	 * @param classes
	 * @param classesSheet
	 */
	private void fillEntityRows(Collection<Class> classes, Sheet classesSheet) {
		int rowNumber = 1;
		for (Class clazz : classes) {
			rowNumber = fillEntityRow(classesSheet, rowNumber, clazz);
		}
	}

	/**
	 * Sor feltöltése az osztály adataival.
	 * 
	 * @param classesSheet
	 * @param rowNumber
	 * @param clazz
	 * @return
	 */
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

	/**
	 * Leszármazott osztály neve, vagy na nincs akkor üres String.
	 * 
	 * @param clazz
	 * @return
	 */
	private String getParentClassName(Class clazz) {
		EList<Generalization> generalizations = clazz.getGeneralizations();
		if (generalizations.isEmpty()) {
			return "";
		}
		Generalization generalization = generalizations.get(0);
		Classifier general = generalization.getGeneral();

		return general.getName();
	}

	private String getFirstImplementedInterfaceName(EList<Interface> implementedInterfaces) {
		if (implementedInterfaces.isEmpty()) {
			return "";
		}
		return implementedInterfaces.get(0).getName();
	}

	/**
	 * Entitás fejléc sor.
	 * 
	 * @param classSheet
	 */
	private void createEntityHeaderRow(Sheet classSheet) {
		Row row = classSheet.createRow(0);
		CellAppender appender = new CellAppender(row);
		appender.appendCellWithValue("Xmi ID")
				.appendCellWithValue("Name")
				.appendCellWithValue("Visibility")
				.appendCellWithValue("Abstract")
				.appendCellWithValue("Extends")
				.appendCellWithValue("Implements")
				.appendCellWithValue("Comment");
		makeRowBold(workbook, row);
	}

}
