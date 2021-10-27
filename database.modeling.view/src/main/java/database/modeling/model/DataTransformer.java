package database.modeling.model;

import org.eclipse.uml2.uml.Property;

import database.modeling.util.ColumnUtil;
import database.modeling.util.FKUtil;
import database.modeling.util.PKUtil;
import database.modeling.util.ProfileUtil;

public class DataTransformer {

	public static SqlDataModel propertyToSqlDataModel(Property property, SqlDataModel dataModel) {
		if (ColumnUtil.hasStereotype(property)) {
			// dataType
			dataModel.setSqlType(ColumnUtil.getDataType(property));
			// defaultValue
			dataModel.setDefaultValue(ColumnUtil.getDefaultValue(property));
			// length
			dataModel.setLength(ColumnUtil.getLength(property));
			// precision
			dataModel.setPrecision(ColumnUtil.getPrecision(property));
			// scale
			dataModel.setScale(ColumnUtil.getScale(property));
			// nullable
			// unique
			// auto increment
		} else {
			dataModel.setSqlType("");
			dataModel.setDefaultValue("");
			dataModel.setLength("");
			dataModel.setPrecision("");
			dataModel.setScale("");
			dataModel.setNullable(false);
			dataModel.setUnique(false);
			dataModel.setAutoIncrement(false);
		}
		if (PKUtil.hasStereotype(property)) {
			// primarykey
			dataModel.setPrimaryKey(true);
			// pk constraint
			dataModel.setPrimaryKeyConstraintName(PKUtil.getConstraintName(property));
		} else {
			dataModel.setPrimaryKey(false);
			dataModel.setPrimaryKeyConstraintName("");
		}
		if (FKUtil.hasStereotype(property)) {
			// fk
			dataModel.setForeignKey(true);
			// fk constraint
			dataModel.setForeignKeyConstraintName(FKUtil.getConstraintName(property));
			// referenced entity, property
			dataModel.setReferencedEntity(FKUtil.getReferencedEntity(property));
			dataModel.setReferencedProperty(FKUtil.getReferencedProperty(property));

		} else {
			dataModel.setForeignKey(false);
			dataModel.setForeignKeyConstraintName("");
			dataModel.setReferencedEntity("");
			dataModel.setReferencedProperty("");
		}

		return dataModel;
	}

	public static Property applyModelOnProperty(SqlDataModel dataModel, Property property) {
		ProfileUtil.applyPofile(property);
		ColumnUtil.applyStereotype(property);
		ColumnUtil.setDataType(property, dataModel.getSqlType());
		ColumnUtil.setDefaultValue(property, dataModel.getDefaultValue());
		ColumnUtil.setLength(property, dataModel.getLength());
		ColumnUtil.setPrecision(property, dataModel.getPrecision());
		ColumnUtil.setScale(property, dataModel.getScale());

		if (dataModel.isPrimaryKey()) {
			PKUtil.applyStereotype(property);
			PKUtil.setConstraintName(property, dataModel.getPrimaryKeyConstraintName());
		}
		if (dataModel.isForeignKey()) {
			FKUtil.applyStereotype(property);
			FKUtil.setConstraintName(property, dataModel.getForeignKeyConstraintName());
			FKUtil.setReferencedEntity(property, dataModel.getReferencedEntity());
			FKUtil.setReferencedProperty(property, dataModel.getReferencedProperty());
		}

		return property;
	}

}
