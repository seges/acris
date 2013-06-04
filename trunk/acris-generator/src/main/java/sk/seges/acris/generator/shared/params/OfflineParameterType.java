package sk.seges.acris.generator.shared.params;

import sk.seges.acris.site.shared.domain.api.ParameterData;

public enum OfflineParameterType implements ParameterData {

	INACTIVE_PROCESSORS("offlinePostProcessorInactive"),
	INACTIVE_INDEX_PROCESSORS("offlineIndexProcessorInactive"),
	AUTODETECT_MODE("offlineAutodetectMode"),
	PUBLISH_ON_SAVE_ENABLED("publishOnSaveEnabled");

	private String key;

	OfflineParameterType(String key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return key;
	}
}
