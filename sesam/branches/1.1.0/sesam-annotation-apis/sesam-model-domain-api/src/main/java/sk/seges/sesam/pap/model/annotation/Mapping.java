package sk.seges.sesam.pap.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
public @interface Mapping {

	public enum MappingType {
		EXPLICIT, AUTOMATIC;
	}
	
	MappingType value() default MappingType.AUTOMATIC;
}
