package database.modeling.view;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.part.ViewPart;

public class DatabaseModelingView {

	public DatabaseModelingView() {
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(4, false));
		
		ToolBar toolBar = new ToolBar(parent, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		
		ToolItem tltmOpen = new ToolItem(toolBar, SWT.NONE);
		tltmOpen.setText("Open");
		
		ToolItem tltmLanguage = new ToolItem(toolBar, SWT.NONE);
		tltmLanguage.setText("Language");
		
		ToolItem tltmGenerateSql = new ToolItem(toolBar, SWT.NONE);
		tltmGenerateSql.setText("Generate SQL...");
		
		Label lblSqlType = new Label(parent, SWT.NONE);
		lblSqlType.setText("SQL type");
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		Label lblLength = new Label(parent, SWT.NONE);
		lblLength.setText("Length");
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		Label lblDefaultValue = new Label(parent, SWT.NONE);
		lblDefaultValue.setText("Default Value");
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		Button btnPrimaryKey = new Button(parent, SWT.CHECK);
		btnPrimaryKey.setText("Primary Key");
		
		Button btnNullable = new Button(parent, SWT.CHECK);
		btnNullable.setText("Nullable");
		
		Button btnUnique = new Button(parent, SWT.CHECK);
		btnUnique.setText("Unique");
		
		Button btnAutoIncrement = new Button(parent, SWT.CHECK);
		btnAutoIncrement.setText("Auto Increment");
		
		Button btnForeignkey = new Button(parent, SWT.CHECK);
		btnForeignkey.setText("ForeignKey");
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		Label lblReferencedEntity = new Label(parent, SWT.NONE);
		lblReferencedEntity.setText("Referenced Entity");
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		Label lblReferencedAttribute = new Label(parent, SWT.NONE);
		lblReferencedAttribute.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblReferencedAttribute.setText("Referenced Attribute");
		
		Combo combo = new Combo(parent, SWT.NONE);
		combo.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));
		new Label(parent, SWT.NONE);
		
		Label label = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		
		Label lblComment = new Label(parent, SWT.NONE);
		lblComment.setText("Comment");
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
		
		TabItem tbtmSql = new TabItem(tabFolder, SWT.NONE);
		tbtmSql.setText("SQL");
		
		TabItem tbtmComment = new TabItem(tabFolder, SWT.NONE);
		tbtmComment.setText("Comment");
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}

}
