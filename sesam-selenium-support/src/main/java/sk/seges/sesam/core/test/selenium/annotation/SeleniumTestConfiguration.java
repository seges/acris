package sk.seges.sesam.core.test.selenium.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import sk.seges.sesam.core.test.selenium.configuration.api.Browsers;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface SeleniumTestConfiguration {
	
	String seleniumServer() default "localhost";
	int seleniumPort() default 4444;
	
	String bromineServer() default "localhost";
	int brominePort() default 8080;
	
	String testURL();
	
	Browsers browser() default Browsers.FIREFOX;
}