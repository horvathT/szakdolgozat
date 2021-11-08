package database.modeling.model;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.uml2.uml.Property;

public interface PropertyEditingViewModel {

	public void selectionChanged(Property property);

	public void databaseChanged(String currentDbName, String newDbName, SelectionEvent event);

	public void save();

}
