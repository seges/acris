package sk.seges.sesam.pap.configuration.api;

import sk.seges.sesam.core.configuration.annotation.GenerateModel;

@GenerateModel
public @interface TestAnnotation {

	public static enum TestType {
		PROPAGATE, ISOLATE
	}
	
	public static enum TestTarget {
		RETURN_VALUE, ARGUMENTS
	}
	
	TestType value() default sk.seges.sesam.pap.configuration.api.TestAnnotation.TestType.PROPAGATE;
	TestTarget[] target() default { sk.seges.sesam.pap.configuration.api.TestAnnotation.TestTarget.RETURN_VALUE, 
									sk.seges.sesam.pap.configuration.api.TestAnnotation.TestTarget.ARGUMENTS };

	String[] fields() default {};
}