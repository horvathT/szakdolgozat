package model.transfer.export;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Property;

import model.transfer.util.CellAppender;

/**
 * Interfész munkalapok létrehozása.
 * 
 * @author Horváth Tibor
 *
 */
public class InterfaceSheetCreator extends SheetCreator {

	private Workbook workbook;

	private Collection<Interface> interfaces;

	public InterfaceSheetCreator(Workbook workbook, Collection<Interface> interfaces) {
		this.workbook = workbook;
		this.interfaces = interfaces;
	}

	/**
	 * Interfész munkalapok létrehozása.
	 * 
	 * @return
	 */
	public List<Sheet> createInterfaceSheets() {
		List<Sheet> sheets = new ArrayList<>();
		for (Interface interfac : interfaces) {
			sheets.add(createInterfacePropertySheet(workbook, interfac));
		}
		return sheets;
	}

	/**
	 * Intrefész munkalapjának létrehozása.
	 * 
	 * @param workbook
	 * @param interfac
	 * @return
	 */
	private Sheet createInterfacePropertySheet(Workbook workbook, Interface interfac) {
		Sheet sheet = workbook.createSheet(interfac.getName());
		int rowNumber = 0;
		rowNumber = createPropertyHeaderRow(sheet, rowNumber);
		rowNumber = fillPropertyRows(sheet, interfac, rowNumber);

		rowNumber++;

		rowNumber = createMethodHeaderRow(sheet, rowNumber);
		rowNumber = fillMethodrows(sheet, interfac, rowNumber);
		return sheet;
	}

	/**
	 * Metódus sorok feltöltése.
	 * 
	 * @param sheet
	 * @param interfac
	 * @param rowNumber
	 * @return
	 */
	private int fillMethodrows(Sheet sheet, Interface interfac, int rowNumber) {
		EList<Operation> ownedOperations = interfac.getOwnedOperations();

		for (Operation operation : ownedOperations) {
			rowNumber = fillMethodRow(sheet, rowNumber, operation);
		}
		return rowNumber;
	}

	/**
	 * Metódus adatai alapján sor feltöltése.
	 * 
	 * @param sheet
	 * @param rowNumber
	 * @param operation
	 * @return
	 */
	private int fillMethodRow(Sheet sheet, int rowNumber, Operation operation) {
		Row row = sheet.createRow(rowNumber);

		EList<Parameter> ownedParameters = operation.getOwnedParameters();
		List<Parameter> parameterList = new ArrayList<>(ownedParameters);
		Parameter parameter = popFirstInputParam(parameterList);

		CellAppender methodRow = new CellAppender(row);
		methodRow.appendCellWithValue("")
				.appendCellWithValue(EcoreUtil.getURI(operation).fragment())
				.appendCellWithValue(operation.getName())
				.appendCellWithValue(operation.getVisibility().toString())
				.appendCellWithValue(booleanTostring(operation.isAbstract()))
				.appendCellWithValue(getReturnType(operation))
				.appendCellWithValue(getParameterType(parameter))
				.appendCellWithValue(getParameterName(parameter))
				.appendCellWithValue(getFirstComment(operation));

		rowNumber = appendRemainingParameters(++rowNumber, sheet, parameterList);
		return rowNumber;
	}

	/**
	 * Lista paramétereinek sorokká alakítása.
	 * 
	 * @param rowNumber
	 * @param sheet
	 * @param ownedParameters
	 * @return
	 */
	private int appendRemainingParameters(int rowNumber, Sheet sheet, List<Parameter> ownedParameters) {
		for (Parameter parameter : ownedParameters) {
			ParameterDirectionKind direction = parameter.getDirection();
			if (!(direction.getValue() == ParameterDirectionKind.RETURN)) {
				Row row = sheet.createRow(rowNumber);

				Cell typeCell = row.createCell(6);
				if (parameter.getType() != null) {
					typeCell.setCellValue(parameter.getType().getName());
				} else {
					typeCell.setCellValue("");
				}

				Cell nameCell = row.createCell(7);
				nameCell.setCellValue(parameter.getName());

				Cell commentCell = row.createCell(8);
				commentCell.setCellValue(getFirstComment(parameter));

				rowNumber++;
			}

		}
		return rowNumber;
	}

	private int fillPropertyRows(Sheet sheet, Interface interfac, int rowNumber) {
		EList<Property> allAttributes = interfac.getOwnedAttributes();

		for (Property property : allAttributes) {
			if (property.getAssociation() == null) {
				Row row = sheet.createRow(rowNumber);
				fillPropertyRow(row, property);
				++rowNumber;
			}
		}
		return rowNumber;
	}

	private void fillPropertyRow(Row row, Property property) {
		CellAppender propertyRow = new CellAppender(row);

		String typeName = getTypeName(property);
		propertyRow.appendCellWithValue("")
				.appendCellWithValue(EcoreUtil.getURI(property).fragment())
				.appendCellWithValue(property.getName())
				.appendCellWithValue(typeName)
				.appendCellWithValue(getFirstComment(property));
	}

	/**
	 * Attribútum fejléc létrehozása.
	 * 
	 * @param sheet
	 * @param rowNumber
	 * @return
	 */
	private int createPropertyHeaderRow(Sheet sheet, int rowNumber) {
		Row row = sheet.createRow(rowNumber);
		CellAppender appender = new CellAppender(row);
		appender.appendCellWithValue("Attributes")
				.appendCellWithValue("Xmi ID")
				.appendCellWithValue("Name")
				.appendCellWithValue("Type")
				.appendCellWithValue("Comment");
		makeRowBold(workbook, row);
		return ++rowNumber;
	}

	/**
	 * Metódus fejléc létrehozása.
	 * 
	 * @param sheet
	 * @param rowNumber
	 * @return
	 */
	private int createMethodHeaderRow(Sheet sheet, int rowNumber) {
		Row row = sheet.createRow(rowNumber);
		CellAppender appender = new CellAppender(row);
		appender.appendCellWithValue("Methods")
				.appendCellWithValue("Xmi ID")
				.appendCellWithValue("Name")
				.appendCellWithValue("visibility")
				.appendCellWithValue("Abstract")
				.appendCellWithValue("Return type")
				.appendCellWithValue("Parameter type")
				.appendCellWithValue("Parameter name")
				.appendCellWithValue("Comment");
		makeRowBold(workbook, row);
		return ++rowNumber;
	}

}
