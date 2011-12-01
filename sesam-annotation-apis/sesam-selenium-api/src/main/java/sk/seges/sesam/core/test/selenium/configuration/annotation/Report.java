package sk.seges.sesam.core.test.selenium.configuration.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import sk.seges.sesam.core.configuration.annotation.Configuration;
import sk.seges.sesam.core.configuration.annotation.Parameter;
import sk.seges.sesam.core.pap.Constants;
import sk.seges.sesam.core.test.selenium.report.model.SeleniumOperation;

@Configuration
@Target(ElementType.TYPE)
//This is not working in the eclipse with the source retention policy
//@Retention(RetentionPolicy.SOURCE)
public @interface Report {

	public static final String CURRENT_DATE = "%date%";
	public static final String CURRENT_TIME = "%time%";
	public static final String TEST_CASE_NAME = "%test_case%";
	public static final String TEST_NAME = "%test%";
	
	@Target(ElementType.ANNOTATION_TYPE)
	public @interface Support {
		
		@Parameter(name = "enabled", description = "Enables/disables support")
		boolean enabled() default true;

		@Parameter(name = "directory", description = "Output directory")
		String directory() default Constants.NULL;
	}
	
	@Parameter(name = "report.screenshot", description = "Defines that screenshot is taken")
	Screenshot screenshot() default @Screenshot;

	@Target(ElementType.ANNOTATION_TYPE)
	public @interface Screenshot {

		public enum When {
			ON_FAILURE, ON_SUCCESS;
		}

		@Target(ElementType.ANNOTATION_TYPE)
		public @interface Before {
			@Parameter(name = "operations", description = "operations")
			SeleniumOperation[] value() default {};
		}

		@Target(ElementType.ANNOTATION_TYPE)
		public @interface After {
			@Parameter(name = "operations", description = "operations")
			SeleniumOperation[] value() default {};
		}

		@Parameter(name = "support", description = "Screenshot reports")
		Support support() default @Support;

		@Parameter(name = "when", description = "on ")
		When[] when() default When.ON_FAILURE;

		@Parameter(name = "before", description = "before specified")
		Before before() default @Before;

		@Parameter(name = "after", description = "after specified")
		After after() default @After;
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