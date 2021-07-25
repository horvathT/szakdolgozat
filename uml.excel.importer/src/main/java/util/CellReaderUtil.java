package util;

import org.apache.poi.ss.usermodel.Cell;

public class CellReaderUtil {
  public static String getStringValue(Cell cell) {
    if(cell == null) {
      return "";
    }
    return cell.getStringCellValue();
  }
  
  public static int getNumericValue(Cell cell) {
    if(cell == null) {
      return 0;
    }
    return (int) cell.getNumericCellValue();
  }
}
