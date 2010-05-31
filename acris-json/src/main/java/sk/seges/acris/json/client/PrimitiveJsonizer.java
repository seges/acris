package sk.seges.acris.json.client;

import java.util.HashSet;

import sk.seges.acris.json.client.context.DeserializationContext;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public abstract class PrimitiveJsonizer<T> implements IJsonizer<T> {

	protected JsonizerContext jsonizerContext;
	
	@Override
	public T fromJson(JSONValue jsonValue, Class<T> clazz) {
		DeserializationContext deserializationContext =new DeserializationContext();
		deserializationContext.setJsonizer(this);
		return (T) fromJson(jsonValue, clazz, deserializationContext);
	}

	@Override
	public T fromJson(String json, Class<T> clazz) {
		return fromJson(JSONParser.parse(json), clazz);
	}

	@Override
	public void setJsonizerContext(JsonizerContext jsonizerContext) {
		this.jsonizerContext = jsonizerContext;
	}

	@SuppressWarnings("unchecked")
	public T fromJson(JSONValue jsonValue, Class<T> clazz, DeserializationContext deserializationContext) {
		return (T)_fromJson(jsonValue, clazz, deserializationContext);
	}
	
	public Object _fromJson(JSONValue jsonValue, Class<T> clazz, DeserializationContext deserializationContext) {
		if (jsonValue.isNull() != null) {
			return null;
		}

		if (clazz.getName().equals(String.class.getName()) && jsonValue.isString() != null) {
			return jsonValue.isString().stringValue();
		}

		if (jsonValue.isNumber() != null) {
			Double doubleValue = jsonValue.isNumber().doubleValue();
			if (clazz.getName().equals(Byte.class.getName())) {
				return doubleValue.byteValue();
			} else if (clazz.getName().equals(Short.class.getName())) {
				return doubleValue.shortValue();
			} else if (clazz.getName().equals(Integer.class.getName())) {
				return doubleValue.byteValue();
			} else if (clazz.getName().equals(Long.class.getName())) {
				return doubleValue.longValue();
			} else if (clazz.getName().equals(Float.class.getName())) {
				return doubleValue.floatValue();
			} else if (clazz.getName().equals(Double.class.getName())) {
				return doubleValue.floatValue();
			}
		}

		if (clazz.getName().equals(Boolean.class.getName()) && jsonValue.isBoolean() != null) {
			return jsonValue.isBoolean().booleanValue();
		} 

		if (jsonValue.isArray() != null) {
			
			JSONArray jsonArray = jsonValue.isArray();
			
			HashSet<T> hashSet = new HashSet<T>();

			for (int i = 0; i < jsonArray.size(); i++) {
				T t = fromJson(jsonArray.get(i), clazz);
				hashSet.add(t);
			}
			
			return hashSet;
		}

		return null;
	}

	@Override
	public boolean supports(JSONValue jsonValue, Class<T> clazz) {
		if (jsonValue.isNull() != null) {
			return true;
		}
		if (clazz.getName().equals(String.class.getName()) && jsonValue.isString() != null) {
			return true;
		}
		if (jsonValue.isNumber() != null) {
			if (clazz.getName().equals(Byte.class.getName()) ||
				clazz.getName().equals(Short.class.getName()) ||
				clazz.getName().equals(Integer.class.getName()) ||
				clazz.getName().equals(Long.class.getName()) ||
				clazz.getName().equals(Float.class.getName()) ||
				clazz.getName().equals(Double.class.getName())) {
				return true;
			}
		}

		if (clazz.getName().equals(Boolean.class.getName()) && jsonValue.isBoolean() != null) {
			return true;
		} 
		
		if (jsonValue.isArray() != null) {
			return true;
		}
		
		return false;
	}
}