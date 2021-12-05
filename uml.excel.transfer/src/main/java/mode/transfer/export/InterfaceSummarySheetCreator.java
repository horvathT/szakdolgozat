package mode.transfer.export;

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
import org.eclipse.uml2.uml.NamedElement;

import mode.transfer.export.util.CellAppender;

public class InterfaceSummarySheetCreator {

	private final String SHEET_NAME = "Interfészek";

	private Workbook workbook;

	private Collection<Interface> interfaces;

	public InterfaceSummarySheetCreator(Workbook workbook, Collection<Interface> interfaces) {
		this.workbook = workbook;
		this.interfaces = interfaces;
	}

	public Sheet createSheet() {
		Sheet interfaceSheet = workbook.createSheet(SHEET_NAME);
		createEntityHeaderRow(interfaceSheet);
		fillEntityRows(interfaces, interfaceSheet);
		return interfaceSheet;
	}

	private void fillEntityRows(Collection<Interface> interfaces, Sheet interfacSheet) {
		int rowNumber = 1;
		for (Interface interfac : interfaces) {
			rowNumber = fillEntityRow(interfacSheet, rowNumber, interfac);
		}
	}

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

	private void createEntityHeaderRow(Sheet entitySheet) {
		CellAppender row = new CellAppender(entitySheet.createRow(0));
		row.appendCellWithValue("Xmi Id")
				.appendCellWithValue("Név")
				.appendCellWithValue("Láthatóság")
				.appendCellWithValue("Leszármazza")
				.appendCellWithValue("Leírás");

	}

	private String getFirstComment(NamedElement element) {
		if (element.getOwnedComments().size() > 0) {
			return element.getOwnedComments().get(0).getBody();
		}
		return "";
	}

}
