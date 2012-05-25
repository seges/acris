package sk.seges.acris.site.server.manager.data;

import sk.seges.acris.site.shared.domain.api.ParameterData;

public class StringParameterData implements ParameterData {

	private static final long serialVersionUID = 1166813050433118402L;

	private String key;
	private String value;

	public StringParameterData(String key, String value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
}
