package sk.seges.sesam.core.pap.builder.api;

import java.lang.reflect.Type;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;

public interface NameTypes {

	public enum ClassSerializer {
		CANONICAL, SIMPLE, QUALIFIED;
	}

	NamedType toType(TypeMirror typeMirror);
	NamedType toType(Element element);
	NamedType toType(Type javaType);
	NamedType toType(String className);
	
	TypeMirror fromType(NamedType type);

	ImmutableType toImmutableType(TypeMirror typeMirror);
	ImmutableType toImmutableType(Element element);
	ImmutableType toImmutableType(Type javaType);
	ImmutableType toImmutableType(String className);
	
	NamedType erasure(NamedType namedType);
}