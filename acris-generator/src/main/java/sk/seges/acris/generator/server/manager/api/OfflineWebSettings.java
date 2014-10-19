package sk.seges.acris.generator.server.manager.api;

import sk.seges.acris.generator.client.json.params.OfflineClientWebParams;

import java.util.Set;

public interface OfflineWebSettings {

	boolean supportsAutodetectMode();

	boolean publishOnSaveEnabled();

    OfflineClientWebParams.OfflineMode getOfflineMode();
}
