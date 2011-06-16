package sk.seges.sesam.core.test.selenium.support.api;

import java.util.Comparator;



public interface SeleniumSupport {

	void fail(String message);
	
	<T> boolean isSorted(Iterable<T> iterable, Comparator<T> comparator);

	String getRandomString(int length);
	String getRandomEmail();
}