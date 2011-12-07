package sk.seges.sesam.pap.test.selenium.processor.accessor;

import javax.lang.model.element.Element;

import sk.seges.sesam.core.pap.accessor.SingleAnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTestCase;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumSuiteRunnerType;

public class SeleniumTestCaseAccessor extends SingleAnnotationAccessor<SeleniumTestCase> {

	public SeleniumTestCaseAccessor(Element element, MutableProcessingEnvironment processingEnv) {
		super(element, SeleniumTestCase.class, processingEnv);
	}

	public boolean isAssignedToRunner(SeleniumSuiteRunnerType seleniumRunnerType) {
		Class<?>[] suiteRunners = annotation.suiteRunner();
		
		for (Class<?> suiteRunner: suiteRunners) {
			if (suiteRunner.getName().equals(seleniumRunnerType.getSeleniumSuite().getCanonicalName())) {
				return true;
			}
		}
		
		return false;
	}
}