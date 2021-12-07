package model.transfer.importer;

import java.io.File;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.Package;
import org.slf4j.Logger;

public class ModelImporter {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(ModelImporter.class);

	private Package modelPackage;

	private String filePath;

	private TransactionalEditingDomain editingDomain;

	private Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();

	public ModelImporter(Package modelPackage, String filePath) {
		this.modelPackage = modelPackage;
		this.filePath = filePath;
	}

	public void validateInput() {
		// TODO Auto-generated method stub
//		Sheet interfaceSheet = workbook.getSheet(InterfaceSummarySheetCreator.SHEET_NAME);
//		if (interfaceSheet == null) {
//			throw new MissingSheetException(InterfaceSummarySheetCreator.SHEET_NAME);
//		}
//
//		Sheet classSheet = workbook.getSheet(ClassSummarySheetCreator.SHEET_NAME);
//		if (classSheet == null) {
//			throw new MissingSheetException(ClassSummarySheetCreator.SHEET_NAME);
//		}
//
//		Sheet enumSheet = workbook.getSheet(EnumSheetCreator.SHEET_NAME);
//		if (enumSheet == null) {
//			throw new MissingSheetException(EnumSheetCreator.SHEET_NAME);
//		}
//		
//		Sheet dataTypeSheet = workbook.getSheet(DataTypeSheetCreator.DATA_TYPE_SHEET_NAME);
//		if (dataTypeSheet == null) {
//			throw new MissingSheetException(DataTypeSheetCreator.DATA_TYPE_SHEET_NAME);
//		}
//		try {
//		} catch (MissingSheetException e) {
//			MessageDialog.openError(shell, "Hiányzó munkalap", e.getMessage());
//			log.error(e.getMessage(), e);
//			System.exit(1);
//		}

	}

	public void executeImport() throws EncryptedDocumentException, IOException {
		Workbook workbook = WorkbookFactory.create(new File(filePath));
		editingDomain = TransactionUtil.getEditingDomain(modelPackage);
		RecordingCommand dataTypeRecordingCommand = new RecordingCommand(editingDomain) {
			@Override
			protected void doExecute() {
				createDataTypes(workbook);
			}
		};
		editingDomain.getCommandStack().execute(dataTypeRecordingCommand);

		RecordingCommand entityRecordingCommand = new RecordingCommand(editingDomain) {
			@Override
			protected void doExecute() {
				createEntities(workbook);
			}
		};
		editingDomain.getCommandStack().execute(entityRecordingCommand);
	}

	private void createEntities(Workbook workbook) {
		EntityCreator entitycreator = new EntityCreator(workbook, modelPackage);
		entitycreator.createEntities();

	}

	private void createDataTypes(Workbook workbook) {
		DataTypeCreator dtc = new DataTypeCreator(workbook, modelPackage, editingDomain);
		dtc.createTypes();

	}

}
