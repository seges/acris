package sk.seges.sesam.core.test.selenium.configuration.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import sk.seges.sesam.core.configuration.annotation.Configuration;
import sk.seges.sesam.core.configuration.annotation.Parameter;
import sk.seges.sesam.core.pap.Constants;
import sk.seges.sesam.core.test.selenium.configuration.api.Browsers;

@Configuration
@Target(ElementType.TYPE)
// This is not working in the eclipse with the source retention policy
// @Retention(RetentionPolicy.SOURCE)
public @interface Selenium {

	@Parameter(name = "test.remoteServerURL", description = "Defines host name where the remote server is located.")
	String remoteServerURL() default Constants.NULL;

	@Parameter(name = "test.testHost", description = "Defines root URL of the testing site.")
	String testURL() default Constants.NULL;

	@Parameter(name = "test.testUri", description = "Defines relative URI of the testing home page.")
	String testURI() default Constants.NULL;

	@Parameter(name = "test.testBrowser", description = "Defines browser the tests are executed with.")
	Browsers browser() default Browsers.FIREFOX;
}