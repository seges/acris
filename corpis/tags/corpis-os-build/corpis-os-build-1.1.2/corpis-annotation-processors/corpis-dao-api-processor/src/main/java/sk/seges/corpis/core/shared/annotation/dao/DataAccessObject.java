package sk.seges.corpis.core.shared.annotation.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataAccessObject {

	public enum Provider {
		INTERFACE, HIBERNATE, JPA, TWIG_PERSISTS;
	}

	Provider provider() default Provider.INTERFACE;
}