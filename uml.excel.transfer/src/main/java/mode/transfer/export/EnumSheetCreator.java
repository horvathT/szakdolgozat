package mode.transfer.export;

import java.util.Collection;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;

import mode.transfer.export.util.CellAppender;

public class EnumSheetCreator extends SheetCreator {

	private final String ENUM_SHEET_NAME = "Enumerációk";

	private Workbook workbook;

	private Collection<Enumeration> enumerations;

	public EnumSheetCreator(Workbook workbook, Collection<Enumeration> enumerations) {
		this.workbook = workbook;
		this.enumerations = enumerations;
	}

	public Sheet creatSheet() {
		Sheet sheet = workbook.createSheet(ENUM_SHEET_NAME);
		createEnumHeaderRow(sheet);
		fillEnumRows(sheet, enumerations);
		return sheet;
	}

	private void fillEnumRows(Sheet sheet, Collection<Enumeration> enumerations) {
		int rowNumber = 1;
		for (Enumeration enumeration : enumerations) {
			Row enumRow = sheet.createRow(rowNumber);
			fillEnumRow(enumRow, enumeration);
			++rowNumber;
			EList<EnumerationLiteral> ownedLiterals = enumeration.getOwnedLiterals();
			for (EnumerationLiteral enumerationLiteral : ownedLiterals) {
				Row literalRow = sheet.createRow(rowNumber);
				fillLiteralRow(literalRow, enumerationLiteral);
				++rowNumber;
			}
		}
	}

	private void fillLiteralRow(Row literalRow, EnumerationLiteral enumerationLiteral) {
		CellAppender row = new CellAppender(literalRow);
		row.appendCellWithValue(EcoreUtil.getURI(enumerationLiteral).fragment()).appendCellWithValue("")
				.appendCellWithValue(enumerationLiteral.getName());
	}

	private void fillEnumRow(Row enumRow, Enumeration enumeration) {
		CellAppender row = new CellAppender(enumRow);
		row.appendCellWithValue(EcoreUtil.getURI(enumeration).fragment()).appendCellWithValue(enumeration.getName());
	}

	private void createEnumHeaderRow(Sheet sheet) {
		Row row = sheet.createRow(0);
		CellAppender appender = new CellAppender(row);
		appender.appendCellWithValue("Xmi Id").appendCellWithValue("Enumeration").appendCellWithValue("Literal");
		makeRowBold(workbook, row);
	}
}
