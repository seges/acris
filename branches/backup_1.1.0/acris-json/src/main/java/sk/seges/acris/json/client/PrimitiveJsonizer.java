package sk.seges.acris.json.client;

import java.util.Collection;

import sk.seges.acris.json.client.context.DeserializationContext;
import sk.seges.acris.json.client.deserialization.JsonDeserializer;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public abstract class PrimitiveJsonizer implements IJsonizer {

	protected JsonizerContext jsonizerContext;

	@Override
	public <T> T fromJson(JSONValue jsonValue, Class<T> clazz) {
		DeserializationContext deserializationContext = new DeserializationContext();
		deserializationContext.setJsonizer(this);
		return fromJson(jsonValue, clazz, deserializationContext);
	}

	public <T> T fromJson(JSONValue jsonValue, T instance) {
		DeserializationContext deserializationContext = new DeserializationContext();
		deserializationContext.setJsonizer(this);
		return fromJson(jsonValue, instance, deserializationContext);
	}

	@Override
	public <T> T fromJson(String json, Class<T> clazz) {
		return fromJson(JSONParser.parse(json), clazz);
	}
	
	@Override
	public <T> T fromJson(String json, String field, Class<T> clazz) {
		JSONValue value = JSONParser.parse(json);
		if (value == null || value.isObject() == null) {
			return null;
		}
		return fromJson(value.isObject().get(field), clazz);
	}

	protected JSONValue get(JSONObject jsonObject, String[] fields) {
		
		if (fields == null || fields.length == 0) {
			return null;
		}
		
		JSONValue result = jsonObject.get(fields[0]);
		
		for (int i = 1; i < fields.length; i++) {
			
			if (result != null && result.isObject() != null) {
				result = result.isObject().get(fields[i]);
			}
		}
		
		return result;
	}
	
	@Override
	public void setJsonizerContext(JsonizerContext jsonizerContext) {
		this.jsonizerContext = jsonizerContext;
	}

	@SuppressWarnings("unchecked")
	public <T> T fromJson(JSONValue jsonValue, Class<T> clazz, DeserializationContext deserializationContext) {
		return (T) _fromJson(jsonValue, clazz, deserializationContext);
	}

	@SuppressWarnings("unchecked")
	public <T> T __fromJson(JSONValue jsonValue, Class<?> clazz, DeserializationContext deserializationContext) {
		return (T) _fromJson(jsonValue, (Class<T>)clazz, deserializationContext);
	}

	public <T> Object _fromJson(JSONValue jsonValue, Class<T> clazz, DeserializationContext deserializationContext) {

		JsonDeserializer<T, JSONValue> deserializer = jsonizerContext.getDeserializer(clazz);
		if (deserializer != null) {
			return deserializer.deserialize(jsonValue, deserializationContext);
		}

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
				return doubleValue.intValue();
			} else if (clazz.getName().equals(Long.class.getName())) {
				return doubleValue.longValue();
			} else if (clazz.getName().equals(Float.class.getName())) {
				return doubleValue.floatValue();
			} else if (clazz.getName().equals(Double.class.getName())) {
				return doubleValue.doubleValue();
			}
		}

		if (clazz.getName().equals(Boolean.class.getName()) && jsonValue.isBoolean() != null) {
			return jsonValue.isBoolean().booleanValue();
		}

		return null;
	}

	protected <T, S extends Collection<T>> S createInstance(Class<S> collectionClazz) {
		InstanceCreator<S> instanceCreator = jsonizerContext.getInstanceCreator(collectionClazz);

		if (instanceCreator == null) {
			return null;
		}

		return instanceCreator.createInstance(collectionClazz);
	}
	
	public <Z, S extends Collection<Z>> S fromJson(JSONArray jsonArray, Class<Z> clazz, S result,
			DeserializationContext deserializationContext) {
		
		if (result == null) {
			return null;
		}

		for (int i = 0; i < jsonArray.size(); i++) {
			Z t = fromJson(jsonArray.get(i), clazz, deserializationContext);
			result.add(t);
		}

		return result;
	}

	public <T, S extends Collection<T>> S fromJson(JSONArray jsonArray, Class<T> clazz,
			Class<S> collectionClazz, DeserializationContext deserializationContext) {

		return fromJson(jsonArray, clazz, createInstance(collectionClazz), deserializationContext);
	}

	@Override
	public <T> boolean supports(JSONValue jsonValue, Class<T> clazz) {
		if (jsonValue.isNull() != null) {
			return true;
		}
		if (clazz.getName().equals(String.class.getName()) && jsonValue.isString() != null) {
			return true;
		}
		if (jsonValue.isNumber() != null) {
			if (clazz.getName().equals(Byte.class.getName()) || clazz.getName().equals(Short.class.getName())
					|| clazz.getName().equals(Integer.class.getName()) || clazz.getName().equals(Long.class.getName())
					|| clazz.getName().equals(Float.class.getName()) || clazz.getName().equals(Double.class.getName())) {
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