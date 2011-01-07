/**
 * 
 */
package sk.seges.corpis.ie.server.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author ladislav.gazo
 */
public class HandlerContext {
	protected Map<String, Object> contextMap;
	
	public HandlerContext() {
		contextMap = new HashMap<String, Object>();
	}
	
	public void injectInto(HandlerContext context) {
		for(Entry<String, Object> entry : contextMap.entrySet()) {
			if(context.contextMap.containsKey(entry.getKey())) {
				continue;
			}
			
			context.contextMap.put(entry.getKey(), entry.getValue());
		}
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T get(String key) {
		return (T) contextMap.get(key);
	}
	
	protected void put(String key, Object value) {
		contextMap.put(key, value);
	}
}
