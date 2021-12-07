package model.transfer.importer;

public class MissingSheetException extends Exception {

	public MissingSheetException(String sheetName) {
		super("A kiválasztott fájl nem tartalmaz " + sheetName + " nevű munkalapot!");
	}

}
