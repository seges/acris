package sk.seges.acris.generator.shared.params;

import sk.seges.acris.site.shared.domain.api.ParameterData;

public enum OfflineParameterType implements ParameterData {

	AUTODETECT_MODE("offlineAutodetectMode"),
	PUBLISH_ON_SAVE_ENABLED("publishOnSaveEnabled"),
    OFFLINE_MODE("offlineMode");

	private String key;

	OfflineParameterType(String key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return key;
	}
}
