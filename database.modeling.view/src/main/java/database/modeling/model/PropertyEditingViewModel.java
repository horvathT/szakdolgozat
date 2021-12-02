package database.modeling.model;

import java.util.Set;

import org.eclipse.uml2.uml.Property;

public interface PropertyEditingViewModel {

	public void selectionChanged(Property property);

	public void changeDatabaseImplementation(String currentDbName, String newDbName);

	public void save();

	public void updateDataInView(Property property);

	public void updateDatabaseChanger(String newDbName);

	public Set<String> getDatabaseTypes();

	public void isAttributeEditingEnabled(boolean isEnabled);

	public void setupReferenceEntityCombo();

	public void setupReferencePropertyCombo(String entityName);
}
