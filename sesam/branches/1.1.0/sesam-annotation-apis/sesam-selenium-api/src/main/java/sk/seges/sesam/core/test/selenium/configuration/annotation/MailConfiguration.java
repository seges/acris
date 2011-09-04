package sk.seges.sesam.core.test.selenium.configuration.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
//This is not working in the eclipse with the source retention policy
//@Retention(RetentionPolicy.SOURCE)
public @interface MailConfiguration {

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
	
	String mail();
	
	String host(); 
	
	String password();
	
	Provider provider() default Provider.IMAP;
}