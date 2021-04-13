package database.modeling.controller;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.uml2.uml.Property;

import database.modeling.model.SqlDataModel;
import database.modeling.view.DatabaseModelingView;
import database.modeling.view.util.SelectionUtil;

public class DatabaseModelingController {
	private Property currentSelection = null;
	
	private DatabaseModelingView view;
	private SqlDataModel model;
	private ISelectionListener listener;
	
	public DatabaseModelingController(DatabaseModelingView view, SqlDataModel model) {
		this.view = view;
		this.model = model;
		this.listener = view.getListener();
	}
	
	public void init() {
		listener = new ISelectionListener() {
			public void selectionChanged(IWorkbenchPart sourcepart, ISelection selection) {
				if(SelectionUtil.isPropertyFromModelEditor(selection)) {
					
					updateLatestValidSelectionFromDiagramEditor(selection);
					updateSelectionFromDiagramEditor(selection);
					
				}else if(SelectionUtil.isPropertyFromModelExplorer(selection)) {
					
					updateSelectionFromModelExplorer(selection);
					updateLatestValidSelectionFromModelExplorer(selection);
					
				}else {
					//setContentDescription("Nothing to show");
				}
			}
		};
	}

	
	
	protected void updateSelectionFromDiagramEditor(ISelection selection) {
		//setContentDescription(SelectionUtil.getPropertyFromModelEditor(selection).getName());
	}

	protected void updateLatestValidSelectionFromDiagramEditor(ISelection selection) {
		currentSelection = SelectionUtil.getPropertyFromModelEditor(selection);
	}

	protected void updateSelectionFromModelExplorer(ISelection selection) {
		//setContentDescription(SelectionUtil.getPropertyFromModelExplorer(selection).getName());
	}
	
	protected void updateLatestValidSelectionFromModelExplorer(ISelection selection) {
		currentSelection = SelectionUtil.getPropertyFromModelExplorer(selection);
	}

}
