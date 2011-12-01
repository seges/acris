package sk.seges.sesam.pap.test.selenium.processor.accessor;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

import sk.seges.sesam.core.pap.accessor.SingleAnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTestCase;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumSuiteTypeElement;

public class SeleniumTestAccessor extends SingleAnnotationAccessor<SeleniumTestCase> {

	public SeleniumTestAccessor(Element element, MutableProcessingEnvironment processingEnv) {
		super(element, SeleniumTestCase.class, processingEnv);
	}

	public List<SeleniumSuiteTypeElement> getSeleniumSuites() {
		
		if (!isValid()) {
			return new ArrayList<SeleniumSuiteTypeElement>();
		}
		
		Class<?>[] suiteRunners = annotation.suiteRunner();

		List<SeleniumSuiteTypeElement> result = new ArrayList<SeleniumSuiteTypeElement>();
				
		for (Class<?> suiteRunner: suiteRunners) {
			result.add(new SeleniumSuiteTypeElement(processingEnv.getElementUtils().getTypeElement(suiteRunner.getCanonicalName()), processingEnv));
		}

		return result;
	}
}
