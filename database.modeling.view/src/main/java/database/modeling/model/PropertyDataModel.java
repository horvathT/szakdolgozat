package database.modeling.model;

/**
 * A {@link Property}-n alkalmazható sztereotípusok és adataikat reprezentáló
 * modell objektum.
 * 
 * @author Horváth Tibor
 *
 */
public class PropertyDataModel {

	private String xmiId;

	private String length;
	private String precision;
	private String scale;
	private String defaultValue;
	private DataTypeDefinition sqlType;

	private String referencedEntity;
	private String referencedProperty;

	private boolean nullable;
	private boolean unique;
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

	public DataTypeDefinition getSqlType() {
		return sqlType;
	}

	public void setSqlType(DataTypeDefinition sqlType) {
		this.sqlType = sqlType;
	}

	public String getReferencedEntity() {
		return referencedEntity;
	}

	public void setReferencedEntity(String referencedEntity) {
		this.referencedEntity = referencedEntity;
	}

	public String getReferencedProperty() {
		return referencedProperty;
	}

	public void setReferencedProperty(String referencedProperty) {
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

	public String getXmiId() {
		return xmiId;
	}

	public void setXmiId(String xmiId) {
		this.xmiId = xmiId;
	}

}
