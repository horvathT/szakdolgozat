package database.modeling.model;

import java.util.List;

import org.eclipse.uml2.uml.Property;

public interface PropertyEditingViewModel {

	public void selectionChanged(Property property);

	public void databaseChanged(String currentDbName, String newDbName);

	public void save();

	public void updateDataInView(Property property);

	public void updateDatabaseChanger(String newDbName);

	public List<String> getDatabaseTypes();
}
