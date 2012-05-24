package sk.seges.acris.generator.server.manager.data;

import sk.seges.acris.site.client.json.params.WebParams;
import sk.seges.acris.site.shared.domain.api.ParameterData;

public enum OfflineGeneratorParameter implements ParameterData {

	INACTIVE_PROCESSORS(WebParams.OFFLINE_POST_PROCESSOR_INACTIVE),
	INACTIVE_INDEX_PROCESSORS(WebParams.OFFLINE_INDEX_PROCESSOR_INACTIVE),
	AUTODETECT_MODE(WebParams.OFFLINE_AUTODETECT_MODE),
	PUBLISH_ON_SAVE_ENABLED(WebParams.PUBLISH_ON_SAVE_ENABLED);

	private String key;

	OfflineGeneratorParameter(String key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return key;
	}
}
