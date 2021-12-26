package model.transfer.importer.validator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.VisibilityKind;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import model.transfer.export.ClassSummarySheetCreator;
import model.transfer.export.DataTypeSheetCreator;
import model.transfer.export.EnumSheetCreator;
import model.transfer.export.InterfaceSummarySheetCreator;
import model.transfer.importer.PropertyAndMethodCreator;
import model.transfer.util.CellUtil;
import model.transfer.util.ExcelReaderUtil;

public class ExcelStructureValidator {

	private static final Bundle BUNDLE = FrameworkUtil.getBundle(ExcelStructureValidator.class);
	private static final ILog LOGGER = Platform.getLog(BUNDLE);

	private String filePath;

	private Workbook workbook;

	private Shell shell;

	private List<String> missingSheetNames = new ArrayList<>();

	private Map<String, String> interfaceExtendsViolation = new HashMap<>();
	private Map<String, String> classExtendsViolation = new HashMap<>();
	private Map<String, String> classImplementsViolation = new HashMap<>();
	private List<String> classSelfReferenceViolation = new ArrayList<>();
	private List<String> interfaceSelfReferenceViolation = new ArrayList<>();

	private Map<String, List<String>> propertyMissingType = new HashMap<>();
	private Set<String> nonExistentType = new HashSet<>();

	/**
	 * Az entity nevekhez mappeli egy listába azoknak az attribútumoknak a neveit
	 * amelyekhez hibás láthatósági kulcsszó lett megadva.
	 */
	private Map<String, List<String>> propertyInvalidVisibility = new HashMap<>();

	/**
	 * Az entity nevekhez mappeli egy listába azoknak az metódusoknak a neveit
	 * amelyekhez hibás láthatósági kulcsszó lett megadva.
	 */
	private Map<String, List<String>> methodInvalidVisibility = new HashMap<>();

	/**
	 * Entity-k amelyekhez hibás láthatósági kulcsszó lett megadva.
	 */
	private List<String> entityInvalidVisibility = new ArrayList<>();

	public ExcelStructureValidator(String filePath) {
		this.filePath = filePath;
		shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
	}

	public void validateInput() throws EncryptedDocumentException, IOException {
		workbook = WorkbookFactory.create(new File(filePath));

		// Hiba ha nincsenek meg a kötelező munkalapok
		checkForMissingSheets();
		if (!missingSheetNames.isEmpty()) {
			String errorMessage = compileMissingSheetErrorMessage();
			validationErrorMessage(errorMessage);
		}

		List<String> dataTypeNames = collectDataTypesFromExcel();

		// Hiba ha nem megfelelő típusú elemek között van beállítva hierarchia
		validateHierarchy();
		if (!interfaceExtendsViolation.isEmpty() || !classExtendsViolation.isEmpty()
				|| !classImplementsViolation.isEmpty() || !classSelfReferenceViolation.isEmpty()
				|| !interfaceSelfReferenceViolation.isEmpty()) {
			String errorMessage = compileHierarchyViolationErrorMessage();
			validationErrorMessage(errorMessage);
		}

		// Ha nincs típus megadva vagy nem létező
		validatePropertyAndMethodsTypes(dataTypeNames);
		if (!nonExistentType.isEmpty()) {
			String errorMessage = nonExistentTypeErrorMessage();
			validationErrorMessage(errorMessage);
		}

		if (!propertyMissingType.isEmpty()) {
			String errorMessage = propertyMissingTypeErrorMessage();
			validationErrorMessage(errorMessage);
		}

		// Nem létező láthatósági változó van megadva
		validateEntityVisibilityKeyword();
		if (!entityInvalidVisibility.isEmpty() || !propertyInvalidVisibility.isEmpty()
				|| !methodInvalidVisibility.isEmpty()) {
			String errorMessage = visibilityKindErrorMessage();
			validationErrorMessage(errorMessage);
		}
	}

