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

import model.transfer.export.AssociationSheetCreator;
import model.transfer.export.ClassSummarySheetCreator;
import model.transfer.export.DataTypeSheetCreator;
import model.transfer.export.EnumSheetCreator;
import model.transfer.export.InterfaceSummarySheetCreator;
import model.transfer.importer.PropertyAndMethodCreator;
import model.transfer.util.CellUtil;
import model.transfer.util.ExcelReaderUtil;

/**
 * Excel szerkezetének hitelesítése.
 * 
 * @author Horváth Tibor
 *
 */
public class ExcelStructureValidator {

	private static final Bundle BUNDLE = FrameworkUtil.getBundle(ExcelStructureValidator.class);
	private static final ILog LOGGER = Platform.getLog(BUNDLE);

	private String filePath;

	private Workbook workbook;

	private Sheet interfaceSummarySheet;
	private Sheet classSummarySheet;
	private Sheet enumSheet;
	private Sheet dataTypeSheet;
	private Sheet associationSheet;

	private Shell shell;

	private List<String> missingSheetNames = new ArrayList<>();

	/**
	 * Interfészek amelyek az Extends oszlopban hibás értéket kaptak.
	 */
	private Map<String, String> interfaceExtendsViolation = new HashMap<>();

	/**
	 * Osztályok amelyek az Extends oszlopban hibás értéket kaptak.
	 */
	private Map<String, String> classExtendsViolation = new HashMap<>();

	/**
	 * Oszályok amelyek az Implements oszlopban hibás értéket kaptak.
	 */
	private Map<String, String> classImplementsViolation = new HashMap<>();

	/**
	 * Osztályok amik saját magukra hivatkoznak.
	 */
	private List<String> classSelfReferenceViolation = new ArrayList<>();

	/**
	 * Interfészek amik saját magukra hivatkoznak.
	 */
	private List<String> interfaceSelfReferenceViolation = new ArrayList<>();

	/**
	 * Attribútumok a szülő elemeik nevével kulcsolva amelyekhez nem lett megadva
	 * típus.
	 */
	private Map<String, List<String>> propertyMissingType = new HashMap<>();
	/**
	 * Nem létező típus nevek
	 */
	private Set<String> nonExistentType = new HashSet<>();

	/**
	 * Az entity nevekhez mappelve egy listába azoknak az attribútumoknak a nevei
	 * amelyekhez hibás láthatósági kulcsszó lett megadva.
	 */
	private Map<String, List<String>> propertyInvalidVisibility = new HashMap<>();

	/**
	 * Az entity nevekhez mappelve egy listába azoknak az metódusoknak a nevei
	 * amelyekhez hibás láthatósági kulcsszó lett megadva.
	 */
	private Map<String, List<String>> methodInvalidVisibility = new HashMap<>();

	/**
	 * Olyan entitások nevei amelyekhez hibás láthatósági kulcsszó lett megadva.
	 */
	private List<String> entityInvalidVisibility = new ArrayList<>();

	/**
	 * Azon asszociációk sorszámát tartalmazzaamelyeknek valamelyik végpontján a
	 * navigable érték hibásan lett megadva.
	 */
	private List<Integer> associaitonInvalidNavigableValue = new ArrayList<>();

	/**
	 * Azon asszociációk sorszámát tartalmazza amelyeknek nem lett végpont megadva
	 * vagy hibásan.
	 */
	private List<Integer> associationWithIncorrectEndpoint = new ArrayList<>();

	/**
	 * Oszályok amelyekhez hibás érték került megadásra az Abstract oszlopba.
	 */
	private List<String> classInvalidIsAbstractValue = new ArrayList<>();

	/**
	 * Attribútumok a szülő elemeik nevével kulcsolva amelyekhez hibás érték került
	 * megadásra a Static oszlopban.
	 */
	private Map<String, List<String>> propertyInvalidIsStaticValue = new HashMap<>();

