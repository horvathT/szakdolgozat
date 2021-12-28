package model.transfer.importer;

import java.io.File;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.uml2.uml.Package;

/**
 * Excel fájl importálást végzi.
 * 
 * @author Horváth Tibor
 *
 */
public class ModelImporter {

	private Package modelPackage;

	private String filePath;

	private TransactionalEditingDomain editingDomain;

	public ModelImporter(Package modelPackage, String filePath) {
		this.modelPackage = modelPackage;
		this.filePath = filePath;
	}

	/**
	 * Típus szerint szétválogatva {@link RecordingCommand}-okba csoportosítva
	 * végrehajtja az importálást. Az elemek létrehozását kell előre venni mert
	 * ameddig nincsenek rögzítve a modellben egy {@link RecordingCommand}
	 * segítségével addig nem lehet asszociációkat létrhozni közöttük.
	 * 
	 * @throws EncryptedDocumentException
	 * @throws IOException
	 */
	public void executeImport() throws EncryptedDocumentException, IOException {
		Workbook workbook = WorkbookFactory.create(new File(filePath));
		editingDomain = TransactionUtil.getEditingDomain(modelPackage);

		RecordingCommand enumRecordingCommand = new RecordingCommand(editingDomain) {
			@Override
			protected void doExecute() {
				createDataTypes(workbook);
			}

			private void createDataTypes(Workbook workbook) {
				EnumCreator enumCreator = new EnumCreator(workbook, modelPackage);
				enumCreator.createEnums();
				enumCreator.removeDeletedEnumerations();
			}
		};
		editingDomain.getCommandStack().execute(enumRecordingCommand);

		RecordingCommand dataTypeRecordingCommand = new RecordingCommand(editingDomain) {
			@Override
			protected void doExecute() {
				createDataTypes(workbook);
			}

			private void createDataTypes(Workbook workbook) {
				DataTypeCreator dtc = new DataTypeCreator(workbook, modelPackage, editingDomain);
				dtc.createTypes();

			}
		};
		editingDomain.getCommandStack().execute(dataTypeRecordingCommand);

		RecordingCommand entityRecordingCommand = new RecordingCommand(editingDomain) {
			@Override
			protected void doExecute() {
				createEntities(workbook);
				createMethodsAndProperties(workbook);
			}

			private void createMethodsAndProperties(Workbook workbook) {
				PropertyAndMethodCreator propertyCreator = new PropertyAndMethodCreator(workbook, modelPackage);
				propertyCreator.removeDeletedMethodsAndProperties();
				propertyCreator.createMethodsAndProperties();
			}

			private void createEntities(Workbook workbook) {
				EntityCreator entitycreator = new EntityCreator(workbook, modelPackage);
				entitycreator.removeDeletedEntities();
				entitycreator.createEntities();
			}
		};
		editingDomain.getCommandStack().execute(entityRecordingCommand);

		RecordingCommand associationRecordingCommand = new RecordingCommand(editingDomain) {
			@Override
			protected void doExecute() {
				createAssociations(workbook);
			}

			private void createAssociations(Workbook workbook) {
				AssociationCreator associationCreator = new AssociationCreator(workbook, modelPackage);
				associationCreator.removeDeletedAssociations();
				associationCreator.createAssociations();

				associationCreator.createEntityHierarchy();
			}

		};
		editingDomain.getCommandStack().execute(associationRecordingCommand);
	}

}
