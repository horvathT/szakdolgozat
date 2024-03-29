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
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;

import database.modeling.util.uml.ModelObjectUtil;
import model.transfer.export.AssociationSheetCreator;
import model.transfer.export.ClassSummarySheetCreator;
import model.transfer.export.InterfaceSummarySheetCreator;
import model.transfer.util.CellUtil;
import model.transfer.util.ExcelReaderUtil;

/**
 * Asszociáció adatok felovasása Excelből és létrhozása modellben.
 * 
 * @author Horváth Tibor
 *
 */
public class AssociationCreator extends ObjectImporter {

	private List<Classifier> classifiers = new ArrayList<>();

	private List<AssociationModel> associationsFromExcel;

	public AssociationCreator(Workbook workbook, Package modelPackage) {
		super(workbook, modelPackage);

		Collection<Interface> interfaces = ModelObjectUtil.getInterfaces(modelPackage.allOwnedElements());
		Collection<Class> classes = ModelObjectUtil.getClasses(modelPackage.allOwnedElements());
		Collection<Enumeration> enumerations = ModelObjectUtil.getEnumerations(modelPackage.allOwnedElements());
		classifiers.addAll(interfaces);
		classifiers.addAll(classes);
		classifiers.addAll(enumerations);

		associationsFromExcel = getAssociationsFromExcel(
				workbook.getSheet(AssociationSheetCreator.ASSOCIATION_SHEET_NAME));
	}

	public void createAssociations() {
		for (AssociationModel associationModel : associationsFromExcel) {
			createAssociation(associationModel);
		}
	}

	/**
	 * Asszociáció létrehozása. Már létező Asszociáció esetén az adatok frissítése.
	 * 
	 * @param associationData
	 */
	private void createAssociation(AssociationModel associationData) {

		Association associationInModel = getAssociationFromModel(associationData.getModelId());

		if (associationInModel != null) {
			updateValues(associationInModel, associationData);
		} else {
			Classifier startClassifier = getClassifierByName(associationData.getEnd1ClassifierName());
			Classifier endClassifier = getClassifierByName(associationData.getEnd2ClassifierName());
			if (startClassifier == null) {
				warningMessageDialog("Missing association endpoint " + associationData.getEnd1PropertyName() + "."
						+ associationData.getEnd1ClassifierName());
			} else if (endClassifier == null) {
				warningMessageDialog("Missing association endpoint " + associationData.getEnd2PropertyName() + "."
						+ associationData.getEnd2ClassifierName());
			} else {

				Association newAssociation = startClassifier.createAssociation(associationData.isEnd2IsNavigable(),
						associationData.getEnd2Aggregation(), associationData.getEnd2PropertyName(),
						associationData.getEnd2Lower(),
						associationData.getEnd2Upper(), endClassifier, associationData.isEnd1IsNavigable(),
						associationData.getEnd1Aggregation(),
						associationData.getEnd1PropertyName(), associationData.getEnd1Lower(),
						associationData.getEnd1Upper());
				newAssociation.createOwnedComment().setBody(associationData.getComment());
			}
		}
	}

	/**
	 * Egyedi azonosító alapján Asszociáció keresése a modellben. Ha nem létezik
	 * null-al tér vissza.
	 * 
	 * @param modelId
	 * @return
	 */
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

