package sk.seges.sesam.pap.test.selenium.processor;

import java.io.File;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.test.AnnotationTest;
import sk.seges.sesam.core.test.selenium.runner.MockSuite;
import sk.seges.sesam.core.test.selenium.usecase.MockSelenise;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumSuiteRunnerTypeElement;

public class SeleniumTestConfigurationProcessorTest extends AnnotationTest {

	@Test
	public void testTestConfiguration() {
		assertCompilationSuccessful(compileFiles(MockSelenise.class, MockSuite.class));
		assertOutput(getResourceFile(MockSuite.class), getOutputFile(MockSuite.class));
	}

	private File getOutputFile(Class<?> clazz) {
		final MutableDeclaredType outputClass = toMutable(clazz).addClassSufix(SeleniumSuiteRunnerTypeElement.SUFFIX);
		return new File(OUTPUT_DIRECTORY, toPath(outputClass.getPackageName()) + "/" + outputClass.getSimpleName() + SOURCE_FILE_SUFFIX);
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
			new SeleniumTestRunnerProcessor()
		};
	}
}