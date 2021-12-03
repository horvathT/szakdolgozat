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
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import database.modeling.model.DataTypeDefinition.InputType;
import database.modeling.util.InputVerifier;
import database.modeling.util.resource.EclipseModelUtil;
import database.modeling.util.stereotype.DatabaseModelUtil;
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
		resetTypeParameterFields();
		resetConstraintfields();

		view.getDataTypeCombo().select(0);
	}

	private void resetConstraintfields() {
		view.getNullableCheck().setSelection(false);
		view.getUniqueCheck().setSelection(false);

		view.getPrimaryKeyCheck().setSelection(false);

		view.getPrimaryKeyConstraintName().setText("");

		view.getForeignKeyCheck().setSelection(false);

		view.getForeignKeyConstraintName().setText("");

		view.getReferencedEntity().clearSelection();
		view.getReferencedProperty().clearSelection();
	}

	private void resetTypeParameterFields() {
		view.getLength().setText("");
		view.getLength().setEnabled(false);
		view.getPrecision().setText("");
		view.getPrecision().setEnabled(false);
		view.getScale().setText("");
		view.getScale().setEnabled(false);
		view.getDefaultValue().setText("");
		view.getDefaultValue().setEnabled(false);
	}

	private void updateViewFromModel(PropertyDataModel model) {
//		attributeEditingEnabled(false);
		DataTypeDefinition dataType = model.getSqlType();
		if (dataType == null) {
			view.getSqlTypeComboViewer()
					.setSelection(new StructuredSelection(DataTypeDefinition.of().name("").hasDefaulValue(false)));
		} else {
			view.getSqlTypeComboViewer().setSelection(new StructuredSelection(dataType), true);
		}

		view.getLength().setText(model.getLength());
		view.getPrecision().setText(model.getPrecision());
		view.getScale().setText(model.getScale());
		view.getDefaultValue().setText(model.getDefaultValue());

		updateConstraintCheckboxSegment(model);

		setupReferenceEntityCombo();
		String referencedEntity = model.getReferencedEntity();
		view.getReferencedEntity().setText(referencedEntity);
		if (!referencedEntity.isEmpty()) {
			setupReferencePropertyCombo(referencedEntity);
		}
		view.getReferencedProperty().setText(model.getReferencedProperty());

	}

	private void updateConstraintCheckboxSegment(PropertyDataModel model) {
		view.getPrimaryKeyCheck().setSelection(model.isPrimaryKey());
		if (model.isPrimaryKey()) {
			view.getPrimaryKeyConstraintName().setEnabled(true);
		}
		view.getPrimaryKeyConstraintName().setText(model.getPrimaryKeyConstraintName());

		view.getUniqueCheck().setSelection(model.isUnique());

		view.getNullableCheck().setSelection(model.isNullable());

		view.getForeignKeyCheck().setSelection(model.isForeignKey());
		if (model.isForeignKey()) {
			view.getForeignKeyConstraintName().setEnabled(true);
			view.getReferencedEntity().setEnabled(true);
			view.getReferencedProperty().setEnabled(true);
		}
		view.getForeignKeyConstraintName().setText(model.getForeignKeyConstraintName());
	}

	public void changeDataTypeInpuScheme(DataTypeDefinition dtd) {
		if (dtd.getName().isEmpty()) {
			resetView();
		} else {
			resetTypeParameterFields();
			checkboxEditingEnabled(true);
			InputType type = dtd.getType();

			if (InputType.NUMERIC.equals(type)) {
				if (dtd.hasLength()) {
					view.getLength().setEnabled(true);
					view.getLength().addVerifyListener(e -> {
						InputVerifier.verifyNumberFieldLength(e, dtd);
					});
				}
				if (dtd.hasScale()) {
					view.getScale().setEnabled(true);
					view.getScale().addVerifyListener(e -> {
						InputVerifier.verifyNumberFieldScale(e, dtd);
					});
				}
				if (dtd.hasPrecision()) {
					view.getPrecision().setEnabled(true);
					view.getPrecision().addVerifyListener(e -> {
						InputVerifier.verifyNumberFieldPrecision(e, dtd);
					});
				}
				if (dtd.hasDefaulValue()) {
					view.getDefaultValue().setEnabled(true);
					view.getDefaultValue().addVerifyListener(e -> {
						InputVerifier.verifyNumberField(e, dtd);
					});
				}
			} else {
				if (dtd.hasLength()) {
					view.getLength().setEnabled(true);
					view.getLength().addVerifyListener(e -> {
						InputVerifier.verifyNumberFieldLength(e, dtd);
					});
				}
				if (dtd.hasDefaulValue()) {
					view.getDefaultValue().setEnabled(true);
				}
			}
		}
	}

	@Override
	public void attributeEditingEnabled(boolean isEnabled) {
		view.getLength().setEnabled(isEnabled);
		view.getScale().setEnabled(isEnabled);
		view.getPrecision().setEnabled(isEnabled);
		view.getDefaultValue().setEnabled(isEnabled);

		checkboxEditingEnabled(isEnabled);
	}

	private void checkboxEditingEnabled(boolean isEnabled) {
		view.getNullableCheck().setEnabled(isEnabled);
		view.getUniqueCheck().setEnabled(isEnabled);

		view.getPrimaryKeyCheck().setEnabled(isEnabled);

		view.getForeignKeyCheck().setEnabled(isEnabled);
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
		if (!isEmpty(view.getDataTypeCombo()) || !isEmpty(view.getReferencedEntity())
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

		IStructuredSelection structuredSelection = view.getSqlTypeComboViewer().getStructuredSelection();
		DataTypeDefinition selection = (DataTypeDefinition) structuredSelection.getFirstElement();
		model.setSqlType(selection);
		model.setReferencedEntity(view.getReferencedEntity().getText());
		model.setReferencedProperty(view.getReferencedProperty().getText());
		return model;
	}

	@Override
	public void updateDatabaseChanger(String newDbName) {
		view.getDatabaseChanger().setText(newDbName);
		view.getDatabaseChanger().setSelection(true);
		fillDatatTypeCombo(newDbName);
	}

	private void fillDatatTypeCombo(String newDbName) {
		Map<String, List<DataTypeDefinition>> dbMap = dbUtil.getDatabaseTypeMap();
		List<DataTypeDefinition> list = dbMap.get(newDbName);

		view.getSqlTypeComboViewer().setContentProvider(ArrayContentProvider.getInstance());
		view.getSqlTypeComboViewer().setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DataTypeDefinition) {
					DataTypeDefinition dtd = (DataTypeDefinition) element;
					return dtd.getName();
				}
				return super.getText(element);
			}
		});

		view.getSqlTypeComboViewer().setInput(list);
	}

	@Override
	public Set<String> getDatabaseTypes() {
		return dbUtil.getDatabases();
	}

	private Map<String, String> getReferencedEntityNameMap(Property property) {
		Map<String, String> referencedEntityFragmentsByName = new HashMap<>();

		Element owner = property.getOwner();
		if (owner instanceof Interface || owner instanceof Class) {

			Classifier classifier = (Classifier) owner;
			EList<Association> associations = classifier.getAssociations();
			for (Association association : associations) {
				EList<Property> memberEnds = association.getMemberEnds();
				Property refProp = memberEnds.get(0);
				Type type = refProp.getType();
				if (type instanceof Interface || type instanceof Class) {
					if (!classifier.equals(type)) {
						String fragment = EcoreUtil.getURI(type).fragment();
						referencedEntityFragmentsByName.put(type.getName(), fragment);
					}
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
