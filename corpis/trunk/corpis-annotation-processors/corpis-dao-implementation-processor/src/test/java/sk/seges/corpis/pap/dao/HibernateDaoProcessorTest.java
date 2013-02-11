package sk.seges.corpis.pap.dao;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.corpis.core.pap.dao.HibernateDaoProcessor;
import sk.seges.corpis.core.pap.dao.model.HibernateDaoType;
import sk.seges.corpis.shared.model.mock.jpa.JpaMockEntity;
import sk.seges.sesam.core.pap.test.AnnotationTest;

public class HibernateDaoProcessorTest extends AnnotationTest {

	@Test
	public void testMockEntityDao() {
		assertCompilationSuccessful(compileFiles(JpaMockEntity.class));
		assertOutput(getResourceFile(JpaMockEntity.class), getOutputFile(JpaMockEntity.class));
	}

	private File getOutputFile(Class<?> clazz) {
		HibernateDaoType hibernateDaoType = new HibernateDaoType(toMutable(clazz), processingEnv);
		return new File(OUTPUT_DIRECTORY, toPath(hibernateDaoType.getPackageName()) + "/" + hibernateDaoType.getSimpleName() + SOURCE_FILE_SUFFIX);
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] { new HibernateDaoProcessor() };
	}
}