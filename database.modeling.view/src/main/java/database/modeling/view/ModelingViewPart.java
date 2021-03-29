package database.modeling.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.viewers.ComboViewer;

public class ModelingViewPart extends ViewPart {
	public ModelingViewPart() {
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(4, false));
		
		Label lblSqlType = new Label(parent, SWT.NONE);
		lblSqlType.setText("SQL Type");
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		Button btnPrimaryKey = new Button(parent, SWT.CHECK);
		btnPrimaryKey.setText("Primary Key");
		
		Button btnCheckButton = new Button(parent, SWT.CHECK);
		btnCheckButton.setText("Check Button");
		
		Button btnCheckButton_1 = new Button(parent, SWT.CHECK);
		btnCheckButton_1.setText("Check Button");
		
		Button btnCheckButton_2 = new Button(parent, SWT.CHECK);
		btnCheckButton_2.setText("Check Button");
		
		Button btnCheckButton_3 = new Button(parent, SWT.CHECK);
		btnCheckButton_3.setText("Check Button");
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		Label lblNewLabel = new Label(parent, SWT.NONE);
		lblNewLabel.setText("Referenced Entity");
		
		ComboViewer comboViewer = new ComboViewer(parent, SWT.NONE);
		Combo combo_1 = comboViewer.getCombo();
		GridData gd_combo_1 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1);
		gd_combo_1.widthHint = 158;
		combo_1.setLayoutData(gd_combo_1);
		
		Label lblNewLabel_1 = new Label(parent, SWT.NONE);
		lblNewLabel_1.setText("Referenced Attribute");
		
		CCombo combo = new CCombo(parent, SWT.BORDER);
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo.widthHint = 93;
		combo.setLayoutData(gd_combo);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		Combo combo_2 = new Combo(parent, SWT.NONE);
		GridData gd_combo_2 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1);
		gd_combo_2.widthHint = 180;
		combo_2.setLayoutData(gd_combo_2);
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
