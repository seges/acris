package sk.seges.acris.json.client;

import sk.seges.acris.json.client.context.DeserializationContext;

import com.google.gwt.json.client.JSONValue;

public interface IJsonizer<T> {
	
	T fromJson(JSONValue jsonValue, T instance);

	T fromJson(JSONValue jsonValue, Class<T> clazz);

	T fromJson(String json, Class<T> clazz);

	T fromJson(JSONValue jsonValue, T instance, DeserializationContext deserializationContext);

	T fromJson(JSONValue jsonValue, Class<T> clazz, DeserializationContext deserializationContext);

	boolean supports(JSONValue jsonValue, Class<T> clazz);

	void setJsonizerContext(JsonizerContext jsonizerContext);
}