package sk.seges.acris.security.server.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author psloboda
 */
public class DummySession {

	private Map<String, Object> attributes = new HashMap<String, Object>();
	
	private String id;
	
	public DummySession(String id) {
		this.id = id;
	}
	 
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	
	public void setAttribute(String key, Object attribute) {
		attributes.put(key, attribute);
	}
	
	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
