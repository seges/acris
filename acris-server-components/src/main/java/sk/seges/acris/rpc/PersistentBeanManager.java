package sk.seges.acris.rpc;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import sk.seges.sesam.dao.PagedResult;

public class PersistentBeanManager extends net.sf.gilead.core.PersistentBeanManager {

	public Object clone(Object object, boolean assignable) {
		//	Precondition checking
		//
		if (object == null) {
			return null;
		}

		//	Collection handling
		//
		if (object instanceof Collection) {
			return cloneCollection((Collection) object, assignable);
		} else if (object instanceof PagedResult<?>) {
			Object result = clone(((PagedResult<Object>) object).getResult());
			((PagedResult<Object>) object).setResult(result);

			return object;
		} else if (object instanceof Map) {
			return cloneMap((Map) object, assignable);
		} else if (object.getClass().isArray()) {
			//	Clone as a collection
			//
			Object[] array = (Object[]) object;
			Collection<?> result = cloneCollection(Arrays.asList(array), assignable);

			//	Get the result as an array (much more tricky !!!)
			//
			Class<?> componentType = object.getClass().getComponentType();
			Object[] copy = (Object[]) java.lang.reflect.Array.newInstance(componentType, array.length);
			return result.toArray(copy);
		} else {
			return clonePojo(object, assignable);
		}
	}
}
