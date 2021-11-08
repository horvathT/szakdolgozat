package database.modeling.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLPackage;

import com.google.gson.Gson;

import database.modeling.util.ColumnUtil;
import database.modeling.util.EclipseResourceUtil;
import database.modeling.view.DatabaseModelingView;

public class ModelConverter {

	Model model;

	DatabaseModelingView view;

	public ModelConverter(Model model, DatabaseModelingView view) {
		this.model = model;
		this.view = view;
	}

	public void convertModelToFile() {
		Collection<Property> propertiesFromModel = getPropertiesFromModel(model);
		// transform property to sql property
		List<SQLProperty> sqlProperties = convertToSqlProperties(propertiesFromModel);
		String json = new Gson().toJson(sqlProperties);
		System.out.println("");
	}

	private Collection<Property> getProperties(EList<Element> elementList) {
		return EcoreUtil.getObjectsByType(elementList, UMLPackage.Literals.PROPERTY);
	}

	private Collection<Property> getPropertiesFromModel(Model model) {
		return getProperties(model.allOwnedElements());
	}

	private String getModelFilePath() {
		return EclipseResourceUtil.getResourceFileLocation(model.eResource());
	}

	private List<SQLProperty> convertToSqlProperties(Collection<Property> properties) {
		List<SQLProperty> sqlProperties = new ArrayList<>();
		for (Property property : properties) {
			if (ColumnUtil.hasStereotype(property)) {
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
