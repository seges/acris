package sk.seges.sesam.core.test.selenium.exception;

public class SeleniumException extends RuntimeException {

	private static final long serialVersionUID = -7423453234432171982L;

	public SeleniumException() {
		super();
	}

	public SeleniumException(String message) {
		super(message);
	}

	public SeleniumException(String message, Throwable cause) {
		super(message, cause);
	}

	public SeleniumException(Throwable cause) {
		super(cause);
	}
}