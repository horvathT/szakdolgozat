package database.modeling.view;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.ISelectionListener;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;

import database.modeling.model.DataTransformer;
import database.modeling.model.SqlDataModel;

public class DatabaseModelingView {

	private Text length;
	private Text precision;
	private Text scale;
	private Text defaultValue;
	private Text primaryKeyConstraintName;
	private Text foreignKeyConstraintName;

	private ToolItem databaseChanger;
	private Combo sqlTypeCombo;
	private Combo referencedEntity;
	private Combo referencedProperty;

	private Button nullableCheck;
	private Button uniqueCheck;
	private Button autoIncrementCheck;
	private Button primaryKeyCheck;
	private Button foreignKeyCheck;

	private Button saveButton;

	private ScrolledComposite scrolledComposite;

	private Label currentSelectionLabel;
	private ToolBar toolBar;

	private SqlDataModel dataModel;
	private Property currentPropertySelection;

	@Inject
	ESelectionService selectionService;

	@Inject
	public DatabaseModelingView() {
	}

	@PostConstruct
	public void createPartControl(Composite parent) {

		dataModel = new SqlDataModel();

		scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		Composite container = new Composite(scrolledComposite, SWT.NONE);
		container.setLayout(new GridLayout(4, false));

		currentSelectionLabel = new Label(container, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
		gd_lblNewLabel.widthHint = 152;
		currentSelectionLabel.setLayoutData(gd_lblNewLabel);
		setCurrentSelectionLabel();

		toolBar = new ToolBar(container, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		databaseChanger = new ToolItem(toolBar, SWT.DROP_DOWN);
		databaseChanger.setText("Database");

		Label lblSqlType = new Label(container, SWT.NONE);
		lblSqlType.setText("SQL type");

		ComboViewer sqlTypeComboViewer = new ComboViewer(container, SWT.READ_ONLY);
		sqlTypeCombo = sqlTypeComboViewer.getCombo();
		sqlTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		Label lblLength = new Label(container, SWT.NONE);
		lblLength.setText("Length");

		length = new Text(container, SWT.BORDER);
		length.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		Label lblPrecision = new Label(container, SWT.NONE);
		lblPrecision.setText("Precision");

		precision = new Text(container, SWT.BORDER);
		precision.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		Label lblScale = new Label(container, SWT.NONE);
		lblScale.setText("Scale");

		scale = new Text(container, SWT.BORDER);
		scale.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		Label lblDefaultValue = new Label(container, SWT.NONE);
		lblDefaultValue.setText("Default value");

		defaultValue = new Text(container, SWT.BORDER);
		defaultValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		saveButton = new Button(container, SWT.NONE);
		GridData gd_btnSave = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		saveButton.setLayoutData(gd_btnSave);
		saveButton.setText("Save as Datatype");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		nullableCheck = new Button(container, SWT.CHECK);
		nullableCheck.setText("Nullable");

		uniqueCheck = new Button(container, SWT.CHECK);
		uniqueCheck.setText("Unique");

		autoIncrementCheck = new Button(container, SWT.CHECK);
		autoIncrementCheck.setText("Auto increment");
		new Label(container, SWT.NONE);

		primaryKeyCheck = new Button(container, SWT.CHECK);
		primaryKeyCheck.setText("Primary Key");
		primaryKeyCheck.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));

		Label lblPrimaryKeyConstraint = new Label(container, SWT.NONE);
		lblPrimaryKeyConstraint.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPrimaryKeyConstraint.setText("Primary Key Constraint Name");

		primaryKeyConstraintName = new Text(container, SWT.BORDER);
		primaryKeyConstraintName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		primaryKeyConstraintName.setEnabled(false);

		foreignKeyCheck = new Button(container, SWT.CHECK);
		foreignKeyCheck.setText("Foreign Key");
		foreignKeyCheck.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));

		Label lblForeignKeyConstraint = new Label(container, SWT.NONE);
		lblForeignKeyConstraint.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblForeignKeyConstraint.setText("Foreign Key Constraint Name");

		foreignKeyConstraintName = new Text(container, SWT.BORDER);
		foreignKeyConstraintName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		foreignKeyConstraintName.setEnabled(false);

		Label lblReferencedEntity = new Label(container, SWT.NONE);
		lblReferencedEntity.setText("Referenced entity");

		ComboViewer referencedEntityComboViewer = new ComboViewer(container, SWT.READ_ONLY);
		referencedEntity = referencedEntityComboViewer.getCombo();
		referencedEntity.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		referencedEntity.setEnabled(false);

		Label lblReferencedAttribute = new Label(container, SWT.NONE);
		lblReferencedAttribute.setText("Referenced Attribute");

		ComboViewer referencedPropertyComboViewer = new ComboViewer(container, SWT.READ_ONLY);
		referencedProperty = referencedPropertyComboViewer.getCombo();
		referencedProperty.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		referencedProperty.setEnabled(false);

		scrolledComposite.setContent(container);
		scrolledComposite.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		primaryKeyCannotBeNullable();
		foreignKeyCheckBoxListener();
		bindValues();
		setupSelectionListener();
	}

	private void setCurrentSelectionLabel() {
		if (currentPropertySelection != null) {
			currentSelectionLabel.setText(currentPropertySelection.getName());
		} else {
			currentSelectionLabel.setText("Not a valid selection");
		}
	}

	private void setupSelectionListener() {
		selectionService.addSelectionListener(new ISelectionListener() {

			@Override
			public void selectionChanged(MPart part, Object selection) {
				if (selection instanceof StructuredSelection) {
					StructuredSelection sSelection = (StructuredSelection) selection;
					Object firstElement = sSelection.getFirstElement();
					if (firstElement instanceof IAdaptable) {
						NamedElement element = ((IAdaptable) firstElement).getAdapter(NamedElement.class);
						if (element instanceof Property) {
							save();
							dataModel = new SqlDataModel();
							currentPropertySelection = (Property) element;
							setCurrentSelectionLabel();
							updateDataInView(currentPropertySelection);
						}
					}
				}
			}
		});
	}

	private void primaryKeyCannotBeNullable() {
		primaryKeyCheck.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (primaryKeyCheck.getSelection()) {
					nullableCheck.setEnabled(false);
					primaryKeyConstraintName.setEnabled(true);
				} else {
					nullableCheck.setEnabled(true);
					primaryKeyConstraintName.setEnabled(false);
				}
			}

		});

		nullableCheck.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (nullableCheck.getSelection()) {
					primaryKeyCheck.setEnabled(false);
					primaryKeyConstraintName.setEnabled(false);
				} else {
					primaryKeyCheck.setEnabled(true);
					primaryKeyConstraintName.setEnabled(true);
				}
			}

		});
	}

	private void foreignKeyCheckBoxListener() {
		foreignKeyCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (foreignKeyCheck.getSelection()) {
					foreignKeyConstraintName.setEnabled(true);
					referencedEntity.setEnabled(true);
					referencedProperty.setEnabled(true);
				} else {
					referencedEntity.setEnabled(false);
					referencedProperty.setEnabled(false);
					foreignKeyConstraintName.setEnabled(false);
				}
			}
		});
	}

	@Focus
	public void setFocus() {

	}

	@PreDestroy
	public void preDestroy() {

	}

	@Persist
	public void save() {
		if (currentPropertySelection == null) {
			return;
		}
		if (isEmpty()) {
			return;
		}

		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(currentPropertySelection);
		if (editingDomain == null) {
			return;
		}
		RecordingCommand recordingCommand = new RecordingCommand(editingDomain) {
			@Override
			protected void doExecute() {
				DataTransformer.applyModelOnProperty(updateModelFromView(), currentPropertySelection);
				// TODO
				// PropertyEditingViewModelImpl pevmi.selectionChanged();
			}
		};
		editingDomain.getCommandStack().execute(recordingCommand);
	}

	private void bindValues() {
		DataBindingContext ctx = new DataBindingContext();

		IObservableValue<String> propertyLength = BeanProperties.value(SqlDataModel.class, "length", String.class)
				.observe(dataModel);
		ISWTObservableValue<String> widgetLength = WidgetProperties.text(SWT.Modify).observe(length);
		ctx.bindValue(widgetLength, propertyLength);

		IObservableValue<String> propertyPrecision = BeanProperties.value(SqlDataModel.class, "precision", String.class)
				.observe(dataModel);
		ISWTObservableValue<String> widgetPrecision = WidgetProperties.text(SWT.Modify).observe(precision);
		ctx.bindValue(widgetPrecision, propertyPrecision);

		IObservableValue<String> propertyScale = BeanProperties.value(SqlDataModel.class, "scale", String.class)
				.observe(dataModel);
		ISWTObservableValue<String> widgetScale = WidgetProperties.text(SWT.Modify).observe(scale);
		ctx.bindValue(widgetScale, propertyScale);

		IObservableValue<String> propertyDefaultValue = BeanProperties
				.value(SqlDataModel.class, "defaultValue", String.class).observe(dataModel);
		ISWTObservableValue<String> widgetDefaultValue = WidgetProperties.text(SWT.Modify).observe(defaultValue);
		ctx.bindValue(widgetDefaultValue, propertyDefaultValue);

		IObservableValue<String> propertyPrimaryKeyConstraintName = BeanProperties
				.value(SqlDataModel.class, "primaryKeyConstraintName", String.class).observe(dataModel);
		ISWTObservableValue<String> widgetPrimaryKeyConstraintName = WidgetProperties.text(SWT.Modify)
				.observe(primaryKeyConstraintName);
		ctx.bindValue(widgetPrimaryKeyConstraintName, propertyPrimaryKeyConstraintName);

		IObservableValue<String> propertyForeignKeyConstraintName = BeanProperties
				.value(SqlDataModel.class, "foreignKeyConstraintName", String.class).observe(dataModel);
		ISWTObservableValue<String> widgetForeignKeyConstraintName = WidgetProperties.text(SWT.Modify)
				.observe(foreignKeyConstraintName);
		ctx.bindValue(widgetForeignKeyConstraintName, propertyForeignKeyConstraintName);

		IObservableValue<String> propertySqlType = BeanProperties.value(SqlDataModel.class, "sqlType", String.class)
				.observe(dataModel);
		ISWTObservableValue<String> widgetSqlType = WidgetProperties.comboSelection().observe(sqlTypeCombo);
		ctx.bindValue(widgetSqlType, propertySqlType);

		IObservableValue<String> propertyReferencedEntity = BeanProperties
				.value(SqlDataModel.class, "referencedEntity", String.class).observe(dataModel);
		ISWTObservableValue<String> widgetReferencedEntity = WidgetProperties.comboSelection()
				.observe(referencedEntity);
		ctx.bindValue(widgetReferencedEntity, propertyReferencedEntity);

		IObservableValue<String> propertyReferencedProperty = BeanProperties
				.value(SqlDataModel.class, "referencedProperty", String.class).observe(dataModel);
		ISWTObservableValue<String> widgetReferencedProperty = WidgetProperties.comboSelection()
				.observe(referencedProperty);
		ctx.bindValue(widgetReferencedProperty, propertyReferencedProperty);

		IObservableValue<Boolean> propertyNullable = BeanProperties.value(SqlDataModel.class, "nullable", Boolean.class)
				.observe(dataModel);
		ISWTObservableValue<Boolean> widgetNullable = WidgetProperties.buttonSelection().observe(nullableCheck);
		ctx.bindValue(widgetNullable, propertyNullable);

		IObservableValue<Boolean> propertyUnique = BeanProperties.value(SqlDataModel.class, "unique", Boolean.class)
				.observe(dataModel);
		ISWTObservableValue<Boolean> widgetUnique = WidgetProperties.buttonSelection().observe(uniqueCheck);
		ctx.bindValue(widgetUnique, propertyUnique);

		IObservableValue<Boolean> propertyAutoIncrement = BeanProperties
				.value(SqlDataModel.class, "autoIncrement", Boolean.class).observe(dataModel);
		ISWTObservableValue<Boolean> widgetAutoIncrement = WidgetProperties.buttonSelection()
				.observe(autoIncrementCheck);
		ctx.bindValue(widgetAutoIncrement, propertyAutoIncrement);

		IObservableValue<Boolean> propertyPrimaryKey = BeanProperties
				.value(SqlDataModel.class, "primaryKey", Boolean.class).observe(dataModel);
		ISWTObservableValue<Boolean> widgetPrimaryKey = WidgetProperties.buttonSelection().observe(primaryKeyCheck);
		ctx.bindValue(widgetPrimaryKey, propertyPrimaryKey);

		IObservableValue<Boolean> propertyForeignKey = BeanProperties
				.value(SqlDataModel.class, "foreignKey", Boolean.class).observe(dataModel);
		ISWTObservableValue<Boolean> widgetForeignKey = WidgetProperties.buttonSelection().observe(foreignKeyCheck);
		ctx.bindValue(widgetForeignKey, propertyForeignKey);
	}

	protected void updateDataInView(Property property) {
		DataTransformer.propertyToSqlDataModel(property, dataModel);
		updateViewFromModel(dataModel);
	}

	private void updateViewFromModel(SqlDataModel model) {
		getNullableCheck().setSelection(model.isNullable());
		getUniqueCheck().setSelection(model.isUnique());
		getAutoIncrementCheck().setSelection(model.isAutoIncrement());

		// check if enabled
		getLength().setText(model.getLength());
		getPrecision().setText(model.getPrecision());
		getScale().setText(model.getScale());
		getDefaultValue().setText(model.getDefaultValue());

		getPrimaryKeyCheck().setSelection(model.isPrimaryKey());
		getForeignKeyCheck().setSelection(model.isForeignKey());
		if (model.isPrimaryKey()) {
			getPrimaryKeyConstraintName().setEnabled(true);
			getPrimaryKeyConstraintName().setText(model.getPrimaryKeyConstraintName());
		}
		if (model.isForeignKey()) {
			getForeignKeyConstraintName().setEnabled(true);
			getForeignKeyConstraintName().setText(model.getForeignKeyConstraintName());
			getReferencedEntity().setText(model.getReferencedEntity());
			getReferencedEntity().setEnabled(true);
			getReferencedProperty().setText(model.getReferencedProperty());
			getReferencedProperty().setEnabled(true);
		}

		getSqlTypeCombo().setText(model.getSqlType());
	}

	public SqlDataModel updateModelFromView() {
		SqlDataModel model = new SqlDataModel();
		model.setNullable(nullableCheck.getSelection());
		model.setUnique(uniqueCheck.getSelection());
		model.setAutoIncrement(autoIncrementCheck.getSelection());
		model.setPrimaryKey(primaryKeyCheck.getSelection());
		model.setForeignKey(foreignKeyCheck.getSelection());

		model.setLength(length.getText());
		model.setPrecision(precision.getText());
		model.setScale(scale.getText());
		model.setDefaultValue(defaultValue.getText());
		model.setPrimaryKeyConstraintName(primaryKeyConstraintName.getText());
		model.setForeignKeyConstraintName(foreignKeyConstraintName.getText());

		model.setSqlType(sqlTypeCombo.getText());
		model.setReferencedEntity(referencedEntity.getText());
		model.setReferencedProperty(referencedProperty.getText());
		return model;
	}

	public boolean isEmpty() {
		if (nullableCheck.getSelection() || uniqueCheck.getSelection() || autoIncrementCheck.getSelection()
				|| primaryKeyCheck.getSelection() || foreignKeyCheck.getSelection()) {
			return false;
		}
		if (!isEmpty(length) || !isEmpty(precision) || !isEmpty(scale) || !isEmpty(defaultValue)
				|| !isEmpty(primaryKeyConstraintName) || !isEmpty(foreignKeyConstraintName)) {
			return false;
		}
		if (!isEmpty(sqlTypeCombo) || !isEmpty(referencedEntity) || !isEmpty(referencedProperty)) {
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

	/**
	 * This method is kept for E3 compatiblity. You can remove it if you do not mix
	 * E3 and E4 code. <br/>
	 * With E4 code you will set directly the selection in ESelectionService and you
	 * do not receive a ISelection
	 * 
	 * @param s the selection received from JFace (E3 mode)
	 */
//	@Inject
//	@Optional
//	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
//		if (selection == null || selection.isEmpty())
//			return;
//
//		Property property = SelectionUtil.getProperty(selection);
//		if (property != null) {
//			save();
//			dataModel = new SqlDataModel();
//			updateSelection(property);
//			updateDataInView(property);
//		}
//	}

	/**
	 * This method manages the selection of your current object. In this example we
	 * listen to a single Object (even the ISelection already captured in E3 mode).
	 * <br/>
	 * You should change the parameter type of your received Object to manage your
	 * specific selection
	 * 
	 * @param o : the current object received
	 */
//	@Inject
//	@Optional
//	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) Object o) {
//
//		// Remove the 2 following lines in pure E4 mode, keep them in mixed mode
//		if (o instanceof ISelection) // Already captured
//			return;
//	}

	/**
	 * This method manages the multiple selection of your current objects. <br/>
	 * You should change the parameter type of your array of Objects to manage your
	 * specific selection
	 * 
	 * @param o : the current array of objects received in case of multiple
	 *          selection
	 */
	@Inject
	@Optional
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) Object[] selectedObjects) {

		// Test if label exists (inject methods are called before PostConstruct)
//		if (myLabelInView != null)
//			myLabelInView.setText("This is a multiple selection of " + selectedObjects.length + " objects");
	}

