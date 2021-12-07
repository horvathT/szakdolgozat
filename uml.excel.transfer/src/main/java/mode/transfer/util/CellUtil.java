package mode.transfer.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public class CellUtil {

	public static String getStringCellValue(Cell cell) {
		if (cell == null) {
			return "";
		}

		CellType cellType = cell.getCellType();

		if (CellType.BLANK == cellType) {
			return "";
		} else if (CellType.STRING == cellType) {
			return cell.getStringCellValue();
		} else if (CellType.NUMERIC == cellType) {
			double numericCellValue = cell.getNumericCellValue();
			return String.valueOf(numericCellValue);
		}

		return "";
	}

}
