package sk.seges.sesam.pap.configuration;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.sesam.core.pap.model.OutputClass;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.test.AnnotationTest;
import sk.seges.sesam.pap.configuration.api.TestConfiguration;

public class ConfigurationProcessorTest extends AnnotationTest {

	@Test
	public void testMockEntityDao() {
		assertCompilationSuccessful(compileFiles(TestConfiguration.class));
		assertOutput(getResourceFile(TestConfiguration.class), getOutputFile(TestConfiguration.class));
	}

	private File getOutputFile(Class<?> clazz) {
		OutputClass inputClass = new OutputClass(clazz.getPackage().getName(), clazz.getSimpleName());
		NamedType outputClass = ConfigurationProcessor.getOutputClass(inputClass);
		return new File(OUTPUT_DIRECTORY, toPath(outputClass.getPackageName()) + "/" + outputClass.getSimpleName() + SOURCE_FILE_SUFFIX);
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
			new ConfigurationProcessor()
		};
	}		

}
