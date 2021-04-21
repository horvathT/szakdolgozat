package database.modeling.view;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.uml2.uml.Property;

import database.modeling.model.SqlDataModel;
import database.modeling.view.util.SelectionUtil;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class DatabaseModelingView extends ViewPart {
	public DatabaseModelingView() {
	}

	public static final String ID = "database.modeling.view.ModelingViewPart"; //$NON-NLS-1$
	
	private Text length;
	private Text precision;
	private Text scale;
	private Text defaultValue;
	private Text primaryKeyConstraintName;
	private Text foreignKeyConstraintName;
	
	private Combo sqlTypeCombo;
	private Combo referencedEntity;
	private Combo referencedProperty;
	
	private Button nullableCheck;
	private Button uniqueCheck;
	private Button autoIncrementCheck;
	private Button primaryKeyCheck;
	private Button foreignKeyCheck;
	
	private ISelectionListener listener;
	private Button btnSave;

	private ScrolledComposite scrolledComposite;
	
	private Label lblNewLabel;
	private ToolBar toolBar;
	private ToolItem databaseChanger;
	
	@Override
	public void createPartControl(Composite parent) {
		
		scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite container = new Composite(scrolledComposite, SWT.NONE);
		container.setLayout(new GridLayout(4, false));
		
		lblNewLabel = new Label(container, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 152;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("Not a valid selection");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
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
		
		btnSave = new Button(container, SWT.NONE);
		GridData gd_btnSave = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnSave.widthHint = 104;
		btnSave.setLayoutData(gd_btnSave);
		btnSave.setText("Save as Datatype");
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
		//lblReferencedAttribute.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblReferencedAttribute.setText("Referenced Attribute");
		
		ComboViewer referencedPropertyComboViewer = new ComboViewer(container, SWT.READ_ONLY);
		referencedProperty = referencedPropertyComboViewer.getCombo();
		referencedProperty.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		referencedProperty.setEnabled(false);
		
		foreignKeyCheckBoxListener(foreignKeyCheck, referencedEntity, referencedProperty);
		primaryKeyCannotBeNullable(primaryKeyCheck, nullableCheck);
		
		scrolledComposite.setContent(container);
		scrolledComposite.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
// AUTO-GEN STUFF
		createActions();
		// Uncomment if you wish to add code to initialize the toolbar
		//initializeToolBar();
		initializeMenu();
	}
	
	public void update(SqlDataModel model) {
		getNullableCheck().setSelection(model.isNullable());
		getUniqueCheck().setSelection(model.isUnique());
		getAutoIncrementCheck().setSelection(model.isAutoIncrement());
		getPrimaryKeyCheck().setSelection(model.isPrimaryKey());
		getForeignKeyCheck().setSelection(model.isForeignKey());
		
		//check if enabled
		getLength().setText(model.getLength());
		getPrecision().setText(model.getPrecision());
		getScale().setText(model.getScale());
		getDefaultValue().setText(model.getDefaultValue());
		getPrimaryKeyConstraintName().setText(model.getPrimaryKeyConstraintName());
		getForeignKeyConstraintName().setText(model.getForeignKeyConstraintName());
		
		getSqlTypeCombo().setText(model.getSqlType());
		//check if enabled
		getReferencedEntity().setText(model.getReferencedEntity());
		getReferencedProperty().setText(model.getReferencedProperty());
	}
	
	public SqlDataModel getData() {
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
		if(!isEmpty(length) || !isEmpty(precision) || !isEmpty(scale) || !isEmpty(defaultValue) ||
				!isEmpty(primaryKeyConstraintName) || !isEmpty(foreignKeyConstraintName)) {
			return false;
		}
		if(!isEmpty(sqlTypeCombo) || !isEmpty(referencedEntity) || !isEmpty(referencedProperty)) {
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
	
	private void primaryKeyCannotBeNullable(Button btnPrimarykey, Button btnNullable) {
		btnPrimarykey.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(btnPrimarykey.getSelection()) {
					btnNullable.setEnabled(false);
					primaryKeyConstraintName.setEnabled(true);
				}else {
					btnNullable.setEnabled(true);
					primaryKeyConstraintName.setEnabled(false);
				}
			}
			
		});
		
		btnNullable.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(btnNullable.getSelection()) {
					btnPrimarykey.setEnabled(false);
					primaryKeyConstraintName.setEnabled(false);
				}else {
					btnPrimarykey.setEnabled(true);
					primaryKeyConstraintName.setEnabled(true);
				}
			}
			
		});
	}

	private void foreignKeyCheckBoxListener(Button btnForeignKey, Combo referencedEntity, Combo referencedProperty) {
		btnForeignKey.addSelectionListener(new SelectionAdapter()
		{
		    @Override
		    public void widgetSelected(SelectionEvent e)
		    {
		        if (btnForeignKey.getSelection()) {
		        	foreignKeyConstraintName.setEnabled(true);
		        	referencedEntity.setEnabled(true);
		        	referencedProperty.setEnabled(true);
		        }else {
		        	referencedEntity.setEnabled(false);
		        	referencedProperty.setEnabled(false);
		        	foreignKeyConstraintName.setEnabled(false);
		        }
		    }
		});
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}
	
	public void dispose() {
		super.dispose();
	}

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

	public ISelectionListener getListener() {
		return listener;
	}

	public void setListener(ISelectionListener listener) {
		this.listener = listener;
	}

	public Button getBtnSave() {
		return btnSave;
	}

	public void setBtnSave(Button btnSave) {
		this.btnSave = btnSave;
	}

	public ScrolledComposite getScrolledComposite() {
		return scrolledComposite;
	}

	public void setScrolledComposite(ScrolledComposite scrolledComposite) {
		this.scrolledComposite = scrolledComposite;
	}

	public Label getLblNewLabel() {
		return lblNewLabel;
	}

	public void setLblNewLabel(Label lblNewLabel) {
		this.lblNewLabel = lblNewLabel;
	}

	public ToolBar getToolBar() {
		return toolBar;
	}

	public void setToolBar(ToolBar toolBar) {
		this.toolBar = toolBar;
	}

	public ToolItem getTltmDropdownItem() {
		return databaseChanger;
	}

	public void setTltmDropdownItem(ToolItem tltmDropdownItem) {
		this.databaseChanger = tltmDropdownItem;
	}

	public ToolItem getDatabaseChanger() {
		return databaseChanger;
	}

	public void setDatabaseChanger(ToolItem databaseChanger) {
		this.databaseChanger = databaseChanger;
	}
	
	

}
