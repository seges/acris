package sk.seges.sesam.pap.service.model;

import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class RemoteServiceTypeElement extends DelegateMutableDeclaredType {

	private final MutableProcessingEnvironment processingEnv;
	private final TypeElement remoteServiceElement;
	private final LocalServiceTypeElement localServiceElement;
	
	public RemoteServiceTypeElement(TypeElement remoteServiceElement, MutableProcessingEnvironment processingEnv) {
		this.remoteServiceElement = remoteServiceElement;
		this.processingEnv = processingEnv;
		this.localServiceElement = new LocalServiceTypeElement(this, processingEnv);
	}

	RemoteServiceTypeElement(TypeElement remoteServiceElement, LocalServiceTypeElement localServiceElement, MutableProcessingEnvironment processingEnv) {
		this.remoteServiceElement = remoteServiceElement;
		this.processingEnv = processingEnv;
		this.localServiceElement = localServiceElement;
	}

	@Override
	protected MutableDeclaredType getDelegate() {
		return (MutableDeclaredType) processingEnv.getTypeUtils().toMutableType(remoteServiceElement.asType());
	}
	
	public TypeElement asElement() {
		return remoteServiceElement;
	}
	
	public LocalServiceTypeElement getLocalServiceElement() {
		return localServiceElement;
	}
}