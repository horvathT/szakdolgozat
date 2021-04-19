package database.modeling.view.util;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.UMLPackage;

public class ProfileUtil {
	private final static String DATABASE_MODELING_PROFILE_PATH = "platform:/plugin/uml.profile/resources/DatabaseModeling.profile.uml";
	
	public static Profile retrieveProfile() {
		URI uri = URI.createURI(DATABASE_MODELING_PROFILE_PATH);
		
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put( Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
		Resource resource = resourceSet.getResource(uri, true);
		
		Profile profile = (Profile) EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.PROFILE);
		return profile;
	}
	
	public static void applyPofile(Element element) {
		Profile profile = retrieveProfile();
		Model model = element.getModel();
		if(!isApplied(model, profile)) {
			model.applyProfile(profile);
		}
	}
	
	public static boolean isApplied(Element element, Profile profile) {
		Model model = element.getModel();
		EList<Profile> appliedProfiles = model.getAppliedProfiles();
		for (Profile appliedProfile : appliedProfiles) {
			String name = appliedProfile.getName();
			if(name.equals(profile.getName())) {
				return true;
			}
		}
		return false;
	}

}
