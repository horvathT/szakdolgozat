package database.modeling.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import database.modeling.util.resource.EclipseModelUtil;
import database.modeling.util.stereotype.DatabaseModelUtil;
import database.modeling.util.stereotype.DatabaseTypesUtil;
import database.modeling.view.DatabaseModelingView;

public class PropertyEditingViewModelImpl implements PropertyEditingViewModel {

	public static final Logger log = LoggerFactory.getLogger(PropertyEditingViewModelImpl.class);

	DatabaseModelingView view;

	private DatabaseTypesUtil dbUtil = new DatabaseTypesUtil();

	public PropertyEditingViewModelImpl(DatabaseModelingView view) {
		this.view = view;
	}

	@Override
	public void selectionChanged(Property porperty) {
		save();
		view.setCurrentPropertySelection(porperty);
		setCurrentSelectionLabel();

		// Meg kell nézni hogy a váltás után is ugyan abban a DB típusban vagyunk-e
		Model model = porperty.getModel();
		String modelDBType = DatabaseModelUtil.getDatabaseType(model);
		ToolItem databaseChanger = view.getDatabaseChanger();

//		if (modelDBType != null || !modelDBType.isEmpty()) {
//			String selectedDBType = databaseChanger.getText();
//			if (modelDBType.equals(selectedDBType)) {
//				updateDataInView(view.getCurrentPropertySelection());
//			} else {
//				updateDatabaseChanger(selectedDBType);
//				changeDatabaseImplementation(selectedDBType, modelDBType);
//			}
//		} else {
//			updateDataInView(view.getCurrentPropertySelection());
//		}

		String selectedDBType = databaseChanger.getText();
		if (modelDBType == null || modelDBType.isEmpty()) {
			TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(model);
			RecordingCommand command = new RecordingCommand(editingDomain) {
				@Override
				protected void doExecute() {
					DatabaseModelUtil.setDatabaseType(model, selectedDBType);
				}
			};
			editingDomain.getCommandStack().execute(command);
			updateDataInView(view.getCurrentPropertySelection());
		} else {
			if (modelDBType.equals(selectedDBType)) {
				updateDataInView(view.getCurrentPropertySelection());
			} else {
				updateDatabaseChanger(modelDBType);
				updateDataInView(view.getCurrentPropertySelection());
//				changeDatabaseImplementation(selectedDBType, modelDBType);
			}
		}

	}

	@Override
	public void changeDatabaseImplementation(String curentlySelectedDB, String newlySelectedDbName) {
		// selection xmiId-t elrakjuk későbbre
		Property currentPropertySelection = view.getCurrentPropertySelection();

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
		String fragment = EcoreUtil.getURI(currentPropertySelection).fragment();
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

		view.getSqlTypeCombo().select(0);

		view.getReferencedEntity().clearSelection();
		view.getReferencedEntity().setEnabled(false);
		view.getReferencedProperty().clearSelection();
		view.getReferencedProperty().setEnabled(false);

		view.getNullableCheck().setSelection(false);
		view.getUniqueCheck().setSelection(false);
		view.getPrimaryKeyCheck().setSelection(false);
		view.getForeignKeyCheck().setSelection(false);
	}

	private void updateViewFromModel(PropertyDataModel model) {
		view.getNullableCheck().setSelection(model.isNullable());
		view.getUniqueCheck().setSelection(model.isUnique());

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

		setupReferenceEntityCombo();
		String referencedEntity = model.getReferencedEntity();
		view.getReferencedEntity().setText(referencedEntity);
		if (!referencedEntity.isEmpty()) {
			setupReferencePropertyCombo(referencedEntity);
		}
		view.getReferencedProperty().setText(model.getReferencedProperty());

		if (model.getSqlType().isEmpty()) {
			view.getSqlTypeCombo().clearSelection();
			isAttributeEditingEnabled(false);
		} else {
			view.getSqlTypeCombo().setText(model.getSqlType());
			isAttributeEditingEnabled(true);
		}
	}

