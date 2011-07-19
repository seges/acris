package sk.seges.sesam.pap.model.annotation;

import sk.seges.sesam.core.pap.Constants;


public @interface Field {
	
	String value() default Constants.NULL;
}
