package database.modeling.handler;

import java.util.Collection;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;

import database.modeling.util.DatabaseTypesUtil;
import database.modeling.view.DatabaseModelingView;
import database.modeling.viewmodel.PropertyEditingViewModelImpl;

/**
 * A SQL implementáció váltását kezelő osztály.
 * 
 * @author Horváth Tibor
 *
 */
public class DatabaseSelectionListener extends SelectionAdapter {
	private ToolItem dropdown;
	private Menu menu;

	PropertyEditingViewModelImpl viewModel;

	private String currentDbSelected;

	public DatabaseSelectionListener(DatabaseModelingView view) {
		this.viewModel = view.getViewModel();
		this.dropdown = view.getDatabaseChanger();

		menu = new Menu(dropdown.getParent().getShell());

		Set<String> databases = viewModel.getDatabaseTypes();
		add(databases);

		currentDbSelected = DatabaseTypesUtil.DEFAULT_DB;
		viewModel.updateDatabaseChanger(currentDbSelected);
	}

	public void add(Collection<String> items) {
		for (String item : items) {
			add(item);
		}
	}

	/**
	 * Elem hozzáadása a legördülő menühöz.
	 * 
	 * @param item menüpont felirata
	 */
	public void add(String item) {
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText(item);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				MenuItem selected = (MenuItem) event.widget;
				String newDbName = selected.getText();

				viewModel.save();
				viewModel.updateDatabaseChanger(newDbName);
				viewModel.changeDatabaseImplementation(currentDbSelected, newDbName);

			}
		});
	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		if (event.detail == SWT.ARROW) {
			ToolItem item = (ToolItem) event.widget;
			Rectangle rect = item.getBounds();
			Point pt = item.getParent().toDisplay(new Point(rect.x, rect.y));
			menu.setLocation(pt.x, pt.y + rect.height);
			menu.setVisible(true);

			setCurrentlySelectedDb();
		}
	}

	private void setCurrentlySelectedDb() {
		currentDbSelected = dropdown.getText();
	}

}