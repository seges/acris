package sk.seges.sesam.pap.test.selenium.processor.model;

import javax.annotation.processing.ProcessingEnvironment;

import sk.seges.sesam.core.pap.model.api.ImmutableType;

public class SeleniumSettingsProviderTypeElement extends AbstractSeleniumTypeElement {

	public static final String SUFFIX = "SettingsProvider";

	private SeleniumSuiteTypeElement seleniumSuite;
	
	public SeleniumSettingsProviderTypeElement(SeleniumSuiteTypeElement seleniumSuite, ProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.seleniumSuite = seleniumSuite;
	}

	private ImmutableType getOutputClass(ImmutableType inputClass) {
		return inputClass.addClassSufix(SUFFIX);
	}

	@Override
	protected ImmutableType getDelegateImmutableType() {
		return getOutputClass(seleniumSuite);
	}
}