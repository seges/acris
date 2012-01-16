/**
   Copyright 2011 Seges s.r.o.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package sk.seges.sesam.shared.model.converter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MapConvertedInstanceCache implements ConvertedInstanceCache {

	private Map<Class<?>, Map<Serializable, Object>> instanceIdCache = new HashMap<Class<?>, Map<Serializable, Object>>();
	private Map<Class<?>, Map<Object, Object>> instancesCache = new HashMap<Class<?>, Map<Object, Object>>();

	@SuppressWarnings("unchecked")
	public <S> S getInstance(Object source) {
		Map<Object, Object> map = instancesCache.get(source.getClass());

		if (map == null) {
			return null;
		}
		return (S) map.get(source);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <S> S getInstance(Class<S> instanceClass, Serializable id) {
		Map<Serializable, Object> map = instanceIdCache.get(instanceClass);
		if (map == null) {
			return null;
		}
		return (S) map.get(id);
	}

	@Override
	public <S> S putInstance(Object source, S result) {
		Map<Object, Object> instances = (Map<Object, Object>) instancesCache.get(source.getClass());

		if (instances == null) {
			instances = new HashMap<Object, Object>();
			instancesCache.put(source.getClass(), instances);
		}

		instances.put(source, result);
		
		return result;
	}
	
	@Override
	public <S> S putInstance(S result, Serializable id) {
		Map<Serializable, Object> instances = (Map<Serializable, Object>) instanceIdCache.get(result.getClass());
		
		if (instances == null) {
			instances = new HashMap<Serializable, Object>();
			instanceIdCache.put(result.getClass(), instances);
		}
		
		instances.put(id, result);
		
		return result;
	}
}