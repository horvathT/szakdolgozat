package model.transfer.export;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Package;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import database.modeling.util.resource.EclipseResourceUtil;
import database.modeling.util.uml.ModelObjectUtil;

public class ModelExporter {
	private static final Bundle BUNDLE = FrameworkUtil.getBundle(ModelExporter.class);
	private static final ILog LOGGER = Platform.getLog(BUNDLE);

	public void export(Package modelPackage, String filePath) {

		Workbook workbook = new XSSFWorkbook();
		EList<Element> modelPackageElements = getModelPackageElements(modelPackage);

		// DATA TYPE
		Collection<DataType> dataTypes = ModelObjectUtil.getDataTypesFromModel(modelPackage);
		DataTypeSheetCreator dataTypeSheetCreator = new DataTypeSheetCreator(workbook,
				new ArrayList<DataType>(dataTypes));
		dataTypeSheetCreator.createSheet();

		// ASSOCIATION
		Collection<Association> associations = ModelObjectUtil.getAssociations(modelPackageElements);
		AssociationSheetCreator associationSheetCreator = new AssociationSheetCreator(workbook, associations);
		associationSheetCreator.createSheet();

		// ENUM
		Collection<Enumeration> enumerations = ModelObjectUtil.getEnumerations(modelPackageElements);
		EnumSheetCreator enumSheetCreator = new EnumSheetCreator(workbook, enumerations);
		enumSheetCreator.creatSheet();

		// INTERFACE SUMMARY
		Collection<Interface> interfaces = ModelObjectUtil.getInterfaces(modelPackageElements);

		InterfaceSummarySheetCreator interfaceSummarySheetCreator = new InterfaceSummarySheetCreator(workbook,
				interfaces);
		interfaceSummarySheetCreator.createSheet();

		// CLASS SUMMARY
		Collection<Class> classes = ModelObjectUtil.getClasses(modelPackageElements);
		ClassSummarySheetCreator classSummarySheetCreator = new ClassSummarySheetCreator(workbook, classes);
		classSummarySheetCreator.createSheet();

		// INTERFACE DETAIL SHEETS
		InterfaceSheetCreator entitySheetCreator = new InterfaceSheetCreator(workbook, interfaces);
		entitySheetCreator.createInterfaceSheets();

		// CLASS DETAIL SHEETS
		ClassSheetCreator classSheetCreator = new ClassSheetCreator(workbook, classes);
		classSheetCreator.createClassSheets();

		autoSizeColumns(workbook);

		try {
			OutputStream fileOut = new FileOutputStream(filePath);
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();
			EclipseResourceUtil.refreshWorkspaceRoot();
		} catch (FileNotFoundException e) {
			LOGGER.error("Output file not found!", e);
			Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
			MessageDialog.openError(shell, "Export failed", "Output file not found!");
		} catch (IOException e) {
			LOGGER.error("Failed to write file!", e);
			Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
			MessageDialog.openError(shell, "Export failed", "Failed to write file!");
		} catch (CoreException e) {
			LOGGER.error("Failed to reopen exported file!", e);
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

	public EList<Element> getModelPackageElements(Package modelPackage) {
		return modelPackage.allOwnedElements();
	}

}
