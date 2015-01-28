package sk.seges.acris.generator.shared.params;

import sk.seges.acris.site.client.json.params.WebParams;
import sk.seges.acris.site.shared.domain.api.ParameterData;

public enum OfflineParameterType implements ParameterData {

	AUTODETECT_MODE(WebParams.OFFLINE_AUTODETECT_MODE),
	PUBLISH_ON_SAVE_ENABLED(WebParams.PUBLISH_ON_SAVE_ENABLED),
    OFFLINE_MODE(WebParams.OFFLINE_MODE);

	private String key;

	OfflineParameterType(String key) {
		this.key = key;
		
	}

	@Override
	public String getKey() {
		return key;
	}
}
