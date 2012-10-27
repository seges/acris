package sk.seges.sesam.core.test.webdriver.exception;

public class WebdriverException extends RuntimeException {

	private static final long serialVersionUID = -7423453234432171982L;

	public WebdriverException() {
		super();
	}

	public WebdriverException(String message) {
		super(message);
	}

	public WebdriverException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebdriverException(Throwable cause) {
		super(cause);
	}
}