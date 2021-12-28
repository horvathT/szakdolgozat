package model.transfer.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

/**
 * Segédosztály a cellák értékeinek kiolvasásához.
 * 
 * @author Horváth Tibor
 *
 */
public class CellUtil {

	/**
	 * Cella értékének visszaadása szövegként.
	 * 
	 * @param cell
	 * @return
	 */
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

	/**
	 * Cella értékének visszaadása számként.
	 * 
	 * @param cell
	 * @return
	 */
	public static int getNumericCellValue(Cell cell) {
		if (cell == null) {
			return 0;
		}

		CellType cellType = cell.getCellType();

		if (CellType.BLANK == cellType) {
			return 0;
		} else if (CellType.STRING == cellType) {
			return Integer.parseInt(cell.getStringCellValue());
		} else if (CellType.NUMERIC == cellType) {
			return (int) cell.getNumericCellValue();
		}

		return 0;
	}

}
