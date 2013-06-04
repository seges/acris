package sk.seges.acris.json.client;

import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.AbstractSequentialList;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import org.gwttime.time.DateTime;

import sk.seges.acris.json.client.deserialization.DateTimeDeserializer;
import sk.seges.acris.json.client.deserialization.DoubleDeserializer;
import sk.seges.acris.json.client.deserialization.IntegerDeserializer;
import sk.seges.acris.json.client.deserialization.JsonDeserializer;
import sk.seges.acris.json.client.deserialization.LongDeserializer;
import sk.seges.acris.json.client.instantiators.CollectionInstanceCreator;
import sk.seges.acris.json.client.instantiators.MapInstanceCreator;
import sk.seges.acris.json.client.instantiators.SequentialListInstanceCreator;
import sk.seges.acris.json.client.instantiators.SortedMapInstanceCreator;
import sk.seges.acris.json.client.instantiators.SortedSetInstanceCreator;
import sk.seges.acris.json.client.serialization.JsonSerializer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;

public class JsonizerBuilder {

	private JsonizerContext jsonizerContext = new JsonizerContext();

	public IJsonizer create() {
		IJsonizer jsonizer = GWT.create(JsonizerProvider.class);

		registerDefaultDeserializers();
		registerDefaultInstantators();

		jsonizer.setJsonizerContext(jsonizerContext);
		return jsonizer;
	}

	protected void registerDefaultDeserializers() {
		registerDeserializer(DateTime.class, new DateTimeDeserializer());
		registerDeserializer(Double.class, new DoubleDeserializer());
		registerDeserializer(Integer.class, new IntegerDeserializer());
		registerDeserializer(Long.class, new LongDeserializer());
	}

	protected void registerDefaultInstantators() {
		registerInstanceCreator(Collection.class, new CollectionInstanceCreator());
		registerInstanceCreator(Set.class, new CollectionInstanceCreator());

		registerInstanceCreator(SortedSet.class, new SortedSetInstanceCreator());

		registerInstanceCreator(List.class, new CollectionInstanceCreator());

		registerInstanceCreator(AbstractSet.class, new CollectionInstanceCreator());
		registerInstanceCreator(AbstractCollection.class, new CollectionInstanceCreator());
		registerInstanceCreator(AbstractList.class, new CollectionInstanceCreator());

		registerInstanceCreator(AbstractSequentialList.class, new SequentialListInstanceCreator());

		registerInstanceCreator(Map.class, new MapInstanceCreator());
		registerInstanceCreator(AbstractMap.class, new MapInstanceCreator());

		registerInstanceCreator(SortedMap.class, new SortedMapInstanceCreator());
	}

	public void registerPropertyType(Class<?> clazz, String property, Class<?> targetType) {
		jsonizerContext.registerPropertyType(clazz, property, targetType);
	}

	public Class<?> getPropertyType(Class<?> clazz, String property) {
		return jsonizerContext.getPropertyType(clazz, property);
	}

	public void registerInstanceCreator(Class<?> clazz, InstanceCreator<?> instanceCreator) {
		jsonizerContext.registerInstanceCreator(clazz, instanceCreator);
	}

	public <T> InstanceCreator<T> getInstanceCreator(Class<T> clazz) {
		return jsonizerContext.getInstanceCreator(clazz);
	}

	public <T, S extends JSONValue> void registerSerializer(Class<T> targetClass, JsonSerializer<T, S> adapter) {
		jsonizerContext.registerSerializer(targetClass, adapter);
	}

	public <T, S extends JSONValue> JsonSerializer<T, S> getSerializer(Class<T> targetClass) {
		return jsonizerContext.getSerializer(targetClass);
	}

	public <T, S extends JSONValue> void registerDeserializer(Class<T> targetClass, JsonDeserializer<T, S> adapter) {
		jsonizerContext.registerDeserializer(targetClass, adapter);
	}

	public <T, S extends JSONValue> JsonDeserializer<T, S> getDeserializer(Class<T> targetClass) {
		return jsonizerContext.getDeserializer(targetClass);
	}
}