package uml.papyrus.doc.handlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.papyrus.infra.ui.util.EditorHelper;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Stereotype;
import uml.papyrus.doc.DocumentationMultiLanguageView;
import uml.papyrus.doc.SelectionUtil;
import uml.papyrus.doc.util.EclipseResourceUtil;

public class OpenExternalDocumentationHandler {

  @Inject
  private Logger logger;

  private String languageFilter;

  @CanExecute
  public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
    if (SelectionUtil.getSelectionByType(selection) != null) {
      return true;
    }
    return false;
  }

  @Execute
  public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
    Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
    setLanguageFiltert(getLanguageFilter());
    EObject selectedElement = SelectionUtil.getSelectionByType(selection);
    String ownedComment = getOwnedComment(selectedElement);
    
    String docFileName = getDocFileName(selectedElement, languagePostfix(languageFilter));

    try {
      String docFolderOsLoc = EclipseResourceUtil.createFolder(
          getOsLocation(selectedElement),
          getMultiLanguageDocFolderLocation(languageFilter));
      
      File file = new File(docFolderOsLoc, docFileName);
      if (file.isFile()) {
        
        IFile elementDocumentation = EclipseResourceUtil.getFileResource(docFolderOsLoc + docFileName);
        
        if(!file.exists()) {
          writeDocx(file, ownedComment);
        }
        
        EclipseResourceUtil.openWithExternalEditor(elementDocumentation);
        
      } else {
        
        boolean result = MessageDialog.openConfirm(shell, "Generate Documentation File",
            "This model part has no documentation generated. Would you like to create it now?");
        
        if (result) {
          createDocumenationFileForElement(selectedElement, docFolderOsLoc);
          EclipseResourceUtil.refreshWorkspaceRoot();
          writeDocx(file, ownedComment);
          IFile elementDocumentation = EclipseResourceUtil.getFileResource(docFolderOsLoc + docFileName);
          EclipseResourceUtil.openWithExternalEditor(elementDocumentation);
        }
        
      }
      
    } catch (CoreException e) {
      logger.error(e, "Reading from config file error");
    } catch (IOException e) {
      logger.error(e, "Failed to create file");
    }

  }

  public static Path getDocFile(EObject element, String language) {
    String resourceFileLocation = getOsLocation(element);
    String folderLocation = getMultiLanguageDocFolderLocation(language);
    String docFileName = getDocFileName(element, languagePostfix(language));
    
    return Paths.get(resourceFileLocation, folderLocation, docFileName);
  }
  
  public static String getMultiLanguageDocFolderLocation(String language) {
    return "documentation" + "/" + language;
  }
  
  private void writeDocx(File file, String content) throws IOException {
    XWPFDocument document = new XWPFDocument(); 
    
    FileOutputStream out = new FileOutputStream(file);
      
    XWPFParagraph paragraph = document.createParagraph();
    XWPFRun run = paragraph.createRun();
    run.setText(content);
          
    document.write(out);
    out.close();
    document.close();
    
  }


  private String getOwnedComment(EObject selectedElement) throws NullPointerException {
    if(selectedElement instanceof Element) {
      Element element = (Element)selectedElement;
      EList<Comment> ownedComments = element.getOwnedComments();
      for(Comment comment : ownedComments) {
        Stereotype appliedStereotype =
            comment.getAppliedStereotype("ActionLanguage::TextualRepresentation");
        if(appliedStereotype != null) {
          Object value = comment.getValue(appliedStereotype, "language");
          String language = (String) value;
          if (languageFilter.equals(language)) {
            String body = comment.getBody();
            return comment.getBody();
          }
        }
      }
    }
    return null;
  }

  public static String getOsLocation(EObject selectedElement) {
    IPath path = EclipseResourceUtil.getResourceFileLocationPath(selectedElement.eResource());
    return path.removeLastSegments(1).toOSString();
  }

  public static String getDocFileName(EObject selectedElement, String languagePostfix) {
    String xmiId = EcoreUtil.getURI(selectedElement).fragment();
    return "/" + xmiId + languagePostfix +".docx";
  }

  private void createDocumenationFileForElement(EObject selectedElement, String docFolderOsLoc)
      throws IOException {
    
    File documentationFile = getDocumentationFile(selectedElement, docFolderOsLoc, languageFilter);
    documentationFile.createNewFile();
  }

  public static File getDocumentationFile(EObject selectedElement, String docFolderOsLoc, String language) {
    String docfileName = getDocFileName(selectedElement, languagePostfix(language));
    return new File(docFolderOsLoc, docfileName);
  }

  private String getLanguageFilter() {
    IWorkbenchPage activePage = EditorHelper.getActiveWindow().getActivePage();
    IWorkbenchPart activePart = activePage.getActivePart();
    if (activePart instanceof DocumentationMultiLanguageView) {
      DocumentationMultiLanguageView dmlv = (DocumentationMultiLanguageView) activePart;
      return dmlv.getLanguageFilter();
    }
    return null;
  }

  private void setLanguageFiltert(String language) {
    languageFilter = language;
  }
  
  public static String languagePostfix(String language) {
    return "_" + language;
  }

}
