package sk.seges.sesam.core.pap.model.api;

import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;

public class ArrayNamedType implements NamedType {

	private NamedType componentType;
	
	public ArrayNamedType(NamedType componentType) {
		this.componentType = componentType;
	}
	
	@Override
	public String toString(ClassSerializer serializer) {
		return componentType.toString(serializer) + "[]";
	}

	@Override
	public String toString(NamedType inputClass, ClassSerializer serializer, boolean typed) {
		return componentType.toString(inputClass, serializer, typed) + "[]";
	}

	@Override
	public String getPackageName() {
		throw new RuntimeException("Invalid operation - arrays doesn't have packages.");
	}

	@Override
	public String getSimpleName() {
		return componentType.getSimpleName() + "[]";
	}

	@Override
	public String getCanonicalName() {
		return componentType.getCanonicalName() + "[]";
	}

	@Override
	public String getQualifiedName() {
		return componentType.getQualifiedName() + "[]";
	}

	@Override
	public TypeMirror asType() {
		return null;
	}
}