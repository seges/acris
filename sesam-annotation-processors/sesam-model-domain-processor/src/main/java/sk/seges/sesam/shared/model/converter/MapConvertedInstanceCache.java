package sk.seges.sesam.shared.model.converter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MapConvertedInstanceCache implements ConvertedInstanceCache {

	private Map<Class<?>, Map<Serializable, Object>> instanceCache = new HashMap<Class<?>, Map<Serializable, Object>>();
	
	@SuppressWarnings("unchecked")
	@Override
	public <S> S getInstance(Class<S> instanceClass, Serializable id) {
		Map<Serializable, Object> map = instanceCache.get(instanceClass);
		if (map == null) {
			return null;
		}
		return (S) map.get(id);
	}

	@Override
	public <S> S putInstance(S instance, Serializable id) {
		Map<Serializable, Object> instances = (Map<Serializable, Object>) instanceCache.get(instance.getClass());
		
		if (instances == null) {
			instances = new HashMap<Serializable, Object>();
			instanceCache.put(instance.getClass(), instances);
		}
		
		instances.put(id, instance);
		
		return instance;
	}
}