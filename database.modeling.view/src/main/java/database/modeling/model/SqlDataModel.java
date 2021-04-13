package database.modeling.model;

import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Property;

public class SqlDataModel {
	
	private String length;
	private String precision;
	private String scale;
	private String defaultValue;
	private String primaryKeyConstraintName;
	private String foreignKeyConstraintName;
	private String sqlType;
	
	private Interface referencedEntity;
	private Property referencedProperty;
	
	private boolean nullable;
	private boolean unique;
	private boolean autoIncrement;
	private boolean primaryKey;
	private boolean foreignKey;
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getPrecision() {
		return precision;
	}
	public void setPrecision(String precision) {
		this.precision = precision;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getPrimaryKeyConstraintName() {
		return primaryKeyConstraintName;
	}
	public void setPrimaryKeyConstraintName(String primaryKeyConstraintName) {
		this.primaryKeyConstraintName = primaryKeyConstraintName;
	}
	public String getForeignKeyConstraintName() {
		return foreignKeyConstraintName;
	}
	public void setForeignKeyConstraintName(String foreignKeyConstraintName) {
		this.foreignKeyConstraintName = foreignKeyConstraintName;
	}
	public String getSqlType() {
		return sqlType;
	}
	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}
	public Interface getReferencedEntity() {
		return referencedEntity;
	}
	public void setReferencedEntity(Interface referencedEntity) {
		this.referencedEntity = referencedEntity;
	}
	public Property getReferencedProperty() {
		return referencedProperty;
	}
	public void setReferencedProperty(Property referencedProperty) {
		this.referencedProperty = referencedProperty;
	}
	public boolean isNullable() {
		return nullable;
	}
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	public boolean isUnique() {
		return unique;
	}
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	public boolean isAutoIncrement() {
		return autoIncrement;
	}
	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}
	public boolean isPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	public boolean isForeignKey() {
		return foreignKey;
	}
	public void setForeignKey(boolean foreignKey) {
		this.foreignKey = foreignKey;
	}
	
	

}
