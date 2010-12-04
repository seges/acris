package sk.seges.acris.generator.server.service.persist.api;

public interface DataPersister {

	void writeTextToFile(String directory, String filename, String content);
}