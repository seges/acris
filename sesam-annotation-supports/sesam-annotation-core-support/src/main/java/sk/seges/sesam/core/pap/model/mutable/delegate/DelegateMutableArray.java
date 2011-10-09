package sk.seges.sesam.core.pap.model.mutable.delegate;

import sk.seges.sesam.core.pap.model.mutable.api.MutableArrayType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;

public abstract class DelegateMutableArray extends DelegateMutableType implements MutableArrayType {

	protected void setDelegate(MutableArrayType delegateType) {
		super.setDelegate(delegateType);
	}
	
	public MutableArrayType ensureDelegateType() {
		return (MutableArrayType) super.ensureDelegateType();
	}

	abstract protected MutableArrayType getDelegate();

	@Override
	public MutableTypeMirror getComponentType() {
		return ensureDelegateType().getComponentType();
	}

}
