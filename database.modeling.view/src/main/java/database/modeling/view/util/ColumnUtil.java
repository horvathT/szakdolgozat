package database.modeling.view.util;

import DatabaseModeling.Column;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;

public class ColumnUtil {
	private static final String LENGTH = "length";
	private static final String DATA_TYPE = "dataType";
	private static final String DEFAULT_VALUE = "defaultValue";
	private final static String STEREOTYPE_QUALIFIED_NAME = "DatabaseModeling::Column";

	public static boolean hasStereotype(Property property) {
		if (property == null) {
			return false;
		}

		Stereotype applicableStereotype = property.getApplicableStereotype(STEREOTYPE_QUALIFIED_NAME);
		if (property.isStereotypeApplied(applicableStereotype)) {
			return true;
		}
		return false;
	}

	public static void applyStereotype(Property property) {
		if (property == null) {
			return;
		}
		Stereotype applicableStereotype = property.getApplicableStereotype(STEREOTYPE_QUALIFIED_NAME);
		if (property.isStereotypeApplied(applicableStereotype)) {
		} else {
			if (applicableStereotype != null) {
				property.applyStereotype(applicableStereotype);
			}
		}
	}
	
	public static String getDataType(Property property) {
	    for (EObject edt : property.getStereotypeApplications()) {
	      if (edt instanceof Column) {
	        Column prop = (Column) edt;
	        String dDlName = prop.getDataType();
	        if (dDlName != null && !dDlName.isEmpty()) {
	          return dDlName;
	        }
	      }
	    }
	    return null;
	  }
	
	public static void setDataType(Property property, String data) {
		setValue(STEREOTYPE_QUALIFIED_NAME, DATA_TYPE, property, data);
	}
	
	public static String getDefaultValue(Property property) {
	    for (EObject edt : property.getStereotypeApplications()) {
	      if (edt instanceof Column) {
	        Column prop = (Column) edt;
	        String dDlName = prop.getDefaultValue();
	        if (dDlName != null && !dDlName.isEmpty()) {
	          return dDlName;
	        }
	      }
	    }
	    return null;
	  }
	
	public static void setLength(Property property, String data) {
		setValue(STEREOTYPE_QUALIFIED_NAME, LENGTH, property, data);
	}
	
	public static String getLength(Property property) {
	    for (EObject edt : property.getStereotypeApplications()) {
	      if (edt instanceof Column) {
	        Column prop = (Column) edt;
	        String dDlName = prop.getDefaultValue();
	        if (dDlName != null && !dDlName.isEmpty()) {
	          return dDlName;
	        }
	      }
	    }
	    return null;
	  }
	
	public static void setDefaultValue(Property property, String data) {
		setValue(STEREOTYPE_QUALIFIED_NAME, DEFAULT_VALUE, property, data);
	}

	private static void setValue(String stereotypeName , String propertyName, Property property, String data) {
		if(data != null && property != null) {
			Stereotype applicableStereotype =
			          property.getApplicableStereotype(stereotypeName);
			if(hasStereotype(property)) {
				property.setValue(applicableStereotype, propertyName, data);
			}else {
				if(applicableStereotype != null) {
					applyStereotype(property);
					property.setValue(applicableStereotype, propertyName, data);
				}
			}
		}
	}
}
