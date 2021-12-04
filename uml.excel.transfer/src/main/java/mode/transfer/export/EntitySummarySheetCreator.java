package mode.transfer.export;

import java.util.Collection;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.NamedElement;

import mode.transfer.export.util.CellAppender;

public class EntitySummarySheetCreator {

	private final String ENTITY_SHEET_NAME = "Entities";

	private Workbook workbook;

	private Collection<Classifier> classifiers;

	public EntitySummarySheetCreator(Workbook workbook, Collection<Classifier> classifiers) {
		this.workbook = workbook;
		this.classifiers = classifiers;
	}

	public Sheet createSheet() {
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

	private void createEntityHeaderRow(Sheet entitySheet) {
		CellAppender row = new CellAppender(entitySheet.createRow(0));
		row.appendCellWithValue("Xmi Id").appendCellWithValue("Type").appendCellWithValue("Name")
				.appendCellWithValue("Label").appendCellWithValue("Description");

	}

	private String getFirstComment(NamedElement element) {
		if (element.getOwnedComments().size() > 0) {
			return element.getOwnedComments().get(0).getBody();
		}
		return "";
	}

}
