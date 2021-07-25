package uml.excel.importer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.papyrus.infra.core.resource.NotFoundException;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.ui.PartInitException;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLPackage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tooling.stereotype.EntityReferenceUtil;
import tooling.stereotype.UniqueIdentifierUtil;
import uml.excel.importer.util.TestData;

class ExcelImportTest {

  private static String IMPORT_FILE_CREATE = "import_create_test.xlsx";
  private String IMPORT_FILE_DELETE = "import_delete_missing_test.xlsx";

  private static Package modelPackage;

  @BeforeAll
  static void setUp() throws IOException, PartInitException, NotFoundException, ServiceException {
    RuntimeProjectLoader projectLoader = new RuntimeProjectLoader();
    IProject project = projectLoader.loadProject();
    IFolder folder = project.getFolder("excel");
    assertTrue(folder.exists());

    IFile file = folder.getFile(IMPORT_FILE_CREATE);
    assertTrue(file.exists());
    String absolutePath = file.getLocation().toFile().getAbsolutePath();

    modelPackage = projectLoader.getModelPackage();

    ImportExcelHandler importHandler = new ImportExcelHandler();
    importHandler.importExcel(modelPackage, absolutePath);

  }

  @Test
  void interfaceImportTest() {
    Collection<Interface> interfaces =
        EcoreUtil.getObjectsByType(modelPackage.allOwnedElements(), UMLPackage.Literals.INTERFACE);

    assertNotNull(getInterfaceByName(interfaces, TestData.APPLE_INTERFACE_NAME));
    assertNotNull(getInterfaceByName(interfaces, TestData.BANANA_INTERFACE_NAME));
    assertNotNull(getInterfaceByName(interfaces, TestData.PEAR_INTERFACE_NAME));
  }

  @Test
  void propertyImportTest() {
    Collection<Interface> interfaces =
        EcoreUtil.getObjectsByType(modelPackage.allOwnedElements(), UMLPackage.Literals.INTERFACE);
    Interface appleInterface = getInterfaceByName(interfaces, TestData.APPLE_INTERFACE_NAME);

    assertNotNull(getPropertyByName(appleInterface.getAllAttributes(), TestData.APPLE_PLACEHOLDER_PROPERTY_NAME));
  }

  @Test
  void primaryKeyPropertyImportTest() {
    Collection<Interface> interfaces =
        EcoreUtil.getObjectsByType(modelPackage.allOwnedElements(), UMLPackage.Literals.INTERFACE);
    Interface appleInterface = getInterfaceByName(interfaces, TestData.APPLE_INTERFACE_NAME);

    assertNotNull(getPropertyByName(appleInterface.getAllAttributes(), TestData.APPLE_PRIMARY_KEY_PROPERTY_NAME));

    assertTrue(UniqueIdentifierUtil.hasNestedInterfaceWithStereotype(appleInterface));
    List<Property> redefinedPrimaryKeyComponents =
        UniqueIdentifierUtil.getRedefinedPrimaryKeyComponents(appleInterface);
    
    assertNotNull(getPropertyByName(redefinedPrimaryKeyComponents, TestData.APPLE_PRIMARY_KEY_PROPERTY_NAME));
  }
  
  @Test
  void referenceImportTest() {
    Collection<Interface> interfaces =
        EcoreUtil.getObjectsByType(modelPackage.allOwnedElements(), UMLPackage.Literals.INTERFACE);
    Interface appleInterface = getInterfaceByName(interfaces, TestData.APPLE_INTERFACE_NAME);
    
    List<Interface> nestedEntityReferenceInterfaces = EntityReferenceUtil.getNestedEntityReferenceInterfaces(appleInterface);
    assertNotNull(getInterfaceByName(nestedEntityReferenceInterfaces, TestData.APPLE_ENTITY_REFERENCE_INTERFACE_NAME));
    
    Interface referenceEntity = getInterfaceByName(nestedEntityReferenceInterfaces, TestData.APPLE_ENTITY_REFERENCE_INTERFACE_NAME);
    assertNotNull(getPropertyByName(referenceEntity.getAllAttributes(), TestData.APPLE_ENTITY_REFERENCE_PROPERTY_NAME));
    
  }


  private Interface getInterfaceByName(Collection<Interface> elements, String name) {
    for (Interface element : elements) {
      if (name.equals(element.getName())) {
        return element;
      }
    }
    return null;
  }

  private Property getPropertyByName(Collection<Property> elements, String name) {
    for (Property element : elements) {
      if (name.equals(element.getName())) {
        return element;
      }
    }
    return null;
  }

}