	/**
	 * Metódusok a szülő elemeik nevével kulcsolva amelyekhez hibás érték került
	 * megadásra a Static oszlopban.
	 */
	private Map<String, List<String>> methodInvalidIsStaticValue = new HashMap<>();

	/**
	 * Metódusok a szülő elemeik nevével kulcsolva amelyekhez hibás érték került
	 * megadásra az abstract oszlopban.
	 */
	private Map<String, List<String>> methodInvalidIsAbstractValue = new HashMap<>();

	public ExcelStructureValidator(String filePath) {
		this.filePath = filePath;
		shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
	}

	/**
	 * Excel struktúrájának ellenőrzése, szükség esetén hibaüzenetek megjelenítése
	 * és végrhajtás leállítása.
	 * 
	 * @throws EncryptedDocumentException
	 * @throws IOException
	 */
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

		// Attribútumok és metódusok validálása
		validatePropertiesAndMethods(dataTypeNames);
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

		// Boolean input értékek validálása
		validateClassIsAbstractInput();
		if (!classInvalidIsAbstractValue.isEmpty()) {
			String errorMessage = classInvalidIsAbstractValueErrorMessage();
			validationErrorMessage(errorMessage);
		}

		if (!propertyInvalidIsStaticValue.isEmpty()) {
			String errorMessage = propertyInvalidIsStaticValueErrorMessage();
			validationErrorMessage(errorMessage);
		}

		if (!methodInvalidIsStaticValue.isEmpty()) {
			String errorMessage = methodInvalidIsStaticValueErrorMessage();
			validationErrorMessage(errorMessage);
		}

		if (!methodInvalidIsAbstractValue.isEmpty()) {
			String errorMessage = methodInvalidIsAbstractValueErrorMessage();
			validationErrorMessage(errorMessage);
		}

		// Asszociációk validálása
		validateAssociations();
		if (!associationWithIncorrectEndpoint.isEmpty()) {
			String errorMessage = incorrectEndpointErrorMessage();
			validationErrorMessage(errorMessage);
		}

