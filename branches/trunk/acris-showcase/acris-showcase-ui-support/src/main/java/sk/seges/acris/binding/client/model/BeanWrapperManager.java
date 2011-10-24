package sk.seges.acris.binding.client.model;

import java.util.HashMap;
import java.util.Map;

import sk.seges.acris.binding.client.wrappers.BeanWrapper;

/**
 * Used for handle cyclic dependencies
 * @author Peter Simun (simun@seges.sk)
 */
public class BeanWrapperManager {

	private static final Map<Object, BeanWrapper<?>> generatedWrappers = new HashMap<Object, BeanWrapper<?>>();

	@SuppressWarnings("unchecked")
	public static <T> BeanWrapper<T> getWrapper(T o) {
		return (BeanWrapper<T>) generatedWrappers.get(o);
	}

	public static void putWrapper(Object o, BeanWrapper<?> bw) {
		generatedWrappers.put(o, bw);
	}
}
