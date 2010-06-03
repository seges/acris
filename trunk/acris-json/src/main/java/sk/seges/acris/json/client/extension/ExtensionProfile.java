package sk.seges.acris.json.client.extension;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ExtensionProfile {
	
	private Map<String, List<ExtensionDescription>> extensions = new HashMap<String, List<ExtensionDescription>>();
	
	private void register(Class<? extends ExtensionPoint> extendedType, ExtensionDescription extensionDescription) {
		List<ExtensionDescription> classExtensions = extensions.get(extendedType.getName());
		if (classExtensions == null) {
			classExtensions = new LinkedList<ExtensionDescription>();
			extensions.put(extendedType.getName(), classExtensions);
		}
		
		classExtensions.add(extensionDescription);
	}
	
	public List<ExtensionDescription> getExtensionDescriptions(Class<? extends ExtensionPoint> extendedType) {
		List<ExtensionDescription> extensionRes = extensions.get(extendedType.getName());
		if (extensionRes != null) {
			return Collections.unmodifiableList(extensionRes);
		}
		
		return null;
	}

	public void declare(Class<? extends ExtensionPoint> extendedType, ExtensionDescription extensionDescription) {
		extensionDescription.setExtendedClass(extendedType);
		register(extendedType, extensionDescription);
	}
	
	public void declare(Class<? extends ExtensionPoint> extendedType, Class<? extends Extension> extensionType) {
		ExtensionDescription extensionDescription = new ExtensionDescription();
		extensionDescription.setExtendedClass(extendedType);
		extensionDescription.setExtensionClass(extensionType);
		register(extendedType, extensionDescription);
	}

	public void declare(Class<? extends ExtensionPoint> extendedType, Class<? extends Extension> extensionType, String pointName) {
		ExtensionDescription extensionDescription = new ExtensionDescription();
		extensionDescription.setExtendedClass(extendedType);
		extensionDescription.setExtensionClass(extensionType);
		extensionDescription.setPointName(pointName);
		register(extendedType, extensionDescription);
	}
}