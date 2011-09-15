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

	@Parameter(name = "test.seleniumHost", description = "Defines host name where the selenium server is located.")
	String seleniumServer() default "localhost";

	@Parameter(name = "test.seleniumPort", description = "Defines port name where the selenium server is located.")
	int seleniumPort() default 4444;

	@Parameter(name = "test.testRemote", description = "Bindings connect to the remote server instance.")
	boolean seleniumRemote() default false;

	@Parameter(name = "test.bromineHost", description = "Defines host name where the bromine server is located.")
	String bromineServer() default "localhost";

	@Parameter(name = "test.brominePort", description = "Defines port name where the bromine server is located.")
	int brominePort() default 8080;

	@Parameter(name = "test.bromineEnabled", description = "Enables/disables support for bromine.")
	boolean bromine() default false;

	@Parameter(name = "test.testHost", description = "Defines root URL of the testing site.")
	String testURL() default Constants.NULL;

	@Parameter(name = "test.testUri", description = "Defines relative URI of the testing home page.")
	String testURI() default Constants.NULL;

	@Parameter(name = "test.testBrowser", description = "Defines browser the tests are executed with.")
	Browsers browser() default Browsers.FIREFOX;
}