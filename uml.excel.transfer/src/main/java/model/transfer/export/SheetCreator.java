package model.transfer.export;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

public class SheetCreator {

	protected static void makeRowBold(Workbook wb, Row row) {
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setBold(true);
		style.setFont(font);

		for (int i = 0; i < row.getLastCellNum(); i++) {
			row.getCell(i).setCellStyle(style);
		}
	}

	protected String getFirstComment(NamedElement element) {
		if (element.getOwnedComments().size() > 0) {
			return element.getOwnedComments().get(0).getBody();
		}
		return "";
	}

	protected String getTypeName(Property property) {
		Type type = property.getType();
		if (type != null) {
			return type.getName();
		}
		return "";
	}

	protected String booleanTostring(boolean bool) {
		if (bool) {
			return "yes";
		}
		return "no";
	}

}
