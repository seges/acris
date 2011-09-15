package sk.seges.sesam.pap.test.selenium.processor.model;

import javax.annotation.processing.ProcessingEnvironment;

import sk.seges.sesam.core.pap.builder.NameTypeUtils;
import sk.seges.sesam.core.pap.model.DelegateImmutableType;

public abstract class AbstractSeleniumTypeElement extends DelegateImmutableType {

	protected ProcessingEnvironment processingEnv;
	private NameTypeUtils nameTypeUtils;
	
	protected AbstractSeleniumTypeElement(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		this.nameTypeUtils = new NameTypeUtils(processingEnv);
	}
	
	protected NameTypeUtils getNameTypeUtils() {
		return nameTypeUtils;
	}
}