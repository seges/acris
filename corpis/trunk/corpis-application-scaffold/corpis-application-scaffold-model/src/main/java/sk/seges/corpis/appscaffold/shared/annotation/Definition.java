package sk.seges.corpis.appscaffold.shared.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Definition {
	
	Class<?> value();
	
}
