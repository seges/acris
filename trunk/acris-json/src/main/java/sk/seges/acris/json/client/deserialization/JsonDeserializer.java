package sk.seges.acris.json.client.deserialization;

import sk.seges.acris.json.client.context.DeserializationContext;

import com.google.gwt.json.client.JSONValue;

public abstract class JsonDeserializer<T, S extends JSONValue> {

	public JsonDeserializer() {
	}

	public abstract T deserialize(S s, DeserializationContext context);
}