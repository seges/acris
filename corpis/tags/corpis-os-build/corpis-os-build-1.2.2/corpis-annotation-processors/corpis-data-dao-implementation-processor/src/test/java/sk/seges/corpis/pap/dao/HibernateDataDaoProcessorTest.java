package sk.seges.corpis.pap.dao;

import java.io.File;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import org.junit.Test;

import sk.seges.corpis.MockEntity;
import sk.seges.corpis.appscaffold.shared.annotation.Definition;
import sk.seges.corpis.core.pap.dao.DaoApiProcessor;
import sk.seges.corpis.core.pap.dao.HibernateDataDaoProcessor;
import sk.seges.corpis.core.pap.dao.model.HibernateDataDaoType;
import sk.seges.corpis.pap.dao.configuration.MockHibernateDataDaoConfiguration;
import sk.seges.corpis.shared.model.MockEntityData;
import sk.seges.sesam.core.pap.test.AnnotationTest;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class HibernateDataDaoProcessorTest extends AnnotationTest {

	@Definition(MockEntity.class)
	class MockEntityDefinition {
		
	}
	
	@Test
	public void testMockEntityDao() {
		assertCompilationSuccessful(compileFiles(MockEntityData.class));
		assertCompilationSuccessful(compileFiles(MockHibernateDataDaoConfiguration.class));
		
		//TODO: fixme
		//assertOutput(getResourceFile(JpaMockEntity.class), getOutputFile(MockEntityDefinition.class));
	}

	private File getOutputFile(Class<?> clazz) {
		HibernateDataDaoType hibernateDaoType = new HibernateDataDaoType(toMutable(clazz), processingEnv);
		return new File(OUTPUT_DIRECTORY, toPath(hibernateDaoType.getPackageName()) + "/" + hibernateDaoType.getSimpleName() + SOURCE_FILE_SUFFIX);
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] { new DaoApiProcessor(), new HibernateDataDaoProcessor() };
	}
}