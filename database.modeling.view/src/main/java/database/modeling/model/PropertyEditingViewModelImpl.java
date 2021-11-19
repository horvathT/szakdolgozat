package database.modeling.model;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;

import database.modeling.util.resource.EclipseModelUtil;
import database.modeling.util.stereotype.DatabaseModelUtil;
import database.modeling.view.DatabaseModelingView;

public class PropertyEditingViewModelImpl implements PropertyEditingViewModel {

	DatabaseModelingView view;

	public PropertyEditingViewModelImpl(DatabaseModelingView view) {
		this.view = view;
	}

	@Override
	public void selectionChanged(Property porperty) {
		save();
		view.setDataModel(new PropertyDataModel());
		view.setCurrentPropertySelection(porperty);
		setCurrentSelectionLabel();

		// Meg kell nézni hogy a váltás után is ugyan abban a DB típusban vagyunk-e
		Model model = porperty.getModel();
		String modelDBType = DatabaseModelUtil.getDatabaseType(model);
		ToolItem databaseChanger = view.getDatabaseChanger();

		if (modelDBType != null) {
			String selectedDBType = databaseChanger.getText();
			if (modelDBType.equals(selectedDBType)) {
				updateDataInView(view.getCurrentPropertySelection());
			} else {
				databaseChanged(selectedDBType, selectedDBType);
				updateDataInView(view.getCurrentPropertySelection());
			}
		} else {
			updateDataInView(view.getCurrentPropertySelection());
		}
	}

	@Override
	public void databaseChanged(String curentlySelectedDB, String newlySelectedDbName) {
		// adott selection mentése
		save();
		// selection xmiId-t elrakjuk későbbre
		Property currentPropertySelection = view.getCurrentPropertySelection();
		String fragment = EcoreUtil.getURI(currentPropertySelection).fragment();

		// TODO(modellen lévő adatok validálása)

		// modellen lévő összes adat begyűjtése
		// adatok kiírása xml-be vagy gson/json serializer
		Model model = currentPropertySelection.getModel();
		ModelConverter converter = new ModelConverter(model, view);
		converter.writeModelToFile(curentlySelectedDB);

		// modell letakarítása
		converter.clearModel();

		// TODO összevetés a modellel, esetleges változások/ összeférhetetlenség
		// keresése (ha
		// TODO van hiba akkor error és hiba üzenet)

		// újonnan kiválaszttot DB-hez xml fájl keresése (ha nincs akkor nem csinálunk
		// semmit)
		// fájl felolvsása
		// apply on model
		converter.applyFileOnModel(newlySelectedDbName);

		// view frissítése az új db adatokkal
		Property propertySelectionAfterDBChange = EclipseModelUtil.getPropertyByXmiId(fragment, model);
		if (propertySelectionAfterDBChange != null) {
			updateDataInView(propertySelectionAfterDBChange);
		} else {
			resetView();
		}
	}

	@Override
	public void save() {
		if (view.getCurrentPropertySelection() == null) {
			return;
		}
		if (isEmpty()) {
			return;
		}

		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(view.getCurrentPropertySelection());
		if (editingDomain == null) {
			return;
		}
		RecordingCommand recordingCommand = new RecordingCommand(editingDomain) {
			@Override
			protected void doExecute() {
				DataTransformer.applyModelOnProperty(updateModelFromView(), view.getCurrentPropertySelection());
			}
		};
		editingDomain.getCommandStack().execute(recordingCommand);
	}

	@Override
	public void updateDataInView(Property property) {
		resetView();
		PropertyDataModel dataModel = DataTransformer.propertyToSqlDataModel(property);
		view.setDataModel(dataModel);
		updateViewFromModel(dataModel);
	}

	private void resetView() {
		view.getLength().setText("");
		view.getPrecision().setText("");
		view.getScale().setText("");
		view.getDefaultValue().setText("");
		view.getPrimaryKeyConstraintName().setText("");
		view.getPrimaryKeyConstraintName().setEnabled(false);
		view.getForeignKeyConstraintName().setText("");
		view.getForeignKeyConstraintName().setEnabled(false);
		view.getSqlTypeCombo().clearSelection();
		view.getReferencedEntity().clearSelection();
		view.getReferencedEntity().setEnabled(false);
		view.getReferencedProperty().clearSelection();
		view.getReferencedProperty().setEnabled(false);
		view.getNullableCheck().setEnabled(false);
		view.getUniqueCheck().setSelection(false);
		view.getAutoIncrementCheck().setSelection(false);
		view.getPrimaryKeyCheck().setSelection(false);
		view.getForeignKeyCheck().setSelection(false);
	}

