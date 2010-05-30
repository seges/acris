package sk.seges.acris.json.client.provider;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.json.client.JSONValue;

public abstract class JsonAdapter<T, S extends JSONValue> {

	public JsonAdapter() {
	}

	public abstract T setValue(S s, String property, Map<String, String> attributes);

	public T setValue(S s, String property) {
		return setValue(s, property, new HashMap<String, String>());
	}
}