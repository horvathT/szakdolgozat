
package database.modeling.view.handler;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class DatabaseModelingViewHandler {

	private static final String DATABASE_MODELING_VIEW_ID = "database.modeling.view.databasemodelingview";

	@Inject
	EPartService partService;

	@Execute
	public void execute() {
		// E4 módszer
		partService.showPart(DATABASE_MODELING_VIEW_ID, PartState.ACTIVATE);
//		try {
//
//			 E3 compatibility
//			 IWorkbenchPage page =
//			 PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//			 IViewPart showView = page.showView(DATABASE_MODELING_VIEW_ID, null,
//			 IWorkbenchPage.VIEW_VISIBLE);
//
//			DatabaseModelingView dbmv = (DatabaseModelingView) showView;
//			DatabaseModelingController dbmc = new DatabaseModelingController(dbmv);
//			dbmc.init();
//
//		} catch (PartInitException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	@CanExecute
	public boolean canExecute() {
		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IViewPart view = activePage.findView(DATABASE_MODELING_VIEW_ID);
		if (view != null) {
			return false;
		}
		return true;
	}

}