
package database.modeling.view.handler;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.papyrus.infra.ui.util.EditorUtils;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import database.modeling.controller.DatabaseModelingController;
import database.modeling.model.SqlDataModel;
import database.modeling.view.DatabaseModelingView;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;

public class DatabaseModelingViewHandler {
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
		try {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IViewPart showView = page.showView("database.modeling.view.databasemodelingview", null, IWorkbenchPage.VIEW_VISIBLE);
			DatabaseModelingView dbmv = (DatabaseModelingView) showView;
			DatabaseModelingController dbmc = new DatabaseModelingController(dbmv);
			dbmc.init();
			
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

	}

	@CanExecute
	public boolean canExecute() {
		// TODO work only on uml elements WORK ONLY ON PROPERTIES
		return true;
	}

}