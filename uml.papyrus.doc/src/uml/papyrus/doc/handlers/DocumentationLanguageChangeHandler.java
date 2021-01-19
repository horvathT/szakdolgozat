package uml.papyrus.doc.handlers;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.papyrus.infra.ui.util.EditorHelper;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import uml.papyrus.doc.DocumentationMultiLanguageView;
import uml.papyrus.doc.SelectionUtil;

public class DocumentationLanguageChangeHandler {

  @Inject
  private EPartService partService;
  

  @CanExecute
  public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
    if (SelectionUtil.getSelectionByType(selection) != null) {
      return true;
    }
    return false;
  }

  @Execute
  public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
    IWorkbenchPage activePage = EditorHelper.getActiveWindow().getActivePage();
    IWorkbenchPart activePart2 = activePage.getActivePart();
    
    if (activePart2 instanceof DocumentationMultiLanguageView) {
      MPart activePart = partService.getActivePart();
      String language = getSelectedLanguage(activePart.getToolbar());
      if(language != null) {
        DocumentationMultiLanguageView dmlv = (DocumentationMultiLanguageView) activePart2;
        dmlv.setLanguageFilter(language);
        dmlv.setSelectedElement(selection);
      }
    }
  }

  private String getSelectedLanguage(MToolBar toolbar) {
    List<MToolBarElement> children = toolbar.getChildren();
    for (MToolBarElement toolbarElement : children) {
      if (toolbarElement instanceof MHandledToolItem) {
        MHandledToolItem handledToolbarItem = (MHandledToolItem) toolbarElement;
        if ("papyrus.ui.handledtoolitem.language".equals(handledToolbarItem.getElementId())) {
          return getRadioButtons(handledToolbarItem);
        }
      }
    }
    return null;
  }

  private String getRadioButtons(MHandledToolItem handledToolbarItem) {
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

