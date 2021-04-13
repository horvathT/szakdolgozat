package database.modeling.view.util;

import database.Column;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;

public class ColumnUtil {
	private final static String STEREOTYPE_QUALIFIED_NAME = "database::Column";

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
	
	public static String getOracleDataType(Property property) {
	    for (EObject edt : property.getStereotypeApplications()) {
	      if (edt instanceof Column) {
	        Column prop = (Column) edt;
	        String dDlName = prop.getOracleDataType();
	        if (dDlName != null && !dDlName.isEmpty()) {
	          return dDlName;
	        }
	      }
	    }
	    return null;
	  }
	
	public static void setOracleDataType(Property property, String data) {
		if(data != null && property != null) {
			Stereotype applicableStereotype =
			          property.getApplicableStereotype(STEREOTYPE_QUALIFIED_NAME);
			if(hasStereotype(property)) {
				property.setValue(applicableStereotype, "oracleDataType", data);
			}else {
				if(applicableStereotype != null) {
					applyStereotype(property);
					property.setValue(applicableStereotype, "oracleDataType", data);
				}
			}
		}
	}
	
	public static String getOracleDefaultValue(Property property) {
	    for (EObject edt : property.getStereotypeApplications()) {
	      if (edt instanceof Column) {
	        Column prop = (Column) edt;
	        String dDlName = prop.getOracleDefaultValue();
	        if (dDlName != null && !dDlName.isEmpty()) {
	          return dDlName;
	        }
	      }
	    }
	    return null;
	  }
	
	public static void setOracleDefaultValue(Property property, String data) {
		if(data != null && property != null) {
			Stereotype applicableStereotype =
			          property.getApplicableStereotype(STEREOTYPE_QUALIFIED_NAME);
			if(hasStereotype(property)) {
				property.setValue(applicableStereotype, "oracleDefaultValue", data);
			}else {
				if(applicableStereotype != null) {
					applyStereotype(property);
					property.setValue(applicableStereotype, "oracleDefaultValue", data);
				}
			}
		}
	}
}
