package database.modeling.viewmodel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import database.modeling.util.resource.EclipseResourceUtil;
import database.modeling.util.stereotype.ColumnUtil;
import database.modeling.util.stereotype.DatabaseModelUtil;
import database.modeling.util.stereotype.StereotypeManagementUtil;
import database.modeling.util.uml.ModelObjectUtil;

/**
 * A modell és az elmentett fájlok közötti kétirányú konverziót kezelő osztály.
 * 
 * @author Horváth Tibor
 *
 */
public class ModelConverter {

	private static final Bundle BUNDLE = FrameworkUtil.getBundle(ModelConverter.class);
	private static final ILog LOGGER = Platform.getLog(BUNDLE);

	private Model model;

	public ModelConverter(Model model) {
		this.model = model;
	}

	/**
	 * A modell összes {@link Property} objektunát összegyűjti. A
	 * {@link DataTransformer} felhasználásával {@link PropertyDataModel}
	 * objetumokat hoz létre, amelyeket ezután a Gson szerizálóval szöveges formává
	 * alakít, és fájlba kiír. A céjfájl nevét a jelenleg kiválasztott SQL
	 * implementáció nevéből képezzük.
	 * 
	 * @param currentySelectedDb A jelenleg kiválasztott SQL implementáció neve
	 */
	public void writeModelToFile(String currentySelectedDb) {
		Collection<Property> propertiesFromModel = ModelObjectUtil.getPropertiesFromModel(model);
		List<PropertyDataModel> sqlProperties = convertToSqlProperties(propertiesFromModel);
		if (!sqlProperties.isEmpty()) {
			String json = new Gson().toJson(sqlProperties);

			String filePath = constructFilePath();
			String fileName = constructFileName(currentySelectedDb);

			try (InputStream targetStream = new ByteArrayInputStream(json.getBytes())) {
				EclipseResourceUtil.writeFile(filePath, fileName, targetStream);
				EclipseResourceUtil.refreshWorkspaceRoot();
			} catch (IOException e) {
				LOGGER.error("Failed to write file! (name: " + fileName + ", path: " + filePath, e);
			} catch (CoreException e) {
				LOGGER.error("Failed to write file! (name: " + fileName + ", path: " + filePath, e);
			}
		}
	}

	/**
	 * Összeállítja a a féjl nevét.
	 * 
	 * @param currentySelectedDb
	 * @return
	 */
	private String constructFileName(String currentySelectedDb) {
		return model.getName() + "." + currentySelectedDb;
	}

	/**
	 * Visszaadja a célkönyvtár elérési útvonlát.
	 * 
	 * @return
	 */
	private String constructFilePath() {
		IPath modelFilePath = ModelObjectUtil.getModelFilePath(model);
		String filePath = modelFilePath.removeLastSegments(1).toOSString();
		return filePath;
	}

	/**
	 * {@link Property}-ket {@link PropertyDataModel}-ekké konvertálja.
	 * 
	 * @param properties
	 * @return
	 */
	private List<PropertyDataModel> convertToSqlProperties(Collection<Property> properties) {
		List<PropertyDataModel> sqlProperties = new ArrayList<>();
		for (Property property : properties) {
			if (StereotypeManagementUtil.hasStereotype(property, ColumnUtil.STEREOTYPE_QUALIFIED_NAME)) {
				sqlProperties.add(convertToSqlPropery(property));
			}
		}
		return sqlProperties;
	}

	/**
	 * {@link Property}-t {@link PropertyDataModel}-é konvertálja.
	 * 
	 * @param properties
	 * @return
	 */
	private PropertyDataModel convertToSqlPropery(Property property) {
		PropertyDataModel sqlProperty = DataTransformer.propertyToSqlDataModel(property);
		String xmiId = EcoreUtil.getURI(property).fragment();
		sqlProperty.setXmiId(xmiId);
		return sqlProperty;
	}

	/**
	 * Minden sztereotípust eltávolít az aktív modellről.
	 */
	public void clearModel() {
		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(model);

		RecordingCommand command = new RecordingCommand(editingDomain) {
			@Override
			protected void doExecute() {

				Stereotype appliedStereotype = model.getAppliedStereotype(DatabaseModelUtil.STEREOTYPE_QUALIFIED_NAME);
				if (appliedStereotype != null) {
					model.unapplyStereotype(appliedStereotype);
				}

				Collection<Property> properties = ModelObjectUtil.getPropertiesFromModel(model);
				for (Property property : properties) {
					EList<Stereotype> appliedStereotypes = property.getAppliedStereotypes();
					for (Stereotype stereotype : appliedStereotypes) {
						property.unapplyStereotype(stereotype);
					}
				}
			}
		};

		editingDomain.getCommandStack().execute(command);
	}

	/**
	 * A paraméterben kapott SQL implementáció név alapján megkeresi a hozzá tartozó
	 * fájlt, és ha létezik annak tartalmát alkalmazza modellen.
	 * 
	 * @param newlySelectedDbName
	 */
	public void applyFileOnModel(String newlySelectedDbName) {
		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(model);
		RecordingCommand command = new RecordingCommand(editingDomain) {
			@Override
			protected void doExecute() {

				String filePath = constructFilePath();
				String fileName = constructFileName(newlySelectedDbName);
				Path path = Path.of(filePath, fileName);

				if (Files.exists(path)) {
					String fileString = null;
					try {
						fileString = Files.readString(path);
					} catch (IOException e) {
						LOGGER.error("Error reading file from path: " + path);
						e.printStackTrace();
					}
					Type targetClassType = new TypeToken<ArrayList<PropertyDataModel>>() {
					}.getType();

					Collection<PropertyDataModel> propertyCollection = new Gson().fromJson(fileString, targetClassType);
					applyOnProperties(propertyCollection);
					applyOnModelPackage(newlySelectedDbName);
				}
				applyOnModelPackage(newlySelectedDbName);

			}

		};
		editingDomain.getCommandStack().execute(command);
	}

	/**
	 * {@link PropertyDataModel} objektumokat sztereotípusokká konvertálja és
	 * alkalmazza a megfelelő {@link Property} objektumokon.
	 * 
	 * @param propertyCollection
	 */
	private void applyOnProperties(Collection<PropertyDataModel> propertyCollection) {
		for (PropertyDataModel sqlProperty : propertyCollection) {
			applyOnProperty(sqlProperty);
		}

	}

	/**
	 * {@link PropertyDataModel} objektumot sztereotípusokká konvertálja és
	 * alkalmazza a megfelelő {@link Property} objektumon.
	 * 
	 * @param propertyCollection
	 */
	private void applyOnProperty(PropertyDataModel sqlProperty) {
		String xmiId = sqlProperty.getXmiId();
		Property property = ModelObjectUtil.getPropertyByXmiId(xmiId, model);
		if (property != null) {
			DataTransformer.applyModelOnProperty(sqlProperty, property);
		}
	}

	/**
	 * {@link DatabaseModel} sztereotípust alkalmazza model csomagon.
	 * 
	 * @param newlySelectedDbName
	 */
	private void applyOnModelPackage(String newlySelectedDbName) {
		DatabaseModelUtil.setDatabaseType(model, newlySelectedDbName);
	}

}
