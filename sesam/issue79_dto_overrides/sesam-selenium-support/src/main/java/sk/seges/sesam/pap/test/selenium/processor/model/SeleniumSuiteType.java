package sk.seges.sesam.pap.test.selenium.processor.model;

import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class SeleniumSuiteType extends AbstractSeleniumType {

	private final TypeElement suiteElement;
	
	private SeleniumSuiteRunnerType suiteRunner;
	
	public SeleniumSuiteType(TypeElement suiteElement, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.suiteElement = suiteElement;
	}

	@Override
	protected MutableDeclaredType getDelegate() {
		return (MutableDeclaredType) getMutableTypeUtils().toMutableType(suiteElement.asType());
	}

	public TypeElement asElement() {
		return suiteElement;
	}
	
	public SeleniumSuiteRunnerType getSuiteRunner() {
		if (suiteRunner == null) {
			suiteRunner = new SeleniumSuiteRunnerType(this, processingEnv);
		}
		
		return suiteRunner;
	}
}