package sk.seges.sesam.pap.test.selenium.processor.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.api.ImmutableType;

public class SeleniumSuiteTypeElement extends AbstractSeleniumTypeElement {

	private final TypeElement suiteElement;
	
	SeleniumSuiteTypeElement(TypeElement suiteElement, ProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.suiteElement = suiteElement;
	}

	@Override
	protected ImmutableType getDelegateImmutableType() {
		return getNameTypeUtils().toImmutableType(suiteElement);
	}

	public TypeElement asElement() {
		return suiteElement;
	}
}