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

public class ModelingViewPart extends ViewPart {

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
	
	private Property latestValidSelection = null;

	private ISelectionListener listener = new ISelectionListener() {
		public void selectionChanged(IWorkbenchPart sourcepart, ISelection selection) {
			//ignore our own selections
			if (sourcepart != ModelingViewPart.this) {
				save();
				
				if(SelectionUtil.isPropertyFromModelEditor(selection)) {
					
					updateLatestValidSelectionFromDiagramEditor(selection);
					updateSelectionFromDiagramEditor(selection);
					
				}else if(SelectionUtil.isPropertyFromModelExplorer(selection)) {
					
					updateSelectionFromModelExplorer(selection);
					updateLatestValidSelectionFromModelExplorer(selection);
					
				}else {
					setContentDescription("Nothing to show");
				}
			}
		}
	};

	private Button nullableCheck;

	private Button uniqueCheck;

	private Button autoIncrementCheck;

	private Button primaryKeyCheck;
	
	protected void updateSelectionFromDiagramEditor(ISelection selection) {
		setContentDescription(SelectionUtil.getPropertyFromModelEditor(selection).getName());
	}

	protected void save() {
		// TODO Auto-generated method stub
		
	}

	protected void updateLatestValidSelectionFromDiagramEditor(ISelection selection) {
		latestValidSelection = SelectionUtil.getPropertyFromModelEditor(selection);
	}

	protected void updateSelectionFromModelExplorer(ISelection selection) {
		setContentDescription(SelectionUtil.getPropertyFromModelExplorer(selection).getName());
	}
	
	protected void updateLatestValidSelectionFromModelExplorer(ISelection selection) {
		latestValidSelection = SelectionUtil.getPropertyFromModelExplorer(selection);
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite container = new Composite(scrolledComposite, SWT.NONE);
		container.setLayout(new GridLayout(4, false));
		
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
		
		nullableCheck = new Button(container, SWT.CHECK);
		nullableCheck.setText("Nullable");
		
		uniqueCheck = new Button(container, SWT.CHECK);
		uniqueCheck.setText("Unique");
		
		autoIncrementCheck = new Button(container, SWT.CHECK);
		autoIncrementCheck.setText("Auto increment");
		new Label(container, SWT.NONE);
		
		primaryKeyCheck = new Button(container, SWT.CHECK);
		primaryKeyCheck.setText("Primary Key");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Label lblPrimaryKeyConstraint = new Label(container, SWT.NONE);
		lblPrimaryKeyConstraint.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPrimaryKeyConstraint.setText("Primary Key Constraint Name");
		
		primaryKeyConstraintName = new Text(container, SWT.BORDER);
		primaryKeyConstraintName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		primaryKeyConstraintName.setEnabled(false);
		
		Button btnForeignKey = new Button(container, SWT.CHECK);
		btnForeignKey.setText("Foreign Key");
		
		// set btnForeignKey to take up 4 cells horizontally
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
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
		
		foreignKeyCheckBoxListener(btnForeignKey, referencedEntity, referencedProperty);
		primaryKeyCannotBeNullable(primaryKeyCheck, nullableCheck);
		scrolledComposite.setContent(container);
		scrolledComposite.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(listener);
		
// AUTO-GEN STUFF
		createActions();
		// Uncomment if you wish to add code to initialize the toolbar
		//initializeToolBar();
		initializeMenu();
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
		getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(listener);
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

	public Property getLatestValidSelection() {
		return latestValidSelection;
	}

	public void setLatestValidSelection(Property latestValidSelection) {
		this.latestValidSelection = latestValidSelection;
	}
	
	
}
