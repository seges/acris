package sk.seges.sesam.pap.test.selenium.processor.model;

import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;

public abstract class AbstractSeleniumTypeElement extends DelegateMutableDeclaredType {

	protected MutableProcessingEnvironment processingEnv;
	
	protected AbstractSeleniumTypeElement(MutableProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}
	
	protected MutableTypes getMutableTypeUtils() {
		return processingEnv.getTypeUtils();
	}
}