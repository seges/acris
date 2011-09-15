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
	//@Retention(RetentionPolicy.SOURCE)
	public @interface ScreenshotConfiguration {
		
		@Parameter(name = "report.produce.screenshots", description = "Enables/disables producing screenshots on the of the test or when test fails")
		boolean produceScreenshots() default false;

		@Parameter(name = "report.result.directory", description = "Output directory for the test HTML reports")
		String resultDirectory() default Constants.NULL;
		
		@Parameter(name = "report.screenshot.directory", description = "Output directory for the screenshots")
		String screenshotsDirectory() default Constants.NULL;
	}
	
	ScreenshotConfiguration screenshotConfiguration() default @ScreenshotConfiguration;
}