package database.modeling.viewmodel;

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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import database.modeling.util.DatabaseTypesUtil;
import database.modeling.util.InputVerifier;
import database.modeling.util.stereotype.DatabaseModelUtil;
import database.modeling.util.uml.ModelObjectUtil;
import database.modeling.view.DatabaseModelingView;
import database.modeling.viewmodel.DataTypeDefinition.InputType;

public class PropertyEditingViewModelImpl implements PropertyEditingViewModel {

	public static final Logger log = LoggerFactory.getLogger(PropertyEditingViewModelImpl.class);

	private DatabaseModelingView view;

	private Shell shell;

	private DatabaseTypesUtil dbUtil = new DatabaseTypesUtil();

	public PropertyEditingViewModelImpl(DatabaseModelingView view) {
		this.view = view;
		shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
	}

	@Override
	public void selectionChanged(Property porperty) {
		save();
		view.setCurrentPropertySelection(porperty);
		setCurrentSelectionLabel();

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
		Property currentPropertySelection = view.getCurrentPropertySelection();
		Model model = currentPropertySelection.getModel();
		if (model == null) {
			MessageDialog.openError(shell, "Database change failed!",
					"The selected attribute is not part of the model!");
			return;
		}

		// modellen lévő összes adat begyűjtése
		// adatok kiírása xml-be vagy gson/json serializer
		ModelConverter converter = new ModelConverter(model);
		converter.writeModelToFile(curentlySelectedDB);

		// minden sztereotípus leszedése a modellről
		converter.clearModel();

		// fájl adatok alkalmazása modellen
		converter.applyFileOnModel(newlySelectedDbName);

		// view frissítése az új db adatokkal
		String fragment = EcoreUtil.getURI(currentPropertySelection).fragment();
		Property propertySelectionAfterDBChange = ModelObjectUtil.getPropertyByXmiId(fragment, model);
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

		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(view.getCurrentPropertySelection());
		if (editingDomain == null) {
			return;
		}
		RecordingCommand recordingCommand = new RecordingCommand(editingDomain) {
			@Override
			protected void doExecute() {
				if (isEmpty()) {
					// remove all Stereotypes
					Property property = view.getCurrentPropertySelection();

					EList<Stereotype> appliedStereotypes = property.getAppliedStereotypes();
					for (Stereotype stereotype : appliedStereotypes) {
						property.unapplyStereotype(stereotype);
					}
				} else {
					DataTransformer.applyModelOnProperty(updateModelFromView(), view.getCurrentPropertySelection());
				}
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

	/**
	 * Képernyő alapállapotba állítása.
	 */
	private void resetView() {
		resetTypeParameterFields();
		resetConstraintfields();

		view.getDataTypeCombo().select(0);
	}

	/**
	 * Megszorításokat kezelő elemek alapállapotba állítása.
	 */
	private void resetConstraintfields() {
		view.getNullableCheck().setSelection(false);
		view.getUniqueCheck().setSelection(false);

		view.getPrimaryKeyCheck().setSelection(false);

		view.getForeignKeyCheck().setSelection(false);

		view.getReferencedEntity().clearSelection();
		view.getReferencedEntity().setEnabled(false);
		view.getReferencedProperty().clearSelection();
		view.getReferencedProperty().setEnabled(false);
	}

	/**
	 * Adattípus paraméter mezők alapállapotba állítása.
	 */
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

	/**
	 * A view adatainak frissítése a modell alapján.
	 * 
	 * @param model
	 */
	private void updateViewFromModel(PropertyDataModel model) {
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

	/**
	 * Megszorításokat kezelő elemek frissítése.
	 * 
	 * @param model
	 */
	private void updateConstraintCheckboxSegment(PropertyDataModel model) {
		view.getPrimaryKeyCheck().setSelection(model.isPrimaryKey());
		if (model.isPrimaryKey()) {
		}

		view.getUniqueCheck().setSelection(model.isUnique());

		view.getNullableCheck().setSelection(model.isNullable());

		view.getForeignKeyCheck().setSelection(model.isForeignKey());
		if (model.isForeignKey()) {
			view.getReferencedEntity().setEnabled(true);
			view.getReferencedProperty().setEnabled(true);
		}
	}

	/**
	 * Adattípus paraméter mezők tiltása/engedélyezése elérhetőség alapján.
	 * 
	 * @param dtd
	 */
	public void changeDataTypeInpuScheme(DataTypeDefinition dtd) {
		if (dtd.getName().isEmpty()) {
			resetView();
		} else {
			resetTypeParameterFields();
			checkboxEditingEnabled(true);
			InputType type = dtd.getType();
			if (type == null) {
				return;
			}

			if (InputType.NUMERIC.equals(type)) {
				if (dtd.hasLength()) {
					view.getLength().setEnabled(true);
					view.getLength().addVerifyListener(e -> {
						InputVerifier.verifyNumberFieldLength(e, dtd);
					});
					numberFieldLengthDecorator(view.getLength(), dtd.getLengthLowerBound(), dtd.getLengthUpperBound());
				}
				if (dtd.hasScale()) {
					view.getScale().setEnabled(true);
					view.getScale().addVerifyListener(e -> {
						InputVerifier.verifyNumberFieldScale(e, dtd);
					});
					numberFieldLengthDecorator(view.getScale(), dtd.getScaleLowerBound(), dtd.getScaleUpperBound());
				}
				if (dtd.hasPrecision()) {
					view.getPrecision().setEnabled(true);
					view.getPrecision().addVerifyListener(e -> {
						InputVerifier.verifyNumberFieldPrecision(e, dtd);
					});
					numberFieldLengthDecorator(view.getPrecision(), dtd.getPrecisionLowerBound(),
							dtd.getPrecisionUpperBound());
				}
				if (dtd.hasDefaulValue()) {
					view.getDefaultValue().setEnabled(true);
					if (dtd.hasPrecision()) {
						view.getDefaultValue().addVerifyListener(e -> {
							InputVerifier.verifyDefaultValueBounds(e, dtd);
						});
						textFieldLengthDecorator(view.getDefaultValue(), dtd.getPrecisionLowerBound(),
								dtd.getPrecisionUpperBound());
					}
				}
			} else {
				if (dtd.hasLength()) {
					view.getLength().setEnabled(true);
					view.getLength().addVerifyListener(e -> {
						InputVerifier.verifyNumberFieldLength(e, dtd);
					});
					numberFieldLengthDecorator(view.getLength(), dtd.getLengthLowerBound(), dtd.getLengthUpperBound());
				}
				if (dtd.hasDefaulValue()) {
					view.getDefaultValue().setEnabled(true);
					view.getDefaultValue().addVerifyListener(e -> {
						InputVerifier.verifyDefaultValueBounds(e, dtd);
					});
					textFieldLengthDecorator(view.getDefaultValue(), dtd.getLengthLowerBound(),
							dtd.getLengthUpperBound());
				}
			}
		}
	}

	private void numberFieldLengthDecorator(Text inputfield, long lower, long upper) {
		final ControlDecoration decorator = new ControlDecoration(inputfield, SWT.CENTER | SWT.LEFT);
		FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_WARNING);
		Image img = fieldDecoration.getImage();
		decorator.setImage(img);
		decorator.setDescriptionText("Value must be between " + lower + " and " + upper);
		decorator.setShowOnlyOnFocus(true);
	}

	private void textFieldLengthDecorator(Text inputfield, long lower, long upper) {
		final ControlDecoration decorator = new ControlDecoration(inputfield, SWT.CENTER | SWT.LEFT);
		FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_WARNING);
		Image img = fieldDecoration.getImage();
		decorator.setImage(img);
		decorator.setDescriptionText("Input length must be between " + lower + " and " + upper);
		decorator.setShowOnlyOnFocus(true);
	}

	@Override
	public void attributeEditingEnabled(boolean isEnabled) {
		view.getLength().setEnabled(isEnabled);
		view.getScale().setEnabled(isEnabled);
		view.getPrecision().setEnabled(isEnabled);
		view.getDefaultValue().setEnabled(isEnabled);

		checkboxEditingEnabled(isEnabled);
	}

	/**
	 * Megszorításokat kezelő elemek tiltása/engedélyezése
	 * 
	 */
	private void checkboxEditingEnabled(boolean isEnabled) {
		view.getNullableCheck().setEnabled(isEnabled);
		view.getUniqueCheck().setEnabled(isEnabled);

		view.getPrimaryKeyCheck().setEnabled(isEnabled);

		view.getForeignKeyCheck().setEnabled(isEnabled);
	}

	/**
	 * Aktív kiválasztás nevének frissítése.
	 */
	private void setCurrentSelectionLabel() {
		Property property = view.getCurrentPropertySelection();

		if (property != null) {
			view.getCurrentSelectionLabel().setText(property.getName());
		} else {
			view.getCurrentSelectionLabel().setText("Not a valid selection");
		}
	}

	/**
	 * Beviteli mezők ürességének ellenőrzése.
	 * 
	 * @return
	 */
	private boolean isEmpty() {
		if (view.getNullableCheck().getSelection() || view.getUniqueCheck().getSelection()
				|| view.getPrimaryKeyCheck().getSelection() || view.getForeignKeyCheck().getSelection()) {
			return false;
		}
		if (!isEmpty(view.getLength()) || !isEmpty(view.getPrecision()) || !isEmpty(view.getScale())
				|| !isEmpty(view.getDefaultValue())) {
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

	/**
	 * Model frissítése a View alapján.
	 * 
	 * @return
	 */
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

	/**
	 * Kiválasztott SQL implementáció adattípusainak betöltése a legördülő menübe.
	 * 
	 * @param newDbName
	 */
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

	/**
	 * A referálható entitások és azonosítójuk gyűjteményét gyűjti össze.
	 * 
	 * @param property
	 * @return
	 */
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

	/**
	 * {@link Classifier} attribútumait gyűjti össze.
	 * 
	 * @param classifierByName
	 * @return
	 */
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

	/**
	 * {@link Classifier} lekérdezése név alapján.
	 * 
	 * @param name
	 * @return
	 */
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

	/**
	 * A paraméterbe kapott elemek listájából kiszűri a {@link Classifer}
	 * típusúakat.
	 * 
	 * @param elementList
	 * @return
	 */
	public static Collection<Classifier> getClassifiers(EList<Element> elementList) {
		Collection<Classifier> classifiers = EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.INTERFACE);
		classifiers.addAll(EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.CLASS));
		return classifiers;
	}

}
