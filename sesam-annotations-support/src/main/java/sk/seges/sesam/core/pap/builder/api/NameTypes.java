package sk.seges.sesam.core.pap.builder.api;

import java.lang.reflect.Type;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;

public interface NameTypes {

	public enum ClassSerializer {
		CANONICAL, SIMPLE, QUALIFIED;
	}

	MutableType toType(TypeMirror typeMirror);
	MutableType toType(Element element);
	
	NamedType toType(Type javaType);

	NamedType toType(String className);

}