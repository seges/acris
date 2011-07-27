package sk.seges.sesam.core.pap.model.api;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;

public class ArrayNamedType implements NamedType {

	private NamedType componentType;
	
	public ArrayNamedType(NamedType componentType) {
		this.componentType = componentType;
	}
	
	public NamedType getComponentType() {
		return componentType;
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
		return null;
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

	@Override
	public void annotateWith(AnnotationMirror annotationMirror) {
		throw new RuntimeException("Array type should not be annnotated.");
	}

	@Override
	public Set<AnnotationMirror> getAnnotations() {
		return new HashSet<AnnotationMirror>();
	}
}