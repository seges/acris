package sk.seges.acris.security.shared.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ServerException extends RuntimeException implements IsSerializable {
	
	private static final long serialVersionUID = 6276695695148424062L;

	public ServerException() {
		super();
	}

	public ServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServerException(String message) {
		super(message);
	}

	public ServerException(Throwable cause) {
		super(cause);
	}
	
	public StackTraceElement[] getOriginalStackTrace()
	{
		return super.getCause().getStackTrace();
	}

}