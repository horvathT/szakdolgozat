package database.modeling.controller;

import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;

import database.modeling.model.SqlDataModel;
import database.modeling.view.DatabaseModelingView;
import database.modeling.view.util.ColumnUtil;
import database.modeling.view.util.ProfileUtil;
import database.modeling.view.util.SelectionUtil;

public class DatabaseModelingController {
	private Property currentSelection = null;

	private DatabaseModelingView view;

	public DatabaseModelingController(DatabaseModelingView view) {
		this.view = view;
	}

	public void init() {
		view.listener = new ISelectionListener() {
			public void selectionChanged(IWorkbenchPart sourcepart, ISelection selection) {
				//save button vagy valami szar kell mert kurvára idegesítő 
				//save();

				if (SelectionUtil.isPropertyFromModelEditor(selection)) {

					updateLatestValidSelectionFromDiagramEditor(selection);
					updateSelectionFromDiagramEditor(selection);

				} else if (SelectionUtil.isPropertyFromModelExplorer(selection)) {

					updateSelectionFromModelExplorer(selection);
					updateLatestValidSelectionFromModelExplorer(selection);

				} else {
					// setContentDescription("Nothing to show");
				}
			}
		};
		view.getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(view.listener);
	}

	protected void save() {
		if (currentSelection == null) {
			return;
		}

		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(currentSelection);
		RecordingCommand recordingCommand = new RecordingCommand(editingDomain) {
			@Override
			protected void doExecute() {
				applyProfile();
				ColumnUtil.applyStereotype(currentSelection);
				ColumnUtil.setOracleDataType(currentSelection, view.getSqlTypeCombo().getText());
				ColumnUtil.setOracleDefaultValue(currentSelection, view.getDefaultValue().getText());
			}
		};
		editingDomain.getCommandStack().execute(recordingCommand);

	}

	private void applyProfile() {
		Profile profile = ProfileUtil.retrieveProfile();
		Model umlModel = currentSelection.getModel();
		if (!umlModel.isProfileApplied(profile)) {
			umlModel.applyProfile(profile);
		}
	}

	protected void updateSelectionFromDiagramEditor(ISelection selection) {
		// setContentDescription(SelectionUtil.getPropertyFromModelEditor(selection).getName());
	}

	protected void updateLatestValidSelectionFromDiagramEditor(ISelection selection) {
		currentSelection = SelectionUtil.getPropertyFromModelEditor(selection);
	}

	protected void updateSelectionFromModelExplorer(ISelection selection) {
		// setContentDescription(SelectionUtil.getPropertyFromModelExplorer(selection).getName());
	}

	protected void updateLatestValidSelectionFromModelExplorer(ISelection selection) {
		currentSelection = SelectionUtil.getPropertyFromModelExplorer(selection);
	}

}
