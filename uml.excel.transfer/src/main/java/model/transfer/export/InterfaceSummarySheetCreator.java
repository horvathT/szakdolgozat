package model.transfer.export;

import java.util.Collection;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;

import model.transfer.util.CellAppender;

/**
 * Interfész összegző munkalap létrehozása.
 * 
 * @author Horváth Tibor
 *
 */
public class InterfaceSummarySheetCreator extends SheetCreator {

	public static final String SHEET_NAME = "Interfaces";

	private Workbook workbook;

	private Collection<Interface> interfaces;

	public InterfaceSummarySheetCreator(Workbook workbook, Collection<Interface> interfaces) {
		this.workbook = workbook;
		this.interfaces = interfaces;
	}

	/**
	 * Interfész összegző munkalap létrehozása.
	 * 
	 * @return
	 */
	public Sheet createSheet() {
		Sheet interfaceSheet = workbook.createSheet(SHEET_NAME);
		createEntityHeaderRow(interfaceSheet);
		fillEntityRows(interfaces, interfaceSheet);
		return interfaceSheet;
	}

	/**
	 * Interfészek adatai alapján sorok feltöltése.
	 * 
	 * @param interfaces
	 * @param interfacSheet
	 */
	private void fillEntityRows(Collection<Interface> interfaces, Sheet interfacSheet) {
		int rowNumber = 1;
		for (Interface interfac : interfaces) {
			rowNumber = fillEntityRow(interfacSheet, rowNumber, interfac);
		}
	}

	/**
	 * Interfész adatai alapján sorok feltöltése.
	 * 
	 * @param interfacSheet
	 * @param rowNumber
	 * @param interfac
	 * @return
	 */
	private int fillEntityRow(Sheet interfacSheet, int rowNumber, Interface interfac) {
		Row row = interfacSheet.createRow(rowNumber);

		EList<Generalization> extendedInterfaces = interfac.getGeneralizations();

		CellAppender interfaceRow = new CellAppender(row);
		interfaceRow.appendCellWithValue(EcoreUtil.getURI(interfac).fragment())
				.appendCellWithValue(interfac.getName())
				.appendCellWithValue(interfac.getVisibility().toString())
				.appendCellWithValue(getFirstExtendedInterfaceName(extendedInterfaces))
				.appendCellWithValue(getFirstComment(interfac));

		rowNumber = appendRemainingExtendedInterfaceLines(++rowNumber, interfacSheet, extendedInterfaces);
		return rowNumber;
	}

	/**
	 * Ha egynél több Interfészt származik le akkor, akkor atöbbi itt kerül
	 * kiírásra.
	 * 
	 * @param rowNumber
	 * @param interfacSheet
	 * @param extendedInterfaces
	 * @return
	 */
	private int appendRemainingExtendedInterfaceLines(int rowNumber, Sheet interfacSheet,
			EList<Generalization> extendedInterfaces) {

		for (int i = 1; i < extendedInterfaces.size(); i++) {
			Row row = interfacSheet.createRow(rowNumber);
			Cell cell = row.createCell(3);
			Generalization generalization = extendedInterfaces.get(i);
			Classifier general = generalization.getGeneral();
			cell.setCellValue(general.getName());

			rowNumber++;
		}
		return rowNumber;
	}

	private String getFirstExtendedInterfaceName(EList<Generalization> extendedInterfaces) {
		if (extendedInterfaces.isEmpty()) {
			return "";
		}
		Generalization generalization = extendedInterfaces.get(0);
		Classifier general = generalization.getGeneral();

		return general.getName();
	}

	/**
	 * Fejléc létrehozása.
	 * 
	 * @param entitySheet
	 */
	private void createEntityHeaderRow(Sheet entitySheet) {
		Row row = entitySheet.createRow(0);
		CellAppender appender = new CellAppender(row);
		appender.appendCellWithValue("Xmi Id")
				.appendCellWithValue("Name")
				.appendCellWithValue("Visibility")
				.appendCellWithValue("Extends")
				.appendCellWithValue("Comment");
		makeRowBold(workbook, row);
	}

}
