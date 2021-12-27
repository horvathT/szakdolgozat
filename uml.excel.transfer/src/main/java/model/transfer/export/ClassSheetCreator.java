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
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import model.transfer.util.CellAppender;

public class ClassSheetCreator extends SheetCreator {

	private Workbook workbook;

	private Collection<Class> classes;

	public ClassSheetCreator(Workbook workbook, Collection<Class> classes) {
		this.workbook = workbook;
		this.classes = classes;
	}

	public List<Sheet> createClassSheets() {
		List<Sheet> sheets = new ArrayList<>();
		for (Class clazz : classes) {
			sheets.add(createclassPropertySheet(workbook, clazz));
		}
		return sheets;
	}

	private Sheet createclassPropertySheet(Workbook workbook, Class clazz) {
		Sheet sheet = workbook.createSheet(clazz.getName());
		int rowNumber = 0;
		rowNumber = createPropertyHeaderRow(sheet, rowNumber);
		rowNumber = fillPropertyRows(sheet, clazz, rowNumber);

		rowNumber++;

		rowNumber = createMethodHeaderRow(sheet, rowNumber);
		rowNumber = fillMethodrows(sheet, clazz, rowNumber);
		return sheet;
	}

	private int fillMethodrows(Sheet sheet, Class clazz, int rowNumber) {
		EList<Operation> ownedOperations = clazz.getOwnedOperations();

		for (Operation operation : ownedOperations) {
			rowNumber = fillMethodRow(sheet, rowNumber, operation);
		}
		return rowNumber;
	}

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
				.appendCellWithValue(booleanTostring(operation.isStatic()))
				.appendCellWithValue(booleanTostring(operation.isAbstract()))
				.appendCellWithValue(getReturnType(operation))
				.appendCellWithValue(getParameterType(parameter))
				.appendCellWithValue(getParameterName(parameter))
				.appendCellWithValue(getFirstComment(operation));

		rowNumber = appendRemainingParameters(++rowNumber, sheet, parameterList);
		return rowNumber;
	}

	private Parameter popFirstInputParam(List<Parameter> ownedParameters) {
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

	private int appendRemainingParameters(int rowNumber, Sheet sheet, List<Parameter> ownedParameters) {
		for (Parameter parameter : ownedParameters) {
			ParameterDirectionKind direction = parameter.getDirection();
			if (direction.getValue() != ParameterDirectionKind.RETURN) {
				Row row = sheet.createRow(rowNumber);

				Cell typeCell = row.createCell(7);
				if (parameter.getType() != null) {
					typeCell.setCellValue(parameter.getType().getName());
				} else {
					typeCell.setCellValue("");
				}

				Cell nameCell = row.createCell(8);
				nameCell.setCellValue(parameter.getName());

				Cell commentCell = row.createCell(9);
				commentCell.setCellValue(getFirstComment(parameter));

				rowNumber++;
			}

		}
		return rowNumber;
	}

	private String getParameterName(Parameter param) {
		if (param == null) {
			return "";
		}
		return param.getName();
	}

	private String getParameterType(Parameter param) {
		if (param == null) {
			return "";
		}

		if (param.getType() != null) {
			return param.getType().getName();
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

	private int fillPropertyRows(Sheet sheet, Class clazz, int rowNumber) {
		EList<Property> allAttributes = clazz.getOwnedAttributes();

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
				.appendCellWithValue(property.getVisibility().toString())
				.appendCellWithValue(typeName)
				.appendCellWithValue(booleanTostring(property.isStatic()))
				.appendCellWithValue(getFirstComment(property));
	}

	private int createPropertyHeaderRow(Sheet sheet, int rowNumber) {
		Row row = sheet.createRow(rowNumber);
		CellAppender appender = new CellAppender(row);
		appender.appendCellWithValue("Attributes")
				.appendCellWithValue("Xmi ID")
				.appendCellWithValue("Name")
				.appendCellWithValue("Visibility")
				.appendCellWithValue("Type")
				.appendCellWithValue("Static")
				.appendCellWithValue("Comment");
		makeRowBold(workbook, row);
		return ++rowNumber;
	}

	private int createMethodHeaderRow(Sheet sheet, int rowNumber) {
		Row row = sheet.createRow(rowNumber);
		CellAppender appender = new CellAppender(row);
		appender.appendCellWithValue("Methods")
				.appendCellWithValue("Xmi ID")
				.appendCellWithValue("Name")
				.appendCellWithValue("Visibility")
				.appendCellWithValue("Static")
				.appendCellWithValue("Abstract")
				.appendCellWithValue("Return type")
				.appendCellWithValue("Parameter type")
				.appendCellWithValue("Parameter name")
				.appendCellWithValue("Comment");
		makeRowBold(workbook, row);
		return ++rowNumber;
	}

}
