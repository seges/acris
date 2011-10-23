package sk.seges.sesam.pap.model.annotation;

public @interface GenerateEquals {
	boolean generate() default true;
}
