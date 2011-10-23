package sk.seges.sesam.pap.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
public @interface Annotations {
	
	PropertyAccessor accessor() default PropertyAccessor.PROPERTY;

	Class<?>[] packageOf() default {};
	Class<?>[] typeOf() default {};	
}