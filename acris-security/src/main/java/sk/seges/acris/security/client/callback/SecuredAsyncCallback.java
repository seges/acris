package sk.seges.acris.security.client.callback;

//import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.seges.acris.callbacks.client.TrackingAsyncCallback;
import sk.seges.acris.security.rpc.exception.ApplicationSecurityException;

/**
 * This is a secured AsyncCallback class, enabling a specific failure management
 * for security errors
 * 
 */
public abstract class SecuredAsyncCallback<T> extends TrackingAsyncCallback<T> {

	public SecuredAsyncCallback() {
		super();

	}

	/**
	 * Override this method to implement your security errors management.
	 * 
	 * @param exception
	 *            the security exception thrown
	 */
	public void onSecurityException(final ApplicationSecurityException exception) {
	}

	/**
	 * Acts as the onFailure in a classic AsyncCallback class.
	 * 
	 * @param exception
	 *            the exception thrown
	 */
	public void onOtherException(final Throwable exception) {
	}

	/**
	 * Override (and protect) the onFailure method in order to provide a
	 * security exception detection.
	 * 
	 * @param exception
	 *            the exception thrown, could be a security exception
	 */
	/*
	 * public final void onFailure(final Throwable exception) {
	 * ApplicationSecurityException newException =
	 * SecurityCallbackHelper.convertToSecurityException(exception);
	 * 
	 * if (newException != null) { onSecurityException(newException); } else {
	 * onOtherException(exception); } }
	 */

	/*
	 * (non-Javadoc) the default behaviour is redirect to input page
	 * 
	 * (java.lang.Throwable)
	 */
	public final Throwable onSecurityFailureCallback(Throwable cause) {
		ApplicationSecurityException newException = SecurityCallbackHelper
				.convertToSecurityException(cause);

		if (newException != null) {
			onSecurityException(newException);
		} else {
			onOtherException(cause);
		}

		return newException;
	}
}