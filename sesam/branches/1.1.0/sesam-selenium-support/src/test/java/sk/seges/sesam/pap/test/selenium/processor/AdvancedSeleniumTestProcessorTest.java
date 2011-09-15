package sk.seges.sesam.pap.test.selenium.processor;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.sesam.core.pap.model.OutputClass;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.test.AnnotationTest;
import sk.seges.sesam.core.test.selenium.runner.MockRunner;
import sk.seges.sesam.core.test.selenium.usecase.AdvancedMockSelenise;

public class AdvancedSeleniumTestProcessorTest extends AnnotationTest {

	@Test
	public void testTestConfiguration() {
		assertCompilationSuccessful(compileFiles(AdvancedMockSelenise.class, MockRunner.class));
		assertOutput(getResourceFile(AdvancedMockSelenise.class), getOutputFile(AdvancedMockSelenise.class));
	}

	private File getOutputFile(Class<?> clazz) {
		OutputClass inputClass = new OutputClass(clazz.getPackage().getName(), clazz.getSimpleName());
		NamedType outputClass = SeleniumTestConfigurationProcessor.getOutputClass(inputClass);
		return new File(OUTPUT_DIRECTORY, toPath(outputClass.getPackageName()) + "/" + outputClass.getSimpleName() + SOURCE_FILE_SUFFIX);
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
			new SeleniumTestConfigurationProcessor()
		};
	}
}