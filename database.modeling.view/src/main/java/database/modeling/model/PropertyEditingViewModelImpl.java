package database.modeling.model;

import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;

import database.modeling.view.DatabaseModelingView;

public class PropertyEditingViewModelImpl implements PropertyEditingViewModel {

	DatabaseModelingView view;

	EPartService partService;

	public PropertyEditingViewModelImpl(DatabaseModelingView view, EPartService partService) {
		this.view = view;
		this.partService = partService;
	}

	@Override
	public void selectionChanged(Property porperty) {
		save();
		view.setDataModel(new SqlDataModel());
		view.setCurrentPropertySelection(porperty);
		setCurrentSelectionLabel();
		updateDataInView(view.getCurrentPropertySelection());
	}

	@Override
	public void databaseChanged(String currentDbSelected, String newDbName, SelectionEvent event) {
		// (modellen lévő adatok validálása)
		// modellen lévő összes adat begyűjtése

		Property currentPropertySelection = view.getCurrentPropertySelection();
		Model model = currentPropertySelection.getModel();
		ModelConverter converter = new ModelConverter(model, view);
		converter.writeModelToFile();

		// adatok kiírása xml-be vagy gson/json serializer
		// modell letakarítása
		// újonnan kiválaszttot DB-hez xml fájl keresése (ha nincs akkor nem csinálunk
		// semmit)
		// fájl felolvsása
		// összevetés a modellel, esetleges változások/ összeférhetetlenség keresése (ha
		// van hiba akkor error és hiba üzenet)
		// apply on model
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

	protected void updateDataInView(Property property) {
		resetView();
		SqlDataModel dataModel = DataTransformer.propertyToSqlDataModel(property);
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

	private void updateViewFromModel(SqlDataModel model) {
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

	public boolean isEmpty() {
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

	public boolean isEmpty(Text text) {
		return text.getText().isEmpty();
	}

	public boolean isEmpty(Combo combo) {
		return combo.getText().isEmpty();
	}

	public SqlDataModel updateModelFromView() {
		SqlDataModel model = new SqlDataModel();
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
