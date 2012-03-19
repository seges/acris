package sk.seges.corpis.core.pap.dao.model;

import javax.lang.model.element.TypeElement;

import sk.seges.corpis.appscaffold.model.pap.model.DomainDataInterfaceType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class DefinitionType extends DelegateMutableDeclaredType {

	private final MutableDeclaredType mutableType;
	private final MutableProcessingEnvironment processingEnv;
	
	public DefinitionType(TypeElement typeElement, MutableProcessingEnvironment processingEnv) {
		this.mutableType = processingEnv.getTypeUtils().toMutableType(typeElement);
		this.processingEnv = processingEnv;
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		return mutableType;
	}
	
	public DataDaoApiType getDaoApiType() {
		return new DataDaoApiType(mutableType, processingEnv);
	}
	
	public DomainDataInterfaceType getDataInterfaceType() {
		return new DomainDataInterfaceType(mutableType, processingEnv);
	}
}