package sk.seges.sesam.pap.service.annotation;

import sk.seges.sesam.core.pap.Constants;

public @interface ExportService {

	Class<?> async();

	String value() default Constants.NULL;
}