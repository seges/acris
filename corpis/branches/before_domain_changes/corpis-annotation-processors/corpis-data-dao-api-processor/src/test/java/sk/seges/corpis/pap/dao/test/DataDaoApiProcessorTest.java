package sk.seges.corpis.pap.dao.test;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.corpis.core.pap.dao.DataDaoApiProcessor;
import sk.seges.corpis.core.pap.dao.model.DataDaoApiType;
import sk.seges.corpis.pap.dao.configuration.MockDataDaoConfiguration;
import sk.seges.corpis.pap.dao.mock.MockEntity;
import sk.seges.corpis.pap.dao.mock.SimpleEntity;
import sk.seges.sesam.core.pap.test.AnnotationTest;

 
public class DataDaoApiProcessorTest extends AnnotationTest {

	@Test
	public void testDataDaoConfiguration() {
		assertCompilationSuccessful(compileFiles(MockDataDaoConfiguration.class));
		assertOutput(getResourceFile(MockEntity.class), getOutputFile(MockEntity.class));
	}

	@Test
	public void testSimpleConfiguration() {
		assertCompilationSuccessful(compileFiles(SimpleEntity.class));
		assertOutput(getResourceFile(SimpleEntity.class), getOutputFile(SimpleEntity.class));
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] { new DataDaoApiProcessor() };
	}
	
	private File getOutputFile(Class<?> clazz) {
		DataDaoApiType daoApiType = new DataDaoApiType(toMutable(clazz), processingEnv);
		return new File(OUTPUT_DIRECTORY, toPath(daoApiType.getPackageName()) + "/" + daoApiType.getSimpleName() + SOURCE_FILE_SUFFIX);
	}
}