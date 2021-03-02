package uml.papyrus.doc.util;
import java.io.File;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class EclipseResourceUtil {
	
	  public static IPath getFileLocation(String path) {
	    return getLocation(path, IResource.FILE);
	  }

	  public static IPath getLocation(String path, int resourceType) {
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

	  public static void openWithExternalEditor(IFile file) throws PartInitException {
	    IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), file,
	        IEditorRegistry.SYSTEM_EXTERNAL_EDITOR_ID);
	  }

	  public static void writeFile(String fileLocation, String fileName, InputStream targetStream)
	      throws CoreException {
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

	  public static String createFolder(String osLocation, String folderName) throws CoreException {
	    File newDir = new File (osLocation, folderName);
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
}
