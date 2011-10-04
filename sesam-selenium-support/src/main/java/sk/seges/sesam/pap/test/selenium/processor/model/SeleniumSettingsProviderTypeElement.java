package sk.seges.sesam.pap.test.selenium.processor.model;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.model.CoreSeleniumSettingsProvider;

public class SeleniumSettingsProviderTypeElement extends AbstractSeleniumTypeElement {

	public static final String SUFFIX = "SettingsProvider";

	private SeleniumSuiteTypeElement seleniumSuite;
	
	public SeleniumSettingsProviderTypeElement(SeleniumSuiteTypeElement seleniumSuite, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.seleniumSuite = seleniumSuite;
		
		setSuperClass(processingEnv.getTypeUtils().toMutableType(CoreSeleniumSettingsProvider.class));
	}

	private MutableDeclaredType getOutputClass(MutableDeclaredType inputClass) {
		return inputClass.clone().addClassSufix(SUFFIX);
	}

	@Override
	protected MutableDeclaredType getDelegate() {
		return getOutputClass(seleniumSuite);
	}
}