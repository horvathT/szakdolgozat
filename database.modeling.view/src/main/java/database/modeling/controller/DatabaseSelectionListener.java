package database.modeling.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;

import database.modeling.view.util.DatabaseTypesUtil;

public class DatabaseSelectionListener extends SelectionAdapter {
	private ToolItem dropdown;
	private Menu menu;
	private Combo combo;
	private DatabaseTypesUtil dbUtil = new DatabaseTypesUtil();

	public DatabaseSelectionListener(ToolItem dropdown, Combo typeCombo) {
		this.dropdown = dropdown;
		this.combo = typeCombo;
		menu = new Menu(dropdown.getParent().getShell());
	}

	public void add(Collection<String> items) {
		for (String item : items) {
			add(item);
		}
	}

	public void add(String item) {
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText(item);
		menuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				MenuItem selected = (MenuItem) event.widget;
				String text = selected.getText();
				dropdown.setText(text);
				Map<String, String[]> dbMap = dbUtil.getDatabaseTypeMap();
				combo.setItems(dbMap.get(text));
			}
		});
	}

	public void widgetSelected(SelectionEvent event) {
		if (event.detail == SWT.ARROW) {
			ToolItem item = (ToolItem) event.widget;
			Rectangle rect = item.getBounds();
			Point pt = item.getParent().toDisplay(new Point(rect.x, rect.y));
			menu.setLocation(pt.x, pt.y + rect.height);
			menu.setVisible(true);
		} 
	}

	public void init() {
		List<String> databases = dbUtil.getDatabases();
		add(databases);
		String deafultDb = databases.get(0);
		dropdown.setText(deafultDb);
		combo.setItems(dbUtil.getDatabaseTypeMap().get(deafultDb));
	}
}