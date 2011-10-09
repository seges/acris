package sk.seges.acris.json.client.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target( { ElementType.TYPE })
@Retention(RUNTIME)
public @interface JsonObject {
	String value() default "";
	String group() default "";
}
