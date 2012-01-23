package sk.seges.corpis.pap.dao;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.corpis.appscaffold.model.pap.model.DomainDataInterfaceType;
import sk.seges.corpis.core.pap.dao.DaoApiProcessor;
import sk.seges.corpis.core.pap.dao.model.DaoApiType;
import sk.seges.corpis.server.dao.configuration.MockDaoConfiguration;
import sk.seges.corpis.shared.model.MockEntityData;
import sk.seges.corpis.shared.model.TypedMockEntityData;
import sk.seges.sesam.core.pap.test.AnnotationTest;

public class DaoProcessorTest extends AnnotationTest {

	@Test
	public void testMockEntityDao() {
		assertCompilationSuccessful(compileFiles(MockEntityData.class));
		assertOutput(getResourceFile(MockEntityData.class), getOutputFile(MockEntityData.class));
	}

	@Test
	public void testTypedMockEntityDao() {
		assertCompilationSuccessful(compileFiles(TypedMockEntityData.class));
		assertOutput(getResourceFile(MockEntityData.class), getOutputFile(MockEntityData.class));
	}

	@Test
	public void testDaoConfiguration() {
		assertCompilationSuccessful(compileFiles(MockDaoConfiguration.class));
		assertOutput(getResourceFile(MockEntityData.class), getOutputFile(MockEntityData.class));
	}
	
	private File getOutputFile(Class<?> clazz) {
		DaoApiType daoApiType = new DaoApiType(new DomainDataInterfaceType(processingEnv.getElementUtils().getTypeElement(clazz.getCanonicalName()), processingEnv), processingEnv);
		return new File(OUTPUT_DIRECTORY, toPath(daoApiType.getPackageName()) + "/" + daoApiType.getSimpleName() + SOURCE_FILE_SUFFIX);
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] { new DaoApiProcessor() };
	}
}