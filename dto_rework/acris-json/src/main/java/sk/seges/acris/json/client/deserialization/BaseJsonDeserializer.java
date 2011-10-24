package sk.seges.acris.json.client.deserialization;



import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public abstract class BaseJsonDeserializer<T> extends JsonDeserializer<T, JSONValue> {

	protected static final String TEXT_PROPERTY_EXPRESSION = "$t";

	protected String _deserialize(JSONValue jsonValue) {
		if (jsonValue.isString() != null) {
			return jsonValue.isString().stringValue();
		}

		JSONObject jsonObject = jsonValue.isObject();
		
		if (jsonObject == null) {
			return null;
		}
		
		if (!jsonObject.containsKey(TEXT_PROPERTY_EXPRESSION)) {
			return null;
		}

		JSONString jsonString = jsonObject.get(TEXT_PROPERTY_EXPRESSION).isString();

		if (jsonString == null) {
			return null;
		}

		return jsonString.stringValue();
	}
}