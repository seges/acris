package sk.seges.acris.generator.server.manager.api;

import sk.seges.acris.site.client.json.params.WebParams.OfflineMode;

public interface OfflineWebSettings {

	boolean supportsAutodetectMode();

	boolean publishOnSaveEnabled();

    OfflineMode getOfflineMode();
}
