/**
 * 
 */
package sk.seges.acris.security.shared.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A more specific exception for authentication error type.
 *
 * @author Peter Simun
 */
public class AuthenticationException extends SecurityException implements IsSerializable {

	private static final long serialVersionUID = -6437864268780034827L;

	public AuthenticationException() {
		super();
	}

	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthenticationException(String message) {
		super(message);
	}

	public AuthenticationException(Throwable cause) {
		super(cause);
	}
}