package uml.papyrus.doc;

import javax.inject.Named;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuItem;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.papyrus.infra.ui.util.EditorHelper;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

public class LanguageSelectionDropdownDynamicMenuItem {

  @Execute
  public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection,
      @Optional MMenuItem menuItem) {

    if (menuItem != null) {
      IWorkbenchPage activePage = EditorHelper.getActiveWindow().getActivePage();
      IWorkbenchPart activePart2 = activePage.getActivePart();

      if (activePart2 instanceof DocumentationMultiLanguageView) {
        String language = menuItem.getElementId();
        if (language != null) {
          DocumentationMultiLanguageView dmlv = (DocumentationMultiLanguageView) activePart2;
          dmlv.setLanguageFilter(language);
          dmlv.setSelectedElement(selection);
        }
      }
    }
  }
  
}
