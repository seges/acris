package sk.seges.acris.generator.client.json.params;

import sk.seges.acris.domain.params.ContentParameters;
import sk.seges.acris.site.client.json.params.WebParams.OfflineMode;

public interface OfflineClientWebParams extends ContentParameters {

	Boolean isPublishOnSaveEnabled();

	void setPublishOnSaveEnabled(boolean publishOnSaveEnabled);

	Boolean supportsAutodetectMode();

    OfflineMode getOfflineMode();

    void setOfflineMode(OfflineMode offlineMode);
}