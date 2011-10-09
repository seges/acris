package sk.seges.sesam.core.test.selenium.support;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import org.openqa.selenium.WebDriver;

public class SeleniumSupport extends AbstractBrowserSupport {

	// protected DefaultSelenium selenium;
	protected WebDriver webDriver;

	private final Random random = new Random();

	private static final char[] symbols = new char[36];

	static {
		for (int idx = 0; idx < 10; ++idx)
			symbols[idx] = (char) ('0' + idx);
		for (int idx = 10; idx < 36; ++idx)
			symbols[idx] = (char) ('a' + idx - 10);
	}

	public SeleniumSupport(WebDriver webDriver) {
		this.webDriver = webDriver;
	}

	public String getRandomString(int length) {
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
}