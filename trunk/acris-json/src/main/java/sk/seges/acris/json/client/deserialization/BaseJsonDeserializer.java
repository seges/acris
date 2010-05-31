package sk.seges.acris.json.client.deserialization;



import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public abstract class BaseJsonDeserializer<T> extends JsonDeserializer<T, JSONValue> {

	protected static final String TEXT_PROPERTY_EXPRESSION = "$t";

	protected String _deserialize(JSONValue jsonValue) {
		JSONObject jsonObject = jsonValue.isObject();
		
		if (jsonObject == null) {
			return null;
		}
		
		if (jsonObject.isString() != null) {
			return jsonObject.isString().stringValue();
		}
		
		JSONObject jsonStringObject = jsonObject.isObject();

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