package sk.seges.acris.site.shared.domain.dto;

import sk.seges.acris.site.shared.domain.api.ParameterData;

public class ParameterDTO implements ParameterData {

	private String key;
	
	@Override
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}