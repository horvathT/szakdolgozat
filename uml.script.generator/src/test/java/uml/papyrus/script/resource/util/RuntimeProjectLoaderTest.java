package uml.papyrus.script.resource.util;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.resource.NotFoundException;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;
import org.eclipse.papyrus.uml.tools.model.UmlModel;
import org.eclipse.papyrus.views.modelexplorer.ModelExplorerPageBookView;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.internal.impl.PackageImpl;
import org.junit.jupiter.api.Test;

import database.modeling.util.resource.EclipseResourceUtil;
import database.modeling.util.stereotype.DatabaseModelUtil;
import database.modeling.util.stereotype.StereotypeManagementUtil;

class RuntimeProjectLoaderTest {

	private static final String TESTPROJECT_NAME = "testmodel";

	private static final String TESTPROJECT_PATH = "src/test/resource/" + TESTPROJECT_NAME;

	private static final String TEST_MODEL = "testmodel.di";

	private static final String PAPYRUS_EDITOR_ID = "org.eclipse.papyrus.infra.core.papyrusEditor";
	private static final String MODEL_EXPLORER_ID = "org.eclipse.papyrus.views.modelexplorer.modelexplorer";

	@Test
	public Package projectLoader() {
		try {
			IProject copiedProject = EclipseResourceUtil.copyAndImportProjectToWorkspace(TESTPROJECT_PATH,
					TESTPROJECT_NAME);
			if (!copiedProject.exists()) {
				fail("Copied project does not exist!");
			}

			IFile testModel = copiedProject.getFile(TEST_MODEL);
			if (!testModel.exists()) {
				fail("Copied " + TEST_MODEL + " does not exist!");
			}

			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IDE.openEditor(page, testModel, PAPYRUS_EDITOR_ID, true);

			Package modelPackage = null;
			try {
				modelPackage = getModelPackage();
			} catch (NotFoundException | ServiceException e) {
				e.printStackTrace();
			}

			if (modelPackage == null) {
				fail("Model package not found!");
			}
			return modelPackage;

		} catch (InvocationTargetException | IOException | CoreException | InterruptedException e) {
			e.printStackTrace();
			fail(e);
		}
		return null;
	}

	private Package getModelPackage() throws PartInitException, NotFoundException,
			org.eclipse.papyrus.infra.core.services.ServiceException {

		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IViewPart modelExplorer = page.showView(MODEL_EXPLORER_ID);
		ModelExplorerPageBookView view = (ModelExplorerPageBookView) modelExplorer;

		ServicesRegistry registry = (ServicesRegistry) view.getAdapter(ServicesRegistry.class);

		ModelSet service = registry.getService(ModelSet.class);
		UmlModel model = (UmlModel) service.getModel(UmlModel.MODEL_ID);
		EObject modelRoot = model.lookupRoot();
		EList<EObject> eContents = modelRoot.eContents();
		for (EObject eObject : eContents) {
			if (eObject instanceof PackageImpl) {
				PackageImpl modelPackage = (PackageImpl) eObject;
				if (StereotypeManagementUtil.hasStereotype(modelPackage, DatabaseModelUtil.STEREOTYPE_QUALIFIED_NAME)) {
					return modelPackage;
				}
				return modelPackage;
			}
		}
		return null;
	}
}
