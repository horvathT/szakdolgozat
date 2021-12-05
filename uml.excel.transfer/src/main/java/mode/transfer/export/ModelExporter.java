package mode.transfer.export;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.UMLPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import database.modeling.util.resource.EclipseResourceUtil;

public class ModelExporter {
	public static final Logger log = LoggerFactory.getLogger(ModelExporter.class);

	public void export(Package modelPackage, String filePath) {

		Workbook workbook = new XSSFWorkbook();
		EList<Element> modelPackageElements = getModelPackageElements(modelPackage);

		// DATA TYPE
		List<DataType> dataTypes = getDataTypes(modelPackage);
		DataTypeSheetCreator dataTypeSheetCreator = new DataTypeSheetCreator(workbook, dataTypes);
		dataTypeSheetCreator.createSheet();

		// ASSOCIATION
		Collection<Association> associations = getAssociations(modelPackageElements);
		AssociationSheetCreator associationSheetCreator = new AssociationSheetCreator(workbook, associations);
		associationSheetCreator.createSheet();

		// ENUM
		Collection<Enumeration> enumerations = getEnumerations(modelPackageElements);
		EnumSheetCreator enumSheetCreator = new EnumSheetCreator(workbook, enumerations);
		enumSheetCreator.creatSheet();

		// ENTITY SUMMARY
		List<Classifier> classifiers = new ArrayList<>();
		Collection<Interface> interfaces = getInterfaces(modelPackageElements);
		classifiers.addAll(interfaces);
		Collection<Class> classes = getClasses(modelPackageElements);
		classifiers.addAll(classes);

		InterfaceSummarySheetCreator interfaceSummarySheetCreator = new InterfaceSummarySheetCreator(workbook,
				interfaces);
		interfaceSummarySheetCreator.createSheet();

		ClassSummarySheetCreator classSummarySheetCreator = new ClassSummarySheetCreator(workbook, classes);
		classSummarySheetCreator.createSheet();

		// ENTITY DETAIL SHEETS
		InterfaceSheetCreator entitySheetCreator = new InterfaceSheetCreator(workbook, interfaces);
		entitySheetCreator.createInterfaceSheets();

		ClassSheetCreator classSheetCreator = new ClassSheetCreator(workbook, classes);
		classSheetCreator.createClassSheets();

		autoSizeColumns(workbook);

		try {
			OutputStream fileOut = new FileOutputStream(filePath);
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();
			EclipseResourceUtil.refreshWorkspaceRoot();
			IFile fileResource = EclipseResourceUtil.getFileResource(filePath);
			EclipseResourceUtil.openWithExternalEditor(fileResource);
		} catch (FileNotFoundException e) {
			log.error("Output file not found!", e);
			Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
			MessageDialog.openError(shell, "Export failed", "Output file not found!");
		} catch (IOException e) {
			log.error("Failed to write file!", e);
			Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
			MessageDialog.openError(shell, "Export failed", "Failed to write file!");
		} catch (CoreException e) {
			log.error("Failed to reopen exported file!", e);
			Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
			MessageDialog.openError(shell, "Export failed", "Failed to reopen exported file!");
		}
	}

	public void autoSizeColumns(Workbook workbook) {
		int numberOfSheets = workbook.getNumberOfSheets();
		for (int i = 0; i < numberOfSheets; i++) {
			Sheet sheet = workbook.getSheetAt(i);
			for (int index = 0; index < 21; index++) {
				sheet.autoSizeColumn(index);
			}
		}
	}

	private List<DataType> getDataTypes(Package modelPackage) {
		ResourceSet resourceSet = modelPackage.getModel().eResource().getResourceSet();
		return getDataTypes(resourceSet);
	}

	public Collection<Interface> getInterfaces(EList<Element> elementList) {
		return EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.INTERFACE);
	}

	private Collection<Class> getClasses(EList<Element> elementList) {
		return EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.CLASS);
	}

	private Collection<Enumeration> getEnumerations(EList<Element> elementList) {
		return EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.ENUMERATION);
	}

	public Collection<Association> getAssociations(EList<Element> elementList) {
		return EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.ASSOCIATION);
	}

	public EList<Element> getModelPackageElements(Package modelPackage) {
		return modelPackage.allOwnedElements();
	}

	public static List<DataType> getDataTypes(ResourceSet resourceSet) {
		List<DataType> dTypes = new ArrayList<>();
		for (Resource resource : resourceSet.getResources()) {
			TreeIterator<EObject> allContents = resource.getAllContents();
			Collection<DataType> allDataType = getPrimitiveTypes(allContents);
			dTypes.addAll(allDataType);
		}
		return dTypes;
	}

	public static Collection<DataType> getPrimitiveTypes(TreeIterator<EObject> elements) {
		Collection<DataType> collection = new HashSet<>();
		while (elements.hasNext()) {
			EObject next = elements.next();
			if (next instanceof PrimitiveType) {
				DataType element = (DataType) next;
				collection.add(element);
			}
		}
		return collection;
	}

}
