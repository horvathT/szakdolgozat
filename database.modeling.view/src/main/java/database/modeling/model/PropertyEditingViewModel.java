package database.modeling.model;

import java.util.Set;

import org.eclipse.uml2.uml.Property;

/**
 * A View és a Model között közvetít az adatok aktuális állapotát.
 * 
 * @author Horváth Tibor
 *
 */
public interface PropertyEditingViewModel {

	/**
	 * A View-n történő érvényes kiejölt elem váltást kezeli.
	 * 
	 * @param property
	 */
	public void selectionChanged(Property property);

	/**
	 * SQL implementáció váltást hajt végre.
	 * 
	 * @param currentDbName Az eddig használt SQL implementáció neve
	 * @param newDbName     Az új SQL implementáció neve
	 */
	public void changeDatabaseImplementation(String currentDbName, String newDbName);

	/**
	 * A kijelölt {@link Property} mentése.
	 */
	public void save();

	/**
	 * A view adatainak frissítése a modell alapján.
	 * 
	 * @param property
	 */
	public void updateDataInView(Property property);

	/**
	 * SQL implementáció váltó legördülő menü frissítése.
	 * 
	 * @param newDbName
	 */
	public void updateDatabaseChanger(String newDbName);

	/**
	 * SQL implementációk neveit adja vissza.
	 * 
	 * @return
	 */
	public Set<String> getDatabaseTypes();

	/**
	 * Az SQL adattípus attribútum mezők szerkesztésének tiltása/engedélyezése a
	 * kapott paraméter értéke alapján.
	 * 
	 * @param isEnabled
	 */
	public void attributeEditingEnabled(boolean isEnabled);

	/**
	 * Referencia entitás választó menü feltöltése értékekkel.
	 */
	public void setupReferenceEntityCombo();

	/**
	 * Referencia attribútum választó menü feltöltése értékekkel a referált entity
	 * alapján.
	 * 
	 * @param entityName A referált entitás neve.
	 */
	public void setupReferencePropertyCombo(String entityName);
}
