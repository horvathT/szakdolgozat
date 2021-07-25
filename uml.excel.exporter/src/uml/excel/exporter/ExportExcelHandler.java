
package uml.excel.exporter;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.inject.Named;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLPackage;
import org.smartbit4all.tooling.emf.EclipseResourceUtil;
import org.smartbit4all.tooling.emf.EclipseSelectionUtil;
import SB4UMLProfile.EntityDataType;
import tooling.stereotype.EntityDataTypeUtil;
import uml.excel.exporter.excelutils.DropDownList;
import uml.excel.exporter.excelutils.SheetFactory;


public class ExportExcelHandler {

  private SheetFactory sheetFactory = new SheetFactory();

  public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
    return EclipseSelectionUtil
        .getPackage(EclipseSelectionUtil.getFirstSelected(selection)) != null;
  }


  @Execute
  public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
    Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
    Package modelPackage =
        EclipseSelectionUtil.getPackage(EclipseSelectionUtil.getFirstSelected(selection));
    try {
      String excelFolderLoc = getExcelFolderPath(modelPackage);
      String filePath = exportToFileDialog(shell, excelFolderLoc);
      if (filePath != null) {
        execute(selection, filePath);
        Desktop.getDesktop().open(new File(filePath));
      }
    } catch (CoreException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }

  public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection,
      String filePath) {
    Package modelPackage =
        EclipseSelectionUtil.getPackage(EclipseSelectionUtil.getFirstSelected(selection));

    Collection<Interface> interfaces = getInterfaces(getModelPackageElements(modelPackage));
    Collection<DataType> dataTypes = getDataTypes(modelPackage);
    Collection<Association> associations = getAssociations(getModelPackageElements(modelPackage));

    Workbook workbook = new XSSFWorkbook();
    sheetFactory.createDataTypeSheet(workbook, dataTypes);
    Sheet associationSheet = sheetFactory.createAssociationSheet(workbook, associations);
    sheetFactory.createEntitiyUtilSheet(workbook, interfaces);
    List<Sheet> propertySheets = sheetFactory.createEntityPropertySheets(workbook, interfaces);
    autoSizeColumns(workbook);
    
    DropDownList ddList = new DropDownList(workbook);
    ddList.createAssociationSheetDropDowns(associationSheet);
    ddList.createEntityPropertySheetDropDowns(propertySheets);

    try {
      OutputStream fileOut = new FileOutputStream(filePath);
      workbook.write(fileOut);
      fileOut.close();
      workbook.close();
      EclipseResourceUtil.refreshWorkspaceRoot();
      //IFile fileResource = EclipseResourceUtil.getFileResource(filePath);
     //EclipseResourceUtil.openWithExternalEditor(fileResource);
    } catch (CoreException | IOException e) {
      Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
      MessageDialog.openError(shell, "Export failed", "Failed to export!");
    } 
  }

  public void autoSizeColumns(Workbook workbook) {
    int numberOfSheets = workbook.getNumberOfSheets();
    for (int i = 0; i < numberOfSheets; i++) {
        Sheet sheet = workbook.getSheetAt(i);
        if (sheet.getPhysicalNumberOfRows() > 0) {
            Row row = sheet.getRow(sheet.getFirstRowNum());
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                int columnIndex = cell.getColumnIndex();
                sheet.autoSizeColumn(columnIndex);
            }
        }
    }
}

  private String exportToFileDialog(Shell shell, String defPath) {
    FileDialog dialog = new FileDialog(shell, SWT.SAVE);
    dialog.setFilterExtensions(new String[] {"*.xlsx"});
    dialog.setFilterPath(defPath);
    return dialog.open();
  }

  private String getExcelFolderPath(Package modelPackage) throws CoreException {
    IPath path = EclipseResourceUtil.getResourceFileLocationPath(modelPackage.eResource());
    String modelParentFolder = path.removeLastSegments(1).toOSString();
    return EclipseResourceUtil.createFolder(modelParentFolder, "excel");
  }

  private Collection<DataType> getDataTypes(Package modelPackage) {
    ResourceSet resourceSet = modelPackage.eResource().getResourceSet();
    return EntityDataTypeUtil.getEntityDataTypes(resourceSet);
  }
  
  public Collection<Interface> getInterfaces(EList<Element> elementList) {
    return EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.INTERFACE);
  }

  public Collection<Enumeration> getEnums(EList<Element> elementList) {
    return EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.ENUMERATION);
  }

  public Collection<Association> getAssociations(EList<Element> elementList) {
    return EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.ASSOCIATION);
  }

  public EList<Element> getModelPackageElements(Package modelPackage) {
    return modelPackage.allOwnedElements();
  }
}
