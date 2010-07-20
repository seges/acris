package sk.seges.acris.json.client;

import java.util.Collection;

import sk.seges.acris.json.client.context.DeserializationContext;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONValue;

public interface IJsonizer {
	
	<T> T fromJson(JSONValue jsonValue, T instance);

	<T> T fromJson(JSONValue jsonValue, Class<T> clazz);
 
	<T> T fromJson(String json, Class<T> clazz);

	<T> T fromJson(String json, String field, Class<T> clazz);

	<T> T fromJson(JSONValue jsonValue, T instance, DeserializationContext deserializationContext);

	<T> T fromJson(JSONValue jsonValue, Class<T> clazz, DeserializationContext deserializationContext);

	<T, S extends Collection<T>> S fromJson(JSONArray jsonArray, Class<T> clazz, S result, DeserializationContext deserializationContext);

	<T, S extends Collection<T>> S fromJson(JSONArray jsonArray, Class<T> clazz, Class<S> collectionClazz, DeserializationContext deserializationContext);

	<T> boolean supports(JSONValue jsonValue, Class<T> clazz);

	void setJsonizerContext(JsonizerContext jsonizerContext);
}