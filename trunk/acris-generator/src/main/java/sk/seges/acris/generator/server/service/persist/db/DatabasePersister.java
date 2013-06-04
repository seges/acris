package sk.seges.acris.generator.server.service.persist.db;

import sk.seges.acris.generator.server.dao.IFileDao;
import sk.seges.acris.generator.server.domain.api.FileData;
import sk.seges.acris.generator.server.domain.api.PersistentDataProvider;
import sk.seges.acris.generator.server.service.persist.api.DataPersister;

public class DatabasePersister implements DataPersister {

	public static final String SEPARATOR = "|";
	
	private IFileDao<FileData> fileDao;

	public DatabasePersister(IFileDao<FileData> fileDao) {
		this.fileDao = fileDao;
	}

	@Override
	public void writeTextToFile(PersistentDataProvider persistentDataProvider) {
		FileData file = fileDao.getEntityInstance();
		file.setPath(persistentDataProvider.getWebId() + SEPARATOR + persistentDataProvider.getId());
		file.setContent(persistentDataProvider.getContent());
		fileDao.merge(file);
	}
}