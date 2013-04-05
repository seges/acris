package sk.seges.sesam.core.pap.model.mutable.delegate;

import java.util.Set;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;

public abstract class DelegateMutableVariable extends DelegateMutableType implements MutableTypeVariable {

	protected void setDelegate(MutableTypeVariable delegateType) {
		super.setDelegate(delegateType);
	}
	
	public MutableTypeVariable ensureDelegateType() {
		return (MutableTypeVariable) super.ensureDelegateType();
	}

	abstract protected MutableTypeVariable getDelegate();
	
	@Override
	public String getVariable() {
		return ensureDelegateType().getVariable();
	}

	@Override
	public MutableTypeVariable setVariable(String variable) {
		return ensureDelegateType().setVariable(variable);
	}

	@Override
	public Set<? extends MutableTypeMirror> getLowerBounds() {
		return ensureDelegateType().getLowerBounds();
	}

	@Override
	public MutableTypeVariable setLowerBounds(Set<? extends MutableTypeMirror> bounds) {
		return ensureDelegateType().setLowerBounds(bounds);
	}

	@Override
	public MutableTypeVariable addLowerBound(MutableTypeMirror bound) {
		return ensureDelegateType().addLowerBound(bound);
	}

	@Override
	public Set<? extends MutableTypeMirror> getUpperBounds() {
		return ensureDelegateType().getUpperBounds();
	}

	@Override
	public MutableTypeVariable setUpperBounds(Set<? extends MutableTypeMirror> bounds) {
		return ensureDelegateType().setUpperBounds(bounds);
	}

	@Override
	public MutableTypeVariable addUpperBound(MutableTypeMirror bound) {
		return ensureDelegateType().addUpperBound(bound);
	}
	
	@Override
	public MutableTypeVariable clone() {
		return ensureDelegateType().clone();
	}
}