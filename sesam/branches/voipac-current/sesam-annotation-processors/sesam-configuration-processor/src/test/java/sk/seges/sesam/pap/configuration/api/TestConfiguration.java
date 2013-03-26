package sk.seges.sesam.pap.configuration.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import sk.seges.sesam.core.configuration.annotation.Configuration;
import sk.seges.sesam.core.configuration.annotation.Parameter;
import sk.seges.sesam.core.pap.Constants;

@Configuration
@Target(ElementType.TYPE)
// This is not working in the eclipse with the source retention policy
// @Retention(RetentionPolicy.SOURCE)
public @interface TestConfiguration {

	public static final String NULL = Constants.NULL;

	@Parameter(name = "test.seleniumHost", description = "Defines host name where the selenium server is located.")
	String seleniumServer() default "localhost";

	@Parameter(name = "test.seleniumPort", description = "Defines port name where the selenium server is located.")
	int seleniumPort() default 4444;

	@Parameter(name = "test.testRemote", description = "Defines support for connection to the remote server instance.")
	boolean seleniumRemote() default false;

	@Parameter(name = "test.bromineHost", description = "Defines host name where the bromine server is located.")
	String bromineServer() default "localhost";

	@Parameter(name = "test.brominePort", description = "Defines port name where the bromine server is located.")
	int brominePort() default 8080;

	@Parameter(name = "test.bromineEnabled", description = "Enables/disables support for bromine.")
	boolean bromine() default false;

	@Parameter(name = "test.testHost", description = "Defines root URL of the testing site.")
	String testURL() default NULL;

	@Parameter(name = "test.testUri", description = "Defines relative URI of the testing home page.")
	String testURI() default NULL;
}
