
package model.transfer.export.handler;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.inject.Named;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.Package;

import database.modeling.util.resource.EclipseResourceUtil;
import database.modeling.util.resource.EclipseSelectionUtil;
import model.transfer.export.ModelExporter;

public class ModelExportHandler {
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
		Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		Package modelPackage = EclipseSelectionUtil.getPackage(EclipseSelectionUtil.getFirstSelected(selection));

		String excelFolderLoc = getExcelFolderPath(modelPackage);
		String filePath = exportToFileDialog(shell, excelFolderLoc);
		if (filePath != null) {
			ModelExporter model2excel = new ModelExporter();
			model2excel.export(modelPackage, filePath);
			try {
				Desktop.getDesktop().open(new File(filePath));
			} catch (IOException e) {
				throw new IllegalArgumentException("A generált fájl megnyitása hibába ütközött", e);
			}
		}
	}

	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
		return EclipseSelectionUtil.getPackage(EclipseSelectionUtil.getFirstSelected(selection)) != null;
	}

	private String exportToFileDialog(Shell shell, String defPath) {
		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog.setFilterExtensions(new String[] { "*.xlsx" });
		dialog.setFilterPath(defPath);
		return dialog.open();
	}

	private String getExcelFolderPath(Package modelPackage) {
		IPath path = EclipseResourceUtil.getResourceFileLocationPath(modelPackage.eResource());
		String modelParentFolder = path.removeLastSegments(1).toOSString();
		String folderPath = null;
		try {
			folderPath = EclipseResourceUtil.createFolder(modelParentFolder, "excel");
		} catch (CoreException e) {
			throw new IllegalArgumentException("Failed to create folder named excel at: " + modelParentFolder, e);
		}
		return folderPath;
	}

}