		if (!associaitonInvalidNavigableValue.isEmpty()) {
			String errorMessage = invalidIsNavigableErrorMessage();
			validationErrorMessage(errorMessage);
		}
	}

	private void mapToString(StringBuilder sb, Map<String, List<String>> map) {
		for (Entry<String, List<String>> entry : map.entrySet()) {
			String entityName = entry.getKey();
			List<String> methods = entry.getValue();

			for (String methodName : methods) {
				sb.append(entityName + "." + methodName + System.lineSeparator());
			}
		}
	}

	private String methodInvalidIsAbstractValueErrorMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("The following methods have invalid values set in the Abstract column: " + System.lineSeparator());
		mapToString(sb, methodInvalidIsAbstractValue);
		return sb.toString();
	}

	private String methodInvalidIsStaticValueErrorMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("The following methods have invalid values set in the Static column: " + System.lineSeparator());
		mapToString(sb, methodInvalidIsStaticValue);
		return sb.toString();
	}

	private String propertyInvalidIsStaticValueErrorMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("The following properties have invalid values set in the Static column: " + System.lineSeparator());
		mapToString(sb, propertyInvalidIsStaticValue);
		return sb.toString();
	}

	private String classInvalidIsAbstractValueErrorMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("The following classes have invalid values set in the Abstract column: " + System.lineSeparator());
		sb.append(String.join(",", classInvalidIsAbstractValue));
		return sb.toString();
	}

	/**
	 * Osztályok Abstract oszlopba kapott értékének ellenőrzése.
	 */
	private void validateClassIsAbstractInput() {
		int lastRowNum = classSummarySheet.getLastRowNum();
		for (int i = 1; i <= lastRowNum; i++) {
			Row row = classSummarySheet.getRow(i);
			String name = CellUtil.getStringCellValue(row.getCell(1));
			if (name.isEmpty()) {
				continue;
			}
			String isAbstract = CellUtil.getStringCellValue(row.getCell(3));
			if (!ExcelReaderUtil.isValidBoolValue(isAbstract)) {
				classInvalidIsAbstractValue.add(name);
				LOGGER.error(classInvalidAbstractValueErrorMessage(name));
			}
		}
	}

	private String invalidIsNavigableErrorMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Associaitons in the following rows have one or more incorrectly set navigable values: "
				+ System.lineSeparator());
		for (int i = 0; i < associationWithIncorrectEndpoint.size(); i++) {
			Integer rowNum = associationWithIncorrectEndpoint.get(i);
			sb.append(rowNum);

			if (i != associationWithIncorrectEndpoint.size() - 1) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

	private String incorrectEndpointErrorMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Associaitons in the following rows have one or more incorrectly set or missing endpoint:"
				+ System.lineSeparator());
		for (int i = 0; i < associationWithIncorrectEndpoint.size(); i++) {
			Integer rowNum = associationWithIncorrectEndpoint.get(i);
			sb.append(rowNum);

			if (i != associationWithIncorrectEndpoint.size() - 1) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

	/**
	 * Asszociációk adatainak ellenőrzése.
	 */
	private void validateAssociations() {

		List<String> entityNames = getEntityNamesFromsheet(enumSheet);
		entityNames.addAll(getEntityNamesFromsheet(classSummarySheet));
		entityNames.addAll(getEntityNamesFromsheet(interfaceSummarySheet));

		int lastRowNum = associationSheet.getLastRowNum();
		for (int i = 1; i <= lastRowNum; i++) {
			Row row = associationSheet.getRow(i);
			if (row == null) {
				continue;
			}

			// Végpontok létezésének ellenőrzése
			String end1ClassifierName = CellUtil.getStringCellValue(row.getCell(1));
			String end2ClassifierName = CellUtil.getStringCellValue(row.getCell(7));
			if (!entityNames.contains(end1ClassifierName) || !entityNames.contains(end2ClassifierName)) {
				associationWithIncorrectEndpoint.add(i + 1);
				LOGGER.error(missingAssociationendpointErrorMessage(i + 1));
			}

			// Navigable boolean értékek validálása
			String end1Navigable = CellUtil.getStringCellValue(row.getCell(3));
			String end2Navigable = CellUtil.getStringCellValue(row.getCell(9));
			if (!ExcelReaderUtil.isValidBoolValue(end1Navigable) || !ExcelReaderUtil.isValidBoolValue(end2Navigable)) {
				associaitonInvalidNavigableValue.add(i + 1);
				LOGGER.error(invalidNavigationValueErrorMessage(i + 1));
			}

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
		sb.append("The following methods have incorrect access modifiers set: " + System.lineSeparator());
		mapToString(sb, methodInvalidVisibility);
		return sb.toString();
	}

	private String propertyVisibilityKindErrorMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("The following attributes have incorrect access modifiers set: " + System.lineSeparator());
		mapToString(sb, propertyInvalidVisibility);
		return sb.toString();
	}

	private String entityVisibilityKindErrorMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("The following entities have incorrect access modifiers set: " + System.lineSeparator());
		for (String entityName : entityInvalidVisibility) {
			sb.append(entityName + System.lineSeparator());
		}
		return sb.toString();
	}

	/**
	 * Láthatósági változók ellenőrzése.
	 */
	private void validateEntityVisibilityKeyword() {
		// entitás
		checkEntityVisibilityKeywords(classSummarySheet);
		checkEntityVisibilityKeywords(interfaceSummarySheet);
	}

	/**
	 * Entitások láthatósági változóinak ellenőrzése.
	 * 
	 * @param sheet
	 */
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
		sb.append("Incorrect file! The following attributes have no type set: " + System.lineSeparator());
		mapToString(sb, propertyMissingType);
		return sb.toString();
	}

	/**
	 * Excelben található adattípusok összegyűjtése.
	 * 
	 * @return
	 */
	private List<String> collectDataTypesFromExcel() {
		List<String> dataTypeNames = new ArrayList<>();
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
		sb.append("Incorrect file! The following types do not exist: " + System.lineSeparator());

		for (String missingType : nonExistentType) {
			sb.append(missingType + System.lineSeparator());
		}
		return sb.toString();
	}

	/**
	 * Attribútumok és metódusok adatinak validálása.
	 * 
	 * @param dataTypeNames
	 */
	private void validatePropertiesAndMethods(List<String> dataTypeNames) {
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
					addToMap(propertyMissingType, className, propertyName);
					LOGGER.error(propertyMissingTypeErrorMessage(className, propertyName));
				} else if (!dataTypeNames.contains(dataTypeName)) {
					nonExistentType.add(dataTypeName);
					LOGGER.error(nonExistentTypeErrorMessage(dataTypeName));
				}
				// láthatósági változó ellenőrzés
				String visibility = CellUtil.getStringCellValue(row.getCell(3));
				VisibilityKind visibilityKind = ExcelReaderUtil.stringToVisibilityKind(visibility);
				if (visibilityKind == null) {
					addToMap(propertyInvalidVisibility, className, propertyName);
					LOGGER.error(propertyInvalidVisibilityErrorMessage(className, propertyName));
				}

				String isStatic = CellUtil.getStringCellValue(row.getCell(5));
				if (!ExcelReaderUtil.isValidBoolValue(isStatic)) {
					addToMap(propertyInvalidIsStaticValue, className, propertyName);
					LOGGER.error(propertyInvalidIsStaticValue(className, propertyName));
				}
			}

			// Metódus paraméter típus vizsgálat
			int lastRowNum = classSheet.getLastRowNum();
			for (int i = ++mehtodHeaderRowNumber; i < lastRowNum; i++) {
				Row row = classSheet.getRow(i);
				if (row == null) {
					continue;
				}

				String parameterTypeName = CellUtil.getStringCellValue(row.getCell(7));
				String parameterName = CellUtil.getStringCellValue(row.getCell(8));
				if (!parameterName.isEmpty()) {
					if (parameterTypeName.isEmpty()) {
						addToMap(propertyMissingType, className, parameterName);
						LOGGER.error(propertyMissingTypeErrorMessage(className, parameterName));
					} else if (!dataTypeNames.contains(parameterTypeName)) {
						nonExistentType.add(parameterTypeName);
						LOGGER.error(nonExistentTypeErrorMessage(parameterTypeName));
					}
				}

				String methodName = CellUtil.getStringCellValue(row.getCell(2));
				if (methodName.isEmpty()) {
					continue;
				}
				valiadateMethodVisibilityKeyword(className, row);

				String isStatic = CellUtil.getStringCellValue(row.getCell(4));
				if (ExcelReaderUtil.isValidBoolValue(isStatic)) {
					addToMap(methodInvalidIsStaticValue, className, methodName);
					LOGGER.error(methodInvalidIsStaticValue(className, methodName));
				}
				String isAbstract = CellUtil.getStringCellValue(row.getCell(5));
				if (ExcelReaderUtil.isValidBoolValue(isAbstract)) {
					addToMap(methodInvalidIsAbstractValue, className, methodName);
					LOGGER.error(methodInvalidIsAbstractValue(className, methodName));
				}

			}

		}

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
					addToMap(propertyMissingType, interfaceName, propertyName);
					LOGGER.error(propertyMissingTypeErrorMessage(interfaceName, propertyName));
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
						addToMap(propertyMissingType, interfaceName, parameterName);
						LOGGER.error(propertyMissingTypeErrorMessage(interfaceName, parameterName));
					} else if (!dataTypeNames.contains(parameterTypeName)) {
						nonExistentType.add(parameterTypeName);
						LOGGER.error(nonExistentTypeErrorMessage(parameterTypeName));
					}
				}
				String methodName = CellUtil.getStringCellValue(row.getCell(2));
				if (methodName.isEmpty()) {
					continue;
				}

				valiadateMethodVisibilityKeyword(interfaceName, row);

				String isAbstract = CellUtil.getStringCellValue(row.getCell(5));
				if (ExcelReaderUtil.isValidBoolValue(isAbstract)) {
					addToMap(methodInvalidIsAbstractValue, interfaceName, methodName);
					LOGGER.error(methodInvalidIsAbstractValue(interfaceName, methodName));
				}
			}
		}
	}

	/**
	 * Interfész metódusok láthatósági változóinak ellenőrzése.
	 * 
	 * @param classifierName
	 * @param row
	 */
	private void valiadateMethodVisibilityKeyword(String classifierName, Row row) {
		String methodName = CellUtil.getStringCellValue(row.getCell(2));
		if (methodName.isEmpty()) {
			return;
		}
		String visibility = CellUtil.getStringCellValue(row.getCell(3));
		VisibilityKind visibilityKind = ExcelReaderUtil.stringToVisibilityKind(visibility);
		if (visibilityKind == null) {
			addToMap(methodInvalidVisibility, classifierName, methodName);
			LOGGER.error(methodInvalidVisibilityErroMessage(classifierName, methodName));
		}
	}

	private void addToMap(Map<String, List<String>> map, String key, String value) {
		List<String> list = map.get(key);
		if (list == null) {
			list = new ArrayList<>();
		}
		list.add(value);
		map.put(key, list);
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

	/**
	 * Entitások hierarchiájának ellenőrzése.
	 */
	private void validateHierarchy() {
		// Interface extends Interface, cannot extend itself
		validateInterfaceHierarchy();
		// Class extends Interface, implements Class, , cannot refer to itself
		validateClassHierarchy();
	}

	/**
	 * Osztály által implementáls és leszármazott elemek helyességének ellenőrzése.
	 */
	private void validateClassHierarchy() {
		List<String> classNames = getEntityNamesFromsheet(classSummarySheet);

		List<String> interfaceNames = getEntityNamesFromsheet(interfaceSummarySheet);

		int lastRowNum = classSummarySheet.getLastRowNum();
		String className = null;

		for (int i = 1; i <= lastRowNum; i++) {
			Row row = classSummarySheet.getRow(i);
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

	/**
	 * Interfész által leszármazott elemek helyességének ellenőrzése.
	 */
	private void validateInterfaceHierarchy() {
		List<String> interfaceNames = getEntityNamesFromsheet(interfaceSummarySheet);
		int lastRowNum = interfaceSummarySheet.getLastRowNum();
		String interfaceName = null;

		for (int i = 1; i <= lastRowNum; i++) {
			Row row = interfaceSummarySheet.getRow(i);
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

	private String methodInvalidIsAbstractValue(String className, String methodName) {
		return "Method " + methodName + " in class " + className + " has invalid value set in Abstract column!";
	}

	private String methodInvalidIsStaticValue(String className, String methodName) {
		return "Method " + methodName + " in class " + className + " has invalid value set in Static column!";
	}

	private String propertyInvalidIsStaticValue(String className, String propertyName) {
		return "Property " + propertyName + " in class " + className + " has invalid value set in Static column!";
	}

	private String classInvalidAbstractValueErrorMessage(String name) {
		return "Class " + name + " has invalid value in Abstract column.";
	}

	private String invalidNavigationValueErrorMessage(int rowNumber) {
		return "Association at row: " + rowNumber + " has invalid navigation value set";
	}

	private String missingAssociationendpointErrorMessage(int rowNumber) {
		return "Association at row: " + rowNumber + " has incorrent endpoint set";
	}

	private String propertyInvalidVisibilityErrorMessage(String className, String propertyName) {
		return propertyName + " property of entity " + className + " has invalid access modifier set!";
	}

	private String methodInvalidVisibilityErroMessage(String className, String methodName) {
		return methodName + " method of entity " + className + " has invalid access modifier set!";
	}

	private String propertyMissingTypeErrorMessage(String entityName, String propertyName) {
		return propertyName + " property of entity " + entityName + " has no type set!";
	}

	private String nonExistentTypeErrorMessage(String parameterTypeName) {
		return "Type " + parameterTypeName + " not found!";
	}

	private String interfaceExtendsViolationErrorMessage(String interfaceName, String generalizedClassifierName) {
		return "Interface " + interfaceName + " cannot extend " + generalizedClassifierName;
	}

	private String interfaceSelfReferencingErrorMessage(String interfaceName) {
		return "Interface " + interfaceName + " cannot extend itself.";
	}

	private String classSelfReferencingErrorMessage(String className) {
		return "Class " + className + " cannot extend or implement itself.";
	}

	private String classExtendsViolationErrorMessage(String className, String generalizedClassifierName) {
		return "Class " + className + " cannot extend " + generalizedClassifierName;
	}

	private String classImplementsViolationErrorMessage(String className, String generalizedClassifierName) {
		return "Class " + className + " cannot implement " + generalizedClassifierName;
	}

	private String compileMissingSheetErrorMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Incomplete Excel file! The following sheets are missing: " + System.lineSeparator());

		for (String sheetName : missingSheetNames) {
			sb.append(sheetName + System.lineSeparator());
		}
		return sb.toString();
	}

	/**
	 * Hiányzó munkalapok ellenőrzése
	 */
	private void checkForMissingSheets() {
		interfaceSummarySheet = workbook.getSheet(InterfaceSummarySheetCreator.SHEET_NAME);
		if (interfaceSummarySheet == null) {
			LOGGER.error("Missing sheet: " + InterfaceSummarySheetCreator.SHEET_NAME);
			missingSheetNames.add(InterfaceSummarySheetCreator.SHEET_NAME);
		} else {
			checkforMissingClassifierSheets(interfaceSummarySheet);
		}

		classSummarySheet = workbook.getSheet(ClassSummarySheetCreator.SHEET_NAME);
		if (classSummarySheet == null) {
			LOGGER.error("Missing sheet: " + ClassSummarySheetCreator.SHEET_NAME);
			missingSheetNames.add(ClassSummarySheetCreator.SHEET_NAME);
		} else {
			checkforMissingClassifierSheets(classSummarySheet);
		}

		enumSheet = workbook.getSheet(EnumSheetCreator.SHEET_NAME);
		if (enumSheet == null) {
			LOGGER.error("Missing sheet: " + EnumSheetCreator.SHEET_NAME);
			missingSheetNames.add(EnumSheetCreator.SHEET_NAME);
		}

		dataTypeSheet = workbook.getSheet(DataTypeSheetCreator.DATA_TYPE_SHEET_NAME);
		if (dataTypeSheet == null) {
			LOGGER.error("Missing sheet: " + EnumSheetCreator.SHEET_NAME);
			missingSheetNames.add(EnumSheetCreator.SHEET_NAME);
		}

		associationSheet = workbook.getSheet(AssociationSheetCreator.ASSOCIATION_SHEET_NAME);
		if (associationSheet == null) {
			LOGGER.error("Missing sheet: " + AssociationSheetCreator.ASSOCIATION_SHEET_NAME);
			missingSheetNames.add(AssociationSheetCreator.ASSOCIATION_SHEET_NAME);
		}
	}

	private void checkforMissingClassifierSheets(Sheet classifierSheet) {
		List<String> sheetNames = getEntityNamesFromsheet(classifierSheet);
		for (String sheetName : sheetNames) {
			Sheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				missingSheetNames.add(sheetName);
				LOGGER.error("Missing sheet: " + sheetName);
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
		MessageDialog.openError(shell, "Validation error!", errorMessage);
		System.exit(1);
	}
}
