package sk.seges.sesam.core.test.selenium.configuration.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import sk.seges.sesam.core.configuration.annotation.Configuration;
import sk.seges.sesam.core.configuration.annotation.Parameter;
import sk.seges.sesam.core.pap.Constants;

@Configuration
@Target(ElementType.TYPE)
//This is not working in the eclipse with the source retention policy
//@Retention(RetentionPolicy.SOURCE)
public @interface Report {

	@Target(ElementType.ANNOTATION_TYPE)
	public @interface Support {
		
		@Parameter(name = "enabled", description = "Enables/disables support")
		boolean enabled() default false;

		@Parameter(name = "directory", description = "Output directory")
		String directory() default Constants.NULL;
	}
	
	@Parameter(name = "report.screenshot", description = "screenshots")
	Support screenshot() default @Support;

	@Parameter(name = "report.html", description = "HTML reports")
	Support html() default @Support;
}