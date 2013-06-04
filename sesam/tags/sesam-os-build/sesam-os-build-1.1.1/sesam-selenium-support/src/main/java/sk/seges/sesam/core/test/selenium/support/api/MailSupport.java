package sk.seges.sesam.core.test.selenium.support.api;

public interface MailSupport {

	void waitForMailNotPresent(String subject) throws InterruptedException;

	String waitForMailPresent(String subject) throws InterruptedException;

}