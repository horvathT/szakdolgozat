package database.modeling.controller;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IPartListener;
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
	private Property currentPropertySelection = null;

	private DatabaseModelingView view;

	public DatabaseModelingController(DatabaseModelingView view) {
		this.view = view;
	}

	public void init() {
		view.listener = new ISelectionListener() {
			public void selectionChanged(IWorkbenchPart sourcepart, ISelection selection) {
				Property property = SelectionUtil.getProperty(selection);
				if(property != null) {
					updateSelection(property);
				}
				
			}
		};
		view.getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(view.listener);
		
		view.getSite().getPage().addPartListener(new IPartListener() {
			
			@Override
			public void partOpened(IWorkbenchPart part) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void partDeactivated(IWorkbenchPart part) {
				if(part.equals(view)) {
					save();
				}
//				if (part instanceof DatabaseModelingView) {
//					save();
//				}
			}
			
			@Override
			public void partClosed(IWorkbenchPart part) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void partBroughtToTop(IWorkbenchPart part) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void partActivated(IWorkbenchPart part) {
				// TODO Auto-generated method stub
			}
		});
	}

	protected void save() {
		if (currentPropertySelection == null) {
			return;
		}
		if(view.isEmpty()) {
			return;
		}

		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(currentPropertySelection);
		RecordingCommand recordingCommand = new RecordingCommand(editingDomain) {
			@Override
			protected void doExecute() {
				ProfileUtil.applyPofile(currentPropertySelection);
				ColumnUtil.setOracleDataType(currentPropertySelection, view.getSqlTypeCombo().getText());
				SqlDataModel data = view.getData();
				ColumnUtil.setOracleDefaultValue(currentPropertySelection, view.getDefaultValue().getText());
			}
		};
		editingDomain.getCommandStack().execute(recordingCommand);
	}

	protected void updateSelection(Property property) {
		currentPropertySelection = property;
		view.getLblNewLabel().setText(currentPropertySelection.getName());
	}

}
