package sk.seges.corpis.shared.domain.invoice;

import java.util.HashMap;
import java.util.Map;

public class PriceConditionContext {
	private Map<String, Object> context = new HashMap<String, Object>();
	
	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T) context.get(key);
	}

	public void put(String key, Object value) {
		context.put(key, value);
	}
}