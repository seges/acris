package sk.seges.sesam.core.test.selenium.configuration.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SeleniumTest {

	String description();
		
	public @interface Issue {
	
		public static final int UNDEFINED = -1;
		
		enum IssueTracker {
			MANTIS, JIRA, OTHER;
		}

		IssueTracker tracker() default IssueTracker.OTHER;

		int value() default UNDEFINED;
	}
	
	Issue issue() default @Issue;
}