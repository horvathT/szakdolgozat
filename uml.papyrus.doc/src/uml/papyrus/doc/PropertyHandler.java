package uml.papyrus.doc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Named;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.uml2.uml.Property;



public class PropertyHandler {

  @CanExecute
  public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
    return true;
  }

  @Execute
  public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
    List<Property> selected = getSelectionByType(selection);
    System.out.println(selection.toString());
    
    PropertyDialog newDialog = new PropertyDialog(Display.getCurrent().getActiveShell(), "CUSTOM WINDOW", selected);
    newDialog.open();
  }

  private List<Property> getSelectionByType(ISelection selection) {
    List<Property> results = new ArrayList<Property>();
    List<Object> objList = getSelection(selection);
    for(Object obj : objList) {
      Property ele = null;
      if (obj instanceof IAdaptable) {
        ele = ((IAdaptable) obj).getAdapter(Property.class);
      }
      if (ele == null) {
        ele = Platform.getAdapterManager().getAdapter(obj, Property.class);
      }

      if (ele != null) {
        results.add(ele);
      }
    }
    return results;
  }
  
  private List<Object> getSelection(ISelection selection) {
    if(selection instanceof IStructuredSelection) {
      IStructuredSelection structuredSelection = (IStructuredSelection) selection;
      return structuredSelection.toList();
    }
    return Collections.emptyList();
  }
  
}
