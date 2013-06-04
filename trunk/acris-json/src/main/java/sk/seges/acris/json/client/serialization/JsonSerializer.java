package sk.seges.acris.json.client.serialization;

import sk.seges.acris.json.client.context.SerializationContext;

import com.google.gwt.json.client.JSONValue;

public abstract class JsonSerializer<T, S extends JSONValue> {

	public JsonSerializer() {
	}

	public abstract S serialize(T t, SerializationContext context);
}