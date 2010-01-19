/**
 * 
 */
package sk.seges.acris.security.rpc.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A more specific exception for access denied error type.
 * 
 */
public class AccessDeniedException extends SecurityException
		implements IsSerializable {

	private static final long serialVersionUID = -276267759288796263L;

	public AccessDeniedException() {
		super();
	}

	public AccessDeniedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccessDeniedException(String message) {
		super(message);
	}

	public AccessDeniedException(Throwable cause) {
		super(cause);
	}
}