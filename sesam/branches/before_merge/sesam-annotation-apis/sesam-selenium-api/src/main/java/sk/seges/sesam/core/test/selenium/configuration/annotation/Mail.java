package sk.seges.sesam.core.test.selenium.configuration.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import sk.seges.sesam.core.configuration.annotation.Configuration;
import sk.seges.sesam.core.configuration.annotation.Parameter;

@Configuration
@Target(ElementType.TYPE)
//This is not working in the eclipse with the source retention policy
//@Retention(RetentionPolicy.SOURCE)
public @interface Mail {

	public enum Provider {
		IMAP("imap"), IMAPS("imaps");
		
		private String name;
		
		Provider(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	@Parameter(name = "mail.address", description = "E-mail recepient settings")
	String mail();
	
	@Parameter(name = "mail.host", description = "E-mail host name")
	String host(); 
	
	@Parameter(name = "mail.password", description = "E-mail password settings")
	String password();
	
	@Parameter(name = "mail.provider", description = "E-mail provider")
	Provider provider() default Provider.IMAP;
}