package database.modeling.viewmodel;

/**
 * SQL adattípusokat definiáló modell objektum.
 * 
 * @author Horváth Tibor
 *
 */
public class DataTypeDefinition {

	private String name;

	private boolean hasLength;
	private int lengthUpperBound;
	private int lengthLowerBound;

	private boolean hasScale;
	private int scaleUpperBound;
	private int scaleLowerBound;

	private boolean hasPrecision;
	private int precisionUpperBound;
	private int precisionLowerBound;

	private boolean hasDefaulValue;

	private long numericDefaultLowerBound;
	private long numericDefaultUpperBound;

	private InputType type;

	public static DataTypeDefinition of() {
		DataTypeDefinition dtd = new DataTypeDefinition();
		dtd.hasLength = false;
		dtd.hasScale = false;
		dtd.hasPrecision = false;
		dtd.hasDefaulValue(true);
		return dtd;
	}

	public String getName() {
		return name;
	}

	public DataTypeDefinition name(String name) {
		this.name = name;
		return this;
	}

	public boolean hasLength() {
		return hasLength;
	}

	public int getLengthUpperBound() {
		return lengthUpperBound;
	}

	public DataTypeDefinition lengthUpperBound(int lengthUpperBound) {
		this.lengthUpperBound = lengthUpperBound;
		this.hasLength = true;
		return this;
	}

	public int getLengthLowerBound() {
		return lengthLowerBound;
	}

	public DataTypeDefinition lengthLowerBound(int lengthLowerBound) {
		this.lengthLowerBound = lengthLowerBound;
		this.hasLength = true;
		return this;
	}

	public boolean hasScale() {
		return hasScale;
	}

	public int getScaleUpperBound() {
		return scaleUpperBound;
	}

	public DataTypeDefinition scaleUpperBound(int scaleUpperBound) {
		this.scaleUpperBound = scaleUpperBound;
		this.hasScale = true;
		return this;
	}

	public int getScaleLowerBound() {
		return scaleLowerBound;
	}

	public DataTypeDefinition scaleLowerBound(int scaleLowerBound) {
		this.scaleLowerBound = scaleLowerBound;
		this.hasScale = true;
		return this;
	}

	public boolean hasPrecision() {
		return hasPrecision;
	}

	public int getPrecisionUpperBound() {
		return precisionUpperBound;
	}

	public DataTypeDefinition precisionUpperBound(int precisionUpperBound) {
		this.precisionUpperBound = precisionUpperBound;
		this.hasPrecision = true;
		return this;
	}

	public int getPrecisionLowerBound() {
		return precisionLowerBound;
	}

	public DataTypeDefinition precisionLowerBound(int precisionLowerBound) {
		this.precisionLowerBound = precisionLowerBound;
		this.hasPrecision = true;
		return this;
	}

	public boolean hasDefaulValue() {
		return hasDefaulValue;
	}

	public Long getNumericDefaultLowerBound() {
		return numericDefaultLowerBound;
	}

	public DataTypeDefinition numericDefaultLowerBound(long numericDefaultLowerBound) {
		this.numericDefaultLowerBound = numericDefaultLowerBound;
		return this;
	}

	public long getNumericDefaultUpperBound() {
		return numericDefaultUpperBound;
	}

	public DataTypeDefinition numericDefaultUpperBound(long numericDefaultUpperBound) {
		this.numericDefaultUpperBound = numericDefaultUpperBound;
		return this;
	}

	public DataTypeDefinition hasDefaulValue(boolean hasDefaulValue) {
		this.hasDefaulValue = hasDefaulValue;
		return this;
	}

	public InputType getType() {
		return type;
	}

	public DataTypeDefinition setType(InputType type) {
		this.type = type;
		return this;
	}

	public enum InputType {
		NUMERIC, TEXT
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (hasDefaulValue ? 1231 : 1237);
		result = prime * result + (hasLength ? 1231 : 1237);
		result = prime * result + (hasPrecision ? 1231 : 1237);
		result = prime * result + (hasScale ? 1231 : 1237);
		result = prime * result + lengthLowerBound;
		result = prime * result + lengthUpperBound;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + precisionLowerBound;
		result = prime * result + precisionUpperBound;
		result = prime * result + scaleLowerBound;
		result = prime * result + scaleUpperBound;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataTypeDefinition other = (DataTypeDefinition) obj;
		if (hasDefaulValue != other.hasDefaulValue)
			return false;
		if (hasLength != other.hasLength)
			return false;
		if (hasPrecision != other.hasPrecision)
			return false;
		if (hasScale != other.hasScale)
			return false;
		if (lengthLowerBound != other.lengthLowerBound)
			return false;
		if (lengthUpperBound != other.lengthUpperBound)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (precisionLowerBound != other.precisionLowerBound)
			return false;
		if (precisionUpperBound != other.precisionUpperBound)
			return false;
		if (scaleLowerBound != other.scaleLowerBound)
			return false;
		if (scaleUpperBound != other.scaleUpperBound)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}
