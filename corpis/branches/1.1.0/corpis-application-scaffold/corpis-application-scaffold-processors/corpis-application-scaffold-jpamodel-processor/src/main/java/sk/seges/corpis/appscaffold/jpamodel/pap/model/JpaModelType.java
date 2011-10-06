package sk.seges.corpis.appscaffold.jpamodel.pap.model;

import sk.seges.corpis.appscaffold.jpamodel.pap.JpaModelProcessor;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;

public class JpaModelType extends DelegateMutableDeclaredType {

	private MutableDeclaredType declaredType;
	
	public JpaModelType(MutableDeclaredType delcaredType) {
		this.declaredType = delcaredType;
		
		setKind(MutableTypeKind.CLASS);
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		return declaredType.clone().removeClassSuffix(JpaModelProcessor.MODEL_SUFFIX);
	}

}