	@Override
	public void isAttributeEditingEnabled(boolean isEnabled) {
		view.getLength().setEnabled(isEnabled);
		view.getScale().setEnabled(isEnabled);
		view.getPrecision().setEnabled(isEnabled);
		view.getDefaultValue().setEnabled(isEnabled);

		view.getNullableCheck().setEnabled(isEnabled);
		view.getUniqueCheck().setEnabled(isEnabled);

		view.getPrimaryKeyCheck().setEnabled(isEnabled);
//		view.getPrimaryKeyConstraintName().setEditable(isEnabled);

		view.getForeignKeyCheck().setEnabled(isEnabled);
//		view.getForeignKeyConstraintName().setEnabled(isEnabled);
//		view.getReferencedEntity().setEnabled(isEnabled);
//		view.getReferencedProperty().setEnabled(isEnabled);
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
				|| view.getPrimaryKeyCheck().getSelection() || view.getForeignKeyCheck().getSelection()) {
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

	@Override
	public void updateDatabaseChanger(String newDbName) {
		view.getDatabaseChanger().setText(newDbName);
		view.getDatabaseChanger().setSelection(true);
		Map<String, String[]> dbMap = dbUtil.getDatabaseTypeMap();
		view.getSqlTypeCombo().setItems(dbMap.get(newDbName));
		view.getSqlTypeCombo().add("", 0);
	}

	@Override
	public List<String> getDatabaseTypes() {
		return dbUtil.getDatabases();
	}

	private Map<String, String> getReferencedEntityNameMap(Property property) {
		Map<String, String> referencedEntityFragmentsByName = new HashMap<>();

		Element owner = property.getOwner();
		if (owner instanceof Classifier) {

			Classifier classifier = (Classifier) owner;
			EList<Association> associations = classifier.getAssociations();
			for (Association association : associations) {
				EList<Property> memberEnds = association.getMemberEnds();
				Property refProp = memberEnds.get(0);
				Type type = refProp.getType();
				if (!classifier.equals(type)) {
					String fragment = EcoreUtil.getURI(type).fragment();
					referencedEntityFragmentsByName.put(type.getName(), fragment);
				}
			}
		}
		return referencedEntityFragmentsByName;
	}

	@Override
	public void setupReferenceEntityCombo() {
		Property currentPropertySelection = view.getCurrentPropertySelection();
		Map<String, String> referencedEntityNameMap = getReferencedEntityNameMap(currentPropertySelection);
		Set<String> keySet = referencedEntityNameMap.keySet();
		view.getReferencedEntity().setItems(keySet.toArray(new String[keySet.size()]));
		view.getReferencedEntity().add("", 0);
	}

	@Override
	public void setupReferencePropertyCombo(String entityName) {
		Classifier classifierByName = getClassifierByName(entityName);
		if (classifierByName != null) {
			List<Property> allAttributes = getOwnedAttributes(classifierByName);
			List<String> listOfPropertyNames = allAttributes.stream().map(Property::getName)
					.collect(Collectors.toList());
			view.getReferencedProperty().setItems(listOfPropertyNames.toArray(new String[listOfPropertyNames.size()]));
			view.getReferencedProperty().add("", 0);
		} else {
			log.error("Classifier by name: " + entityName + "not found!");
		}
	}

	private List<Property> getOwnedAttributes(Classifier classifierByName) {
		List<Property> propertyList = new ArrayList<>();
		for (Property property : classifierByName.getAllAttributes()) {
			if (!isAssociation(property)) {
				propertyList.add(property);
			}
		}
		return propertyList;
	}

	public static boolean isAssociation(Property property) {
		return property.getAssociation() != null;
	}

	private Classifier getClassifierByName(String name) {
		Model model = view.getCurrentPropertySelection().getModel();
		Collection<Classifier> classifiers = getClassifiers(model.allOwnedElements());
		for (Classifier classifier : classifiers) {
			if (classifier.getName().equals(name)) {
				return classifier;
			}
		}
		return null;
	}

	public static Collection<Classifier> getClassifiers(EList<Element> elementList) {
		Collection<Classifier> classifiers = EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.INTERFACE);
		classifiers.addAll(EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.CLASS));
		return classifiers;
	}

}
