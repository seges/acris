package sk.seges.acris.json.client;

import sk.seges.acris.json.client.deserialization.JsonDeserializer;
import sk.seges.acris.json.client.serialization.JsonSerializer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;

public class JsonizerBuilder {

	private JsonizerContext jsonizerContext = new JsonizerContext();

	public <T> IJsonizer<T> create() {
		IJsonizer<T> jsonizer = GWT.create(JsonizerProvider.class);
		jsonizer.setJsonizerContext(jsonizerContext);
		return jsonizer;
	}

	public void registerInstanceCreator(Class<?> clazz, InstanceCreator<?> instanceCreator) {
		jsonizerContext.registerInstanceCreator(clazz, instanceCreator);
	}

	public <T> InstanceCreator<T> getInstanceCreator(Class<T> clazz) {
		return jsonizerContext.getInstanceCreator(clazz);
	}

	public <T, S extends JSONValue> void registerSerializer(Class<T> targetClass, JsonSerializer<T, S> adapter) {
		jsonizerContext.registerSerializer(targetClass, adapter);
	}

	public <T, S extends JSONValue> JsonSerializer<T, S> getSerializer(Class<T> targetClass) {
		return jsonizerContext.getSerializer(targetClass);
	}

	public <T, S extends JSONValue> void registerDeserializer(Class<T> targetClass, JsonDeserializer<T, S> adapter) {
		jsonizerContext.registerDeserializer(targetClass, adapter);
	}

	public <T, S extends JSONValue> JsonDeserializer<T, S> getDeserializer(Class<T> targetClass) {
		return jsonizerContext.getDeserializer(targetClass);
	}
}