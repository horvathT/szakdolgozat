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
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import database.modeling.util.resource.EclipseResourceUtil;
import database.modeling.util.stereotype.ColumnUtil;
import database.modeling.util.stereotype.DatabaseModelUtil;
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

	public void writeModelToFile(String currentySelectedDb) {
		Collection<Property> propertiesFromModel = getPropertiesFromModel(model);
		List<SQLProperty> sqlProperties = convertToSqlProperties(propertiesFromModel);
		String json = new Gson().toJson(sqlProperties);

		String filePath = constructFilePath();
		String fileName = constructFileName(currentySelectedDb);

		try (InputStream targetStream = new ByteArrayInputStream(json.getBytes())) {
			EclipseResourceUtil.writeFile(filePath, fileName, targetStream);
			EclipseResourceUtil.refreshWorkspaceRoot();
		} catch (IOException e) {
			log.error("Failed to write file! (name: " + fileName + ", path: " + filePath, e);
		} catch (CoreException e) {
			log.error("Failed to write file! (name: " + fileName + ", path: " + filePath, e);
		}
	}

	private String constructFileName(String currentySelectedDb) {
		return model.getName() + "." + currentySelectedDb;
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

	public void clearModel() {
		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(model);

		RecordingCommand command = new RecordingCommand(editingDomain) {
			@Override
			protected void doExecute() {

				Stereotype appliedStereotype = model.getAppliedStereotype(DatabaseModelUtil.STEREOTYPE_QUALIFIED_NAME);
				if (appliedStereotype != null) {
					model.unapplyStereotype(appliedStereotype);
				}

				Collection<Property> properties = getPropertiesFromModel(model);
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
//		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(model);
//		RecordingCommand command = new RecordingCommand(editingDomain) {
//			@Override
//			protected void doExecute() {
//
//				String filePath = constructFilePath();
//				String fileName = constructFileName(newlySelectedDbName);
//				Path path = Path.of(filePath, fileName);
//
//				if (Files.exists(path)) {
//					String fileString = null;
//					try {
//						fileString = Files.readString(path);
//					} catch (IOException e) {
//						log.error("Error reading file from path: " + path);
//						e.printStackTrace();
//					}
//					Type targetClassType = new TypeToken<ArrayList<SQLProperty>>() {
//					}.getType();
//					Collection<SQLProperty> propertyCollection = new Gson().fromJson(fileString, targetClassType);
//				}
//
//			}
//		};
//		editingDomain.getCommandStack().execute(command);
	}

}
