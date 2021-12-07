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
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import mode.transfer.util.CellAppender;

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
		EList<Operation> allOperations = clazz.getAllOperations();

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
				.appendCellWithValue(operation.getVisibility().toString())
				.appendCellWithValue(booleanTostring(operation.isStatic()))
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
			Parameter parameter = ownedParameters.get(i);
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

	private String getFirstParameterName(EList<Parameter> ownedParameters) {
		if (ownedParameters.isEmpty()) {
			return "";
		}
		for (Parameter param : ownedParameters) {
			ParameterDirectionKind direction = param.getDirection();
			if (direction.getValue() != ParameterDirectionKind.RETURN) {
				return param.getName();
			}
		}
		return "";
	}

	private String getFirstParameterType(EList<Parameter> ownedParameters) {
		if (ownedParameters.isEmpty()) {
			return "";
		}

		for (Parameter param : ownedParameters) {
			ParameterDirectionKind direction = param.getDirection();
			if (direction.getValue() != ParameterDirectionKind.RETURN) {
				if (param.getType() != null) {
					return param.getType().getName();
				}
			}
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
				.appendCellWithValue(property.getVisibility().toString())
				.appendCellWithValue(typeName)
				.appendCellWithValue(booleanTostring(property.isStatic()))
				.appendCellWithValue(getFirstComment(property));
	}

	private int createPropertyHeaderRow(Sheet sheet, int rowNumber) {
		Row row = sheet.createRow(rowNumber);
		CellAppender appender = new CellAppender(row);
		appender.appendCellWithValue("Attribútumok")
				.appendCellWithValue("Xmi ID")
				.appendCellWithValue("Név")
				.appendCellWithValue("Láthatóság")
				.appendCellWithValue("Típus")
				.appendCellWithValue("Static")
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
				.appendCellWithValue("Láthatóság")
				.appendCellWithValue("Static")
				.appendCellWithValue("Abstract")
				.appendCellWithValue("Visszatérési érték")
				.appendCellWithValue("Paraméter típus")
				.appendCellWithValue("Paraméter név")
				.appendCellWithValue("Leírás");
		makeRowBold(workbook, row);
		return ++rowNumber;
	}

}
