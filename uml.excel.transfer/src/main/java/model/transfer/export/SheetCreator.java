package model.transfer.export;

import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

public class SheetCreator {

	/**
	 * Sor tartalmát félkövérré teszi.
	 * 
	 * @param wb
	 * @param row
	 */
	protected static void makeRowBold(Workbook wb, Row row) {
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setBold(true);
		style.setFont(font);

		for (int i = 0; i < row.getLastCellNum(); i++) {
			row.getCell(i).setCellStyle(style);
		}
	}

	/**
	 * Viszaadja az elem első kommentjét vagy ürs String-et ha nincs.
	 * 
	 * @param element
	 * @return
	 */
	protected String getFirstComment(NamedElement element) {
		if (element.getOwnedComments().size() > 0) {
			return element.getOwnedComments().get(0).getBody();
		}
		return "";
	}

	/**
	 * Property típusának a nevét adja vissza.
	 * 
	 * @param property
	 * @return
	 */
	protected String getTypeName(Property property) {
		Type type = property.getType();
		if (type != null) {
			return type.getName();
		}
		return "";
	}

	/**
	 * boolean értéket szöveggé alakítja.
	 * 
	 * @param bool
	 * @return
	 */
	protected String booleanTostring(boolean bool) {
		if (bool) {
			return "yes";
		}
		return "no";
	}

	/**
	 * Első bementi paraméter kivétele ás törlése a listából.
	 * 
	 * @param ownedParameters
	 * @return
	 */
	protected Parameter popFirstInputParam(List<Parameter> ownedParameters) {
		for (int i = 1; i < ownedParameters.size(); i++) {
			Parameter parameter = ownedParameters.get(i);
			ParameterDirectionKind direction = parameter.getDirection();
			if (!(direction.getValue() == ParameterDirectionKind.RETURN)) {
				ownedParameters.remove(parameter);
				return parameter;
			}
		}
		return null;
	}

	protected String getParameterName(Parameter param) {
		if (param == null) {
			return "";
		}
		return param.getName();
	}

	protected String getParameterType(Parameter param) {
		if (param == null) {
			return "";
		}

		if (param.getType() != null) {
			return param.getType().getName();
		}

		return "";
	}

	/**
	 * Metódus visszatérési értékének lekérdezése.
	 * 
	 * @param operation
	 * @return
	 */
	protected String getReturnType(Operation operation) {
		Parameter returnResult = operation.getReturnResult();
		if (returnResult != null) {
			Type returnResultType = returnResult.getType();
			if (returnResultType != null) {
				return returnResultType.getName();
			}
		}
		return "";
	}

}
