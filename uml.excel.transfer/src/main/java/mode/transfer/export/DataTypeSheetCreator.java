package mode.transfer.export;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.DataType;

import mode.transfer.export.util.CellAppender;

public class DataTypeSheetCreator extends SheetCreator {

	private final String DATA_TYPE_SHEET_NAME = "Adattípusok";

	private Workbook workbook;

	private List<DataType> dataTypes;

	public DataTypeSheetCreator(Workbook workbook, List<DataType> dataTypes) {
		this.workbook = workbook;
		this.dataTypes = dataTypes;
	}

	public Sheet createSheet() {
		Comparator<DataType> compareByName = (DataType dt1, DataType dt2) -> dt1.getName().compareTo(dt2.getName());
		Collections.sort(dataTypes, compareByName);

		Sheet sheet = workbook.createSheet(DATA_TYPE_SHEET_NAME);
		createDataTypeHeaderRow(sheet);
		fillDataTypeRows(sheet, dataTypes);
		return sheet;
	}

	private void fillDataTypeRows(Sheet sheet, List<DataType> dataTypes) {
		int rowNumber = 1;
		for (DataType dataType : dataTypes) {
			Row dataRow = sheet.createRow(rowNumber);
			fillDataTypeRow(dataRow, dataType);
			++rowNumber;
		}
	}

	private void fillDataTypeRow(Row dataRow, DataType dataType) {
		CellAppender row = new CellAppender(dataRow);
		String fragment = EcoreUtil.getURI(dataType).fragment();
		row.appendCellWithValue(fragment).appendCellWithValue(dataType.getName());
	}

	private void createDataTypeHeaderRow(Sheet sheet) {
		Row row = sheet.createRow(0);
		CellAppender appender = new CellAppender(row);
		appender.appendCellWithValue("XmiId").appendCellWithValue("TypeName");
		makeRowBold(workbook, row);
	}

}
