package sk.seges.acris.security.rpc.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * General security exception
 * @author fat
 *
 */
public class SecurityException extends RuntimeException implements
		IsSerializable {

	private static final long serialVersionUID = 6276695695148424062L;

	public SecurityException() {
		super();
	}

	public SecurityException(String securityMessage, Throwable cause) {
		super(securityMessage, cause);
	}

	public SecurityException(String securityMessage) {
		super(securityMessage);
	}

	public SecurityException(Throwable cause) {
		super(cause);
	}
}