package sk.seges.sesam.core.pap.model.mutable.utils;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeValue;

class MutableReference implements MutableReferenceType {

	private final MutableTypeValue reference;
	private String name;
	
	MutableReference(MutableTypeValue reference, String name) {
		this.reference = reference;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public String toString(ClassSerializer serializer) {
		return name;
	}

	@Override
	public String toString(ClassSerializer serializer, boolean typed) {
		return name;
	}

	@Override
	public MutableTypeValue getReference() {
		return reference;
	}

	public void setName(String name) {
		this.name = name;
	}
}
