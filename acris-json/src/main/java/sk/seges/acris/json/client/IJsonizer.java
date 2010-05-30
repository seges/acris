package sk.seges.acris.json.client;

import com.google.gwt.json.client.JSONValue;

public interface IJsonizer<T> {
	boolean jsonize(JSONValue jsonValue, T t);
}
