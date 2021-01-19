
package uml.papyrus.scriptgen;

import java.util.Map;
import javax.inject.Named;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.uml2.uml.Package;
import org.smartbit4all.tooling.emf.EclipseSelectionUtil;

public class ScriptGenerationHandler {
  @Execute
  public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
    Package modelPackage = EclipseSelectionUtil.getPackage(EclipseSelectionUtil.getFirstSelected(selection));
    Map<String, String> configMap = readFromXml(selection);
    ExternalScriptGenerator externalScriptGen = new ExternalScriptGenerator();
    externalScriptGen.executeFromGeneratorMenu(modelPackage, configMap);

  }


  private Map<String, String> readFromXml(ISelection selection) {
    // TODO Auto-generated method stub
    return null;
  }


  @CanExecute
  public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
    return EclipseSelectionUtil.getPackage(EclipseSelectionUtil.getFirstSelected(selection)) != null;
  }

}
