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

	@Target(ElementType.ANNOTATION_TYPE)
	public @interface Screenshot {

		public enum When {
			ON_FAILURE, AFTER_COMMAND, BEFORE_COMMNAND, ON_SUCESS
		}
		
		@Parameter(name = "support", description = "Screenshot reports")
		Support support() default @Support;

		@Parameter(name = "when", description = "Defines when a screenshot is taken")
		When[] when() default When.ON_FAILURE;
	}
	
	@Target(ElementType.ANNOTATION_TYPE)
	public @interface HtmlReport {

		@Parameter(name = "report.html", description = "HTML reports")
		Support support() default @Support;
		
		@Parameter(name = "template.path", description = "Defines path to the used template")
		String templatePath() default Constants.NULL;
	}
	
	@Parameter(name = "report.html", description = "HTML reports")
	HtmlReport html() default @HtmlReport;
}