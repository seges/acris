package sk.seges.sesam.core.pap.model.mutable.delegate;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;

public abstract class DelegateMutableType implements MutableTypeMirror {

	private MutableTypeMirror delegate;

	protected void setDelegate(MutableTypeMirror delegateType) {
		this.delegate = delegateType;
	}
	
	public MutableTypeMirror ensureDelegateType() {
		if (delegate == null) {
			delegate = getDelegate();
		}
		return delegate;
	}

	abstract protected MutableTypeMirror getDelegate();

	@Override
	public boolean isSameType(MutableTypeMirror type) {
		return ensureDelegateType().isSameType(type);
	}
	
	@Override
	public MutableTypeKind getKind() {
		return ensureDelegateType().getKind();
	}

	@Override
	public String toString(ClassSerializer serializer) {
		return ensureDelegateType().toString(serializer);
	}

	@Override
	public String toString(ClassSerializer serializer, boolean typed) {
		return ensureDelegateType().toString(serializer, typed);
	}
	
	@Override
	public String toString() {
		return ensureDelegateType().toString();
	}

	@Override
	public int hashCode() {
		return ensureDelegateType().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DelegateMutableType) {
			return ensureDelegateType().equals(((DelegateMutableType)obj).ensureDelegateType());
		}
		return ensureDelegateType().equals(obj);
	}
}