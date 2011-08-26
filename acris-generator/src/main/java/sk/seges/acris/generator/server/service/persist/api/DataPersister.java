package sk.seges.acris.generator.server.service.persist.api;

import sk.seges.acris.generator.shared.domain.api.PersistentDataProvider;

public interface DataPersister {

	void writeTextToFile(PersistentDataProvider persistentDataProvider);
}