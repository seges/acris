package sk.seges.sesam.core.pap.builder.api;

import java.lang.reflect.Type;

import javax.lang.model.element.Element;

import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;

public interface NameTypes {

	public enum ClassSerializer {
		CANONICAL, SIMPLE, QUALIFIED;
	}

	MutableType toType(Element element);
	
	NamedType toType(Type javaType);

	NamedType toType(String className);

}