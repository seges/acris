package sk.seges.sesam.core.test.webdriver.support;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;

public class SeleniumSupport extends AbstractBrowserSupport {

	// protected DefaultSelenium selenium;
	protected WebDriver webDriver;
	protected Wait<WebDriver> wait;

	private final Random random = new Random();

	private static final char[] symbols = new char[36];
	private static final char[] numbers = new char[10];

	static {
		for (int idx = 0; idx < 10; ++idx) {
			char c = (char) ('0' + idx);
			numbers[idx] = c;
			symbols[idx] = c;
		}
		
		for (int idx = 10; idx < 36; ++idx) {
			symbols[idx] = (char) ('a' + idx - 10);
		}
	}

	public SeleniumSupport(WebDriver webDriver, Wait<WebDriver> wait) {
		this.webDriver = webDriver;
		this.wait = wait;
	}

	public String getRandomNumericString(int length) {
		return getRandomString(numbers, length);
	}
	
	public String getRandomString(int length) {
		return getRandomString(symbols, length);
	}
	
	private String getRandomString(char[] symbols, int length) {
		if (length < 1) {
			throw new IllegalArgumentException("length < 1: " + length);
		}

		char[] buf = new char[length];
		for (int idx = 0; idx < buf.length; ++idx)
			buf[idx] = symbols[random.nextInt(symbols.length)];
		return new String(buf);
	}

	public <T> boolean isSorted(Iterable<T> iterable, Comparator<T> comparator) {
		Iterator<T> iter = iterable.iterator();
		if (!iter.hasNext()) {
			return true;
		}
		T t = iter.next();
		while (iter.hasNext()) {
			T t2 = iter.next();
			if (comparator.compare(t, t2) > 0) {
				return false;
			}
			t = t2;
		}
		return true;
	}

	public String getRandomEmail() {
		return getRandomString(6) + "@" + getRandomString(6) + "." + getRandomString(2);
	}
	
	public Boolean getRandomBoolean() {
		return random.nextBoolean();
	}
	
	public void waitUntilLoaded() {

		webDriver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
		
		wait.until(new Function<WebDriver, Boolean>() {

			@Override
			public Boolean apply(WebDriver arg0) {
				Long requestsCount = (Long)((JavascriptExecutor)webDriver).executeScript("return document.ajax_outstanding;", new Object[] {});
				return requestsCount != null && 0 == requestsCount;
			}
		});

		webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}
}