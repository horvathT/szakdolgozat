package mode.transfer.export;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import mode.transfer.export.util.CellAppender;

public class SheetFactory {

	private final String DATA_TYPE_SHEET_NAME = "DataTypes";
	private final String ASSOCIATION_SHEET_NAME = "Associations";
	private final String ENTITY_SHEET_NAME = "Entities";
	private final String ENUM_SHEET_NAME = "Enumerations";

	public Sheet createDataTypeSheet(Workbook workbook, List<DataType> dataTypes) {
		Sheet sheet = workbook.createSheet(DATA_TYPE_SHEET_NAME);
		createDataTypeHeaderRow(sheet);
		fillDataTypeRows(sheet, dataTypes);
		return sheet;
	}

	private void fillDataTypeRows(Sheet sheet, List<DataType> dataTypes) {
		int rowNumber = 1;
		for (DataType dataType : dataTypes) {
			Row dataRow = sheet.createRow(rowNumber);
			fillDataTypeRow(dataRow, dataType);
			++rowNumber;
		}
	}

	private void fillDataTypeRow(Row dataRow, DataType dataType) {
		CellAppender row = new CellAppender(dataRow);
		String fragment = EcoreUtil.getURI(dataType).fragment();
		row.appendCellWithValue(fragment).appendCellWithValue(dataType.getName());
	}

	private void createDataTypeHeaderRow(Sheet sheet) {
		CellAppender row = new CellAppender(sheet.createRow(0));
		row.appendCellWithValue("XmiId").appendCellWithValue("TypeName");
	}

	public Sheet createAssociationSheet(Workbook workbook, Collection<Association> associations) {
		Sheet associationSheet = workbook.createSheet(ASSOCIATION_SHEET_NAME);
		createAssociationHeaderRow(associationSheet);
		fillAssociationRows(associationSheet, associations);
		return associationSheet;
	}

	private void fillAssociationRows(Sheet associationSheet, Collection<Association> associations) {
		int rowNumber = 1;
		for (Association association : associations) {
			Row row = associationSheet.createRow(rowNumber);
			fillAssociationRow(row, association);
			++rowNumber;
		}
	}

	private void fillAssociationRow(Row row, Association association) {
		EList<Property> memberEnds = association.getMemberEnds();
		Property otherEnd = memberEnds.get(0);
		Property ownEnd = memberEnds.get(1);

		CellAppender assocRow = new CellAppender(row);
		assocRow.appendCellWithValue(EcoreUtil.getURI(association).fragment())

				.appendCellWithValue(ownEnd.getType().getName()).appendCellWithValue(ownEnd.getName())
				.appendCellWithValue(ownEnd.isNavigable() ? "yes" : "no")
				.appendCellWithValue(ownEnd.getAggregation().toString()).appendCellWithValue(ownEnd.getLower())
				.appendCellWithValue(ownEnd.getUpper())

				.appendCellWithValue(otherEnd.getType().getName()).appendCellWithValue(otherEnd.getName())
				.appendCellWithValue(otherEnd.isNavigable() ? "yes" : "no")
				.appendCellWithValue(otherEnd.getAggregation().toString()).appendCellWithValue(otherEnd.getLower())
				.appendCellWithValue(otherEnd.getUpper());

	}

	private void createAssociationHeaderRow(Sheet associationSheet) {
		CellAppender row = new CellAppender(associationSheet.createRow(0));
		row.appendCellWithValue("XmiId")

				.appendCellWithValue("Entity").appendCellWithValue("Property").appendCellWithValue("Navigable")
				.appendCellWithValue("Aggregation Type").appendCellWithValue("Lower").appendCellWithValue("Upper")

				.appendCellWithValue("Entity").appendCellWithValue("Property").appendCellWithValue("Navigable")
				.appendCellWithValue("Aggregation Type").appendCellWithValue("Lower").appendCellWithValue("Upper");

	}

	public Sheet createEntitySummarySheet(Workbook workbook, Collection<Classifier> classifiers) {
		Sheet interfaceSheet = workbook.createSheet(ENTITY_SHEET_NAME);
		createEntityHeaderRow(interfaceSheet);
		fillEntityRows(classifiers, interfaceSheet);
		return interfaceSheet;
	}

	private void fillEntityRows(Collection<Classifier> classifiers, Sheet classifierSheet) {
		int rowNumber = 1;
		for (Classifier classifier : classifiers) {
			Row row = classifierSheet.createRow(rowNumber);
			fillEntityRow(row, classifier);
			++rowNumber;
		}
	}

