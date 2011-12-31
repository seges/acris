package sk.seges.sesam.core.test.selenium.configuration.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTest.Issue.IssueTracker;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SeleniumTest {

	String description();
	
	public static final String UNDEFINED = "";

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.ANNOTATION_TYPE)
	public @interface Issue {
			
		enum IssueTracker {
			MANTIS {
				@Override
				public String getIconName() {
					return "mantis_64.png";
				}
			}, JIRA {
				@Override
				public String getIconName() {
					return "jira_64.png";
				}
			}, YOU_TRACK {
				@Override
				public String getIconName() {
					return "you_track_64.png";
				}
			}, BUGZILLA {
				@Override
				public String getIconName() {
					return "bugzilla_64.png";
				}
			}, GOOGLE_CODE {
				@Override
				public String getIconName() {
					return "google_code_64.png";
				}
			}, GEMINI {
				@Override
				public String getIconName() {
					return "gemini_64.png";
				}
			}, GIT_HUB {
				@Override
				public String getIconName() {
					return "git_hub_64.png";
				}
			}, RED_MINE {
				@Override
				public String getIconName() {
					return "red_mine_64.png";
				}
			}, SOURCE_FORGE {
				@Override
				public String getIconName() {
					return "source_forge_64.png";
				}
			}, TRAC {
				@Override
				public String getIconName() {
					return "trac_logo_64.png";
				}
			}, OTHER {
				@Override
				public String getIconName() {
					return "default_64.png";
				}
			};
			
			public abstract String getIconName();
		}

		IssueTracker tracker();

		String value();
	}
	
	Issue issue() default @Issue(tracker = IssueTracker.OTHER, value = UNDEFINED);
}