	/**
	 * Asszociáció adatok összegyűtése excelből és {@link AssociationModel}
	 * oberktumokká alakítása.
	 * 
	 * @param associationSheet
	 * @return
	 */
	private List<AssociationModel> getAssociationsFromExcel(Sheet associationSheet) {
		List<AssociationModel> excelAssociations = new ArrayList<>();
		int lastRowNum = associationSheet.getLastRowNum();
		for (int i = 1; i <= lastRowNum; i++) {
			Row row = associationSheet.getRow(i);
			if (row == null) {
				continue;
			}
			AssociationModel associationData = new AssociationModel();
			associationData
					.setModelId(CellUtil.getStringCellValue(row.getCell(0)));
			// END1
			associationData
					.setEnd1ClassifierName(CellUtil.getStringCellValue(row.getCell(1)));
			associationData
					.setEnd1PropertyName(CellUtil.getStringCellValue(row.getCell(2)));

			String end1Navigable = CellUtil.getStringCellValue(row.getCell(3));
			boolean isNavigable1 = ExcelReaderUtil.stringToBoolean(end1Navigable);
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
					.setEnd2ClassifierName(CellUtil.getStringCellValue(row.getCell(7)));
			associationData
					.setEnd2PropertyName(CellUtil.getStringCellValue(row.getCell(8)));

			String end2Navigable = CellUtil.getStringCellValue(row.getCell(9));
			boolean isNavigable2 = ExcelReaderUtil.stringToBoolean(end2Navigable);
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

	/**
	 * Asszociáció adatainak frissítése.
	 * 
	 * @param associationInModel
	 * @param assoc
	 */
	private void updateValues(Association associationInModel, AssociationModel assoc) {
		EList<Property> memberEnds = associationInModel.getMemberEnds();
		Property end2 = memberEnds.get(0);
		Property end1 = memberEnds.get(1);

		Classifier end1Type = getClassifierByName(assoc.getEnd1ClassifierName());
		Classifier end2Type = getClassifierByName(assoc.getEnd2ClassifierName());

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

	private Classifier getClassifierByName(String name) {
		for (Classifier classifier : classifiers) {
			if (classifier.getName().equals(name)) {
				return classifier;
			}
		}
		return null;
	}

	private void warningMessageDialog(String message) {
		Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		MessageDialog.openWarning(shell, "Warning", message);
	}

	/**
	 * A modellben még igen de az Excelben már nem szereplő asszociációk törlése.
	 */
	public void removeDeletedAssociations() {
		Collection<Association> associations = ModelObjectUtil.getAssociations(modelPackage.allOwnedElements());
		for (Association association : associations) {
			if (!validAssociation(association)) {
				association.getMemberEnds().get(0).destroy();
				association.destroy();
			}
		}
	}

	private boolean validAssociation(Association association) {
		if (associationsFromExcel.isEmpty()) {
			return false;
		}
		for (AssociationModel associationModel : associationsFromExcel) {
			EList<Property> memberEnds = association.getMemberEnds();
			Property end1 = memberEnds.get(0);
			Property end2 = memberEnds.get(1);
			String end1ClassifierName = end1.getType().getName();
			String end1PropertyName = end1.getName();
			String end2ClassifierName = end2.getType().getName();
			String end2PropertyName = end2.getName();

			if (associationModel.getEnd1ClassifierName().equals(end1ClassifierName) &&
					associationModel.getEnd1PropertyName().equals(end1PropertyName) &&
					associationModel.getEnd2ClassifierName().equals(end2ClassifierName) &&
					associationModel.getEnd2PropertyName().equals(end2PropertyName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Entitások közötti öröklődési kapcsolatok létrehozása.
	 */
	public void createEntityHierarchy() {
		createInterfaceGeneralizations();
		createClassHierarchy();
	}

	/**
	 * Osztályok öröklődési kapcsolatainak létrehozása.
	 */
	private void createClassHierarchy() {
		Sheet classSummarySheet = workbook.getSheet(ClassSummarySheetCreator.SHEET_NAME);
		int lastRowNum = classSummarySheet.getLastRowNum();
		Classifier classifier = null;

		for (int i = 1; i <= lastRowNum; i++) {
			Row row = classSummarySheet.getRow(i);
			if (row == null) {
				continue;
			}

			String name = CellUtil.getStringCellValue(row.getCell(1));
			if (!name.isEmpty()) {
				classifier = getClassifierByName(name);
				classifier.getGeneralizations().clear();
				classifier.allRealizedInterfaces().clear();
			}
			String generalizedInterfaceName = CellUtil.getStringCellValue(row.getCell(4));
			if (!generalizedInterfaceName.isEmpty()) {
				Classifier parentClassifier = getClassifierByName(generalizedInterfaceName);
				classifier.createGeneralization(parentClassifier);
			}

			String implementedInterfaceName = CellUtil.getStringCellValue(row.getCell(5));
			if (!implementedInterfaceName.isEmpty()) {
				Classifier implementedInterface = getClassifierByName(implementedInterfaceName);
				addInterfaceImplementationRelation(classifier, implementedInterface);
			}
		}
	}

	/**
	 * Intrefészek öröklődési kapcsolatainak létrehozása.
	 */
	private void createInterfaceGeneralizations() {
		Sheet interfaceSummarySheet = workbook.getSheet(InterfaceSummarySheetCreator.SHEET_NAME);
		int lastRowNum = interfaceSummarySheet.getLastRowNum();
		Classifier classifier = null;

		for (int i = 1; i <= lastRowNum; i++) {
			Row row = interfaceSummarySheet.getRow(i);
			if (row == null) {
				continue;
			}

			String name = CellUtil.getStringCellValue(row.getCell(1));
			if (!name.isEmpty()) {
				classifier = getClassifierByName(name);
				classifier.getGeneralizations().clear();
			}
			String generalizedClassifierName = CellUtil.getStringCellValue(row.getCell(3));
			if (!generalizedClassifierName.isEmpty()) {
				Classifier parentClassifier = getClassifierByName(generalizedClassifierName);
				classifier.createGeneralization(parentClassifier);
			}
		}
	}

	private void addInterfaceImplementationRelation(Classifier classifier, Classifier implementedInterface) {
		InterfaceRealization realization = UMLFactory.eINSTANCE.createInterfaceRealization();
		realization.setContract((Interface) implementedInterface);
		realization.setImplementingClassifier((Class) classifier);
	}

}
