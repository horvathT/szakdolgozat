package uml.papyrus.doc;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.papyrus.views.documentation.views.DocumentationView;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;

import uml.papyrus.doc.util.EclipseResourceUtil;

public class DocumentationMultiLanguageView extends DocumentationView {

  public static final String ACTION_LANGUAGE_LANGUAGE_PROPERTY = "language";

  public static final String ACTION_LANGUAGE_TEXTUAL_REPRESENTATION = "ActionLanguage::TextualRepresentation";
  
  private String languageFilter = "hu";

  private List<String> languages = new ArrayList<String>(); 

  private Text text = null;

  @Override
  protected void createMainComposite(Composite parent) {
    super.createMainComposite(parent);

    List<Control> controls = new ArrayList<>();
    getControls(parent, controls);

    for (Control control : controls) {
      if (control instanceof Text) {
        text = (Text) control;
      }
    }
  }

  public void getControls(Composite rootComposite, List<Control> results) {
    for (Control control : rootComposite.getChildren()) {
      if (control instanceof Composite) {
        Composite childComposite = (Composite) control;
        getControls(childComposite, results);
      } else {
        results.add(control);
      }
    }
  }

  @Override
  protected Comment getDocumentationComment(EList<Comment> ownedComments) {
    Comment docComment = null;

    for (Iterator<Comment> iterator = ownedComments.iterator(); iterator.hasNext()
        && null == docComment;) {
      Comment comment = iterator.next();
      Stereotype appliedStereotype =
          comment.getAppliedStereotype(ACTION_LANGUAGE_TEXTUAL_REPRESENTATION);
      if (null != appliedStereotype) {
        Object value = comment.getValue(appliedStereotype, ACTION_LANGUAGE_LANGUAGE_PROPERTY);
        String language = (String) value;
        if (languageFilter.equals(language)) {
          docComment = comment;
          refreshEditors();
        }
      }
    }
    return docComment;
  }

  @Override
  protected CompoundCommand getCreateNewCommentCommand() {
    CompoundCommand createNewCommentCommand = super.getCreateNewCommentCommand();

    RecordingCommand stereoTypeCommand = null;

    for (Command command : createNewCommentCommand.getCommandList()) {
      if (command instanceof AddCommand) {
        AddCommand addCommand = (AddCommand) command;
        Collection<?> collection = addCommand.getCollection();

        if (!collection.isEmpty()) {
          Object object = collection.iterator().next();

          if (object instanceof Comment) {
            Comment comment = (Comment) object;

            Element owner = (Element) addCommand.getOwner();

            Stereotype textStereotype = null;
            for (Profile profile : owner.getModel().getAllAppliedProfiles()) {
              if ("ActionLanguage".equals(profile.getQualifiedName())) {
                for (Stereotype stereotype : profile.getOwnedStereotypes()) {
                  if (ACTION_LANGUAGE_TEXTUAL_REPRESENTATION
                      .equals(stereotype.getQualifiedName())) {
                    textStereotype = stereotype;
                  }
                }
              }
            }

            if (textStereotype != null) {
              final Stereotype stereoTypeFinal = textStereotype;

              TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(owner);

              stereoTypeCommand = new RecordingCommand(editingDomain) {

                @Override
                protected void doExecute() {
                  comment.applyStereotype(stereoTypeFinal);
                  comment.setValue(stereoTypeFinal, "language", languageFilter);
                }

              };
            }
          }
        }
      }
    }

    if (stereoTypeCommand != null) {
      createNewCommentCommand.append(stereoTypeCommand);
    }

    return createNewCommentCommand;
  }

  @Override
  protected void setSelectedElement(Element element) {
    super.setSelectedElement(element);

    if (element != null) {
      IPath resourceFileLocation =
          EclipseResourceUtil.getResourceFileLocationPath(element.eResource());
      String umlFolder = resourceFileLocation.removeLastSegments(1).toOSString();
      String modelName = getModelName(resourceFileLocation);
      languages.clear();
      List<String> listOfLanguages = getLanguages(umlFolder, modelName);
      languages.addAll(listOfLanguages);
    }
  }

  private String getModelName(IPath resourceFileLocation) {
    String[] split = resourceFileLocation.lastSegment().split("\\.");
    return split[0];
  }

  private List<String> getLanguages(String umlFolderPath, String modelName) {
    List<String> languages = new ArrayList<String>();
    File umlFolder = new File(umlFolderPath);
    if (umlFolder.isDirectory()) {
      for (File file : umlFolder.listFiles()) {
        String name = file.getName();
        if (name.endsWith(".properties") &&  name.startsWith(modelName)) {
          if (trimFileName(file.getName()) != null) {
            languages.add(trimFileName(file.getName()));
          }
        }
      }
    }
    return languages;
  }

  private String trimFileName(String name) {
    name = name.replaceAll(".properties", "");
    String[] nameSplit = name.split("_");
    if(nameSplit.length >= 2) {
      return nameSplit[1];
    }
    return null;
  }

  public List<String> getLanguages() {
    return new ArrayList<>(languages);
  }

  public void setLanguageFilter(String language) {
    languageFilter = language;
  }

  public String getLanguageFilter() {
    return languageFilter;
  }

}
