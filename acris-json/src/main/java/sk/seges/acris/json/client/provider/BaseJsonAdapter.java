package sk.seges.acris.json.client.provider;


import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public abstract class BaseJsonAdapter<T> extends JsonAdapter<T, JSONValue> {

	protected static final String TEXT_PROPERTY_EXPRESSION = "$t";

	protected String getStringValue(JSONValue jsonValue, String property) {
		JSONObject jsonObject = jsonValue.isObject();
		
		if (jsonObject == null) {
			return null;
		}
		
		if (!jsonObject.containsKey(property)) {
			return null;
		}

		JSONValue jsonObjectValue = jsonObject.get(property);

		if (jsonObjectValue.isString() != null) {
			return jsonObjectValue.isString().stringValue();
		}
		
		JSONObject jsonStringObject = jsonObjectValue.isObject();
		if (jsonStringObject == null) {
			return null;
		}

		JSONString jsonString1Try = jsonStringObject.isString();

		if (jsonString1Try != null) {
			return jsonString1Try.stringValue();
		}

		if (!jsonStringObject.containsKey(TEXT_PROPERTY_EXPRESSION)) {
			return null;
		}

		JSONString jsonString = jsonStringObject.get(TEXT_PROPERTY_EXPRESSION)
				.isString();

		if (jsonString == null) {
			return null;
		}

		return jsonString.stringValue();
	}
}