package sk.seges.acris.generator.client.json.params;

import sk.seges.acris.domain.params.ContentParameters;

public interface OfflineClientWebParams extends ContentParameters {

	Boolean isPublishOnSaveEnabled();

	void setPublishOnSaveEnabled(boolean publishOnSaveEnabled);

}