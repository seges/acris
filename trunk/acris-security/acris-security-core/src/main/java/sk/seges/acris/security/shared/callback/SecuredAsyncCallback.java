package sk.seges.acris.security.shared.callback;

// import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.seges.acris.callbacks.client.TrackingAsyncCallback;
import sk.seges.acris.security.shared.exception.SecurityException;

/**
 * This is a secured AsyncCallback class, enabling a specific failure management for security errors
 * 
 * @author Peter Simun
 */
public abstract class SecuredAsyncCallback<T> extends TrackingAsyncCallback<T> {

	public SecuredAsyncCallback() {
		super();
	}

	/**
	 * Override this method to implement your security errors management.
	 * 
	 * @param exception the security exception thrown
	 */
	public void onSecurityException(final SecurityException exception) {
	}

	/**
	 * Acts as the onFailure in a classic AsyncCallback class.
	 * 
	 * @param exception the exception thrown
	 */
	public void onOtherException(final Throwable exception) {
	}

	/**
	 * Override (and protect) the onFailure method in order to provide a security exception detection.
	 * 
	 * @param exception the exception thrown, could be a security exception
	 */

	public final void onFailureCallback(final Throwable exception) {
		SecurityException newException = SecurityExceptionsProcessor.convertToSecurityException(exception);

		if (newException != null) {
			onSecurityException(newException);
		} else {
			onOtherException(exception);
		}
	}

	/*
	 * (non-Javadoc) the default behaviour is redirect to input page (java.lang.Throwable)
	 */
	public final Throwable onSecurityFailureCallback(Throwable cause) {
		SecurityException newException = SecurityExceptionsProcessor.convertToSecurityException(cause);

		if (newException != null) {
			onSecurityException(newException);
		} else {
			onOtherException(cause);
		}

		return newException;
	}
}