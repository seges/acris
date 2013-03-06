package sk.seges.sesam.pap.model.model;

import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.core.pap.utils.TypeParametersSupport;

abstract class TomBaseType extends DelegateMutableType {

	protected final EnvironmentContext<TransferObjectProcessingEnvironment> envContext;
	
	protected final TypeParametersSupport typeParametersSupport;
	
	protected TomBaseType(EnvironmentContext<TransferObjectProcessingEnvironment> envContext) {
		this.envContext = envContext;
		this.typeParametersSupport = new TypeParametersSupport(envContext.getProcessingEnv());
	}
	
	protected MutableTypes getMutableTypesUtils() {
		return envContext.getProcessingEnv().getTypeUtils();
	}
	
	protected MutableTypes getTypeUtils() {
		return envContext.getProcessingEnv().getTypeUtils();
	}
}