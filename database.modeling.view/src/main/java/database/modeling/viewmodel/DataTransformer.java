package database.modeling.viewmodel;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.Property;

import database.modeling.util.DatabaseTypesUtil;
import database.modeling.util.stereotype.ColumnUtil;
import database.modeling.util.stereotype.DatabaseModelUtil;
import database.modeling.util.stereotype.FKUtil;
import database.modeling.util.stereotype.PKUtil;
import database.modeling.util.stereotype.ProfileUtil;
import database.modeling.util.stereotype.StereotypeManagementUtil;

/**
 * UML modellből érkező adatok és {@link PropertyDataModel} közötti kétirányú
 * konverziót kezeli.
 * 
 * @author Horváth Tibor
 *
 */
public class DataTransformer {

	/**
	 * UML modellből érkező {@link Property} objektum konvertálása
	 * {@link PropertyDataModel} objektummá.
	 * 
	 * @param property
	 * @return
	 */
	public static PropertyDataModel propertyToSqlDataModel(Property property) {
		PropertyDataModel dataModel = new PropertyDataModel();

		dataModel.setXmiId(EcoreUtil.getURI(property).fragment());

		if (StereotypeManagementUtil.hasStereotype(property, ColumnUtil.STEREOTYPE_QUALIFIED_NAME)) {
			// dataType
			DataTypeDefinition dtd = getDataTypeDefinition(property);
			dataModel.setSqlType(dtd);
			// defaultValue
			dataModel.setDefaultValue(ColumnUtil.getDefaultValue(property));
			// length
			dataModel.setLength(ColumnUtil.getLength(property));
			// precision
			dataModel.setPrecision(ColumnUtil.getPrecision(property));
			// scale
			dataModel.setScale(ColumnUtil.getScale(property));
			// nullable
			dataModel.setNullable(ColumnUtil.getNullable(property));
			// unique
			dataModel.setUnique(ColumnUtil.getUnique(property));
		} else {
			dataModel.setDefaultValue("");
			dataModel.setLength("");
			dataModel.setPrecision("");
			dataModel.setScale("");
			dataModel.setNullable(false);
			dataModel.setUnique(false);
		}
		if (StereotypeManagementUtil.hasStereotype(property, PKUtil.STEREOTYPE_QUALIFIED_NAME)) {
			dataModel.setPrimaryKey(true);
		} else {
			dataModel.setPrimaryKey(false);
		}
		if (StereotypeManagementUtil.hasStereotype(property, FKUtil.STEREOTYPE_QUALIFIED_NAME)) {
			// fk
			dataModel.setForeignKey(true);
			// referenced entity, property
			dataModel.setReferencedEntity(FKUtil.getReferencedEntity(property));
			dataModel.setReferencedProperty(FKUtil.getReferencedProperty(property));

		} else {
			dataModel.setForeignKey(false);
			dataModel.setReferencedEntity("");
			dataModel.setReferencedProperty("");
		}

		return dataModel;
	}

	/**
	 * A {@link DatabaseTypesUtil}-ban definiált {@link DataTypeDefinition}
	 * lekérdezése a {@link Column} sztereotípusba beleírt típusnév szerint.
	 * 
	 * @param property
	 * @return
	 */
	private static DataTypeDefinition getDataTypeDefinition(Property property) {
		String dataType = ColumnUtil.getDataType(property);
		String databaseType = DatabaseModelUtil.getDatabaseType(property.getModel());

		Map<String, List<DataTypeDefinition>> databaseTypeMap = new DatabaseTypesUtil().getDatabaseTypeMap();
		List<DataTypeDefinition> dataTypeList = databaseTypeMap.get(databaseType);
		for (DataTypeDefinition dtd : dataTypeList) {
			if (dtd.getName().equals(dataType)) {
				return dtd;
			}
		}

		Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		MessageDialog.openError(shell, "",
				"DataType with name " + dataType + " not found among the types of " + databaseType);
		throw new IllegalArgumentException(
				"DataType with name " + dataType + " not found among the types of " + databaseType);
	}

	/**
	 * Adatmodell alkalmazása a paraméterben kapott {@link Property} objektumon.
	 * 
	 * @param dataModel
	 * @param property
	 * @return
	 */
	public static Property applyModelOnProperty(PropertyDataModel dataModel, Property property) {
		ProfileUtil.applyPofile(property);
		StereotypeManagementUtil.applyStereotype(property, ColumnUtil.STEREOTYPE_QUALIFIED_NAME);
		ColumnUtil.setDataType(property, dataModel.getSqlType().getName());
		ColumnUtil.setDefaultValue(property, dataModel.getDefaultValue());
		ColumnUtil.setLength(property, dataModel.getLength());
		ColumnUtil.setPrecision(property, dataModel.getPrecision());
		ColumnUtil.setScale(property, dataModel.getScale());
		ColumnUtil.setUnique(property, dataModel.isUnique());
		ColumnUtil.setNullable(property, dataModel.isNullable());

		if (dataModel.isPrimaryKey()) {
			StereotypeManagementUtil.applyStereotype(property, PKUtil.STEREOTYPE_QUALIFIED_NAME);
		} else {
			StereotypeManagementUtil.unapplyStereotype(property, PKUtil.STEREOTYPE_QUALIFIED_NAME);
		}
		if (dataModel.isForeignKey()) {
			StereotypeManagementUtil.applyStereotype(property, FKUtil.STEREOTYPE_QUALIFIED_NAME);
			FKUtil.setReferencedEntity(property, dataModel.getReferencedEntity());
			FKUtil.setReferencedProperty(property, dataModel.getReferencedProperty());
		} else {
			StereotypeManagementUtil.unapplyStereotype(property, FKUtil.STEREOTYPE_QUALIFIED_NAME);
		}

		return property;
	}

}
