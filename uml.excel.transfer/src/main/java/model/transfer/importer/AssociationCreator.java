package model.transfer.importer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;

import mode.transfer.export.AssociationSheetCreator;
import mode.transfer.util.CellUtil;
import mode.transfer.util.ModelObjectUtil;

public class AssociationCreator {

	private Workbook workbook;

	private Package modelPackage;

	private List<Classifier> classifiers = new ArrayList<>();

	public AssociationCreator(Workbook workbook, Package modelPackage) {
		this.workbook = workbook;
		this.modelPackage = modelPackage;
	}

	public void createAssociations() {
		Collection<Interface> interfaces = ModelObjectUtil.getInterfaces(modelPackage.allOwnedElements());
		Collection<Class> classes = ModelObjectUtil.getClasses(modelPackage.allOwnedElements());
		classifiers.addAll(interfaces);
		classifiers.addAll(classes);

		List<AssociationModel> associationsFromExcel = getAssociationsFromExcel(
				workbook.getSheet(AssociationSheetCreator.ASSOCIATION_SHEET_NAME));

		for (AssociationModel associationModel : associationsFromExcel) {
			createAssociation(associationModel);
		}

	}

	public void createAssociation(AssociationModel associationData) {

		Association associationInModel = getAssociationFromModel(associationData.getModelId());

		if (associationInModel != null) {
			updateValues(associationInModel, associationData);
		} else {
			Classifier start = getClassifierByName(associationData.getEnd1InterfaceName());
			Classifier end = getClassifierByName(associationData.getEnd2InterfaceName());
			if (start == null) {
				warningMessageDialog("Missing association endpoint " + associationData.getEnd1PropertyName() + " at: "
						+ associationData.getEnd1InterfaceName());
			} else if (end == null) {
				warningMessageDialog("Missing association endpoint " + associationData.getEnd2PropertyName() + " at: "
						+ associationData.getEnd2InterfaceName());
			} else {
				Association newAssociation = start.createAssociation(associationData.isEnd2IsNavigable(),
						associationData.getEnd2Aggregation(), associationData.getEnd2PropertyName(),
						associationData.getEnd2Lower(),
						associationData.getEnd2Upper(), end, associationData.isEnd1IsNavigable(),
						associationData.getEnd1Aggregation(),
						associationData.getEnd1PropertyName(), associationData.getEnd1Lower(),
						associationData.getEnd1Upper());

				newAssociation.createOwnedComment().setBody(associationData.getComment());
			}
		}
	}

	private Association getAssociationFromModel(String modelId) {
		if (modelId != null && !modelId.isEmpty()) {
			Collection<Association> associations = ModelObjectUtil.getAssociations(modelPackage.allOwnedElements());
			for (Association assoc : associations) {
				if (modelId.equals(EcoreUtil.getURI(assoc).fragment())) {
					return assoc;
				}
			}
		}
		return null;
	}

	private List<AssociationModel> getAssociationsFromExcel(Sheet associationSheet) {
		List<AssociationModel> excelAssociations = new ArrayList<>();
		for (int i = 1; i <= associationSheet.getLastRowNum(); i++) {
			Row row = associationSheet.getRow(i);
			if (row == null) {
				continue;
			}
			AssociationModel associationData = new AssociationModel();
			associationData
					.setModelId(CellUtil.getStringCellValue(row.getCell(0)));
			// END1
			associationData
					.setEnd1InterfaceName(CellUtil.getStringCellValue(row.getCell(1)));
			associationData
					.setEnd1PropertyName(CellUtil.getStringCellValue(row.getCell(2)));

			boolean isNavigable1 = CellUtil.getStringCellValue(row.getCell(3)).contains("yes")
					? true
					: false;
			associationData.setEnd1IsNavigable(isNavigable1);

			AggregationKind aggregationKind1 = getAggregationKind(
					CellUtil.getStringCellValue(row.getCell(4)));
			if (aggregationKind1 != null) {
				associationData.setEnd1Aggregation(aggregationKind1);
			}

			int lower1 = CellUtil.getNumericCellValue(row.getCell(5));
			associationData.setEnd1Lower(lower1);
			int upper1 = CellUtil.getNumericCellValue(row.getCell(6));
			associationData.setEnd1Upper(upper1);

			// END2
			associationData
					.setEnd2InterfaceName(CellUtil.getStringCellValue(row.getCell(7)));
			associationData
					.setEnd2PropertyName(CellUtil.getStringCellValue(row.getCell(8)));

			boolean isNavigable2 = CellUtil.getStringCellValue(row.getCell(9)).contains("yes")
					? true
					: false;
			associationData.setEnd2IsNavigable(isNavigable2);

			AggregationKind aggregationKind2 = getAggregationKind(
					CellUtil.getStringCellValue(row.getCell(10)));
			if (aggregationKind2 != null) {
				associationData.setEnd2Aggregation(aggregationKind2);
			}

			int lower2 = CellUtil.getNumericCellValue(row.getCell(11));
			associationData.setEnd2Lower(lower2);
			int upper2 = CellUtil.getNumericCellValue(row.getCell(12));
			associationData.setEnd2Upper(upper2);

			excelAssociations.add(associationData);
		}
		return excelAssociations;
	}

	private AggregationKind getAggregationKind(String stringValue) {
		return AggregationKind.getByName(stringValue);
	}

	private void updateValues(Association associationInModel, AssociationModel assoc) {
		EList<Property> memberEnds = associationInModel.getMemberEnds();
		Property end2 = memberEnds.get(0);
		Property end1 = memberEnds.get(1);

		Classifier end1Type = getClassifierByName(assoc.getEnd1InterfaceName());
		Classifier end2Type = getClassifierByName(assoc.getEnd2InterfaceName());

		end1.setType(end1Type);
		end1.setName(assoc.getEnd1PropertyName());
		end1.setIsNavigable(assoc.isEnd1IsNavigable());
		end1.setAggregation(assoc.getEnd1Aggregation());
		end1.setLower(assoc.getEnd1Lower());
		end1.setUpper(assoc.getEnd1Upper());

		end2.setType(end2Type);
		end2.setName(assoc.getEnd2PropertyName());
		end2.setIsNavigable(assoc.isEnd2IsNavigable());
		end2.setAggregation(assoc.getEnd2Aggregation());
		end2.setLower(assoc.getEnd2Lower());
		end2.setUpper(assoc.getEnd2Upper());

		EList<Comment> ownedComments = associationInModel.getOwnedComments();
		boolean newCommentNeeded = true;
		for (Comment comment : ownedComments) {
			String body = comment.getBody();
			if (body.equals(assoc.getComment())) {
				newCommentNeeded = false;
			}
		}
		if (newCommentNeeded) {
			associationInModel.createOwnedComment().setBody(assoc.getComment());
		}
	}

	public Classifier getClassifierByName(String name) {
		for (Classifier classifier : classifiers) {
			if (classifier.getName().equals(name)) {
				return classifier;
			}
		}
		return null;
	}

	public void warningMessageDialog(String message) {
		Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		MessageDialog.openWarning(shell, "Warning", message);
	}

}
