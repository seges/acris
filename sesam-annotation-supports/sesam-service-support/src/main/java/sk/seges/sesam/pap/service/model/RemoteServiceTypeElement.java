package sk.seges.sesam.pap.service.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.builder.NameTypeUtils;
import sk.seges.sesam.core.pap.model.DelegateImmutableType;
import sk.seges.sesam.core.pap.model.api.ImmutableType;

public class RemoteServiceTypeElement extends DelegateImmutableType {

	private final TypeElement remoteServiceElement;
	private final NameTypeUtils nameTypesUtils;
	private final LocalServiceTypeElement localServiceElement;
	
	public RemoteServiceTypeElement(TypeElement remoteServiceElement, ProcessingEnvironment processingEnv) {
		this.remoteServiceElement = remoteServiceElement;
		this.nameTypesUtils = new NameTypeUtils(processingEnv);
		this.localServiceElement = new LocalServiceTypeElement(this, processingEnv);
	}

	RemoteServiceTypeElement(TypeElement remoteServiceElement, LocalServiceTypeElement localServiceElement, ProcessingEnvironment processingEnv) {
		this.remoteServiceElement = remoteServiceElement;
		this.nameTypesUtils = new NameTypeUtils(processingEnv);
		this.localServiceElement = localServiceElement;
	}

	@Override
	protected ImmutableType getDelegateImmutableType() {
		return nameTypesUtils.toImmutableType(remoteServiceElement);
	}
	
	public TypeElement asElement() {
		return remoteServiceElement;
	}
	
	public LocalServiceTypeElement getLocalServiceElement() {
		return localServiceElement;
	}
}