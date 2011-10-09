package sk.seges.sesam.core.pap.model.mutable.utils;

import java.util.HashSet;
import java.util.Set;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableWildcardType;

class MutableWildcard extends MutableVariable implements MutableWildcardType {
	
	public MutableWildcard() {
		this.variable = WILDCARD_NAME;
	}

	@Override
	public MutableTypeMirror getExtendsBound() {
		if (this.upperBounds.size() == 0) {
			return null;
		}
		return this.upperBounds.iterator().next();
	}

	@Override
	public void setExtendsBound(MutableTypeMirror bound) {
		Set<MutableTypeMirror> bounds = new HashSet<MutableTypeMirror>();
		if (bound != null) {
			bounds.add(bound);
		}
		setUpperBounds(bounds);
	}

	@Override
	public MutableTypeMirror getSuperBound() {
		if (this.upperBounds.size() == 0) {
			return null;
		}
		return this.upperBounds.iterator().next();
	}

	@Override
	public void setSuperBound(MutableTypeMirror bound) {
		Set<MutableTypeMirror> bounds = new HashSet<MutableTypeMirror>();
		if (bound != null) {
			bounds.add(bound);
		}
		setLowerBounds(bounds);
	}
	
	@Override
	public MutableWildcard addLowerBound(MutableTypeMirror bound) {
		if (lowerBounds.size() > 0) {
			throw new RuntimeException("Wildcard type can hold only one super type!");
		}
		super.addLowerBound(bound);
		return this;
	}

	@Override
	public MutableWildcard addUpperBound(MutableTypeMirror bound) {
		if (upperBounds.size() > 0) {
			throw new RuntimeException("Wildcard type can hold only one extends type!");
		}
		super.addUpperBound(bound);
		return this;
	}

	@Override
	public MutableTypeKind getKind() {
		return MutableTypeKind.WILDCARD;
	}
}