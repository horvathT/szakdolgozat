package model.transfer.util;

import java.util.Arrays;
import java.util.List;

import org.eclipse.uml2.uml.VisibilityKind;

/**
 * Segédosztály szöveges értékek átalakítására.
 * 
 * @author Horváth Tibor
 *
 */
public class ExcelReaderUtil {

	private final static List<String> valuesAcceptedAsTrue = Arrays.asList("igen", "i", "yes", "y", "true", "1");
	private final static List<String> valuesAcceptedAsFalse = Arrays.asList("nem", "n", "no", "false", "0");

	/**
	 * Szövegesen megkapott láthatósági változó kasztolása {@link VisibilityKind}
	 * objektummá.
	 * 
	 * @param visibility
	 * @return
	 */
	public static VisibilityKind stringToVisibilityKind(String visibility) {
		if (visibility.equals("public")) {
			return VisibilityKind.PUBLIC_LITERAL;
		} else if (visibility.equals("private")) {
			return VisibilityKind.PRIVATE_LITERAL;
		} else if (visibility.equals("protected")) {
			return VisibilityKind.PROTECTED_LITERAL;
		} else if (visibility.equals("package") || visibility.isEmpty()) {
			return VisibilityKind.PACKAGE_LITERAL;
		}
		return null;
	}

	/**
	 * Szövegesen megkapott igaz/hamis érték konvertálása {@link Boolean} objektummá
	 * 
	 * @param boolString
	 * @return
	 */
	public static boolean stringToBoolean(String boolString) {
		if (valuesAcceptedAsTrue.contains(boolString)) {
			return true;
		} else if (valuesAcceptedAsFalse.contains(boolString)) {
			return false;
		}
		throw new IllegalArgumentException("Unknown true/false value: " + boolString);
	}

	/**
	 * Eldönti hogy a paraméterben kapott szöveg szerepel-e az ismert igaz/hamis
	 * értékek között.
	 * 
	 * @param boolString
	 * @return
	 */
	public static boolean isValidBoolValue(String boolString) {
		if (valuesAcceptedAsFalse.contains(boolString) || valuesAcceptedAsTrue.contains(boolString)) {
			return true;
		}
		return false;
	}

}