	private void updateViewFromModel(PropertyDataModel model) {
		view.getNullableCheck().setSelection(model.isNullable());
		view.getUniqueCheck().setSelection(model.isUnique());
		view.getAutoIncrementCheck().setSelection(model.isAutoIncrement());

		view.getLength().setText(model.getLength());
		view.getPrecision().setText(model.getPrecision());
		view.getScale().setText(model.getScale());
		view.getDefaultValue().setText(model.getDefaultValue());

		view.getPrimaryKeyCheck().setSelection(model.isPrimaryKey());
		view.getForeignKeyCheck().setSelection(model.isForeignKey());
		if (model.isPrimaryKey()) {
			view.getPrimaryKeyConstraintName().setEnabled(true);
		}
		view.getPrimaryKeyConstraintName().setText(model.getPrimaryKeyConstraintName());
		if (model.isForeignKey()) {
			view.getForeignKeyConstraintName().setEnabled(true);
			view.getReferencedEntity().setEnabled(true);
			view.getReferencedProperty().setEnabled(true);
		}
		view.getForeignKeyConstraintName().setText(model.getForeignKeyConstraintName());
		view.getReferencedEntity().setText(model.getReferencedEntity());
		view.getReferencedProperty().setText(model.getReferencedProperty());

		view.getSqlTypeCombo().setText(model.getSqlType());
	}

	private void setCurrentSelectionLabel() {
		Property property = view.getCurrentPropertySelection();

		if (property != null) {
			view.getCurrentSelectionLabel().setText(property.getName());
		} else {
			view.getCurrentSelectionLabel().setText("Not a valid selection");
		}
	}

	private boolean isEmpty() {
		if (view.getNullableCheck().getSelection() || view.getUniqueCheck().getSelection()
				|| view.getAutoIncrementCheck().getSelection() || view.getPrimaryKeyCheck().getSelection()
				|| view.getForeignKeyCheck().getSelection()) {
			return false;
		}
		if (!isEmpty(view.getLength()) || !isEmpty(view.getPrecision()) || !isEmpty(view.getScale())
				|| !isEmpty(view.getDefaultValue()) || !isEmpty(view.getPrimaryKeyConstraintName())
				|| !isEmpty(view.getForeignKeyConstraintName())) {
			return false;
		}
		if (!isEmpty(view.getSqlTypeCombo()) || !isEmpty(view.getReferencedEntity())
				|| !isEmpty(view.getReferencedProperty())) {
			return false;
		}

		return true;
	}

	private boolean isEmpty(Text text) {
		return text.getText().isEmpty();
	}

	private boolean isEmpty(Combo combo) {
		return combo.getText().isEmpty();
	}

	public PropertyDataModel updateModelFromView() {
		PropertyDataModel model = new PropertyDataModel();
		model.setNullable(view.getNullableCheck().getSelection());
		model.setUnique(view.getUniqueCheck().getSelection());
		model.setAutoIncrement(view.getAutoIncrementCheck().getSelection());
		model.setPrimaryKey(view.getPrimaryKeyCheck().getSelection());
		model.setForeignKey(view.getForeignKeyCheck().getSelection());

		model.setLength(view.getLength().getText());
		model.setPrecision(view.getPrecision().getText());
		model.setScale(view.getScale().getText());
		model.setDefaultValue(view.getDefaultValue().getText());
		model.setPrimaryKeyConstraintName(view.getPrimaryKeyConstraintName().getText());
		model.setForeignKeyConstraintName(view.getForeignKeyConstraintName().getText());

		model.setSqlType(view.getSqlTypeCombo().getText());
		model.setReferencedEntity(view.getReferencedEntity().getText());
		model.setReferencedProperty(view.getReferencedProperty().getText());
		return model;
	}

}
