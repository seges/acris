package sk.seges.acris.json.client.provider;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.json.client.JSONValue;

public class JsonDataAdapterProvider {

	static Map<String, JsonAdapter<?, ? extends JSONValue>> jsonAdapters = 
		new HashMap<String, JsonAdapter<?, ? extends JSONValue>>();

	public static <T, S extends JSONValue> void registerAdapter(Class<T> targetClass, JsonAdapter<T, S> adapter) {
		jsonAdapters.put(targetClass.getName(), adapter);
	}
	
	@SuppressWarnings("unchecked")
	public static <T, S extends JSONValue> JsonAdapter<T, S> getAdapter(Class<T> targetClass) {
		return (JsonAdapter<T, S>)jsonAdapters.get(targetClass.getName());
	}
}