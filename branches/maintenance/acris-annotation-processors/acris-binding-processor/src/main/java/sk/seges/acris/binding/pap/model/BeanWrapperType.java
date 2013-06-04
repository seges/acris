package sk.seges.acris.binding.pap.model;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class BeanWrapperType extends DelegateMutableDeclaredType {

	public static final String BEAN_WRAPPER_SUFFIX = "BeanWrapper";

	private MutableDeclaredType entityType;
	
	public BeanWrapperType(MutableDeclaredType entityType, MutableProcessingEnvironment processingEnv) {
		this.entityType = entityType;
		
		setKind(MutableTypeKind.INTERFACE);
		
		setTypeVariables(entityType.getTypeVariables().toArray(new MutableTypeVariable[] {}));
		
		List<MutableTypeMirror> interfaces = new ArrayList<MutableTypeMirror>();
		interfaces.add(processingEnv.getTypeUtils().toMutableType(BeanWrapper.class).
				setTypeVariables(processingEnv.getTypeUtils().getTypeVariable(null, entityType.stripTypeParametersTypes())));
		setInterfaces(interfaces);
		setSuperClass(null);
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		return entityType.clone().addClassSufix(BEAN_WRAPPER_SUFFIX);
	}
}
