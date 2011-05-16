package sk.seges.sesam.core.test.selenium.support.api;

public interface MailSupport {

	void waitForMailNotPresent(String subject);

	String waitForMailPresent(String subject);

}