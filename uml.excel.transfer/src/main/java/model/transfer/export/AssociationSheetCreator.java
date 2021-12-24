package model.transfer.export;

import java.util.Collection;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;

import model.transfer.util.CellAppender;

public class AssociationSheetCreator extends SheetCreator {

	public static final String ASSOCIATION_SHEET_NAME = "Asszociációk";

	private Workbook workbook;

	private Collection<Association> associations;

	public AssociationSheetCreator(Workbook workbook, Collection<Association> associations) {
		this.workbook = workbook;
		this.associations = associations;
	}

	public Sheet createSheet() {
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
		Row row = associationSheet.createRow(0);
		CellAppender appender = new CellAppender(row);
		appender.appendCellWithValue("XmiId")

				.appendCellWithValue("Entity").appendCellWithValue("Property").appendCellWithValue("Navigable")
				.appendCellWithValue("Aggregation Type").appendCellWithValue("Lower").appendCellWithValue("Upper")

				.appendCellWithValue("Entity").appendCellWithValue("Property").appendCellWithValue("Navigable")
				.appendCellWithValue("Aggregation Type").appendCellWithValue("Lower").appendCellWithValue("Upper");
		makeRowBold(workbook, row);
	}
}
