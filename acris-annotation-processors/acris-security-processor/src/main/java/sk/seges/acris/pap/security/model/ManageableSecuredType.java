package sk.seges.acris.pap.security.model;

import sk.seges.acris.security.client.CheckableSecuredObject;
import sk.seges.acris.security.client.IManageableSecuredObject;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class ManageableSecuredType extends SecuredType {

	private static final String CLASS_SUFFIX = "ManageableSecured";
	
	public ManageableSecuredType(MutableDeclaredType type, MutableProcessingEnvironment processingEnv) {
		super(type, processingEnv);
	}
	
	protected Class<?>[] getInterfaceClasses() {
		return new Class<?>[] { IManageableSecuredObject.class, CheckableSecuredObject.class };
	}

	@Override
	protected String getClassSuffix() {
		return CLASS_SUFFIX;
	}
}