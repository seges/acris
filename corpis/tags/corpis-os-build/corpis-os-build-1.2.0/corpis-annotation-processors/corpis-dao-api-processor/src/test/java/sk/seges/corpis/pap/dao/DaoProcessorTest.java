package sk.seges.corpis.pap.dao;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.corpis.core.pap.dao.DaoApiProcessor;
import sk.seges.corpis.core.pap.dao.model.DaoApiType;
import sk.seges.corpis.shared.model.mock.MockEntity;
import sk.seges.sesam.core.pap.test.AnnotationTest;

public class DaoProcessorTest extends AnnotationTest {

	@Test
	public void testMockEntityDao() {
		assertCompilationSuccessful(compileFiles(MockEntity.class));
		assertOutput(getResourceFile(MockEntity.class), getOutputFile(MockEntity.class));
	}

	private File getOutputFile(Class<?> clazz) {
		DaoApiType daoApiType = new DaoApiType(toMutable(clazz), processingEnv);
		return new File(OUTPUT_DIRECTORY, toPath(daoApiType.getPackageName()) + "/" + daoApiType.getSimpleName() + SOURCE_FILE_SUFFIX);
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] { new DaoApiProcessor() };
	}
}