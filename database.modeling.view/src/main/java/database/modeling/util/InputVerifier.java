package database.modeling.util;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Text;

import database.modeling.viewmodel.DataTypeDefinition;

public class InputVerifier {

	/**
	 * Hossz inputmező validálása. A bemenet csak szám lehet és a határértékek közé
	 * kell esnie.
	 * 
	 * @param e   event
	 * @param dtd
	 */
	public static void verifyNumberFieldLength(VerifyEvent e, DataTypeDefinition dtd) {
		String input = e.text;
		if (input.isEmpty()) {
			e.doit = true;
			return;
		}

		if (!input.matches("\\d*")) {
			e.doit = false;
			return;
		}

		Text text = (Text) e.widget;
		String currentInput = text.getText();
		String possibleInput = currentInput.substring(0, e.start) + e.text + currentInput.substring(e.end);

		Long longInput = Long.valueOf(possibleInput);
		if (dtd.getLengthLowerBound() >= longInput) {
			e.doit = false;
			return;
		}

		if (dtd.getLengthUpperBound() <= longInput) {
			e.doit = false;
			return;
		}

		e.doit = true;
	}

	/**
	 * Precizitás inputmező validálása. A bemenet csak szám lehet és a határértékek
	 * közé kell esnie.
	 * 
	 * @param e   event
	 * @param dtd
	 */
	public static void verifyNumberFieldPrecision(VerifyEvent e, DataTypeDefinition dtd) {
		String input = e.text;
		if (input.isEmpty()) {
			e.doit = true;
			return;
		}

		if (!input.matches("\\d*")) {
			e.doit = false;
			return;
		}

		String currentInput = ((Text) e.widget).getText();
		String possibleInput = currentInput.substring(0, e.start) + e.text + currentInput.substring(e.end);

		Long longInput = Long.valueOf(possibleInput);
		if (dtd.getPrecisionLowerBound() >= longInput) {
			e.doit = false;
			return;
		}

		if (dtd.getPrecisionUpperBound() <= longInput) {
			e.doit = false;
			return;
		}

		e.doit = true;
	}

	/**
	 * Skála inputmező validálása. A bemenet csak szám lehet és a határértékek közé
	 * kell esnie.
	 * 
	 * @param e   event
	 * @param dtd
	 */
	public static void verifyNumberFieldScale(VerifyEvent e, DataTypeDefinition dtd) {
		String input = e.text;
		if (input.isEmpty()) {
			e.doit = true;
			return;
		}

		if (!input.matches("\\d*")) {
			e.doit = false;
			return;
		}

		String currentInput = ((Text) e.widget).getText();
		String possibleInput = currentInput.substring(0, e.start) + e.text + currentInput.substring(e.end);

		Long longInput = Long.valueOf(possibleInput);
		if (dtd.getScaleLowerBound() >= longInput) {
			e.doit = false;
			return;
		}

		if (dtd.getScaleUpperBound() <= longInput) {
			e.doit = false;
			return;
		}

		e.doit = true;
	}

	/**
	 * Szöveges mező értékének validálása. Ha van megdava akkor a hossznak a
	 * határértékek közé kell esnie.
	 * 
	 * @param e
	 * @param dtd
	 */
	public static void verifyDefaultValueBounds(VerifyEvent e, DataTypeDefinition dtd) {
		String input = e.text;
		if (input.isEmpty()) {
			e.doit = true;
			return;
		}

		if (!input.matches("\\d*")) {
			e.doit = false;
			return;
		}

		String currentInput = ((Text) e.widget).getText();
		String possibleInput = currentInput.substring(0, e.start) + e.text + currentInput.substring(e.end);

		int precisionLowerBound = dtd.getPrecisionLowerBound();
		if (precisionLowerBound != 0) {
			if (precisionLowerBound >= possibleInput.length()) {
				e.doit = false;
				return;
			}
		}

		int precisionUpperBound = dtd.getPrecisionUpperBound();
		if (precisionUpperBound != 0) {
			if (precisionUpperBound <= possibleInput.length()) {
				e.doit = false;
				return;
			}
		}

		Long longInput = Long.valueOf(possibleInput);
		Long numericDefaultLowerBound = dtd.getNumericDefaultLowerBound();
		if (numericDefaultLowerBound != 0) {
			if (numericDefaultLowerBound >= longInput) {
				e.doit = false;
				return;
			}
		}

		long numericDefaultUpperBound = dtd.getNumericDefaultUpperBound();
		if (numericDefaultUpperBound != 0) {
			if (numericDefaultUpperBound <= longInput) {
				e.doit = false;
				return;
			}
		}

		e.doit = true;

	}

}
