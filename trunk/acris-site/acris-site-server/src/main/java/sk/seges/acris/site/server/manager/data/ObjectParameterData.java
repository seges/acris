package sk.seges.acris.site.server.manager.data;

import sk.seges.acris.site.shared.domain.api.ParameterData;

public class ObjectParameterData implements ParameterData {

	private static final long serialVersionUID = -6799395402870066422L;

	private String key;
	private Object value;

	public ObjectParameterData(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public String getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}
}
