package database.modeling.controller;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ISelectionListener;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.uml2.uml.Property;

import database.modeling.model.DataTransformer;
import database.modeling.model.SqlDataModel;
import database.modeling.view.DatabaseModelingView;
import database.modeling.view.util.SelectionUtil;

public class DatabaseModelingController {
	private Property currentPropertySelection = null;

	private DatabaseModelingView view;
	private ToolItem databaseChanger;

	public DatabaseModelingController(DatabaseModelingView view) {
		this.view = view;
	}

	public void init() {
		initSelectionChangeListener();
		// initPartListener();
		initDatabaseChangeListener();
	}

	private void initDatabaseChangeListener() {
		databaseChanger = view.getDatabaseChanger();
		Combo sqlTypeCombo = view.getSqlTypeCombo();
		DatabaseSelectionListener dbsl = new DatabaseSelectionListener(databaseChanger, sqlTypeCombo);
		dbsl.init();
		databaseChanger.addSelectionListener(dbsl);
	}

//	private void initPartListener() {
//		view.get
//		view.getSite().getPage().addPartListener(new IPartListener() {
//
//			@Override
//			public void partOpened(IWorkbenchPart part) {
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			public void partDeactivated(IWorkbenchPart part) {
//				if (part.equals(view)) {
//					save();
//				}
////				if (part instanceof DatabaseModelingView) {
////					save();
////				}
//			}
//
//			@Override
//			public void partClosed(IWorkbenchPart part) {
//				if (part.equals(view)) {
//					save();
//				}
//			}
//
//			@Override
//			public void partBroughtToTop(IWorkbenchPart part) {
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			public void partActivated(IWorkbenchPart part) {
//				// TODO Auto-generated method stub
//			}
//		});
//	}

	private void initSelectionChangeListener() {
		ISelectionListener listener = new ISelectionListener() {
			public void selectionChanged(IWorkbenchPart sourcepart, ISelection selection) {
				if (view == null) {
				}
				Property property = SelectionUtil.getProperty(selection);
				if (property != null) {
					updateSelection(property);
					updateDataInView(property);
				}

			}

			@Override
			public void selectionChanged(MPart part, Object selection) {
				// TODO check if ISelection
				ISelection iSelection = null;
				if (selection instanceof ISelection) {
					iSelection = (ISelection) selection;
				}
				if (view == null) {
				}
				Property property = SelectionUtil.getProperty(iSelection);
				if (property != null) {
					updateSelection(property);
					updateDataInView(property);
				}

			}
		};
		// view.setListener(listener);
		// view.getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(listener);
	}

	protected void updateDataInView(Property property) {
		SqlDataModel dataModel = DataTransformer.propertyToSqlDataModel(property);
		view.update(dataModel);
	}

	public void save() {
		if (currentPropertySelection == null) {
			return;
		}
		if (view.isEmpty()) {
			return;
		}

		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(currentPropertySelection);
		if (editingDomain == null) {
			return;
		}
		RecordingCommand recordingCommand = new RecordingCommand(editingDomain) {
			@Override
			protected void doExecute() {
				DataTransformer.applyModelOnProperty(view.getData(), currentPropertySelection);
			}
		};
		editingDomain.getCommandStack().execute(recordingCommand);
	}

	protected void updateSelection(Property property) {
		currentPropertySelection = property;
		// view.getLblNewLabel().setText(currentPropertySelection.getName());
	}

}
