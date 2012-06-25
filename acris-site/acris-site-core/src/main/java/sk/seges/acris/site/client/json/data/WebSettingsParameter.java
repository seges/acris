package sk.seges.acris.site.client.json.data;

import sk.seges.acris.site.client.json.params.WebParams;
import sk.seges.acris.site.shared.domain.api.ParameterData;

public enum WebSettingsParameter implements ParameterData {

	PRODUCT_LIST_SORT_ENABLED(WebParams.PRODUCT_LIST_SORT_ENABLED);

	private String key;

	WebSettingsParameter(String key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return key;
	}
}