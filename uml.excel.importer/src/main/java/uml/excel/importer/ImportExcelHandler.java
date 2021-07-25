
package uml.excel.importer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.smartbit4all.tooling.emf.EclipseResourceUtil;
import org.smartbit4all.tooling.emf.EclipseSelectionUtil;
import tooling.stereotype.ColumnUtil;
import tooling.stereotype.EntityDataTypeUtil;
import tooling.stereotype.TableUtil;
import tooling.stereotype.UniqueIdentifierUtil;
import uml.excel.exporter.ExportExcelHandler;
import util.CellReaderUtil;

public class ImportExcelHandler {

  @Inject
  private Logger log;
  
  private final String DATA_TYPE_SHEET_NAME = "DataTypes";
  private final String ASSOCIATION_SHEET_NAME = "Associations";
  private final String ENTITIES_SHEET_NAME = "Entities";

  @CanExecute
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
      String importFile = importFileDialog(shell, excelFolderLoc);
      importExcel(modelPackage, importFile);
      
      // RE-EXPORT UPDATED DATA TO EXCEL FILE
      ExportExcelHandler exporter = new ExportExcelHandler();
      exporter.execute(selection, importFile);
    } catch (EncryptedDocumentException | IOException | CoreException e) {
      MessageDialog.openError(shell, "Import failed", "Failed to import from Excel! Make sure to close file before import.");
    }
  }

  public void importExcel( Package modelPackage, String importFile)
      throws IOException {
    Workbook workbook = WorkbookFactory.create(new File(importFile));
    TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(modelPackage);
    
    RecordingCommand recordingCommand = new RecordingCommand(editingDomain) {
      @Override
      protected void doExecute() {
        
        //removeDeletedEntities(workbook, modelPackage);
        
        Sheet dataTypeSheet = workbook.getSheet(DATA_TYPE_SHEET_NAME);
        DataTypeCreator dtc = new DataTypeCreator(modelPackage);
        dtc.createNewTypes(dataTypeSheet);
        
        Sheet entityUtil = workbook.getSheet(ENTITIES_SHEET_NAME);
        InterfaceCreator ifaceCreator = new InterfaceCreator();
        List<Interface> interfaces = ifaceCreator.createInterfacesFromSheet(modelPackage, entityUtil);
        
        for(Interface interfac : interfaces) {
          Sheet sheet = workbook.getSheet(interfac.getName());
          PropertyCreator propCreator = new PropertyCreator();
          propCreator.interfacePropertiesFromSheet(modelPackage, sheet);
        }
      }
    };
    editingDomain.getCommandStack().execute(recordingCommand);
    
    RecordingCommand recordingCommand2 = new RecordingCommand(editingDomain) {
      @Override
      protected void doExecute() {
        Sheet associationSheet = workbook.getSheet(ASSOCIATION_SHEET_NAME);
        AssociationCreator assocCreator = new AssociationCreator(modelPackage);
        assocCreator.associationFromSheet(associationSheet);
      }
    };
    editingDomain.getCommandStack().execute(recordingCommand2);
    workbook.close();
    
  }

  private void removeDeletedEntities(Workbook workbook, Package modelPackage) {
    Sheet entityUtilSheet = workbook.getSheet(ENTITIES_SHEET_NAME);
    Collection<Interface> interfaces =
        EcoreUtil.getObjectsByType(modelPackage.allOwnedElements(), UMLPackage.Literals.INTERFACE);
    
    List<String> entitiesInExcel = new ArrayList<String>();
    int rowNum = entityUtilSheet.getLastRowNum();
    for(int i=1; i<= rowNum; i++) {
      String entityName = CellReaderUtil.getStringValue(entityUtilSheet.getRow(i).getCell(1)); 
      entitiesInExcel.add(entityName);
    }
    for (Iterator<Interface> iterator = interfaces.iterator(); iterator.hasNext();) {
      Interface interfac = iterator.next();
      if(!entitiesInExcel.contains(interfac.getName())) {
        EcoreUtil.delete(interfac);
        iterator.remove();
      }
    }
  }

  private String getExcelFolderPath(Package modelPackage) throws CoreException {
    IPath path = EclipseResourceUtil.getResourceFileLocationPath(modelPackage.eResource());
    String modelParentFolder = path.removeLastSegments(1).toOSString();
    return EclipseResourceUtil.createFolder(modelParentFolder, "excel");
  }
  
  private String importFileDialog(Shell shell, String defPath) {
    FileDialog dialog = new FileDialog(shell, SWT.OPEN);
    dialog.setFilterExtensions(new String[] {"*.xlsx"});
    dialog.setFilterPath(defPath);
    return dialog.open();
  }

}
