package sk.seges.acris.security.rpc.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ApplicationSecurityException extends RuntimeException implements
		IsSerializable {

	private static final long serialVersionUID = 6276695695148424062L;

	public ApplicationSecurityException() {
		super();
	}

	public ApplicationSecurityException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationSecurityException(String message) {
		super(message);
	}

	public ApplicationSecurityException(Throwable cause) {
		super(cause);
	}
}