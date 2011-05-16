package sk.seges.sesam.core.test.selenium.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import sk.seges.sesam.core.test.selenium.core.NavigablePage;

@Target(ElementType.FIELD)
//This is not working in the eclipse with the source retention policy
//@Retention(RetentionPolicy.SOURCE)
public @interface Navigate {
	
	Class<? extends NavigablePage> target();

}