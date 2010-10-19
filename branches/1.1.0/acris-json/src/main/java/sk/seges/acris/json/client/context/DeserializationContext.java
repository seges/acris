package sk.seges.acris.json.client.context;

import java.util.HashMap;
import java.util.Map;

import sk.seges.acris.json.client.IJsonizer;

public class DeserializationContext {

	private Map<String, String> attributes = new HashMap<String, String>();
	private IJsonizer jsonizer;

	public IJsonizer getJsonizer() {
		return jsonizer;
	}

	public void setJsonizer(IJsonizer jsonizer) {
		this.jsonizer = jsonizer;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public String putAttribute(String key, String value) {
		return attributes.put(key, value);
	}

	public String getAttribute(String key) {
		return attributes.get(key);
	}
}
