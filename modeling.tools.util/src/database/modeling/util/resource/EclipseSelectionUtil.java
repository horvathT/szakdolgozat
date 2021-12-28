package database.modeling.util.resource;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.papyrus.emf.facet.custom.metamodel.v0_2_0.internal.treeproxy.EObjectTreeElement;
import org.eclipse.uml2.uml.Package;

/**
 * Képernyőről érkező kiválasztások szűrése eés kezelése.
 * 
 * @author Horváth Tibor
 *
 */
public class EclipseSelectionUtil {

	/**
	 * Ha a paraméterben kapott objektum első eleme {@link EObjectTreeElement}
	 * átkasztolja és visszatér vele egyébként null-t ad vissza.
	 * 
	 * @param selection
	 * @return
	 */
	public static EObjectTreeElement getFirstSelected(ISelection selection) {
		Object firstSelection = getFirstSelection(selection);

		if (firstSelection instanceof EObjectTreeElement) {
			return (EObjectTreeElement) firstSelection;
		}
		return null;
	}

	/**
	 * Ha a paraméterben kapott objektum {@link TreeSelection} átkasztolja, kiveszi
	 * az első elemét és visszatér vele egyébként null-t ad vissza.
	 * 
	 * @param selection
	 * @return
	 */
	public static Object getFirstSelection(ISelection selection) {
		if (selection instanceof TreeSelection) {
			TreeSelection treeSelection = (TreeSelection) selection;
			return treeSelection.getFirstElement();
		}
		return null;
	}

	/**
	 * Ha a paraméterben kapott elem {@link Package} átkasztolja és visszatér vele
	 * egyébként null-t ad vissza.
	 * 
	 * @param element
	 * @return
	 */
	public static Package getPackage(EObjectTreeElement element) {
		if (element != null && element.getEObject() instanceof Package) {
			return (Package) element.getEObject();
		}
		return null;
	}
}
