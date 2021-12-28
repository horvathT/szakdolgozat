
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

/**
 * Script generálás kezelése.
 * 
 * @author Horváth Tibor
 *
 */
public class ScriptGeneratorHandler {

	/**
	 * Model validálása. Ha minden adat megfelelő a modellen szereplő SQL
	 * implmentáció jelölés szerinti generálás végrehajtása.
	 * 
	 * @param selection
	 */
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
		Package modelPackage = EclipseSelectionUtil.getPackage(EclipseSelectionUtil.getFirstSelected(selection));
		ModelValidator validator = new ModelValidator(modelPackage);
		validator.validateModel();

		Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		String databaseType = DatabaseModelUtil.getDatabaseType(modelPackage);
		if (databaseType.isEmpty()) {
			MessageDialog.openError(shell, "Generation failed", "The model has no database type set!");
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

			MySqlScriptGenerator mySqlScriptGenerator = new MySqlScriptGenerator(modelPackage, fileDestinationPath);
			mySqlScriptGenerator.generateDdlScript();

		} else if (databaseType.equals(PostgreSqlScriptGenerator.IDENTIFIER)) {

			PostgreSqlScriptGenerator postgreSqlScriptGenerator = new PostgreSqlScriptGenerator(modelPackage,
					fileDestinationPath);
			postgreSqlScriptGenerator.generateDdlScript();

		} else {
			MessageDialog.openError(shell, "Generation failed",
					"SQL implementation with name " + databaseType + " not found!");
			System.exit(1);
		}

	}

	/**
	 * Ellenőrzi, hogy a kijelölt elemen végrehajtható-e a script generálás.
	 * 
	 * @param selection
	 * @return
	 */
	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
		return EclipseSelectionUtil.getPackage(EclipseSelectionUtil.getFirstSelected(selection)) != null;
	}

	/**
	 * Féjlkezelő kinyitása a célmappa kiválasztásához.
	 * 
	 * @param shell
	 * @return
	 */
	private String openFileDialog(Shell shell) {
		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog.setFilterExtensions(new String[] { "*.sql" });
		return dialog.open();
	}

}