	private void fillEntityRow(Row row, Classifier classifier) {
		CellAppender classifierRow = new CellAppender(row);
		String type = getClassifierType(classifier);
		classifierRow.appendCellWithValue(EcoreUtil.getURI(classifier).fragment()).appendCellWithValue(type)
				.appendCellWithValue(classifier.getName()).appendCellWithValue(classifier.getLabel())
				.appendCellWithValue(getFirstComment(classifier));
	}

	private String getClassifierType(Classifier classifier) {
		if (classifier instanceof Interface) {
			return "Interface";
		} else if (classifier instanceof Class) {
			return "Class";
		} else {
			return "Unknown";
		}
	}

	private String getFirstComment(NamedElement element) {
		if (element.getOwnedComments().size() > 0) {
			return element.getOwnedComments().get(0).getBody();
		}
		return "";
	}

	private void createEntityHeaderRow(Sheet entitySheet) {
		CellAppender row = new CellAppender(entitySheet.createRow(0));
		row.appendCellWithValue("Xmi Id").appendCellWithValue("Type").appendCellWithValue("Name")
				.appendCellWithValue("Label").appendCellWithValue("Description");

	}

	public List<Sheet> createEntityPropertySheets(Workbook workbook, Collection<Classifier> classifiers) {
		List<Sheet> sheets = new ArrayList<>();
		for (Classifier classifier : classifiers) {
			sheets.add(createEntityPropertySheet(workbook, classifier));
		}
		return sheets;
	}

	private Sheet createEntityPropertySheet(Workbook workbook, Classifier classifier) {
		Sheet sheet = workbook.createSheet(classifier.getName());
		createPropertyHeaderRow(sheet);
		fillPropertyRows(sheet, classifier);
		return sheet;
	}

	private void fillPropertyRows(Sheet sheet, Classifier classifier) {
		EList<Property> allAttributes = classifier.allAttributes();

		int rowNumber = 1;
		for (Property property : allAttributes) {
			if (property.getAssociation() == null) {
				Row row = sheet.createRow(rowNumber);
				fillPropertyRow(row, property);
				++rowNumber;
			}
		}
	}

	private void fillPropertyRow(Row row, Property property) {
		CellAppender propertyRow = new CellAppender(row);

		Type type = property.getType();
		String typeName;
		if (type != null) {
			typeName = type.getName();
		} else {
			typeName = "";
		}
		propertyRow.appendCellWithValue(EcoreUtil.getURI(property).fragment()).appendCellWithValue(property.getName())
				.appendCellWithValue(typeName).appendCellWithValue(getFirstComment(property));
	}

	private void createPropertyHeaderRow(Sheet sheet) {
		CellAppender row = new CellAppender(sheet.createRow(0));
		row.appendCellWithValue("Model ID").appendCellWithValue("Name").appendCellWithValue("Typename")
				.appendCellWithValue("Description");
	}

	public Sheet creatEnumerationSheet(Workbook workbook, Collection<Enumeration> enumerations) {
		Sheet sheet = workbook.createSheet(ENUM_SHEET_NAME);
		createEnumHeaderRow(sheet);
		fillEnumRows(sheet, enumerations);
		return sheet;
	}

	private void fillEnumRows(Sheet sheet, Collection<Enumeration> enumerations) {
		int rowNumber = 1;
		for (Enumeration enumeration : enumerations) {
			Row enumRow = sheet.createRow(rowNumber);
			fillEnumRow(enumRow, enumeration);
			++rowNumber;
			EList<EnumerationLiteral> ownedLiterals = enumeration.getOwnedLiterals();
			for (EnumerationLiteral enumerationLiteral : ownedLiterals) {
				Row literalRow = sheet.createRow(rowNumber);
				fillLiteralRow(literalRow, enumerationLiteral);
				++rowNumber;
			}
		}
	}

	private void fillLiteralRow(Row literalRow, EnumerationLiteral enumerationLiteral) {
		CellAppender row = new CellAppender(literalRow);
		row.appendCellWithValue(EcoreUtil.getURI(enumerationLiteral).fragment()).appendCellWithValue("")
				.appendCellWithValue(enumerationLiteral.getName());
	}

	private void fillEnumRow(Row enumRow, Enumeration enumeration) {
		CellAppender row = new CellAppender(enumRow);
		row.appendCellWithValue(EcoreUtil.getURI(enumeration).fragment()).appendCellWithValue(enumeration.getName());
	}

	private void createEnumHeaderRow(Sheet sheet) {
		CellAppender row = new CellAppender(sheet.createRow(0));
		row.appendCellWithValue("Xmi Id").appendCellWithValue("Enumeration").appendCellWithValue("Literal");

	}
}
