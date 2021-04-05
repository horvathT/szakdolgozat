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

import database.modeling.view.util.SelectionUtil;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;

public class ModelingViewPart extends ViewPart {
	public ModelingViewPart() {
	}

	public static final String ID = "database.modeling.view.ModelingViewPart"; //$NON-NLS-1$
	private Text length;
	private Text precision;
	private Text scale;
	private Text defaultValue;

	private ISelectionListener listener = new ISelectionListener() {
		public void selectionChanged(IWorkbenchPart sourcepart, ISelection selection) {
			//ignore our own selections
			if (sourcepart != ModelingViewPart.this) {
				if(SelectionUtil.isPropertyFromModelEditor(selection)) {
					updateSelectionFromDiagramEditor(selection);
				}else if(SelectionUtil.isProperty(selection)) {
					updateSelectionFromModelExplorer(selection);
				}else {
					setContentDescription("Incorrect element");
				}
			}
		}
	};
	private Text primaryKeyConstraintName;
	private Text foreignKeyConstraintName;
	
	protected void updateSelectionFromDiagramEditor(ISelection selection) {
		setContentDescription(SelectionUtil.getPropertyFromModelEditor(selection).getName());
	}

	protected void updateSelectionFromModelExplorer(ISelection selection) {
		setContentDescription(SelectionUtil.getProperty(selection).getName());
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(4, false));
		
		Label lblSqlType = new Label(container, SWT.NONE);
		lblSqlType.setText("SQL type");
		
		ComboViewer comboViewer_2 = new ComboViewer(container, SWT.NONE);
		Combo combo_2 = comboViewer_2.getCombo();
		combo_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
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
		
		Button btnNullable = new Button(container, SWT.CHECK);
		btnNullable.setText("Nullable");
		
		Button btnUnique = new Button(container, SWT.CHECK);
		btnUnique.setText("Unique");
		
		Button btnAutoIncrement = new Button(container, SWT.CHECK);
		btnAutoIncrement.setText("Auto increment");
		new Label(container, SWT.NONE);
		
		Button btnPrimarykey = new Button(container, SWT.CHECK);
		btnPrimarykey.setText("Primary Key");
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
		
		ComboViewer comboViewer = new ComboViewer(container, SWT.NONE);
		Combo referencedEntity = comboViewer.getCombo();
		referencedEntity.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		referencedEntity.setEnabled(false);
		
		
		Label lblReferencedAttribute = new Label(container, SWT.NONE);
		//lblReferencedAttribute.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblReferencedAttribute.setText("Referenced Attribute");
		
		ComboViewer comboViewer_1 = new ComboViewer(container, SWT.NONE);
		Combo referencedProperty = comboViewer_1.getCombo();
		referencedProperty.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		referencedProperty.setEnabled(false);
		
		foreignKeyCheckBoxListener(btnForeignKey, referencedEntity, referencedProperty);
		primaryKeyCannotBeNullable(btnPrimarykey, btnNullable);

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
}
