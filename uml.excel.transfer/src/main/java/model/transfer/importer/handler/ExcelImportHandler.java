
package model.transfer.importer.handler;

import java.io.IOException;

import javax.inject.Named;

import org.apache.poi.EncryptedDocumentException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.Package;

import database.modeling.util.resource.EclipseResourceUtil;
import database.modeling.util.resource.EclipseSelectionUtil;
import model.transfer.export.ModelExporter;
import model.transfer.importer.ModelImporter;
import model.transfer.importer.validator.ExcelStructureValidator;

/**
 * Excel importálás kezelését végzi.
 * 
 * @author Horváth Tibor
 *
 */
public class ExcelImportHandler {

	/**
	 * Importálandó excel fájl validálását végzi, majd ha nincs benne hiba az Excel
	 * importálás végrehajtását.
	 * 
	 * @param selection
	 */
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {

		Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		Package modelPackage = EclipseSelectionUtil.getPackage(EclipseSelectionUtil.getFirstSelected(selection));

		String excelFolderLoc = getFolderPath(modelPackage);
		String filePath = importFileDialog(shell, excelFolderLoc);
		if (filePath == null) {
			return;
		}

		ExcelStructureValidator excelValidator = new ExcelStructureValidator(filePath);
		ModelImporter excel2Model = new ModelImporter(modelPackage, filePath);
		try {
			excelValidator.validateInput();
			excel2Model.executeImport();

//			 RE-EXPORT TO FILL UP XMI ID-s
			ModelExporter model2excel = new ModelExporter();
			model2excel.export(modelPackage, filePath);
		} catch (EncryptedDocumentException e) {
			MessageDialog.openError(shell, "Import failed",
					"Failed to import file! Make sure the file is not encrypted.");
			e.printStackTrace();
		} catch (IOException e) {
			MessageDialog.openError(shell, "Import failed",
					"Failed to import file! Make sure the file is closed before importing.");
			e.printStackTrace();
		}
	}

	/**
	 * Ellenőrzi, hogy a kiválasztott elemen végrehajtható-e az importálás.
	 * 
	 * @param selection
	 * @return
	 */
	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
		return EclipseSelectionUtil.getPackage(EclipseSelectionUtil.getFirstSelected(selection)) != null;
	}

	/**
	 * Kinyitja a fájlkezelőt az importálandó fájl kiválasztásához. Csak a ".xlsx"
	 * kiterjesztésű fájlokat listázza.
	 * 
	 * @param shell
	 * @param defPath
	 * @return
	 */
	private String importFileDialog(Shell shell, String defPath) {
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		dialog.setFilterExtensions(new String[] { "*.xlsx" });
		dialog.setFilterPath(defPath);
		return dialog.open();
	}

	/**
	 * A modell szülőkönyvtárának elérési útvonalával tér vissza.
	 * 
	 * @param modelPackage
	 * @return
	 */
	private String getFolderPath(Package modelPackage) {
		IPath path = EclipseResourceUtil.getResourceFileLocationPath(modelPackage.eResource());
		return path.removeLastSegments(1).toOSString();
	}
}