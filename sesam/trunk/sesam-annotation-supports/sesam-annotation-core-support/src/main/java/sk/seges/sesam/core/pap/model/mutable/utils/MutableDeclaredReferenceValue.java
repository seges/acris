package sk.seges.sesam.core.pap.model.mutable.utils;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceTypeValue;

class MutableDeclaredReferenceValue extends MutableValue implements MutableReferenceTypeValue  {

	private final MutableDeclaredType type;
	private final MutableReferenceType reference;
	
	public MutableDeclaredReferenceValue(MutableDeclaredType referenceType, MutableReferenceType reference) {
		super(reference);
		this.reference = reference;
		this.type = referenceType;
	}
	
	@Override
	public String toString() {
		return toString(ClassSerializer.CANONICAL);
	}
	
	@Override
	public String toString(ClassSerializer serializer) {
		return toString(serializer, true);
	}

	@Override
	public String toString(ClassSerializer serializer, boolean typed) {
		if (type == null) {
			return "null";
		}

		return "new " + type.toString(serializer, typed) + "(" + reference.toString(serializer, typed) + ")";
	}
	
	public MutableDeclaredType asType() {
		return type;
	}
}
