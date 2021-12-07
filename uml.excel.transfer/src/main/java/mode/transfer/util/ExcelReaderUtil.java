package mode.transfer.util;

import org.eclipse.uml2.uml.VisibilityKind;

public class ExcelReaderUtil {

	public static VisibilityKind stringToVisibilityKind(String visibility) {
		if (visibility.equals("public")) {
			return VisibilityKind.PUBLIC_LITERAL;
		} else if (visibility.equals("private")) {
			return VisibilityKind.PRIVATE_LITERAL;
		} else if (visibility.equals("protected")) {
			return VisibilityKind.PROTECTED_LITERAL;
		} else if (visibility.equals("package")) {
			return VisibilityKind.PACKAGE_LITERAL;
		}
		throw new IllegalArgumentException("Hibás láthatósági paraméter: " + visibility);
	}

	public static boolean stringToBoolean(String boolString) {
		if ("igen".equals(boolString)) {
			return true;
		} else if ("nem".equals(boolString)) {
			return false;
		}
		throw new IllegalArgumentException("Hibás igen/nem paraméter: " + boolString);
	}

}
