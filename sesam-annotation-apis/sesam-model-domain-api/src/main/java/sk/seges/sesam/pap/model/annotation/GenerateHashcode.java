package sk.seges.sesam.pap.model.annotation;

public @interface GenerateHashcode {
	boolean generate() default true;
}
