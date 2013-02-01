package sk.seges.acris.pap.security.model;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.security.client.CheckableSecuredObject;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class SecuredType extends DelegateMutableDeclaredType {

	private final MutableDeclaredType type;
	
	private static final String CLASS_SUFFIX = "SecurityWrapper";

	public SecuredType(MutableDeclaredType type, MutableProcessingEnvironment processingEnv) {
		this.type = type;
		
		List<MutableTypeMirror> interfaces = new ArrayList<MutableTypeMirror>();
		
		for (Class<?> clazz: getInterfaceClasses()) {
			interfaces.add(processingEnv.getTypeUtils().toMutableType(clazz));
		}
		
		setInterfaces(interfaces);
		setSuperClass(type);
	}

	protected Class<?>[] getInterfaceClasses() {
		return new Class<?>[] { CheckableSecuredObject.class };
	}
	
	protected String getClassSuffix() {
		return CLASS_SUFFIX;
	}
	
	protected MutableDeclaredType getDelegate() {
		return type.clone().addClassSufix(getClassSuffix());
	}
}