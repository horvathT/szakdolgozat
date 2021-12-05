package mode.transfer.export;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import mode.transfer.export.util.CellAppender;

public class InterfaceSheetCreator extends SheetCreator {

	private Workbook workbook;

	private Collection<Interface> interfaces;

	public InterfaceSheetCreator(Workbook workbook, Collection<Interface> interfaces) {
		this.workbook = workbook;
		this.interfaces = interfaces;
	}

	public List<Sheet> createInterfaceSheets() {
		List<Sheet> sheets = new ArrayList<>();
		for (Interface interfac : interfaces) {
			sheets.add(createEntityPropertySheet(workbook, interfac));
		}
		return sheets;
	}

	private Sheet createEntityPropertySheet(Workbook workbook, Interface interfac) {
		Sheet sheet = workbook.createSheet(interfac.getName());
		int rowNumber = 0;
		rowNumber = createPropertyHeaderRow(sheet, rowNumber);
		rowNumber = fillPropertyRows(sheet, interfac, rowNumber);

		rowNumber++;

		rowNumber = createMethodHeaderRow(sheet, rowNumber);
		rowNumber = fillMethodrows(sheet, interfac, rowNumber);
		return sheet;
	}

	private int fillMethodrows(Sheet sheet, Interface interfac, int rowNumber) {
		EList<Operation> allOperations = interfac.getAllOperations();

		for (Operation operation : allOperations) {
			rowNumber = fillMethodRow(sheet, rowNumber, operation);
		}
		return rowNumber;
	}

	private int fillMethodRow(Sheet sheet, int rowNumber, Operation operation) {
		Row row = sheet.createRow(rowNumber);

		EList<Parameter> ownedParameters = operation.getOwnedParameters();

		CellAppender methodRow = new CellAppender(row);
		methodRow.appendCellWithValue("")
				.appendCellWithValue(EcoreUtil.getURI(operation).fragment())
				.appendCellWithValue(operation.getName())
				.appendCellWithValue(booleanTostring(operation.isAbstract()))
				.appendCellWithValue(getReturnType(operation))
				.appendCellWithValue(getFirstParameterType(ownedParameters))
				.appendCellWithValue(getFirstParameterName(ownedParameters))
				.appendCellWithValue(getFirstComment(operation));

		rowNumber = appendRemainingParameters(++rowNumber, sheet, ownedParameters);
		return rowNumber;
	}

	private int appendRemainingParameters(int rowNumber, Sheet sheet, EList<Parameter> ownedParameters) {
		for (int i = 1; i < ownedParameters.size(); i++) {
			Row row = sheet.createRow(rowNumber);
			Parameter parameter = ownedParameters.get(i);

			Cell typeCell = row.createCell(5);
			if (parameter.getType() != null) {
				typeCell.setCellValue(parameter.getType().getName());
			} else {
				typeCell.setCellValue("");
			}

			Cell nameCell = row.createCell(6);
			nameCell.setCellValue(parameter.getName());

			Cell commentCell = row.createCell(7);
			commentCell.setCellValue(getFirstComment(parameter));

			rowNumber++;
		}
		return rowNumber;
	}

	private String getFirstParameterName(EList<Parameter> ownedParameters) {
		if (ownedParameters.isEmpty()) {
			return "";
		}
		Parameter parameter = ownedParameters.get(0);
		return parameter.getName();
	}

	private String getFirstParameterType(EList<Parameter> ownedParameters) {
		if (ownedParameters.isEmpty()) {
			return "";
		}
		Parameter parameter = ownedParameters.get(0);
		if (parameter.getType() != null) {
			return parameter.getType().getName();
		}
		return "";
	}

	private String getReturnType(Operation operation) {
		Parameter returnResult = operation.getReturnResult();
		if (returnResult != null) {
			Type returnResultType = returnResult.getType();
			if (returnResultType != null) {
				return returnResultType.getName();
			}
		}
		return "";
	}

	private int fillPropertyRows(Sheet sheet, Classifier classifier, int rowNumber) {
		EList<Property> allAttributes = classifier.allAttributes();

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

	private int createPropertyHeaderRow(Sheet sheet, int rowNumber) {
		Row row = sheet.createRow(rowNumber);
		CellAppender appender = new CellAppender(row);
		appender.appendCellWithValue("Attribútumok")
				.appendCellWithValue("Xmi ID")
				.appendCellWithValue("Név")
				.appendCellWithValue("Típus")
				.appendCellWithValue("Leírás");
		makeRowBold(workbook, row);
		return ++rowNumber;
	}

	private int createMethodHeaderRow(Sheet sheet, int rowNumber) {
		Row row = sheet.createRow(rowNumber);
		CellAppender appender = new CellAppender(row);
		appender.appendCellWithValue("Metódusok")
				.appendCellWithValue("Xmi ID")
				.appendCellWithValue("Név")
				.appendCellWithValue("Absztrakt")
				.appendCellWithValue("Visszatérési érték")
				.appendCellWithValue("Paraméter típus")
				.appendCellWithValue("Paraméter név")
				.appendCellWithValue("Leírás");
		makeRowBold(workbook, row);
		return ++rowNumber;
	}

}
