/**
 * 
 */
package sk.seges.corpis.ie.server.domain;

import java.util.Map;
import java.util.Map.Entry;

/**
 * @author ladislav.gazo
 */
public class CSVHandlerContext {
	private static final String ROW = "row";
	
	protected Map<String, Object> contextMap;
	
	public void injectInto(CSVHandlerContext context) {
		for(Entry<String, Object> entry : contextMap.entrySet()) {
			if(context.contextMap.containsKey(entry.getKey())) {
				continue;
			}
			
			context.contextMap.put(entry.getKey(), entry.getValue());
		}

	}
	
	public Integer getRow() {
		return (Integer) contextMap.get(ROW);
	}
	
	public void setRow(Integer row) {
		contextMap.put(ROW, row);
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T get(String key) {
		return (T) contextMap.get(key);
	}
	
	protected void put(String key, Object value) {
		contextMap.put(key, value);
	}
}
