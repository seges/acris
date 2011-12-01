package sk.seges.sesam.pap.test.selenium.processor.model;

import java.util.HashSet;
import java.util.Set;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.DefaultTestSettings;

public class SeleniumTestConfigurationTypeElement extends AbstractSeleniumTypeElement {

	public static final String SUFFIX = "Configuration";
	
	private SeleniumTestTypeElement seleniumTestTypeElement;
	
	SeleniumTestConfigurationTypeElement(SeleniumTestTypeElement seleniumTestTypeElement, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.seleniumTestTypeElement = seleniumTestTypeElement;
		
		setSuperClass(processingEnv.getTypeUtils().toMutableType(DefaultTestSettings.class));

		Set<MutableTypeMirror> interfaces = new HashSet<MutableTypeMirror>();
		for (SeleniumSuiteTypeElement seleniumSuite: seleniumTestTypeElement.getSeleniumSuites()) {
			interfaces.add(new SeleniumSettingsProviderTypeElement(seleniumSuite, processingEnv));
		}
		setInterfaces(interfaces);
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		return seleniumTestTypeElement.clone().addClassSufix(SUFFIX);
	}
	
	public SeleniumTestTypeElement getSeleniumTest() {
		return seleniumTestTypeElement;
	}
}