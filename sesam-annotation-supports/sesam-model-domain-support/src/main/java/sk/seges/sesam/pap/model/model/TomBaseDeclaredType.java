package sk.seges.sesam.pap.model.model;

import javax.annotation.processing.Messager;
import javax.lang.model.util.Elements;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.core.pap.utils.TypeParametersSupport;

abstract class TomBaseDeclaredType extends DelegateMutableDeclaredType {

	protected final EnvironmentContext<TransferObjectProcessingEnvironment> environmentContext;

	protected final TypeParametersSupport typeParametersSupport;
	
	protected TomBaseDeclaredType(EnvironmentContext<TransferObjectProcessingEnvironment> environmentContext) {
		this.environmentContext = environmentContext;
		this.typeParametersSupport = new TypeParametersSupport(environmentContext.getProcessingEnv());
	}
	
	protected Messager getMessager() {
		return environmentContext.getProcessingEnv().getMessager();
	}
	
	protected MutableTypes getTypeUtils() {
		return environmentContext.getProcessingEnv().getTypeUtils();
	}
	
	protected TransferObjectTypes getTransferObjectUtils() {
		return environmentContext.getProcessingEnv().getTransferObjectUtils();
	}
	
	protected Elements getElementUtils() {
		return environmentContext.getProcessingEnv().getElementUtils();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (!(obj instanceof MutableDeclaredType)) {
			return false;
		}

		return getCanonicalName().equals(((MutableDeclaredType)obj).getCanonicalName());
	}

	@Override
	public int hashCode() {
		return getCanonicalName().hashCode();
	}
}