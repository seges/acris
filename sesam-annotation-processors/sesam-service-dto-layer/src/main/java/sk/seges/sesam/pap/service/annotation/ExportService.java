package sk.seges.sesam.pap.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import sk.seges.sesam.core.pap.Constants;

@Target(ElementType.TYPE)
public @interface ExportService {

	Class<?> async();

	String value() default Constants.NULL;
}