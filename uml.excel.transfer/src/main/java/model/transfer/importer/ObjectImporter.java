package model.transfer.importer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Package;

import model.transfer.util.CellUtil;

/**
 * A legtöbb Objektum importáláshoz szükséges függvényeket, attrbútumokat
 * tartalmazza. Az importálást végző osztályoknak célszerű leszármaznia.
 * 
 * @author Horváth Tibor
 *
 */
public class ObjectImporter {

	protected Package modelPackage;

	protected Workbook workbook;

	public ObjectImporter(Workbook workbook, Package modelPackage) {
		this.workbook = workbook;
		this.modelPackage = modelPackage;
	}

	/**
	 * Egyedi azonosító alapján visszadja a {@link Classifieri}-t vagy null-t ha nem
	 * találja.
	 * 
	 * @param classifiers
	 * @param xmiId
	 * @return
	 */
	protected EObject getByXmiId(Collection<? extends EObject> classifiers, String xmiId) {
		for (EObject classifier : classifiers) {
			String fragment = EcoreUtil.getURI(classifier).fragment();
			if (fragment.equals(xmiId)) {
				return classifier;
			}
		}
		return null;
	}

	/**
	 * Entitások neveit gyűjti össze a paraméterben kapott munkalapról.
	 * 
	 * @param sheet
	 * @return
	 */
	protected List<String> collectEntityNamesFromSheet(Sheet sheet) {
		List<String> entityNames = new ArrayList<String>();
		int lastRowNum = sheet.getLastRowNum();
		for (int i = 1; i <= lastRowNum; i++) {
			Row row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			String name = CellUtil.getStringCellValue(row.getCell(1));
			if (!name.isEmpty()) {
				entityNames.add(name);
			}
		}
		return entityNames;
	}

}
