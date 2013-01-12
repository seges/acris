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

	public static final String CLASSPATH_TEMPLATE_PREFIX = "classpath:";

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
	Screenshot screenshot() default @Screenshot(support = @Support(enabled = false));

	@Target(ElementType.ANNOTATION_TYPE)
	public @interface Screenshot {

		public enum When {
			ON_FAILURE, ON_SUCCESS;
		}

		@Target(ElementType.ANNOTATION_TYPE)
		public @interface Before {
			@Parameter(name = "operations", description = "operations")
			SeleniumOperation[] value() default { SeleniumOperation.ASSERTION, SeleniumOperation.FAIL,
				   SeleniumOperation.CLICK_ON, SeleniumOperation.CHANGE_VALUE, SeleniumOperation.NAVIGATE_TO, 
			  	   SeleniumOperation.NAVIGATE_BACK, SeleniumOperation.NAVIGATE_FORWARD };
		}

		@Target(ElementType.ANNOTATION_TYPE)
		public @interface After {
			@Parameter(name = "operations", description = "operations")
			SeleniumOperation[] value() default { SeleniumOperation.CLICK_ON, SeleniumOperation.CHANGE_VALUE, SeleniumOperation.SEND_KEYS,
				SeleniumOperation.NAVIGATE_TO};
		}

		@Parameter(name = "support", description = "Screenshot reports")
		Support support() default @Support;

		@Parameter(name = "when", description = "on ")
		When[] when() default { When.ON_SUCCESS, When.ON_FAILURE };

		@Parameter(name = "before", description = "before specified")
		Before before() default @Before;

		@Parameter(name = "after", description = "after specified")
		After after() default @After;
	}
	
	@Target(ElementType.ANNOTATION_TYPE)
	public @interface HtmlReport {

		@Target(ElementType.ANNOTATION_TYPE)
		public @interface IssueTracker {

			public static final String URL = "%url%";
			public static final String ISSUE_NUMBER = "%issue_number%";

			@Parameter(name = "url", description = "URL")
			String url() default Constants.NULL;

			@Parameter(name = "issue.link", description = "issue link format")
			String issueLink() default URL + ISSUE_NUMBER;
		}
		
		@Target(ElementType.ANNOTATION_TYPE)
		public @interface CommandReport {

			@Parameter(name = "command", description = "command")
			SeleniumOperation[] value() default {};
		}

		@Parameter(name = "report.html", description = "enables/disables HTML report")
		Support support() default @Support(directory = Report.CURRENT_DATE + "_" + Report.CURRENT_TIME + "_" + Report.TEST_CASE_NAME + "_" + Report.TEST_NAME);
		
		@Parameter(name = "test.template.path", description = "Defines path to the used template for tests")
		String testTemplatePath() default Report.CLASSPATH_TEMPLATE_PREFIX + "sk/seges/sesam/webdriver/report/metal/test_default.vm";

		@Parameter(name = "suite.template.path", description = "Defines path to the used template for suite")
		String suiteTemplatePath() default Report.CLASSPATH_TEMPLATE_PREFIX + "sk/seges/sesam/webdriver/report/metal/report_default.vm";

		@Parameter(name = "suite.template.locale", description = "Defines locale for the generated report")
		String locale() default "en";
		
		@Parameter(name = "report.command.test.failure.log", description = "Defines what to log when test fails")
		CommandReport onFailure() default @CommandReport({SeleniumOperation.ASSERTION, SeleniumOperation.FAIL,
				SeleniumOperation.CLICK_ON, SeleniumOperation.CHANGE_VALUE, SeleniumOperation.NAVIGATE_TO, 
		  	   	SeleniumOperation.NAVIGATE_BACK, SeleniumOperation.NAVIGATE_FORWARD});

		@Parameter(name = "report.command.test.success.log", description = "Defines what to log when test succeed")
		CommandReport onSucess() default @CommandReport({SeleniumOperation.ASSERTION, SeleniumOperation.FAIL,
				SeleniumOperation.CLICK_ON, SeleniumOperation.CHANGE_VALUE, SeleniumOperation.NAVIGATE_TO, 
		  	   	SeleniumOperation.NAVIGATE_BACK, SeleniumOperation.NAVIGATE_FORWARD});
		
		@Parameter(name = "issue.tracker", description = "Settings for issue tracker")
		IssueTracker issueTracker() default @IssueTracker();
	}
	
	@Parameter(name = "report", description = "HTML report settings")
	HtmlReport html() default @HtmlReport(support = @Support(enabled = false));
}