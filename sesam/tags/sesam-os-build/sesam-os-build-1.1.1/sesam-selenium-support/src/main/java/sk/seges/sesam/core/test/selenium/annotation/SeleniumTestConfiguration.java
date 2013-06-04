package sk.seges.sesam.core.test.selenium.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import sk.seges.sesam.core.test.selenium.configuration.api.Browsers;

@Target(ElementType.TYPE)
//This is not working in the eclipse with the source retention policy
//@Retention(RetentionPolicy.SOURCE)
public @interface SeleniumTestConfiguration {
	
	String seleniumServer() default "localhost";
	int seleniumPort() default 4444;
	
	String bromineServer() default "localhost";
	int brominePort() default 8080;
	
	String testURL();
	String testURI() default "";
	
	Browsers browser() default Browsers.FIREFOX;
}