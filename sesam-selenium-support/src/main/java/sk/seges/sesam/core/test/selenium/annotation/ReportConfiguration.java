package sk.seges.sesam.core.test.selenium.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import sk.seges.sesam.pap.test.selenium.processor.Constants;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface ReportConfiguration {
	
	@Target(ElementType.ANNOTATION_TYPE)
	@Retention(RetentionPolicy.SOURCE)
	public @interface ScreenshotConfiguration {
		
		boolean produceScreenshots() default false;
		
		String resultDirectory() default Constants.NULL;
		
		String screenshotsDirectory() default Constants.NULL;
	}
	
	ScreenshotConfiguration screenshotConfiguration() default @ScreenshotConfiguration;
}