package database.modeling.view.util;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.papyrus.emf.facet.custom.metamodel.v0_2_0.internal.treeproxy.EObjectTreeElement;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.internal.impl.PropertyImpl;

public class SelectionUtil {

	private static EObjectTreeElement getFirstSelected(ISelection selection) {
		Object firstSelection = getFirstSelection(selection);

		if (firstSelection instanceof EObjectTreeElement) {
			return (EObjectTreeElement) firstSelection;
		}
		return null;
	}

	private static Object getFirstSelection(ISelection selection) {
		if (selection instanceof TreeSelection) {
			TreeSelection treeSelection = (TreeSelection) selection;
			return treeSelection.getFirstElement();
		}
		return null;
	}

// in case sql generation is started from model explorer menu
	private static Package getPackage(EObjectTreeElement element) {
		if (element != null && element.getEObject() instanceof Package) {
			return (Package) element.getEObject();
		}
		return null;
	}
	
	public static Package getPackage(ISelection selection) {
		return getPackage(getFirstSelected(selection));
	}

	private static Property getProperty(EObjectTreeElement element) {
		if (element != null && element.getEObject() instanceof Property) {
			return (Property) element.getEObject();
		}
		return null;
	}
	
	public static Property getProperty(ISelection selection) {
		return getProperty(getFirstSelected(selection));
	}
	
	private static boolean isProperty(EObjectTreeElement element) {
		if (element != null && element.getEObject() instanceof Property) {
			return true;
		}
		return false;
	}
	
	public static boolean isProperty(ISelection selection) {
		return isProperty(getFirstSelected(selection));
	}
	
	public static boolean isPropertyFromModelEditor(ISelection selection) {
		List<Object> objList = getSelection(selection);
		if(objList.isEmpty()) {
			return false;
		}
		
		Object object = objList.get(0);
		if(object instanceof IAdaptable) {
			NamedElement element = ((IAdaptable)object).getAdapter(NamedElement.class);
			if(element instanceof Property) {
				return true;
			}
		}
		return false;
	}
	
	public static Property getPropertyFromModelEditor(ISelection selection) {
		List<Object> objList = getSelection(selection);
		if(objList.isEmpty()) {
			return null;
		}
		
		Object object = objList.get(0);
		if(object instanceof IAdaptable) {
			NamedElement element = ((IAdaptable)object).getAdapter(NamedElement.class);
			if(element instanceof Property) {
				return (Property)element;
			}
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