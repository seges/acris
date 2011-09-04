package sk.seges.sesam.pap.service.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.builder.NameTypesUtils;
import sk.seges.sesam.core.pap.model.DelegateImmutableType;
import sk.seges.sesam.core.pap.model.api.ImmutableType;

public class RemoteServiceTypeElement extends DelegateImmutableType {

	private final TypeMirror remoteServiceType;
	private final NameTypesUtils nameTypesUtils;
	private final LocalServiceTypeElement localServiceElement;
	
	public RemoteServiceTypeElement(TypeMirror serviceType, ProcessingEnvironment processingEnv) {
		this.remoteServiceType = serviceType;
		this.nameTypesUtils = new NameTypesUtils(processingEnv);
		this.localServiceElement = new LocalServiceTypeElement(this);
	}

	@Override
	protected ImmutableType getDelegateImmutableType() {
		return nameTypesUtils.toImmutableType(remoteServiceType);
	}
	
	public TypeMirror asType() {
		return remoteServiceType;
	}
	
	public LocalServiceTypeElement getLocalServiceElement() {
		return localServiceElement;
	}
}