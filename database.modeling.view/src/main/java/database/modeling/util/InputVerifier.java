package database.modeling.util;

import org.eclipse.swt.events.VerifyEvent;

import database.modeling.model.DataTypeDefinition;

public class InputVerifier {

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

		Long longInput = Long.valueOf(input);
		if (dtd.getLengthLowerBound() > longInput) {
			e.doit = false;
			return;
		}

		if (dtd.getLengthUpperBound() < longInput) {
			e.doit = false;
			return;
		}

		e.doit = true;
		return;
	}

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

		Long longInput = Long.valueOf(input);
		if (dtd.getPrecisionLowerBound() > longInput) {
			e.doit = false;
			return;
		}

		if (dtd.getPrecisionUpperBound() < longInput) {
			e.doit = false;
			return;
		}

		e.doit = true;
		return;
	}

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

		Long longInput = Long.valueOf(input);
		if (dtd.getScaleLowerBound() > longInput) {
			e.doit = false;
			return;
		}

		if (dtd.getScaleUpperBound() < longInput) {
			e.doit = false;
			return;
		}

		e.doit = true;
		return;
	}

	public static void verifyNumberField(VerifyEvent e, DataTypeDefinition dtd) {
		String input = e.text;
		if (input.isEmpty()) {
			e.doit = true;
			return;
		}

		e.doit = input.matches("\\d*");
		return;
	}

	public static void verifyTextFieldLength(VerifyEvent e, DataTypeDefinition dtd) {
		String input = e.text;

		if (dtd.getLengthLowerBound() > input.length()) {
			e.doit = false;
			return;
		}

		if (dtd.getLengthUpperBound() < input.length()) {
			e.doit = false;
			return;
		}

		e.doit = true;
		return;
	}

}
