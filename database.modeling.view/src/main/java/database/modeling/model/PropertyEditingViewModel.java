package database.modeling.model;

import org.eclipse.uml2.uml.Property;

public interface PropertyEditingViewModel {

	public void selectionChanged(Property property);

	public void databaseChanged(String currentDbName, String newDbName);

	public void save();

	public void updateDataInView(Property property);
}
