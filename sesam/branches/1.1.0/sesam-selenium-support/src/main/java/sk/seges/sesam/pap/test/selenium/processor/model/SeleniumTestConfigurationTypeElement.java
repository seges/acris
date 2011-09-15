package sk.seges.sesam.pap.test.selenium.processor.model;

import javax.annotation.processing.ProcessingEnvironment;

import sk.seges.sesam.core.pap.model.api.ImmutableType;

public class SeleniumTestConfigurationTypeElement extends AbstractSeleniumTypeElement {

	private static final String SUFFIX = "Configuration";
	
	private SeleniumTestTypeElement seleniumTestTypeElement;
	
	SeleniumTestConfigurationTypeElement(SeleniumTestTypeElement seleniumTestTypeElement, ProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.seleniumTestTypeElement = seleniumTestTypeElement;
	}
	
	@Override
	protected ImmutableType getDelegateImmutableType() {
		return seleniumTestTypeElement.addClassSufix(SUFFIX);
	}
	
	public SeleniumTestTypeElement getSeleniumTest() {
		return seleniumTestTypeElement;
	}
}