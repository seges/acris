package sk.seges.sesam.pap.test.selenium.processor.accessor;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.accessor.SingleAnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTestCase;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumSuiteType;

public class SeleniumTestAccessor extends SingleAnnotationAccessor<SeleniumTestCase> {

	public SeleniumTestAccessor(Element element, MutableProcessingEnvironment processingEnv) {
		super(element, SeleniumTestCase.class, processingEnv);
	}

	public List<SeleniumSuiteType> getSeleniumSuites() {
		
		if (!isValid()) {
			return new ArrayList<SeleniumSuiteType>();
		}
		
		Class<?>[] suiteRunners = annotation.suiteRunner();

		List<SeleniumSuiteType> result = new ArrayList<SeleniumSuiteType>();
				
		for (Class<?> suiteRunner: suiteRunners) {
			result.add(new SeleniumSuiteType(processingEnv.getElementUtils().getTypeElement(suiteRunner.getCanonicalName()), processingEnv));
		}

		return result;
	}
	
	public TypeElement getConfiguration() {
		return processingEnv.getElementUtils().getTypeElement(annotation.configuration().getName());
	}
}