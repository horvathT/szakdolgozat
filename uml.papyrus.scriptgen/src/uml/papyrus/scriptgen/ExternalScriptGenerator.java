package uml.papyrus.scriptgen;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLPackage;


public class ExternalScriptGenerator {
  
  @Inject
  Logger logger;

  public void executeFromGeneratorMenu(Package modelPackage, Map<String, String> configMap) {
    String projectName = configMap.get(ScriptConfigurationConstants.PROJECT_NAME);
    IFolder folder =  addFolder(projectName, configMap.get(ScriptConfigurationConstants.OUTPUT_FOLDER));
    Collection<Interface> interfaces = EcoreUtil.getObjectsByType(modelPackage.allOwnedElements() , UMLPackage.Literals.INTERFACE);
    writeSqlScriptsToFile(interfaces, configMap, folder.getLocation().toOSString());
  }

  private void writeSqlScriptsToFile(Collection<Interface> interfaces,
      Map<String, String> configMap, 
      String scriptFilesLocation) {
    
    String projectName = configMap.get(ScriptConfigurationConstants.PROJECT_NAME);
    String sqlLengthLimit = configMap.get(ScriptConfigurationConstants.SQL_LENGTH_LIMIT);
    String license = configMap.get(ScriptConfigurationConstants.LICENSE_GENERATION);
    
    
    SqlScriptFactory sqlFactory = new SqlScriptFactory(sqlLengthLimit);
    sqlFactory.generateScripts(interfaces, scriptFilesLocation, projectName, license);
    
    if (!sqlFactory.getLongKeywords().isEmpty()) {
      String warningMessage = "Keywords longer than " + sqlLengthLimit + " character: "
          + System.lineSeparator() + formatList(sqlFactory.getLongKeywords());
      warningMessageDialog(warningMessage);
    }
  }

  public IFolder addFolder(String projectName, String name) {
    IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
    IFolder folder = project.getFolder(name);
    if (!folder.exists()) {
      try {
        folder.create(false, true, null);
      } catch (CoreException e) {
        logger.error(e, "Folder creation error");
      }
    }
    return folder;
  }
  
  private String formatList(List<String> list) {
    StringBuilder sb = new StringBuilder();
    list.stream().forEach(s -> sb.append("    " + s + System.lineSeparator()));
    return sb.toString();
  }
  
  public void warningMessageDialog(String message) {
    Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
    MessageDialog.openWarning(shell, "Warning", message);
  }
}
