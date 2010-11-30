package sk.seges.acris.generator.server.service.persist.db;

import java.io.File;

import sk.seges.acris.generator.server.dao.IFileDao;
import sk.seges.acris.generator.server.domain.api.FileData;
import sk.seges.acris.generator.server.service.persist.api.DataPersister;

public class DatabasePersister implements DataPersister {

	private IFileDao<FileData> fileDao;

	public DatabasePersister(IFileDao<FileData> fileDao) {
		this.fileDao = fileDao;
	}

	@Override
	public void writeTextToFile(String directory, String filename, String content) {
		FileData file = fileDao.getEntityInstance();
		file.setId(directory + File.separator + filename);
		file.setContent(content);
		fileDao.merge(file);
	}
}