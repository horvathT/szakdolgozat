package model.transfer.importer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Package;

import mode.transfer.util.CellUtil;

public class ObjectImporter {

	protected Package modelPackage;

	protected Workbook workbook;

	public ObjectImporter(Workbook workbook, Package modelPackage) {
		this.workbook = workbook;
		this.modelPackage = modelPackage;
	}

	protected EObject getByXmiId(Collection<? extends EObject> classifiers, String xmiId) {
		for (EObject classifier : classifiers) {
			String fragment = EcoreUtil.getURI(classifier).fragment();
			if (fragment.equals(xmiId)) {
				return classifier;
			}
		}
		return null;
	}

	protected List<String> collectEntityNamesFromSheet(Sheet sheet) {
		List<String> entityNames = new ArrayList<String>();
		int rowNum = sheet.getLastRowNum();
		for (int i = 1; i <= rowNum; i++) {
			String name = CellUtil.getStringCellValue(sheet.getRow(i).getCell(1));
			if (!name.isEmpty()) {
				entityNames.add(name);
			}
		}
		return entityNames;
	}

}
