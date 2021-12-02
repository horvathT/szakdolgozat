package database.modeling.view;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.ISelectionListener;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
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

import database.modeling.handler.DatabaseSelectionListener;
import database.modeling.model.DataTypeDefinition;
import database.modeling.model.PropertyEditingViewModelImpl;

public class DatabaseModelingView {

	private Text length;
	private Text precision;
	private Text scale;
	private Text defaultValue;
	private Text primaryKeyConstraintName;
	private Text foreignKeyConstraintName;

	private ToolItem databaseChanger;
	private Combo dataTypeCombo;
	private Combo referencedEntity;
	private Combo referencedProperty;

	private Button nullableCheck;
	private Button uniqueCheck;
	private Button primaryKeyCheck;
	private Button foreignKeyCheck;

	private ScrolledComposite scrolledComposite;

	private Label currentSelectionLabel;
	private ToolBar toolBar;

	private Property currentPropertySelection;

	private PropertyEditingViewModelImpl viewModel;
	private DatabaseSelectionListener dbSelectionListener;

	@Inject
	ESelectionService selectionService;

	private SelectionAdapter forignKeyCheckListener;
	private SelectionAdapter primaryKeyCheckListener;
	private SelectionAdapter nullableCheckListener;
	private ISelectionListener selectionChangeListener;
	private ISelectionChangedListener dataTypeSelectionListener;
	private FocusListener referencedEntityFocusListener;
	private SelectionAdapter refernecedEntitySelectionListener;
	private FocusListener referencedPropertyFocusListener;
	private ComboViewer sqlTypeComboViewer;

	@Inject
	public DatabaseModelingView() {

	}

	@PostConstruct
	public void createPartControl(Composite parent) {
		onDispose(parent);

		scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		Composite container = new Composite(scrolledComposite, SWT.NONE);
		container.setLayout(new GridLayout(4, false));

		currentSelectionLabel = new Label(container, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1);
		gd_lblNewLabel.widthHint = 152;
		currentSelectionLabel.setLayoutData(gd_lblNewLabel);

		toolBar = new ToolBar(container, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		databaseChanger = new ToolItem(toolBar, SWT.DROP_DOWN);
		databaseChanger.setText("Database");
		databaseChanger.setEnabled(false);

		Label lblSqlType = new Label(container, SWT.NONE);
		lblSqlType.setText("SQL type");

		sqlTypeComboViewer = new ComboViewer(container, SWT.READ_ONLY);
		dataTypeCombo = sqlTypeComboViewer.getCombo();
		dataTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

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

		nullableCheck = new Button(container, SWT.CHECK);
		nullableCheck.setText("Nullable");

		uniqueCheck = new Button(container, SWT.CHECK);
		uniqueCheck.setText("Unique");

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

		dataTypeSelectionListener();
		primaryKeyCannotBeNullable();
		foreignKeyCheckBoxListener();
		setupSelectionListener();
		referenceEntityFocusListener();
		referenceEntitySelectionListener();
		referencePropertyFocusListener();

		viewModel = new PropertyEditingViewModelImpl(this);
		viewModel.isAttributeEditingEnabled(false);
		dataTypeCombo.setEnabled(false);

		dbSelectionListener = new DatabaseSelectionListener(this);
		databaseChanger.addSelectionListener(dbSelectionListener);

	}

	private void referencePropertyFocusListener() {
		referencedPropertyFocusListener = new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				String entityName = referencedEntity.getText();
				if (!entityName.isEmpty()) {
					viewModel.setupReferencePropertyCombo(entityName);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub

			}

		};
		referencedProperty.addFocusListener(referencedPropertyFocusListener);
	}

