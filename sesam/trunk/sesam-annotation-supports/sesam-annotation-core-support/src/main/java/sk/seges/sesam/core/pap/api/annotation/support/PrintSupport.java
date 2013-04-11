package sk.seges.sesam.core.pap.api.annotation.support;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;

@Target(TYPE)
@Retention(RUNTIME)

public @interface PrintSupport {

	boolean autoIdent() default true;

	public @interface TypePrinterSupport {
		
		ClassSerializer printSerializer() default ClassSerializer.CANONICAL;
	
		boolean printTypeParameters() default true;
	}
	
	TypePrinterSupport printer() default @TypePrinterSupport;
}