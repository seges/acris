package sk.seges.sesam.core.test.selenium.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
//@Retention(RetentionPolicy.RUNTIME)
public @interface XPath {
	
	String value();
}