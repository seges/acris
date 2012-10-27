package sk.seges.sesam.pap.service.model;

import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class ServiceConvertProviderType extends ConverterProviderType {

	public ServiceConvertProviderType(ServiceTypeElement serviceTypeElement, MutableProcessingEnvironment processingEnv) {
		super(serviceTypeElement, processingEnv);
	}

	@Override
	protected void setModifiers() {
		addModifier(Modifier.PROTECTED);
	}

}