	private String visibilityKindErrorMessage() {
		StringBuilder sb = new StringBuilder();

		if (!entityInvalidVisibility.isEmpty()) {
			sb.append(entityVisibilityKindErrorMessage());
		}
		sb.append(System.lineSeparator());
		if (!propertyInvalidVisibility.isEmpty()) {
			sb.append(propertyVisibilityKindErrorMessage());
		}
		sb.append(System.lineSeparator());
		if (!methodInvalidVisibility.isEmpty()) {
			sb.append(methodVisibilityKindErrorMessage());
		}
		return sb.toString();
	}

	private String methodVisibilityKindErrorMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("A következő metódusokhoz hibás láthatósági módosítószó lett megadva: " + System.lineSeparator());
		for (Entry<String, List<String>> entity : methodInvalidVisibility.entrySet()) {
			String entityName = entity.getKey();
			List<String> methods = entity.getValue();

			for (String methodName : methods) {
				sb.append(entityName + "." + methodName + System.lineSeparator());
			}
		}
		return sb.toString();
	}

	private String propertyVisibilityKindErrorMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("A következő attribútumokhoz hibás láthatósági módosítószó lett megadva: " + System.lineSeparator());
		for (Entry<String, List<String>> entity : propertyInvalidVisibility.entrySet()) {
			String entityName = entity.getKey();
			List<String> properties = entity.getValue();

			for (String propertyName : properties) {
				sb.append(entityName + "." + propertyName + System.lineSeparator());
			}
		}
		return sb.toString();
	}

	private String entityVisibilityKindErrorMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("A következő entitásokhoz hibás láthatósági módosítószó lett megadva: " + System.lineSeparator());
		for (String entityName : entityInvalidVisibility) {
			sb.append(entityName + System.lineSeparator());
		}
		return sb.toString();
	}

	private void validateEntityVisibilityKeyword() {
		// entitás
		Sheet classSummarySheet = workbook.getSheet(ClassSummarySheetCreator.SHEET_NAME);
		Sheet interfaceSummarySheet = workbook.getSheet(InterfaceSummarySheetCreator.SHEET_NAME);
		checkEntityVisibilityKeywords(classSummarySheet);
		checkEntityVisibilityKeywords(interfaceSummarySheet);
	}

	private void checkEntityVisibilityKeywords(Sheet sheet) {
		int rowNum = sheet.getLastRowNum();
		for (int i = 1; i <= rowNum; i++) {
			Row row = sheet.getRow(i);
			if (row == null) {
				continue;
			}

			String entityName = CellUtil.getStringCellValue(row.getCell(1));
			String visibility = CellUtil.getStringCellValue(row.getCell(2));
			VisibilityKind visibilityKind = ExcelReaderUtil.stringToVisibilityKind(visibility);
			if (visibilityKind == null) {
				entityInvalidVisibility.add(entityName);
			}
		}
	}

	private String propertyMissingTypeErrorMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Hibás Exccel fájl! A következő attribútumokhoz nincs típus beállítva: " + System.lineSeparator());

		for (Entry<String, List<String>> entry : propertyMissingType.entrySet()) {
			String classifierName = entry.getKey();
			List<String> propertyNames = entry.getValue();
			for (String property : propertyNames) {
				sb.append(classifierName + "." + property + System.lineSeparator());
			}
		}

		return sb.toString();
	}

	private List<String> collectDataTypesFromExcel() {
		List<String> dataTypeNames = new ArrayList<>();
		Sheet dataTypeSheet = workbook.getSheet(DataTypeSheetCreator.DATA_TYPE_SHEET_NAME);
		for (int i = 1; i <= dataTypeSheet.getLastRowNum(); i++) {
			Row row = dataTypeSheet.getRow(i);
			if (row == null) {
				continue;
			}
			String typeName = CellUtil.getStringCellValue(row.getCell(1));
			if (!typeName.isEmpty()) {
				dataTypeNames.add(typeName);
			}
		}

		return dataTypeNames;
	}

	private String nonExistentTypeErrorMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Hibás Exccel fájl! A következő adattípusok nem léteznek: " + System.lineSeparator());

		for (String missingType : nonExistentType) {
			sb.append(missingType + System.lineSeparator());
		}
		return sb.toString();
	}

	private void validatePropertyAndMethodsTypes(List<String> dataTypeNames) {
		Sheet classSummarySheet = workbook.getSheet(ClassSummarySheetCreator.SHEET_NAME);
		List<String> classNames = getEntityNamesFromsheet(classSummarySheet);

		for (String className : classNames) {
			Sheet classSheet = workbook.getSheet(className);

			int mehtodHeaderRowNumber = PropertyAndMethodCreator.getMethodHeaderRowNumber(classSheet);
			for (int i = 1; i < mehtodHeaderRowNumber; i++) {
				Row row = classSheet.getRow(i);
				if (row == null) {
					continue;
				}
				// Attríbútumnak nincs beállítva típus
				String propertyName = CellUtil.getStringCellValue(row.getCell(2));
				String dataTypeName = CellUtil.getStringCellValue(row.getCell(4));
				if (dataTypeName.isEmpty()) {
					addToPropertyMissingTypeMap(className, propertyName);
				} else if (!dataTypeNames.contains(dataTypeName)) {
					nonExistentType.add(dataTypeName);
					LOGGER.error(nonExistentTypeErrorMessage(dataTypeName));
				}
				// láthatósági változó ellenőrzés
				String visibility = CellUtil.getStringCellValue(row.getCell(3));
				VisibilityKind visibilityKind = ExcelReaderUtil.stringToVisibilityKind(visibility);
				if (visibilityKind == null) {
					addToPropertyInvalidVisibility(className, propertyName);
				}
			}

			// Metódus paraméter típus vizsgálat
			int lastRowNum = classSheet.getLastRowNum();
			for (int i = ++mehtodHeaderRowNumber; i < lastRowNum; i++) {
				Row row = classSheet.getRow(i);
				String parameterTypeName = CellUtil.getStringCellValue(row.getCell(7));
				String parameterName = CellUtil.getStringCellValue(row.getCell(8));
				if (!parameterName.isEmpty()) {
					if (parameterTypeName.isEmpty()) {
						addToPropertyMissingTypeMap(className, parameterName);
					} else if (!dataTypeNames.contains(parameterTypeName)) {
						nonExistentType.add(parameterTypeName);
						LOGGER.error(nonExistentTypeErrorMessage(parameterTypeName));
					}
				}
				valiadateMethodVisibilityKeyword(className, row);

			}

		}

		Sheet interfaceSummarySheet = workbook.getSheet(InterfaceSummarySheetCreator.SHEET_NAME);
		List<String> interfaceNames = getEntityNamesFromsheet(interfaceSummarySheet);

		for (String interfaceName : interfaceNames) {
			Sheet interfaceSheet = workbook.getSheet(interfaceName);

			int mehtodHeaderRowNumber = PropertyAndMethodCreator.getMethodHeaderRowNumber(interfaceSheet);
			for (int i = 1; i < mehtodHeaderRowNumber; i++) {
				Row row = interfaceSheet.getRow(i);
				if (row == null) {
					continue;
				}
				// Attríbútumnak nincs beállítva típus
				String propertyName = CellUtil.getStringCellValue(row.getCell(2));
				String dataTypeName = CellUtil.getStringCellValue(row.getCell(3));
				if (dataTypeName.isEmpty()) {
					addToPropertyMissingTypeMap(interfaceName, propertyName);
				} else if (!dataTypeNames.contains(dataTypeName)) {
					nonExistentType.add(dataTypeName);
					LOGGER.error(nonExistentTypeErrorMessage(dataTypeName));
				}
			}

			// Metódus paraméter típus vizsgálat
			int lastRowNum = interfaceSheet.getLastRowNum();
			for (int i = ++mehtodHeaderRowNumber; i < lastRowNum; i++) {
				Row row = interfaceSheet.getRow(i);
				String parameterTypeName = CellUtil.getStringCellValue(row.getCell(6));
				String parameterName = CellUtil.getStringCellValue(row.getCell(7));
				if (!parameterName.isEmpty()) {
					if (parameterTypeName.isEmpty()) {
						addToPropertyMissingTypeMap(interfaceName, parameterName);
					} else if (!dataTypeNames.contains(parameterTypeName)) {
						nonExistentType.add(parameterTypeName);
						LOGGER.error(nonExistentTypeErrorMessage(parameterTypeName));
					}
				}
				valiadateMethodVisibilityKeyword(interfaceName, row);
			}
		}
	}

	private void valiadateMethodVisibilityKeyword(String classifierName, Row row) {
		String methodName = CellUtil.getStringCellValue(row.getCell(2));
		if (methodName.isEmpty()) {
			return;
		}
		String visibility = CellUtil.getStringCellValue(row.getCell(3));
		VisibilityKind visibilityKind = ExcelReaderUtil.stringToVisibilityKind(visibility);
		if (visibilityKind == null) {
			addToMethodInvalidVisibility(classifierName, methodName);
		}
	}

	private void addToMethodInvalidVisibility(String className, String methodName) {
		List<String> list = methodInvalidVisibility.get(className);
		if (list == null) {
			list = new ArrayList<>();
		}
		list.add(methodName);
		methodInvalidVisibility.put(className, list);
		LOGGER.error(methodInvalidVisibilityErroMessage(className, methodName));
	}

	private void addToPropertyInvalidVisibility(String className, String propertyName) {
		List<String> list = propertyInvalidVisibility.get(className);
		if (list == null) {
			list = new ArrayList<>();
		}
		list.add(propertyName);
		propertyInvalidVisibility.put(className, list);
		LOGGER.error(propertyInvalidVisibilityErrorMessage(className, propertyName));
	}

	private void addToPropertyMissingTypeMap(String className, String propertyName) {
		List<String> typelessPropertyList = propertyMissingType.get(className);
		if (typelessPropertyList == null) {
			typelessPropertyList = new ArrayList<>();
		}
		typelessPropertyList.add(propertyName);
		propertyMissingType.put(className, typelessPropertyList);
		LOGGER.error(propertyMissingTypeErrorMessage(className, propertyName));
	}

	private String compileHierarchyViolationErrorMessage() {
		StringBuilder sb = new StringBuilder();

		if (!interfaceExtendsViolation.isEmpty()) {
			for (Entry<String, String> entry : interfaceExtendsViolation.entrySet()) {
				sb.append(interfaceExtendsViolationErrorMessage(entry.getKey(), entry.getValue())
						+ System.lineSeparator());
			}
		}

		if (!interfaceSelfReferenceViolation.isEmpty()) {
			for (String name : interfaceSelfReferenceViolation) {
				sb.append(interfaceSelfReferencingErrorMessage(name)
						+ System.lineSeparator());
			}
		}

		if (!classExtendsViolation.isEmpty()) {
			for (Entry<String, String> entry : classExtendsViolation.entrySet()) {
				sb.append(classExtendsViolationErrorMessage(entry.getKey(), entry.getValue())
						+ System.lineSeparator());
			}
		}

		if (!classImplementsViolation.isEmpty()) {
			for (Entry<String, String> entry : classImplementsViolation.entrySet()) {
				sb.append(classImplementsViolationErrorMessage(entry.getKey(), entry.getValue())
						+ System.lineSeparator());
			}
		}

		if (!classSelfReferenceViolation.isEmpty()) {
			for (String name : classSelfReferenceViolation) {
				sb.append(classSelfReferencingErrorMessage(name)
						+ System.lineSeparator());
			}
		}

		return sb.toString();
	}

	private void validateHierarchy() {
		// Interface extends Interface, cannot extend itself
		validateInterfaceHierarchy();
		// Class extends Interface, implements Class, , cannot refer to itself
		validateClassHierarchy();
	}

	private void validateClassHierarchy() {
		Sheet classSheet = workbook.getSheet(ClassSummarySheetCreator.SHEET_NAME);
		List<String> classNames = getEntityNamesFromsheet(classSheet);

		Sheet interfaceSheet = workbook.getSheet(InterfaceSummarySheetCreator.SHEET_NAME);
		List<String> interfaceNames = getEntityNamesFromsheet(interfaceSheet);

		int lastRowNum = classSheet.getLastRowNum();
		String className = null;

		for (int i = 1; i <= lastRowNum; i++) {
			Row row = classSheet.getRow(i);
			if (row == null) {
				continue;
			}

			String name = CellUtil.getStringCellValue(row.getCell(1));
			if (!name.isEmpty()) {
				className = name;
			}

			String generalizedClassifierName = CellUtil.getStringCellValue(row.getCell(4));
			if (!generalizedClassifierName.isEmpty()) {
				if (generalizedClassifierName.equals(className)) {
					classSelfReferenceViolation.add(name);
					LOGGER.error(classSelfReferencingErrorMessage(className));
				} else if (!classNames.contains(generalizedClassifierName)) {
					classExtendsViolation.put(className, generalizedClassifierName);
					LOGGER.error(classExtendsViolationErrorMessage(className, generalizedClassifierName));
				}
			}

			String implementedInterfaceName = CellUtil.getStringCellValue(row.getCell(5));
			if (!implementedInterfaceName.isEmpty()) {
				if (implementedInterfaceName.equals(className)) {
					classSelfReferenceViolation.add(name);
					LOGGER.error(classSelfReferencingErrorMessage(className));
				} else if (!interfaceNames.contains(implementedInterfaceName)) {
					classImplementsViolation.put(className, implementedInterfaceName);
					LOGGER.error(classImplementsViolationErrorMessage(className, generalizedClassifierName));
				}

			}
		}

	}

	private void validateInterfaceHierarchy() {
		Sheet interfaceSheet = workbook.getSheet(InterfaceSummarySheetCreator.SHEET_NAME);
		List<String> interfaceNames = getEntityNamesFromsheet(interfaceSheet);
		int lastRowNum = interfaceSheet.getLastRowNum();
		String interfaceName = null;

		for (int i = 1; i <= lastRowNum; i++) {
			Row row = interfaceSheet.getRow(i);
			if (row == null) {
				continue;
			}

			String name = CellUtil.getStringCellValue(row.getCell(1));
			if (!name.isEmpty()) {
				interfaceName = name;
			}
			String generalizedClassifierName = CellUtil.getStringCellValue(row.getCell(3));
			if (!generalizedClassifierName.isEmpty()) {
				if (generalizedClassifierName.equals(interfaceName)) {
					interfaceSelfReferenceViolation.add(name);
					LOGGER.error(interfaceSelfReferencingErrorMessage(interfaceName));
				} else if (!interfaceNames.contains(generalizedClassifierName)) {
					interfaceExtendsViolation.put(interfaceName, generalizedClassifierName);
					LOGGER.error(interfaceExtendsViolationErrorMessage(interfaceName, generalizedClassifierName));
				}
			}
		}
	}

	private String propertyInvalidVisibilityErrorMessage(String className, String propertyName) {
		return "A(z) " + className + " entity " + propertyName
				+ " attribútumához imeretlen láthatósági módosítószó van beállítva!";
	}

	private String methodInvalidVisibilityErroMessage(String className, String methodName) {
		return "A(z) " + className + " entity " + methodName
				+ " metódusához imeretlen láthatósági módosítószó van beállítva!";
	}

	private String propertyMissingTypeErrorMessage(String entityName, String propertyName) {
		return "A(z) " + entityName + " munkalap " + propertyName + " attribútumához nincs típus beállítva";
	}

	private String nonExistentTypeErrorMessage(String parameterTypeName) {
		return "Típus " + parameterTypeName + " nem található.";
	}

	private String interfaceExtendsViolationErrorMessage(String interfaceName, String generalizedClassifierName) {
		return "Interface " + interfaceName + " nem származhatja le a(z) " + generalizedClassifierName
				+ " elemet.";
	}

	private String interfaceSelfReferencingErrorMessage(String interfaceName) {
		return "Interface " + interfaceName + " nem hivatkozhat saját magára.";
	}

	private String classSelfReferencingErrorMessage(String className) {
		return "Osztály " + className + " nem hivatkozhat saját magára.";
	}

	private String classExtendsViolationErrorMessage(String className, String generalizedClassifierName) {
		return "Osztály " + className + " nem származhatja le a(z) " + generalizedClassifierName
				+ " elemet.";
	}

	private String classImplementsViolationErrorMessage(String className, String generalizedClassifierName) {
		return "Osztály " + className + " nem implementálhatja a(z) " + generalizedClassifierName
				+ " elemet.";
	}

	private String compileMissingSheetErrorMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Hibás Exccel fájl! A következő mukalapok hiányoznak: " + System.lineSeparator());

		for (String sheetName : missingSheetNames) {
			sb.append(sheetName + System.lineSeparator());
		}
		return sb.toString();
	}

	private void checkForMissingSheets() {
		Sheet interfaceSheet = workbook.getSheet(InterfaceSummarySheetCreator.SHEET_NAME);
		if (interfaceSheet == null) {
			LOGGER.error("Hiányzó munkalap: " + InterfaceSummarySheetCreator.SHEET_NAME);
			missingSheetNames.add(InterfaceSummarySheetCreator.SHEET_NAME);
		} else {
			checkforMissingClassifierSheets(interfaceSheet);
		}

		Sheet classSheet = workbook.getSheet(ClassSummarySheetCreator.SHEET_NAME);
		if (classSheet == null) {
			LOGGER.error("Hiányzó munkalap: " + ClassSummarySheetCreator.SHEET_NAME);
			missingSheetNames.add(ClassSummarySheetCreator.SHEET_NAME);
		} else {
			checkforMissingClassifierSheets(classSheet);
		}

		Sheet enumSheet = workbook.getSheet(EnumSheetCreator.SHEET_NAME);
		if (enumSheet == null) {
			LOGGER.error("Hiányzó munkalap: " + EnumSheetCreator.SHEET_NAME);
			missingSheetNames.add(EnumSheetCreator.SHEET_NAME);
		}

		Sheet dataTypeSheet = workbook.getSheet(DataTypeSheetCreator.DATA_TYPE_SHEET_NAME);
		if (dataTypeSheet == null) {
			LOGGER.error("Hiányzó munkalap: " + EnumSheetCreator.SHEET_NAME);
			missingSheetNames.add(EnumSheetCreator.SHEET_NAME);
		}

	}

	private void checkforMissingClassifierSheets(Sheet classifierSheet) {
		List<String> sheetNames = getEntityNamesFromsheet(classifierSheet);
		for (String sheetName : sheetNames) {
			Sheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				missingSheetNames.add(sheetName);
				LOGGER.error("Hiányzó munkalap: " + sheetName);
			}
		}
	}

	private List<String> getEntityNamesFromsheet(Sheet sheet) {
		List<String> entityNames = new ArrayList<>();
		int lastRowNum = sheet.getLastRowNum();
		for (int i = 1; i <= lastRowNum; i++) {
			Row row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			String name = CellUtil.getStringCellValue(row.getCell(1));
			if (!name.isEmpty()) {
				entityNames.add(name);
			}
		}
		return entityNames;
	}

	private void validationErrorMessage(String errorMessage) {
		MessageDialog.openError(shell, "Excel szerkezeti hiba!", errorMessage);
		System.exit(1);
	}
}
