package uml.papyrus.doc;

import java.util.Collections;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.papyrus.emf.facet.custom.metamodel.v0_2_0.internal.treeproxy.EObjectTreeElement;

public class SelectionUtil {
  public static EObject getSelectionByType(ISelection selection) {

    List<Object> objList = getSelection(selection);

    Object object = objList.get(0);
    if (object instanceof EditPart) {
      EditPart obj = (EditPart) object;
      Object model = obj.getModel();
      if (model instanceof View) {
        View shape = (View) model;
        EObject element = shape.getElement();
        return element;
      }
    }
    if(object instanceof EObjectTreeElement) {
      EObjectTreeElement treeElement = (EObjectTreeElement) object;
      EObject eObject = treeElement.getEObject();
      return eObject;
    }
    
    return null;
  }

  public static List<Object> getSelection(ISelection selection) {
    if (selection instanceof IStructuredSelection) {
      IStructuredSelection structuredSelection = (IStructuredSelection) selection;
      return structuredSelection.toList();
    }
    return Collections.emptyList();
  }
}
