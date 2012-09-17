package sk.seges.acris.generator.server.service.persist.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sk.seges.acris.core.server.utils.io.StringFile;
import sk.seges.acris.generator.server.domain.api.PersistentDataProvider;
import sk.seges.acris.generator.server.service.GeneratorService;
import sk.seges.acris.generator.server.service.persist.api.DataPersister;

public class FilePersister implements DataPersister {

	private static Log log = LogFactory.getLog(GeneratorService.class);

	protected String rootDirectory;
	
	public FilePersister(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}
	
	@Override
	public void writeTextToFile(PersistentDataProvider persistentDataProvider) {
		File dirFile = new File(getRootDirectory(rootDirectory, persistentDataProvider));

		if (!dirFile.exists()) {
			if (!dirFile.mkdirs()) {
				throw new RuntimeException("Directory " + dirFile.getAbsolutePath() + " cannot be created.");
			}
		}

		if (log.isDebugEnabled()) {
			log.debug("Writing to the root directory: " + dirFile.getAbsolutePath());
		}
		
		String fileName = persistentDataProvider.getId();

		if (log.isDebugEnabled()) {
			log.debug("Output relative filename: " + fileName);
		}
		
		boolean exists = new File(dirFile, fileName).exists();

		StringFile file = createFile(dirFile, fileName);

		if (file == null) {
			return;
		}

		if (!exists && isRootIndexFile(fileName)) {
			//first time we generate offline here, create backup of the index.html for sure
			try {
				new StringFile(dirFile, "index_js.html").writeTextToFile(file.readTextFromFile());
			} catch (IOException ex) {
				log.warn("Trying to backup root index.html, but something has failed.", ex);
			}
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Writing offline content to file " + file.getAbsolutePath());
		}

		try {
			file.writeTextToFile(persistentDataProvider.getContent());
		} catch (IOException e) {
			throw new RuntimeException("Unable to write text to file " + file.getAbsolutePath(), e);
		}
	}

	protected boolean isRootIndexFile(String filename) {
		return filename.contains(File.separator);
		
	}
	
	protected String getRootDirectory(String rootDirectory, PersistentDataProvider persistentDataProvider) {
		return rootDirectory;
	}
	
	private boolean mkdirs(File file) {
		
		if (file.exists()) {
			return true;
		}

		File rootDir = file;
		
		while (rootDir.getParentFile() != null) {
			rootDir = rootDir.getParentFile();
			
			if (rootDir.exists()) {
				break;
			}
		}
		
		String subDir = file.getAbsolutePath().substring(rootDir.getAbsolutePath().length());
		
		if (subDir.startsWith(File.separator)) {
			subDir = subDir.substring(1);
		}
		
		String[] subDirs = subDir.split(File.separator);
		
		for (String dir: subDirs) {
			if (dir.length() > 0) {
				file = new File(rootDir, dir);
				if (!file.mkdir()) {
					log.warn("Unable to create directory " + dir + " in the " + rootDir.getAbsolutePath());
					return false;
				}
				
				rootDir = file;
			}
		}
		
		return true;
	}
	
	protected StringFile createFile(File dirFile, String filename) {
		StringFile file = new StringFile(dirFile, filename);

		if (!file.exists()) {
			try {
				if (!file.getParentFile().exists()) {
					if (log.isDebugEnabled()) {
						log.debug("Directory " + file.getParentFile().getAbsolutePath() + " does not exists. Creating a new file.");
					}
					if (!file.getParentFile().mkdirs()) {
						log.warn("Unable to create directory " + file.getParentFile().getAbsolutePath() + " Check the permissions!");
						//Just for logging purposes
						mkdirs(file.getParentFile());
					}
				}

				if (!file.createNewFile()) {
					log.error("Unable to create empty file " + file.getAbsolutePath() + ".");
				}
			} catch (IOException e) {
				log.error("IO exception occured while creating file " + file.getAbsolutePath() + ", dirname: " + dirFile.getAbsolutePath() 
						+ ", filename: " + filename, e);
				return null;
				//throw new RuntimeException(e);
			}
		}

		return file;
	}
}