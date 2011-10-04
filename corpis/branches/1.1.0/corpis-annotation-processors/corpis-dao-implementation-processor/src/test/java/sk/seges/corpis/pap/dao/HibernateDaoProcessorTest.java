package sk.seges.corpis.pap.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.corpis.core.pap.dao.DaoApiProcessor;
import sk.seges.corpis.core.pap.dao.HibernateDaoProcessor;
import sk.seges.corpis.core.pap.dao.model.DaoApiType;
import sk.seges.corpis.core.pap.dao.model.HibernateDaoType;
import sk.seges.corpis.shared.model.mock.api.MockEntity;
import sk.seges.corpis.shared.model.mock.jpa.JpaMockEntity;
import sk.seges.sesam.core.pap.test.AnnotationTest;

public class HibernateDaoProcessorTest extends AnnotationTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testMockEntityDao() {
		assertCompilationSuccessful(compileFiles(MockEntity.class));
		
		List<File> files = new ArrayList<File>();
		File file = getInterfaceOutputFile(MockEntity.class);
		files.add(file);
		
		addCollection(files, JpaMockEntity.class);
		
		assertCompilationSuccessful(compileFiles(files));
		assertOutput(getResourceFile(JpaMockEntity.class), getOutputFile(JpaMockEntity.class));
	}

	private File getInterfaceOutputFile(Class<?> clazz) {
		DaoApiType daoApiType = new DaoApiType(toMutable(clazz), processingEnv);
		return new File(OUTPUT_DIRECTORY, toPath(daoApiType.getPackageName()) + "/" + daoApiType.getSimpleName() + SOURCE_FILE_SUFFIX);
	}

	private File getOutputFile(Class<?> clazz) {
		HibernateDaoType hibernateDaoType = new HibernateDaoType(toMutable(clazz), processingEnv);
		return new File(OUTPUT_DIRECTORY, toPath(hibernateDaoType.getPackageName()) + "/" + hibernateDaoType.getSimpleName() + SOURCE_FILE_SUFFIX);
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] { new DaoApiProcessor(), new HibernateDaoProcessor() };
	}
}