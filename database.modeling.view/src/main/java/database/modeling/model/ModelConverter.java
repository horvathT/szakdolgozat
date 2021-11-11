package database.modeling.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import database.modeling.util.resource.EclipseResourceUtil;
import database.modeling.util.stereotype.ColumnUtil;
import database.modeling.util.stereotype.StereotypeApplicationUtil;
import database.modeling.view.DatabaseModelingView;

public class ModelConverter {

	public static final Logger log = LoggerFactory.getLogger(ModelConverter.class);

	Model model;

	DatabaseModelingView view;

	public ModelConverter(Model model, DatabaseModelingView view) {
		this.model = model;
		this.view = view;
	}

	public void writeModelToFile() {
		Collection<Property> propertiesFromModel = getPropertiesFromModel(model);
		List<SQLProperty> sqlProperties = convertToSqlProperties(propertiesFromModel);
		String json = new Gson().toJson(sqlProperties);

		String filePath = constructFilePath();
		String fileName = constructFileName();

		try (InputStream targetStream = new ByteArrayInputStream(json.getBytes())) {
			EclipseResourceUtil.writeFile(filePath, fileName, targetStream);
			EclipseResourceUtil.refreshWorkspaceRoot();
		} catch (IOException e) {
			log.error("Failed to write file! (name: " + fileName + ", path: " + filePath, e);
		} catch (CoreException e) {
			log.error("Failed to write file! (name: " + fileName + ", path: " + filePath, e);
		}
	}

	private String constructFileName() {
		return model.getName() + "." + view.getDatabaseChanger().getText();
	}

	private String constructFilePath() {
		IPath modelFilePath = getModelFilePath();
		String filePath = modelFilePath.removeLastSegments(1).toOSString();
		return filePath;
	}

	private Collection<Property> getProperties(EList<Element> elementList) {
		return EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.PROPERTY);
	}

	private Collection<Property> getPropertiesFromModel(Model model) {
		return getProperties(model.allOwnedElements());
	}

	private IPath getModelFilePath() {
		return EclipseResourceUtil.getResourceFileLocationPath(model.eResource());
	}

	private List<SQLProperty> convertToSqlProperties(Collection<Property> properties) {
		List<SQLProperty> sqlProperties = new ArrayList<>();
		for (Property property : properties) {
			if (StereotypeApplicationUtil.hasStereotype(property, ColumnUtil.STEREOTYPE_QUALIFIED_NAME)) {
				sqlProperties.add(convertToSqlPropery(property));
			}
		}
		return sqlProperties;
	}

	private SQLProperty convertToSqlPropery(Property property) {
		SQLProperty sqlProperty = (SQLProperty) DataTransformer.propertyToSqlDataModel(property);
		String xmiId = EcoreUtil.getURI(property).fragment();
		sqlProperty.setXmiId(xmiId);
		return sqlProperty;
	}

}
