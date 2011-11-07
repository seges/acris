package sk.seges.acris.json.client.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target( { ElementType.FIELD })
@Retention(RUNTIME)
public @interface DateTimePattern {
	String value();
}