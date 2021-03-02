
package uml.papyrus.doc.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.papyrus.infra.ui.util.EditorHelper;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import uml.papyrus.doc.DocumentationMultiLanguageView;

public class OpenMultiLanguageDocumentationViewHandler {

  @Inject
  private Logger logger;
  
  @Execute
  public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection,
      IEclipseContext context, EPartService partService) {
    
    try {
      IWorkbenchPage activePage = EditorHelper.getActiveWindow().getActivePage();
      IViewPart showView = activePage.showView("uml.papyrus.doc.view.multilanguageview", null, IWorkbenchPage.VIEW_VISIBLE);
 
      DocumentationMultiLanguageView dmlv = (DocumentationMultiLanguageView) showView;
      dmlv.setSelectedElement(selection);
    } catch (PartInitException e) {
      logger.error("Cannot open Multi Language Documentation view", e);
    }
  }

}
