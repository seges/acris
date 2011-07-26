package sk.seges.sesam.pap.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import sk.seges.sesam.core.pap.Constants;

@Target(ElementType.METHOD)
public @interface Field {
	
	String value() default Constants.NULL;
}