// GETTERS, SETTERS -----------------------------------------------------------------------------------------------
	public Text getLength() {
		return length;
	}

	public void setLength(Text length) {
		this.length = length;
	}

	public Text getPrecision() {
		return precision;
	}

	public void setPrecision(Text precision) {
		this.precision = precision;
	}

	public Text getScale() {
		return scale;
	}

	public void setScale(Text scale) {
		this.scale = scale;
	}

	public Text getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Text defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Text getPrimaryKeyConstraintName() {
		return primaryKeyConstraintName;
	}

	public void setPrimaryKeyConstraintName(Text primaryKeyConstraintName) {
		this.primaryKeyConstraintName = primaryKeyConstraintName;
	}

	public Text getForeignKeyConstraintName() {
		return foreignKeyConstraintName;
	}

	public void setForeignKeyConstraintName(Text foreignKeyConstraintName) {
		this.foreignKeyConstraintName = foreignKeyConstraintName;
	}

	public Combo getSqlTypeCombo() {
		return sqlTypeCombo;
	}

	public void setSqlTypeCombo(Combo sqlTypeCombo) {
		this.sqlTypeCombo = sqlTypeCombo;
	}

	public Combo getReferencedEntity() {
		return referencedEntity;
	}

	public void setReferencedEntity(Combo referencedEntity) {
		this.referencedEntity = referencedEntity;
	}

	public Combo getReferencedProperty() {
		return referencedProperty;
	}

	public void setReferencedProperty(Combo referencedProperty) {
		this.referencedProperty = referencedProperty;
	}

	public Button getNullableCheck() {
		return nullableCheck;
	}

	public void setNullableCheck(Button nullableCheck) {
		this.nullableCheck = nullableCheck;
	}

	public Button getUniqueCheck() {
		return uniqueCheck;
	}

	public void setUniqueCheck(Button uniqueCheck) {
		this.uniqueCheck = uniqueCheck;
	}

	public Button getAutoIncrementCheck() {
		return autoIncrementCheck;
	}

	public void setAutoIncrementCheck(Button autoIncrementCheck) {
		this.autoIncrementCheck = autoIncrementCheck;
	}

	public Button getPrimaryKeyCheck() {
		return primaryKeyCheck;
	}

	public void setPrimaryKeyCheck(Button primaryKeyCheck) {
		this.primaryKeyCheck = primaryKeyCheck;
	}

	public Button getForeignKeyCheck() {
		return foreignKeyCheck;
	}

	public void setForeignKeyCheck(Button foreignKeyCheck) {
		this.foreignKeyCheck = foreignKeyCheck;
	}

	public Button getSaveButton() {
		return saveButton;
	}

	public void setSaveButton(Button saveButton) {
		this.saveButton = saveButton;
	}

	public ScrolledComposite getScrolledComposite() {
		return scrolledComposite;
	}

	public void setScrolledComposite(ScrolledComposite scrolledComposite) {
		this.scrolledComposite = scrolledComposite;
	}

	public Label getCurrentSelectionLabel() {
		return currentSelectionLabel;
	}

	public void setCurrentSelectionLabel(Label currentSelectionLabel) {
		this.currentSelectionLabel = currentSelectionLabel;
	}

	public ToolBar getToolBar() {
		return toolBar;
	}

	public void setToolBar(ToolBar toolBar) {
		this.toolBar = toolBar;
	}

	public ToolItem getDatabaseChanger() {
		return databaseChanger;
	}

	public void setDatabaseChanger(ToolItem databaseChanger) {
		this.databaseChanger = databaseChanger;
	}
//	public DatabaseModelingController getController() {
//		return controller;
//	}
//
//	public void setController(DatabaseModelingController controller) {
//		this.controller = controller;
//	}

}
