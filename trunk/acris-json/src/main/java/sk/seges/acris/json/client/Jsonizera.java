package sk.seges.acris.json.client;

import sk.seges.acris.json.client.context.DeserializationContext;
import sk.seges.acris.json.client.deserialization.JsonDeserializer;
import sk.seges.acris.json.client.sample.data.FooSampler;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public class Jsonizera extends PrimitiveJsonizer<Object> {

	@Override
	public Object fromJson(JSONValue jsonValue, Class clazz, DeserializationContext deserializationContext) {
		if (jsonValue.isObject() != null && clazz.getName().equals(sk.seges.acris.json.client.sample.data.SampleData.class)) {
			JsonDeserializer<sk.seges.acris.json.client.sample.data.SampleData, JSONValue> deserializer = 
				jsonizerContext.getDeserializer(sk.seges.acris.json.client.sample.data.SampleData.class);

			if (deserializer != null) {
				return deserializer.deserialize(jsonValue, deserializationContext);
			} 
			
			JSONObject jsonObject = jsonValue.isObject();
			sk.seges.acris.json.client.sample.data.SampleData sample = new sk.seges.acris.json.client.sample.data.SampleData();
			deserializationContext = new DeserializationContext();
			deserializationContext.setJsonizer(this);
			deserializationContext.putAttribute("sk.seges.acris.json.client.annotation.Field_value", "");
			deserializationContext.putAttribute("sk.seges.acris.json.client.annotation.DateTimePattern_value", "y-M-d'T'H:m:s.SSSZ");
			sample.setData((String)fromJson(jsonObject.get("data"), String.class, deserializationContext));
			sample.setFooSampler((FooSampler)fromJson(jsonObject.get("fooSampler"), String.class, deserializationContext));
			return sample;
		}
		return null;
	}

	@Override
	public boolean supports(JSONValue jsonValue, Class clazz) {
		if (super.supports(jsonValue, clazz)) {
			return true;
		}
		if (jsonValue.isObject() != null  && clazz.getName().equals(sk.seges.acris.json.client.sample.data.FooSampler.class.getName())) {
			return true;
		}
		return false;
	}
}
