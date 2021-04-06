 
package database.modeling.view.handler;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.papyrus.infra.ui.util.EditorHelper;
import org.eclipse.ui.IWorkbenchPart;

import database.modeling.view.ModelingViewPart;
import database.modeling.view.util.DatabaseTypesUtil;

public class ChangeDatabaseHandler {
	
	@Inject
	private EPartService partService;
	
	@Execute
	public void execute() {
		
		IWorkbenchPart activePart = EditorHelper.getActiveWindow().getActivePage().getActivePart();
		if(activePart instanceof ModelingViewPart) {
			MToolBar toolbar = partService.getActivePart().getToolbar();
			String selectedDatabase = getSelectedDatabase(toolbar);
			//TODO mitol fut 2x? 1.null 2. jo ertek
			if(selectedDatabase != null) {
				ModelingViewPart mvp = (ModelingViewPart) activePart;
				DatabaseTypesUtil dtu = new DatabaseTypesUtil();
				Map<String, String[]> databaseMap = dtu.getDatabaseMap();
				
				mvp.getSqlTypeCombo().setItems(databaseMap.get(selectedDatabase));
			}
			
		}
		
		//Database changed save current data then refresh
	}

	private String getSelectedDatabase(MToolBar toolbar) {
		List<MToolBarElement> children = toolbar.getChildren();
		for (MToolBarElement mToolBarElement : children) {
			if(mToolBarElement instanceof MHandledToolItem) {
				MHandledToolItem handledToolbarItem = (MHandledToolItem) mToolBarElement;
				if("database.modeling.view.handledtoolitem.database".contentEquals(handledToolbarItem.getElementId())) {
					return getSelectedButtonLabel(handledToolbarItem);
				}
			}
		}
		return null;
	}
	
	private String getSelectedButtonLabel(MHandledToolItem handledToolbarItem) {
	    List<MMenuElement> radioButtons = handledToolbarItem.getMenu().getChildren();
	    for (MMenuElement button : radioButtons) {
	      if (button instanceof MHandledMenuItem) {
	        MHandledMenuItem buttonItem = (MHandledMenuItem) button;
	        if (buttonItem.isSelected()) {
	          return buttonItem.getLabel();
	        }
	      }
	    }
	    return null;
	  }

}





