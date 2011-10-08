package sk.seges.sesam.core.test.selenium.configuration.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import sk.seges.sesam.core.configuration.annotation.Configuration;
import sk.seges.sesam.core.configuration.annotation.Parameter;

@Configuration
@Target(ElementType.TYPE)
//This is not working in the eclipse with the source retention policy
//@Retention(RetentionPolicy.SOURCE)
public @interface Credentials {

	@Parameter(name = "test.username", description = "Username used to login to the system")
	String username();

	@Parameter(name = "test.password", description = "Password used to login to the system")
	String password();
}