
package model.transfer.importer;

import javax.inject.Named;

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
import org.slf4j.Logger;

import database.modeling.util.resource.EclipseResourceUtil;
import database.modeling.util.resource.EclipseSelectionUtil;
import mode.transfer.export.ModelExporter;

public class ExcelImportHandler {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(ExcelImportHandler.class);

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {

		Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		Package modelPackage = EclipseSelectionUtil.getPackage(EclipseSelectionUtil.getFirstSelected(selection));

		String excelFolderLoc = getExcelFolderPath(modelPackage);
		String filePath = importFileDialog(shell, excelFolderLoc);

		ModelImporter excel2Model = new ModelImporter(modelPackage, filePath);
		excel2Model.validateInput();
		excel2Model.executeImport();

		// RE-EXPORT TO FILL UP XMI ID-s
		ModelExporter model2excel = new ModelExporter();
		model2excel.export(modelPackage, filePath);
	}

	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
		return EclipseSelectionUtil.getPackage(EclipseSelectionUtil.getFirstSelected(selection)) != null;
	}

	private String importFileDialog(Shell shell, String defPath) {
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		dialog.setFilterExtensions(new String[] { "*.xlsx" });
		dialog.setFilterPath(defPath);
		return dialog.open();
	}

	private String getExcelFolderPath(Package modelPackage) {
		IPath path = EclipseResourceUtil.getResourceFileLocationPath(modelPackage.eResource());
		return path.removeLastSegments(1).toOSString();
	}
}