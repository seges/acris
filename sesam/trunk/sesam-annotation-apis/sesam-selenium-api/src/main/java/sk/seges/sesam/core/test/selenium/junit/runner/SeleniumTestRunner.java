package sk.seges.sesam.core.test.selenium.junit.runner;

import java.util.List;

import org.junit.Test;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTest;

public class SeleniumTestRunner extends BlockJUnit4ClassRunner {

	public SeleniumTestRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	@Override
	protected void validateTestMethods(List<Throwable> errors) {
		validatePublicVoidNoArgMethods(Test.class, false, errors);
		validatePublicVoidNoArgMethods(SeleniumTest.class, false, errors);
	}

	@Override
	protected List<FrameworkMethod> computeTestMethods() {
		List<FrameworkMethod> annotatedMethods = getTestClass().getAnnotatedMethods(Test.class);
		annotatedMethods.addAll(getTestClass().getAnnotatedMethods(SeleniumTest.class));
		
		return annotatedMethods;
	}
}