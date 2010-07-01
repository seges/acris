package sk.seges.acris.json.client;

import java.util.HashMap;
import java.util.Map;

import sk.seges.acris.json.client.deserialization.JsonDeserializer;
import sk.seges.acris.json.client.extension.Extension;
import sk.seges.acris.json.client.extension.ExtensionPoint;
import sk.seges.acris.json.client.extension.ExtensionProfile;
import sk.seges.acris.json.client.serialization.JsonSerializer;

import com.google.gwt.json.client.JSONValue;

public class JsonizerContext {

	private Map<String, InstanceCreator<?>> instanceCreators = new HashMap<String, InstanceCreator<?>>();
	private Map<String, JsonDeserializer<?, ? extends JSONValue>> jsonDeserializers = new HashMap<String, JsonDeserializer<?, ? extends JSONValue>>();
	private Map<String, JsonSerializer<?, ? extends JSONValue>> jsonSerializers = new HashMap<String, JsonSerializer<?, ? extends JSONValue>>();

	private ExtensionProfile extensionProfile = new ExtensionProfile();
	
	private Map<String, Map<String, Class<?>>> propertyMapping = new HashMap<String, Map<String, Class<?>>>();
	
	public void declare(Class<? extends ExtensionPoint> extendedType, Class<? extends Extension> extensionType) {
		extensionProfile.declare(extendedType, extensionType);
	}
	
	public void registerPropertyType(Class<?> clazz, String property, Class<?> targetType) {
		Map<String, Class<?>> classProperties = propertyMapping.get(clazz.getName());

		if (classProperties == null) {
			classProperties = new HashMap<String, Class<?>>();
		}

		classProperties.put(property, targetType);
	}

	public Class<?> getPropertyType(Class<?> clazz, String[] properties) {
		Class<?> currentClass = clazz;
		
		for (String property : properties) {
			if (currentClass == null) {
				return null;
			}
			currentClass = getPropertyType(currentClass, property);
		}
		
		return currentClass;
	}
	
	public Class<?> getPropertyType(Class<?> clazz, String property) {
		Map<String, Class<?>> classProperties = propertyMapping.get(clazz.getName());
		if (classProperties == null) {
			return null;
		}
		return classProperties.get(property);
	}

	public void registerInstanceCreator(Class<?> clazz, InstanceCreator<?> instanceCreator) {
		instanceCreators.put(clazz.getName(), instanceCreator);
	}

	@SuppressWarnings("unchecked")
	public <T> InstanceCreator<T> getInstanceCreator(Class<T> clazz) {
		return (InstanceCreator<T>) instanceCreators.get(clazz.getName());
	}

	public <T, S extends JSONValue> void registerDeserializer(Class<T> targetClass, JsonDeserializer<T, S> adapter) {
		jsonDeserializers.put(targetClass.getName(), adapter);
	}

	@SuppressWarnings("unchecked")
	public <T, S extends JSONValue> JsonDeserializer<T, S> getDeserializer(Class<T> targetClass) {
		return (JsonDeserializer<T, S>) jsonDeserializers.get(targetClass.getName());
	}

	public <T, S extends JSONValue> void registerSerializer(Class<T> targetClass, JsonSerializer<T, S> adapter) {
		jsonSerializers.put(targetClass.getName(), adapter);
	}

	@SuppressWarnings("unchecked")
	public <T, S extends JSONValue> JsonSerializer<T, S> getSerializer(Class<T> targetClass) {
		return (JsonSerializer<T, S>) jsonSerializers.get(targetClass.getName());
	}

	public ExtensionProfile getExtensionProfile() {
		return extensionProfile;
	}
}