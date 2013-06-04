package sk.seges.acris.pap.security.model;

import sk.seges.acris.security.client.CheckableSecuredObject;
import sk.seges.acris.security.client.mediator.IRuntimeAuthorityMediator;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class RuntimeSecuredType extends SecuredType {

	private static final String CLASS_SUFFIX = "RuntimeSecured";

	public RuntimeSecuredType(MutableDeclaredType type, MutableProcessingEnvironment processingEnv) {
		super(type, processingEnv);
	}

	@Override
	protected Class<?>[] getInterfaceClasses() {
		return new Class<?>[] {
				IRuntimeAuthorityMediator.class, CheckableSecuredObject.class
		};
	}
	
	@Override
	protected String getClassSuffix() {
		return CLASS_SUFFIX;
	}
}