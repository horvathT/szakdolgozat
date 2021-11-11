package database.modeling.util.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.refactoring.IJavaRefactorings;
import org.eclipse.jdt.core.refactoring.descriptors.RenameJavaElementDescriptor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringContribution;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;

public class EclipseResourceUtil {

	public static void copyDir(String src, String dest, boolean overwrite) throws IOException {
		Iterator<Path> iterDirWalk = Files.walk(Paths.get(src)).iterator();
		while (iterDirWalk.hasNext()) {
			Path source = iterDirWalk.next();
			Path destination = Paths.get(dest, source.toString().substring(src.length()));
			if (!source.toString().equals(src))
				Files.copy(source, destination,
						overwrite ? new CopyOption[] { StandardCopyOption.REPLACE_EXISTING } : new CopyOption[] {});
		}
	}

	public static boolean isRelativePath(String path) {
		return URI.createURI(path).isRelative();
	}

	public static IPath getFileLocation(String path) {
		return getLocation(path, IResource.FILE);
	}

	public static IPath getFolderLocation(String path) {
		return getLocation(path, IResource.FOLDER);
	}

	private static IPath getLocation(String path, int resourceType) {
		IPath ipath = org.eclipse.core.runtime.Path.fromOSString(path);
		if (URI.createURI(path).isRelative()) {
			return getResource(ipath, resourceType).getLocation();
		}
		return ipath;
	}

	public static IResource getResource(String path, int resourceType) {
		return getResource(org.eclipse.core.runtime.Path.fromOSString(path), resourceType);
	}

	public static IResource getResource(IPath path, int resourceType) {
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		if (IResource.FOLDER == resourceType) {
			return workspaceRoot.getFolder(path);
		} else if (IResource.FILE == resourceType) {
			return workspaceRoot.getFile(path);
		} else {
			throw new IllegalArgumentException("Not handled resourceType argument: " + resourceType);
		}
	}

	public static IFile getFileResource(String filePath) {
		return ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(getFileLocation(filePath));
	}

	public static void refreshFolder(String path) throws CoreException {
		IResource folderResource = getResource(path, IResource.FOLDER);
		if (folderResource != null) {
			folderResource.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		}
	}

	public static IProject copyAndImportProjectToWorkspace(String projectPath, String projectName)
			throws IOException, CoreException, InvocationTargetException, InterruptedException {
		IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();

		IProject project = wsRoot.getProject(projectName);
		project.delete(true, null);

		String dest = wsRoot.getLocation().append(projectName).toOSString();

		EclipseResourceUtil.copyDir(projectPath, dest, true);

		importProjectToWorkspace(projectName, dest);

		return wsRoot.getProject(projectName);
	}

	public static void importProjectToWorkspace(String containerPath, String sourceProjectPath)
			throws InvocationTargetException, InterruptedException {

		IOverwriteQuery overwriteQuery = new IOverwriteQuery() {
			@Override
			public String queryOverwrite(String file) {
				return ALL;
			}
		};

		ImportOperation importOperation = new ImportOperation(org.eclipse.core.runtime.Path.fromOSString(containerPath),
				new File(sourceProjectPath), FileSystemStructureProvider.INSTANCE, overwriteQuery);

		importOperation.setCreateContainerStructure(false);
		importOperation.run(new NullProgressMonitor());
	}

	public static IPath getResourceFileLocationPath(Resource resource) {
		URI uri = resource.getURI();

		String filePath = null;
		if (uri.isFile()) {
			filePath = uri.toFileString();
		} else if (uri.isPlatform()) {
			filePath = (uri.toPlatformString(true));
		} else {
			filePath = uri.toString();
		}

		return EclipseResourceUtil.getFileLocation(filePath);
	}

	public static String getResourceFileLocation(Resource resource) {
		IPath resourceFileLocation = getResourceFileLocationPath(resource);
		if (resourceFileLocation != null) {
			return resourceFileLocation.toOSString();
		}
		return null;
	}

	public static void openWithExternalEditor(IFile file) throws PartInitException {
		IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), file,
				IEditorRegistry.SYSTEM_EXTERNAL_EDITOR_ID);
	}

	public static void writeFile(String fileLocation, String fileName, InputStream targetStream) throws CoreException {
		File file = new File(fileLocation, fileName);
		IPath fromOSString = org.eclipse.core.runtime.Path.fromOSString(file.getPath());
		IFile iFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(fromOSString);
		String oldFile = iFile.toString();
		String newFile = targetStream.toString();

		if (!iFile.exists()) {
			iFile.create(targetStream, true, null);
		} else if (!newFile.equals(oldFile)) {
			iFile.setContents(targetStream, true, true, null);
		}

	}

	public static void rename(IFile file, String newName) throws CoreException {
		RefactoringContribution contribution = RefactoringCore
				.getRefactoringContribution(IJavaRefactorings.RENAME_COMPILATION_UNIT);

		RenameJavaElementDescriptor descriptor = (RenameJavaElementDescriptor) contribution.createDescriptor();
		descriptor.setProject(null);
		descriptor.setJavaElement(JavaCore.createCompilationUnitFrom(file));
		descriptor.setNewName(newName);

		RefactoringStatus status = new RefactoringStatus();
		Refactoring refactoring = descriptor.createRefactoring(status);

		IProgressMonitor monitor = new NullProgressMonitor();
		refactoring.checkInitialConditions(monitor);
		refactoring.checkFinalConditions(monitor);

		Change change = refactoring.createChange(monitor);
		change.perform(monitor);
	}

	public static String createFolder(String osLocation, String folderName) throws CoreException {
		File newDir = new File(osLocation, folderName);
		newDir.mkdirs();
		EclipseResourceUtil.refreshFolder(osLocation);
		return newDir.getAbsolutePath();
	}

	public static void refreshProject(String projectName) throws CoreException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot workspaceRoot = workspace.getRoot();
		IProject project = workspaceRoot.getProject(projectName);
		if (project.exists()) {
			project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		}

	}

	public static void refreshWorkspaceRoot() throws CoreException {
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		workspaceRoot.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
	}

	public static void deleteFile(String filePath) throws CoreException {
		IFile file = getFileResource(filePath);
		file.delete(true, new NullProgressMonitor());
	}
}
