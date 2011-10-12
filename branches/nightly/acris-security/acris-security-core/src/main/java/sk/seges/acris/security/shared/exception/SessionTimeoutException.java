package sk.seges.acris.security.shared.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This exception is thrown when the server side session is invalidated. User should be
 * logged again.
 *
 * @author Peter Simun
 */
public class SessionTimeoutException extends SecurityException implements IsSerializable {

	private static final long serialVersionUID = 2027325643848090179L;

	public SessionTimeoutException() {
		super();
	}

	public SessionTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

	public SessionTimeoutException(String message) {
		super(message);
	}

	public SessionTimeoutException(Throwable cause) {
		super(cause);
	}

}
