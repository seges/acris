package sk.seges.acris.generator.server.service.persist.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sk.seges.acris.core.server.utils.io.StringFile;
import sk.seges.acris.generator.server.service.GeneratorService;
import sk.seges.acris.generator.server.service.persist.api.DataPersister;

public class FilePersister implements DataPersister {

	private static Log log = LogFactory.getLog(GeneratorService.class);

	@Override
	public void writeTextToFile(String directory, String filename, String content) {
		File dirFile = new File(directory);

		if (!dirFile.exists()) {
			if (!dirFile.mkdirs()) {
				throw new RuntimeException("Directory " + dirFile.getAbsolutePath() + " cannot be created.");
			}
		}

		StringFile file = createFile(dirFile, filename);

		if (log.isDebugEnabled()) {
			log.debug("Writing offline content to file " + file.getAbsolutePath());
		}

		try {
			file.writeTextToFile(content);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected StringFile createFile(File dirFile, String filename) {
		StringFile file = new StringFile(dirFile, filename);

		if (!file.exists()) {
			try {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
					if (log.isDebugEnabled()) {
						log.debug("Directory " + file.getParentFile().getAbsolutePath() + " does not exists. Creating a new file.");
					}
				}
				if (log.isDebugEnabled()) {
					log.debug("File " + file.getAbsolutePath() + " does not exists. Creating an empty new file.");
				}

				if (!file.createNewFile()) {
					log.error("Unable to create empty file " + file.getAbsolutePath() + ".");
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		return file;
	}

}
