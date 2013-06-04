package sk.seges.sesam.pap.test.selenium.processor.model;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.test.selenium.runner.SeleniumSuiteRunner;

public class SeleniumSuiteRunnerType extends DelegateMutableDeclaredType {

	public static final String SUFFIX = "Runner";

	private final SeleniumSuiteType seleniumSuite;
	
	SeleniumSuiteRunnerType(SeleniumSuiteType seleniumSuite, MutableProcessingEnvironment processingEnv) {
		this.seleniumSuite = seleniumSuite;
		
		setKind(MutableTypeKind.CLASS);

		setSuperClass(processingEnv.getTypeUtils().toMutableType(SeleniumSuiteRunner.class));
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		return seleniumSuite.clone().addClassSufix(SUFFIX);
	}

	public SeleniumSuiteType getSeleniumSuite() {
		return seleniumSuite;
	}
}