	private void referenceEntitySelectionListener() {
		refernecedEntitySelectionListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String entityName = referencedEntity.getText();
				if (!entityName.isEmpty()) {
					viewModel.setupReferencePropertyCombo(entityName);
				}
			}

		};
		referencedEntity.addSelectionListener(refernecedEntitySelectionListener);
	}

	private void referenceEntityFocusListener() {
		referencedEntityFocusListener = new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				viewModel.setupReferenceEntityCombo();
			}

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub

			}

		};
		referencedEntity.addFocusListener(referencedEntityFocusListener);
	}

	private void onDispose(Composite parent) {
		parent.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				foreignKeyCheck.removeSelectionListener(forignKeyCheckListener);
				primaryKeyCheck.removeSelectionListener(primaryKeyCheckListener);
				nullableCheck.removeSelectionListener(nullableCheckListener);
				databaseChanger.removeSelectionListener(dbSelectionListener);
				selectionService.removeSelectionListener(selectionChangeListener);
				referencedEntity.removeFocusListener(referencedEntityFocusListener);
				referencedEntity.removeSelectionListener(refernecedEntitySelectionListener);
				sqlTypeComboViewer.removeSelectionChangedListener(dataTypeSelectionListener);
			}
		});

	}

	private void dataTypeSelectionListener() {
		dataTypeSelectionListener = new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				if (selection.size() > 0) {
					DataTypeDefinition dtd = (DataTypeDefinition) selection.getFirstElement();
					viewModel.setupdataTypeInpuScheme(dtd);
				}
			}
		};

		sqlTypeComboViewer.addSelectionChangedListener(dataTypeSelectionListener);
	}

	private void setupSelectionListener() {
		selectionChangeListener = new ISelectionListener() {

			@Override
			public void selectionChanged(MPart part, Object selection) {
				if (selection instanceof StructuredSelection) {
					StructuredSelection sSelection = (StructuredSelection) selection;
					Object firstElement = sSelection.getFirstElement();
					if (firstElement instanceof IAdaptable) {
						NamedElement element = ((IAdaptable) firstElement).getAdapter(NamedElement.class);
						if (element instanceof Property) {
							viewModel.selectionChanged((Property) element);
							databaseChanger.setEnabled(true);
							dataTypeCombo.setEnabled(true);
						}
					}
				}
			}
		};
		selectionService.addSelectionListener(selectionChangeListener);
	}

	private void primaryKeyCannotBeNullable() {
		primaryKeyCheckListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (primaryKeyCheck.getSelection()) {
					nullableCheck.setSelection(false);
					nullableCheck.setEnabled(false);
					uniqueCheck.setSelection(true);
					primaryKeyConstraintName.setEnabled(true);
				} else {
					nullableCheck.setEnabled(true);
					primaryKeyConstraintName.setEnabled(false);
				}
			}
		};
		primaryKeyCheck.addSelectionListener(primaryKeyCheckListener);

		nullableCheckListener = new SelectionAdapter() {
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
		};
		nullableCheck.addSelectionListener(nullableCheckListener);
	}

	private void foreignKeyCheckBoxListener() {
		forignKeyCheckListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (foreignKeyCheck.getSelection()) {
					foreignKeyConstraintName.setEnabled(true);
					referencedEntity.setEnabled(true);
					referencedProperty.setEnabled(true);
					viewModel.setupReferenceEntityCombo();
				} else {
					referencedEntity.setEnabled(false);
					referencedProperty.setEnabled(false);
					foreignKeyConstraintName.setEnabled(false);
				}
			}
		};
		foreignKeyCheck.addSelectionListener(forignKeyCheckListener);
	}

	@Focus
	public void setFocus() {

	}

	@PreDestroy
	public void preDestroy() {

	}

	@Persist
	public void save() {
		viewModel.save();
	}

// GETTERS, SETTERS -----------------------------------------------------------------------------------------------
	public Text getLength() {
		return length;
	}

	public Text getPrecision() {
		return precision;
	}

	public Text getScale() {
		return scale;
	}

	public Text getDefaultValue() {
		return defaultValue;
	}

	public Text getPrimaryKeyConstraintName() {
		return primaryKeyConstraintName;
	}

	public Text getForeignKeyConstraintName() {
		return foreignKeyConstraintName;
	}

	public Combo getDataTypeCombo() {
		return dataTypeCombo;
	}

	public Combo getReferencedEntity() {
		return referencedEntity;
	}

	public Combo getReferencedProperty() {
		return referencedProperty;
	}

	public Button getNullableCheck() {
		return nullableCheck;
	}

	public Button getUniqueCheck() {
		return uniqueCheck;
	}

	public Button getPrimaryKeyCheck() {
		return primaryKeyCheck;
	}

	public Button getForeignKeyCheck() {
		return foreignKeyCheck;
	}

	public ScrolledComposite getScrolledComposite() {
		return scrolledComposite;
	}

	public Label getCurrentSelectionLabel() {
		return currentSelectionLabel;
	}

	public ToolBar getToolBar() {
		return toolBar;
	}

	public ToolItem getDatabaseChanger() {
		return databaseChanger;
	}

	public Property getCurrentPropertySelection() {
		return currentPropertySelection;
	}

	public void setCurrentPropertySelection(Property currentPropertySelection) {
		this.currentPropertySelection = currentPropertySelection;
	}

	public PropertyEditingViewModelImpl getViewModel() {
		return viewModel;
	}

	public ComboViewer getSqlTypeComboViewer() {
		return sqlTypeComboViewer;
	}

	public void setSqlTypeComboViewer(ComboViewer sqlTypeComboViewer) {
		this.sqlTypeComboViewer = sqlTypeComboViewer;
	}

}
