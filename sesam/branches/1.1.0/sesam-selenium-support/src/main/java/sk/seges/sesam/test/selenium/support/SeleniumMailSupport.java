package sk.seges.sesam.test.selenium.support;

public interface SeleniumMailSupport {

	void waitForMailNotPresent(String subject) throws InterruptedException;

	String waitForMailPresent(String subject) throws InterruptedException;

}