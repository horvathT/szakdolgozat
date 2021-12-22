
package uml.papyrus.script.handler;

import javax.inject.Named;

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

import database.modeling.util.resource.EclipseSelectionUtil;
import database.modeling.util.stereotype.DatabaseModelUtil;
import uml.papyrus.script.generator.MySqlScriptGenerator;
import uml.papyrus.script.generator.OracleScriptGenerator;
import uml.papyrus.script.generator.PostgreSqlScriptGenerator;
import uml.papyrus.script.validator.ModelValidator;

public class ScriptGeneratorHandler {

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
		Package modelPackage = EclipseSelectionUtil.getPackage(EclipseSelectionUtil.getFirstSelected(selection));
		ModelValidator validator = new ModelValidator(modelPackage);
		validator.validateModel();

		Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		String databaseType = DatabaseModelUtil.getDatabaseType(modelPackage);
		if (databaseType.isEmpty()) {
			MessageDialog.openError(shell, "Generálási hiba", "A modellen adatbázis típus meghatározva!");
			System.exit(1);
		}

		String fileDestinationPath = openFileDialog(shell);
		if (fileDestinationPath == null) {
			return;
		}

		if (databaseType.equals(OracleScriptGenerator.IDENTIFIER)) {

			OracleScriptGenerator oracleScriptGenerator = new OracleScriptGenerator(modelPackage, fileDestinationPath);
			oracleScriptGenerator.generateDdlScript();

		} else if (databaseType.equals(MySqlScriptGenerator.IDENTIFIER)) {

		} else if (databaseType.equals(PostgreSqlScriptGenerator.IDENTIFIER)) {

		} else {
			MessageDialog.openError(shell, "Generálási hiba",
					databaseType + " névvel adatbázis implementáció nem található!");
			System.exit(1);
		}

	}

	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
		return EclipseSelectionUtil.getPackage(EclipseSelectionUtil.getFirstSelected(selection)) != null;
	}

	private String openFileDialog(Shell shell) {
		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog.setFilterExtensions(new String[] { "*.sql" });
		return dialog.open();
	}

}