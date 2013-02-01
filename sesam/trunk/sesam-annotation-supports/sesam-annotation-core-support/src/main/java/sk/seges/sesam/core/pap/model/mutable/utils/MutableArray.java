package sk.seges.sesam.core.pap.model.mutable.utils;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableArrayType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;

class MutableArray extends MutableType implements MutableArrayType {

	private MutableTypeMirror componentType;

	public MutableArray(MutableTypeMirror componentType) {
		this.componentType = componentType;
	}
	
	public MutableTypeMirror getComponentType() {
		return componentType;
	}

	@Override
	public String toString() {
		return toString(ClassSerializer.CANONICAL, true);
	}
	
	@Override
	public String toString(ClassSerializer serializer) {
		return componentType.toString(serializer) + "[]";
	}

	@Override
	public String toString(ClassSerializer serializer, boolean typed) {
		return componentType.toString(serializer, typed) + "[]";
	}

	@Override
	public MutableTypeKind getKind() {
		return MutableTypeKind.ARRAY;
	}
}