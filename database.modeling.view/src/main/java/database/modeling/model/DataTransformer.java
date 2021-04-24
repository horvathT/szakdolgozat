package database.modeling.model;

import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;

import database.modeling.view.util.ColumnUtil;

public class DataTransformer {

	public SqlDataModel propertyToSqlDataModel(Property property) {
		SqlDataModel dataModel = new SqlDataModel();
		if (ColumnUtil.hasStereotype(property)) {
			// dataType
			// defaultValue
			// length
			// precision
			// scale
			// nullable
			// unique
			// auto increment

		}
//		if (PKUtil.hasStereotype(property)) {
//			//primarykey
//			//pk constraint
//		}
//		if (FKUtil.hasStereotype(property)) {
//			//fk
//			//fk constraint
//			//referenced entity, property
//		}

		return dataModel;
	}

	public Property SqlDataModelToProperty(SqlDataModel dataModel) {
		Property property = UMLFactory.eINSTANCE.createProperty();
		if (dataModel.isPrimaryKey()) {

		}
		if (dataModel.isForeignKey()) {

		}

		return property;
	}

}
