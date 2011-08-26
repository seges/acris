package sk.seges.acris.generator.server.dao.twig;

import sk.seges.acris.generator.server.dao.IFileDao;
import sk.seges.acris.generator.server.domain.api.FileData;
import sk.seges.acris.generator.server.domain.twig.TwigFile;
import sk.seges.acris.security.server.dao.twig.AbstractTwigCrud;

import com.vercer.engine.persist.ObjectDatastore;


public class TwigFileDao extends AbstractTwigCrud<FileData> implements IFileDao<FileData> {

	public TwigFileDao(ObjectDatastore datastore) {
		super(datastore, TwigFile.class);
	}

	@Override
	public TwigFile getEntityInstance() {
		return new TwigFile();
	}
}
