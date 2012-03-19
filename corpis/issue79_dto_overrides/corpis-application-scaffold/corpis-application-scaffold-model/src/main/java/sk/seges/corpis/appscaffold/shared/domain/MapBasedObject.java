/**
 * 
 */
package sk.seges.corpis.appscaffold.shared.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author ladislav.gazo
 */
public class MapBasedObject implements Serializable {
	private static final long serialVersionUID = -1383864308128396L;
	
	protected Map<String, Object> map = new HashMap<String, Object>();
	
	@SuppressWarnings("unchecked")
	protected <T> T get(String key) {
		return (T) map.get(key);
	}
	
	public void set(String key, Object value) {
		map.put(key, value);
	}
	
	public Set<Entry<String, Object>> entrySet() {
		return map.entrySet();
	}
	
	public Map<String, Object> map() {
		return map;
	}
}
