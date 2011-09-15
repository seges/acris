package sk.seges.sesam.core.pap.model.api;

import javax.lang.model.type.TypeMirror;

public interface NamedType extends java.lang.reflect.Type, PrintableType, HasAnnotations {

	String getPackageName();
	
	String getSimpleName();

	String getCanonicalName();

	String getQualifiedName();

	TypeMirror asType();
	
	NamedType getEnclosedClass();
}