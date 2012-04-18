package sk.seges.corpis.appscaffold.model.pap.model;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public abstract class AbstractDataType extends DelegateMutableDeclaredType {

	protected MutableDeclaredType domainDataType;
	protected MutableProcessingEnvironment processingEnv;
	
	protected AbstractDataType(MutableDeclaredType dataDefinition, MutableProcessingEnvironment processingEnv) {
		this.domainDataType = dataDefinition;
		this.processingEnv = processingEnv;
	}
}