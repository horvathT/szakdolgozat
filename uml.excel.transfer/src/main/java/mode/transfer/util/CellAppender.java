package mode.transfer.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class CellAppender {

	private Row row;
	private Cell cell;
	private int counter;

	public CellAppender(Row row) {
		this.row = row;
		this.counter = 0;
		this.cell = row.createCell(counter);
	}

	public CellAppender appendCellWithValue(String value) {
		cell.setCellValue(value);
		cell = row.createCell(++counter);
		return this;
	}

	public CellAppender appendCellWithValue(int value) {
		cell.setCellValue(value);
		cell = row.createCell(++counter);
		return this;
	}
}
