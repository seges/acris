package sk.seges.acris.json.client;

import java.util.LinkedList;
import java.util.List;

import sk.seges.acris.json.client.context.DeserializationContext;
import sk.seges.acris.json.client.extension.Extension;
import sk.seges.acris.json.client.extension.ExtensionDescription;
import sk.seges.acris.json.client.extension.ExtensionPoint;
import sk.seges.acris.json.client.extension.ExtensionProfile;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public abstract class ExtendableJsonizer extends PrimitiveJsonizer {
	
	public <E extends Extension, S extends ExtensionPoint> S fromJson(JSONObject jsonObject, Class<S> type, S s, DeserializationContext deserializationContext) {

		ExtensionProfile extensionProfile = jsonizerContext.getExtensionProfile();
		
		if (extensionProfile == null) {
			return s;
		}
		
		List<ExtensionDescription> extensionDescriptions = extensionProfile.getExtensionDescriptions(type);

		if (extensionDescriptions == null) {
			//let's try to determine extensions for a type
			s.declareExtensions(extensionProfile);
			extensionDescriptions = extensionProfile.getExtensionDescriptions(type);
		}

		if (extensionDescriptions == null) {
			//no extension points defines
			return s;
		}
		
		for (ExtensionDescription extensionDescription : extensionDescriptions) {
			String pointName = extensionDescription.getPointName();
			if (pointName == null || pointName.length() == 0) {
				//no json extension point name defined
				pointName = getDefaultPointName(extensionDescription.getExtensionClass());
			}
			
			if (pointName != null) {
				JSONValue jsonExtensionPoint = jsonObject.get(pointName);
				if (jsonExtensionPoint != null) {
					if (jsonExtensionPoint.isArray() != null) {
						List<E> result = new LinkedList<E>();
						result = fromJson(jsonExtensionPoint.isArray(), (Class<E>)extensionDescription.getExtensionClass(), result, deserializationContext);
						if (result != null) {
							for (E ex : result) {
								s.addRepeatingExtension(ex);
							}
						}
					} else {
						Extension result = fromJson(jsonExtensionPoint, extensionDescription.getExtensionClass(), deserializationContext);
						if (result != null) {
							s.setExtension(result);
						}
					}
				}
			}
		}
		
		return s;
	}
	
	protected abstract String getDefaultPointName(Class<?> type);
}
