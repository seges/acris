package sk.seges.acris.pap.bean.model;

import sk.seges.acris.core.client.bean.BeanWrapper;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class BeanWrapperType extends DelegateMutableDeclaredType {

	private final MutableDeclaredType bean;
	private final MutableProcessingEnvironment processingEnv;
	
	public BeanWrapperType(MutableDeclaredType bean, MutableProcessingEnvironment processingEnv) {
		this.bean = bean;
		this.processingEnv = processingEnv;
		
		setKind(MutableTypeKind.INTERFACE);
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		return processingEnv.getTypeUtils().getDeclaredType(
				processingEnv.getTypeUtils().toMutableType(BeanWrapper.class), new MutableDeclaredType[] { bean });
	}
}