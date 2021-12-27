package database.modeling.model;

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

import database.modeling.util.resource.EclipseModelUtil;
import database.modeling.util.resource.EclipseResourceUtil;
import database.modeling.util.stereotype.ColumnUtil;
import database.modeling.util.stereotype.DatabaseModelUtil;
import database.modeling.util.stereotype.StereotypeManagementUtil;
import database.modeling.view.DatabaseModelingView;

public class ModelConverter {

	private static final Bundle BUNDLE = FrameworkUtil.getBundle(ModelConverter.class);
	private static final ILog LOGGER = Platform.getLog(BUNDLE);

	private Model model;

	private DatabaseModelingView view;

	public ModelConverter(Model model, DatabaseModelingView view) {
		this.model = model;
		this.view = view;
	}

	public void writeModelToFile(String currentySelectedDb) {
		Collection<Property> propertiesFromModel = EclipseModelUtil.getPropertiesFromModel(model);
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

	private String constructFileName(String currentySelectedDb) {
		return model.getName() + "." + currentySelectedDb;
	}

	private String constructFilePath() {
		IPath modelFilePath = EclipseModelUtil.getModelFilePath(model);
		String filePath = modelFilePath.removeLastSegments(1).toOSString();
		return filePath;
	}

	private List<PropertyDataModel> convertToSqlProperties(Collection<Property> properties) {
		List<PropertyDataModel> sqlProperties = new ArrayList<>();
		for (Property property : properties) {
			if (StereotypeManagementUtil.hasStereotype(property, ColumnUtil.STEREOTYPE_QUALIFIED_NAME)) {
				sqlProperties.add(convertToSqlPropery(property));
			}
		}
		return sqlProperties;
	}

	private PropertyDataModel convertToSqlPropery(Property property) {
		PropertyDataModel sqlProperty = DataTransformer.propertyToSqlDataModel(property);
		String xmiId = EcoreUtil.getURI(property).fragment();
		sqlProperty.setXmiId(xmiId);
		return sqlProperty;
	}

	public void clearModel() {
		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(model);

		RecordingCommand command = new RecordingCommand(editingDomain) {
			@Override
			protected void doExecute() {

				Stereotype appliedStereotype = model.getAppliedStereotype(DatabaseModelUtil.STEREOTYPE_QUALIFIED_NAME);
				if (appliedStereotype != null) {
					model.unapplyStereotype(appliedStereotype);
				}

				Collection<Property> properties = EclipseModelUtil.getPropertiesFromModel(model);
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

	private void applyOnProperties(Collection<PropertyDataModel> propertyCollection) {
		for (PropertyDataModel sqlProperty : propertyCollection) {
			applyOnProperty(sqlProperty);
		}

	}

	private void applyOnProperty(PropertyDataModel sqlProperty) {
		String xmiId = sqlProperty.getXmiId();
		Property property = EclipseModelUtil.getPropertyByXmiId(xmiId, model);
		if (property != null) {
			DataTransformer.applyModelOnProperty(sqlProperty, property);
		}
	}

	private void applyOnModelPackage(String newlySelectedDbName) {
		DatabaseModelUtil.setDatabaseType(model, newlySelectedDbName);
	}

}
