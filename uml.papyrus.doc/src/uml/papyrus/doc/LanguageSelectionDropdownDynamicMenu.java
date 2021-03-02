package uml.papyrus.doc;

import java.util.List;

import org.eclipse.e4.ui.di.AboutToHide;
import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.ui.model.application.ui.menu.ItemType;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.papyrus.infra.ui.util.EditorHelper;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

public class LanguageSelectionDropdownDynamicMenu {

  @AboutToShow
  public void aboutToShow(EPartService partService, List<MMenuElement> items) {
    IWorkbenchPage activePage = EditorHelper.getActiveWindow().getActivePage();
    IWorkbenchPart activePart = activePage.getActivePart();

    if (activePart instanceof DocumentationMultiLanguageView) {
      DocumentationMultiLanguageView dmlv = (DocumentationMultiLanguageView) activePart;
      String actualLanguage = dmlv.getLanguageFilter();

      for (String language : dmlv.getLanguages()) {
        MDirectMenuItem newLangMenuItem = MMenuFactory.INSTANCE
            .createDirectMenuItem();

        newLangMenuItem.setElementId(language);
        newLangMenuItem.setLabel(language);
        newLangMenuItem.setContributorURI("platform:/plugin/uml.papyrus.doc");
        newLangMenuItem
            .setContributionURI(
                "bundleclass://uml.papyrus.doc/uml.papyrus.doc.LanguageSelectionDropdownDynamicMenuItem");
        newLangMenuItem.setType(ItemType.RADIO);
        newLangMenuItem.setEnabled(true);

        newLangMenuItem.setSelected(actualLanguage.equals(language));

        items.add(newLangMenuItem);
      }
    }
  }

  @AboutToHide
  public void aboutToHide(List<MMenuElement> items) {

  }

}
