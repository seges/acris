package sk.seges.sesam.core.test.bromine.exception;

public class BromineException extends RuntimeException {

	private static final long serialVersionUID = -7423453234432171982L;

	public BromineException() {
		super();
	}

	public BromineException(String message) {
		super(message);
	}

	public BromineException(String message, Throwable cause) {
		super(message, cause);
	}

	public BromineException(Throwable cause) {
		super(cause);
	}
}