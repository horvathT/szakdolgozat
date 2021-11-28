package database.modeling.util.resource;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.papyrus.emf.facet.custom.metamodel.v0_2_0.internal.treeproxy.EObjectTreeElement;
import org.eclipse.uml2.uml.Package;

public class EclipseSelectionUtil {

	public static EObjectTreeElement getFirstSelected(ISelection selection) {
		Object firstSelection = getFirstSelection(selection);

		if (firstSelection instanceof EObjectTreeElement) {
			return (EObjectTreeElement) firstSelection;
		}
		return null;
	}

	public static Object getFirstSelection(ISelection selection) {
		if (selection instanceof TreeSelection) {
			TreeSelection treeSelection = (TreeSelection) selection;
			return treeSelection.getFirstElement();
		}
		return null;
	}

	public static Package getPackage(EObjectTreeElement element) {
		if (element != null && element.getEObject() instanceof Package) {
			return (Package) element.getEObject();
		}
		return null;
	}
}
