package mode.transfer.export;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import mode.transfer.export.util.CellAppender;

public class InterfaceSheetCreator {

	private Workbook workbook;

	private Collection<Interface> classifiers;

	public InterfaceSheetCreator(Workbook workbook, Collection<Interface> classifiers) {
		this.workbook = workbook;
		this.classifiers = classifiers;
	}

	public List<Sheet> createInterfaceSheets() {
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

	private String getFirstComment(NamedElement element) {
		if (element.getOwnedComments().size() > 0) {
			return element.getOwnedComments().get(0).getBody();
		}
		return "";
	}

}
